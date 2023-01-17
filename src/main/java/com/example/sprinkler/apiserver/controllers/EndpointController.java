package com.example.sprinkler.apiserver.controllers;

import com.example.sprinkler.apiserver.dtos.AddEndpointDto;
import com.example.sprinkler.apiserver.dtos.EndpointResponseDto;
import com.example.sprinkler.apiserver.entities.Endpoint;
import com.example.sprinkler.apiserver.entities.User;
import com.example.sprinkler.apiserver.exceptions.NoSuchEndpointException;
import com.example.sprinkler.apiserver.services.EndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "gpio")
public class EndpointController {

  @Autowired
  EndpointService endpointService;

  @GetMapping("/ledon")
  public String turnLedOn(@RequestParam String name, Authentication authentication) {
        return endpointService.turnOnLed(name,authentication);
  }

  @GetMapping("/ledoff")
  public String turnLedOff(@RequestParam String name, Authentication authentication) {
    return endpointService.turnOffLed(name,authentication);
  }

  @PostMapping("/add_endpoint")
  public String addEndpoint(@RequestBody AddEndpointDto addEndpointDto) {
    return endpointService.addEndpoint(addEndpointDto);
  }

  @GetMapping("/get_endpoint")
  public ResponseEntity<?> getEndpoint(@RequestParam String name, Authentication authentication) {
    try {
      return ResponseEntity.ok(endpointService.getEndpoint(name,authentication).toEndpointResponseDto());
    } catch (NoSuchEndpointException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/get_endpoints")
  public List<EndpointResponseDto> getEndpoints(Authentication authentication) {
    return endpointService.getEndpoints(authentication).stream()
            .map(Endpoint::toEndpointResponseDto)
            .collect(Collectors.toList());
  }

  @DeleteMapping("/delete_endpoint")
  public String deleteEndpoint(@RequestParam String name, Authentication authentication) {
    endpointService.deleteEndpoint(name, authentication);
    return "deleted endpoint";
  }
}
