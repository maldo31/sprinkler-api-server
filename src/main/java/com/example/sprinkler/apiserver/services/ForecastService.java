package com.example.sprinkler.apiserver.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ForecastService {


    public String getForecast(String city) {
        WebClient client = WebClient.create();

        WebClient.ResponseSpec responseSpec = client.get()
                .uri("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=de338ff9b1c030f6106b720bd64c0821")
                .retrieve();
        return responseSpec.bodyToMono(String.class).block();
    }
    public <T> void dump(T[] table){
        for(T t: table) System.out.println(t);
    }
}
