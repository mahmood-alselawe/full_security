package com.takarub.securityPermissions.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/management")
public class ManagerController {

    @GetMapping
    public String get(){
        return "GET :: management Controller";
    }
    @PostMapping
    public String post(){
        return "POST :: management Controller";
    }
    @DeleteMapping
    public String delete(){
        return "DELETE :: management Controller";
    }
    @PutMapping
    public String put(){
        return "PUT management Controller";
    }

}
