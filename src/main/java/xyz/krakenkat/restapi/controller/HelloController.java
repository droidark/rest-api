package xyz.krakenkat.restapi.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello(Authentication authentication) {
        return "hello " + authentication.getName() + "!";
    }
}
