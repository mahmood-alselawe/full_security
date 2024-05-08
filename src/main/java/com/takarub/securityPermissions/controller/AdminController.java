package com.takarub.securityPermissions.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read')")
    public String get(){
        return "GET :: Admin Controller";
    }
    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin:create')")
    public String post(){
        return "POST :: Admin Controller";
    }
    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('admin:delete')")
    public String delete(){
        return "DELETE :: Admin Controller";
    }
    @PutMapping
    @PreAuthorize("hasAnyAuthority('admin:update')")
    public String put(){
        return "PUT Admin Controller";
    }

}
