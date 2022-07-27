package com.dentalportal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignUpController {

    @GetMapping("/hello1")
    public String healthCheck() {
        return "Ok";
    }

    @GetMapping("/hello2")
    public String healthCheck2() {
        return "Ok hello 2";
    }

    @PostMapping("/hello")
    public String healthCheck3() {
        return "Ok2";
    }


}
