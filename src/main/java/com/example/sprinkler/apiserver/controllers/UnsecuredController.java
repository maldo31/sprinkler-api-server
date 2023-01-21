package com.example.sprinkler.apiserver.controllers;

import com.example.sprinkler.apiserver.dtos.AddEndpointDto;
import com.example.sprinkler.apiserver.dtos.RegisterEndpointDto;
import com.example.sprinkler.apiserver.services.EndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "UNSECURED")
public class UnsecuredController {

    @Autowired
    EndpointService endpointService;

    @PostMapping("/registerEndpoint")
    public ResponseEntity<?>  registerEndpoint(@RequestBody RegisterEndpointDto registerEndpointDto){
        endpointService.registerEndpoint(registerEndpointDto.getAddress());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
