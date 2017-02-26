package com.cassess.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class BackEndUserController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/user")
    public Principal user(Principal user){
        return user;
    }
}
