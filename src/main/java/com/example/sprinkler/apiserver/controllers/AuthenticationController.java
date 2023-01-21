package com.example.sprinkler.apiserver.controllers;

import com.example.sprinkler.apiserver.dtos.AuthenticationRequestDto;
import com.example.sprinkler.apiserver.dtos.AuthenticationResponseDto;
import com.example.sprinkler.apiserver.dtos.RegisterRequestDto;
import com.example.sprinkler.apiserver.exceptions.UserAlreadyExistException;
import com.example.sprinkler.apiserver.services.AuthenticationService;
import com.example.sprinkler.apiserver.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final UsersService usersService;

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

    @DeleteMapping("/removeUser")
    public ResponseEntity<?> removeUser(Authentication authentication){
        try {
            usersService.remove(authentication);
            return ResponseEntity.ok("Removed");
        }
        catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
