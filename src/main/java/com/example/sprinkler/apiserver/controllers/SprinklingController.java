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
import org.springframework.web.service.annotation.PostExchange;

@RestController
@RequestMapping(value = "sprinkler")
@Slf4j
public class SprinklingController {

    @Autowired
    EndpointService endpointService;

    @GetMapping("/relayOff")
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

    @GetMapping("/relayOn")
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

    @PostMapping("/executeSprinklig")
    public ResponseEntity<?> executeSprinkling(@RequestBody ExecuteSprinklingDto executeSprinklingDto, Authentication authentication){
        try {
            endpointService.sprinkleWithDuration(executeSprinklingDto,authentication);
            return ResponseEntity.ok().build();
        } catch (NoSuchEndpointException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @GetMapping("/getMoisture")
    public ResponseEntity<?> getMoisture(@RequestParam String name, Authentication authentication){
        try {
            var endpointResponse = endpointService.getMoisture(name,authentication);
            return ResponseEntity.ok().body(endpointResponse);
        } catch (NoSuchEndpointException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @PostMapping("/setMoistureSensor")
    public ResponseEntity<?> setMoistureSensor(@RequestParam String name, @RequestParam Boolean takeSensorIntoAccount, Authentication authentication){
        try {
             endpointService.setTakeSensorIntoAccount(name,takeSensorIntoAccount,authentication);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NoSuchEndpointException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        } catch (WrongResponseFromEndpoint e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @GetMapping("/currentFlow")
    public ResponseEntity<?> getCurrentFlow(@RequestParam String name, Authentication authentication){
        try {
            var endpointResponse = endpointService.getCurrentFlow(name,authentication);
            return ResponseEntity.ok().body(endpointResponse);
        } catch (NoSuchEndpointException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @GetMapping("/totalFlow")
    public ResponseEntity<?> getTotalFlow(@RequestParam String name, Authentication authentication){
        try {
            var endpointResponse = endpointService.getTotalFlow(name,authentication);
            return ResponseEntity.ok().body(endpointResponse);
        } catch (NoSuchEndpointException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }
}
