package com.example.sprinkler.apiserver.controllers;

import com.example.sprinkler.apiserver.services.EndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "gpio")
public class EndpointController {
    @Autowired
    EndpointService endpointService;

    @GetMapping("/ledon")
    public String turnLedOn(@RequestParam String name) throws Exception {
        return endpointService.turnOnLed(name);
    }

    @GetMapping("/ledoff")
    public String turnLedOff(@RequestParam String name) throws Exception {
        return endpointService.turnOffLed(name);
    }

    @PostMapping("/add_endpoint")
    public String addEndpoint(@RequestParam String name, @RequestParam String address) {
        return endpointService.addEndpoint(name, address);
    }

    @GetMapping("/get_endpoint")
    public String getEndpoint(@RequestParam String name) {
        return endpointService.getEndpoint(name);
    }

    @GetMapping("/get_endpoints")
    public String getEndpoints() {
        return endpointService.getEndpoints();
    }

    @DeleteMapping("/delete_endpoint")
    public String deleteEndpoint(@RequestParam String name) {
        endpointService.deleteEndpoint(name);
        return "deleted endpoint";
    }


}
