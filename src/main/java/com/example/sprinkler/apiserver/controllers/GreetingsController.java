package com.example.sprinkler.apiserver.controllers;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "greetings")
public class GreetingsController {

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from API");
    }

    @GetMapping("/goodbye")
    public ResponseEntity<String> sayGoodBye() {
        return ResponseEntity.ok("Good bye sir");
    }
}
