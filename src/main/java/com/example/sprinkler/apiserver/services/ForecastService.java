package com.example.sprinkler.apiserver.services;

import com.example.sprinkler.apiserver.repositories.EndpointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalTime;
import java.util.stream.StreamSupport;

@Service
public class ForecastService {

    @Autowired
    EndpointRepository endpointRepository;

    public String getForecast(String city) {
        WebClient client = WebClient.create();
        var responseSpec = client.get()
                .uri("https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=de338ff9b1c030f6106b720bd64c0821")
                .retrieve()
                .bodyToMono(String.class);
        return responseSpec.block();
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void getForecastScheduled() {
        var cities = endpointRepository.findAll();
        StreamSupport.stream(cities.spliterator(), true).forEach(endpoint -> requestForecast(endpoint.getCity()));
    }

    private String requestForecast(String city) {
        WebClient client = WebClient.create();
        var responseSpec = client.get()
                .uri("https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=de338ff9b1c030f6106b720bd64c0821")
                .retrieve()
                .bodyToMono(String.class);
        System.out.println(city);
        System.out.println(LocalTime.now());
        System.out.println(responseSpec.block());
        return responseSpec.block();
    }


    public <T> void dump(T[] table) {
        for (T t : table) System.out.println(t);
    }
}
