package com.example.sprinkler.apiserver.controllers;

import com.example.sprinkler.apiserver.dtos.AddEndpointDto;
import com.example.sprinkler.apiserver.dtos.EndpointResponseDto;
import com.example.sprinkler.apiserver.entities.Endpoint;
import com.example.sprinkler.apiserver.exceptions.NoSuchEndpointException;
import com.example.sprinkler.apiserver.services.EndpointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping(value = "endpoint")
public class EndpointController {

  @Autowired
  EndpointService endpointService;

  @PostMapping("/add_endpoint")
  public ResponseEntity<Endpoint> addEndpoint(@RequestBody AddEndpointDto addEndpointDto) {
    return ResponseEntity.ok().body(endpointService.addEndpoint(addEndpointDto));
  }

  @GetMapping("/get_endpoint")
  public ResponseEntity<?> getEndpoint(@RequestParam String name, Authentication authentication) {
    try {
      return ResponseEntity.ok(endpointService.getEndpoint(name,authentication).toEndpointResponseDto());
    } catch (NoSuchEndpointException e) {
      log.error(e.getMessage());
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
