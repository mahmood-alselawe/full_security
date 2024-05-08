package com.takarub.securityPermissions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.takarub.securityPermissions.model.TokenType;
import lombok.AllArgsConstructor;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("access_token_expiry")
    private int accessTokenExpiry;

    @JsonProperty("token_type")
    private TokenType tokenType;

    @JsonProperty("user_name")
    private String userName;

//    @JsonProperty("Refresh_token")
//    private String RefreshToken;

}
