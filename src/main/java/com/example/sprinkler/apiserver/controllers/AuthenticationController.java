package com.example.sprinkler.apiserver.controllers;

import com.example.sprinkler.apiserver.dtos.AuthenticationRequestDto;
import com.example.sprinkler.apiserver.dtos.AuthenticationResponseDto;
import com.example.sprinkler.apiserver.dtos.RegisterRequestDto;
import com.example.sprinkler.apiserver.exceptions.UserAlreadyExistException;
import com.example.sprinkler.apiserver.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequestDto request
    ){
        try {
            return ResponseEntity.ok(authenticationService.register(request));
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @RequestBody AuthenticationRequestDto request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
