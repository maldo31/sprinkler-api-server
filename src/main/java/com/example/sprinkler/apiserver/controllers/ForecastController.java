package com.example.sprinkler.apiserver.controllers;

import com.example.sprinkler.apiserver.services.ForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "forecast")
public class ForecastController {

    @Autowired
    ForecastService forecastService;

    @GetMapping
    public String getForecast(@RequestParam String city) {
        return forecastService.getForecast(city);
    }
}
