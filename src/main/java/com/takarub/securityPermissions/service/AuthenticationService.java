package com.takarub.securityPermissions.service;

import com.takarub.securityPermissions.dto.AuthenticationResponse;
import com.takarub.securityPermissions.model.Token;
import com.takarub.securityPermissions.model.TokenType;
import com.takarub.securityPermissions.model.User;
import com.takarub.securityPermissions.repository.TokenRepository;
import com.takarub.securityPermissions.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.takarub.securityPermissions.model.TokenType.Bearer;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;


    public AuthenticationResponse register(User request) {
        User user = User
                .builder()
                .name(request.getName())
                .email(request.getUsername())
                .passWord(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        User save = repository.save(user);

        String jwt = jwtService.generateToken(save);

        saveUserToken(save, jwt);


        return AuthenticationResponse
                .builder()
                .accessToken(jwt)
                .tokenType(Bearer)
                .accessTokenExpiry(30 * 60)
                .userName(user.getName())
                .build();

    }



    public AuthenticationResponse authenticate(User request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = repository.findByEmail(request.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user);

        revokAllTokenByUser(user);
        saveUserToken(user,token);

        return AuthenticationResponse
                .builder()
                .accessToken(token)
                .tokenType(Bearer)
                .accessTokenExpiry(30 * 60)
                .userName(user.getName())
                .build();


    }

    private void revokAllTokenByUser(User user) {
        List<Token> validTokenListByUser =  tokenRepository.findAllTokensByUser(user.getId());

        if(validTokenListByUser.isEmpty()) {
            return;
        }

        validTokenListByUser.forEach(t-> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokenListByUser);
    }

    private void saveUserToken(User save, String jwt) {
        Token build = Token
                .builder()
                .token(jwt)
                .user(save)
                .loggedOut(false)
                .build();
        tokenRepository.save(build);
    }


}
