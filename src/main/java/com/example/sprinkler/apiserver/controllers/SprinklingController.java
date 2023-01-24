package com.example.sprinkler.apiserver.controllers;

import com.example.sprinkler.apiserver.dtos.ExecuteSprinklingDto;
import com.example.sprinkler.apiserver.exceptions.NoSuchEndpointException;
import com.example.sprinkler.apiserver.exceptions.WrongResponseFromEndpoint;
import com.example.sprinkler.apiserver.services.EndpointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "sprinkler")
@Slf4j
public class SprinklingController {

    @Autowired
    EndpointService endpointService;

    @GetMapping("/relay-off")
    public ResponseEntity<?> relayOff(@RequestParam String name, Authentication authentication) {
        try {
            endpointService.relayOff(name,authentication);
        } catch (NoSuchEndpointException e) {
            log.error("Couldn't find given endpoint " + name + " for given auth " + authentication.getDetails());
        } catch (WrongResponseFromEndpoint e) {
            log.error("Bad response from endpoint");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/relay-on")
    public ResponseEntity<?> turnLedOff(@RequestParam String name, Authentication authentication) {
        try {
            endpointService.relayOn(name,authentication);
        } catch (WrongResponseFromEndpoint e) {
            log.error("Couldn't find given endpoint " + name + " for given auth " + authentication.getDetails());
        } catch (NoSuchEndpointException e) {
            log.error("Bad response from endpoint");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/execute_sprinklig")
    public ResponseEntity<?> executeSprinkling(@RequestBody ExecuteSprinklingDto executeSprinklingDto, Authentication authentication){
        try {
            endpointService.sprinkleWithDuration(executeSprinklingDto,authentication);
            return ResponseEntity.ok().build();
        } catch (NoSuchEndpointException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }
}
