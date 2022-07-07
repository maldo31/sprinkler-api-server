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
    public String turnLedOn() throws Exception {
        endpointService.turnOnLed();
        return "led state set to on";
    }
    @GetMapping("/ledoff")
    public String turnLedOff() throws Exception {
        endpointService.turnOffLed();
        return "led state set to off";
    }
    @PostMapping("/add_endpoin")
    public String addEndpoint(@RequestParam String name,@RequestParam String address){
        endpointService.addEndpoint(name,address);
        return "created endpoint";
    }


}
