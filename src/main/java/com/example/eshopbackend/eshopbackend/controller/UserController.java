package com.example.eshopbackend.eshopbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    // http://localhost:8090/api/user/info
    @GetMapping("/info")
    public Object userAppInfo(){
        return "User Controller";
    }
}
