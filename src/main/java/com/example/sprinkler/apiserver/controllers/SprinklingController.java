package com.example.sprinkler.apiserver.controllers;

import com.example.sprinkler.apiserver.dtos.ExecuteSprinklingDto;
import com.example.sprinkler.apiserver.exceptions.NoSuchEndpointException;
import com.example.sprinkler.apiserver.services.EndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "sprinkler")
public class SprinklingController {

    @Autowired
    EndpointService endpointService;

    @GetMapping("/relay-off")
    public String relayOff(@RequestParam String name, Authentication authentication) {
        return endpointService.relayOff(name,authentication);
    }

    @GetMapping("/relay-on")
    public String turnLedOff(@RequestParam String name, Authentication authentication) {
        return endpointService.relayOn(name,authentication);
    }

    @PostMapping("/execute_sprinklig")
    public ResponseEntity<?> executeSprinkling(@RequestBody ExecuteSprinklingDto executeSprinklingDto, Authentication authentication){
        try {
            endpointService.sprinkleWithDuration(executeSprinklingDto,authentication);
            return ResponseEntity.ok().body("OK");
        } catch (NoSuchEndpointException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }
}
