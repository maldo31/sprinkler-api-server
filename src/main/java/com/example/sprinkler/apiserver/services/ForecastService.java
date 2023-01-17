package com.example.sprinkler.apiserver.services;

import com.example.sprinkler.apiserver.repositories.EndpointRepository;
import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ForecastService {

  @Autowired
  EndpointRepository endpointRepository;

  public String getForecast(String city) {
    WebClient client = WebClient.create();
    var responseSpec = client.get()
        .uri("https://api.openweathermap.org/data/2.5/forecast?q=" + city
            + "&appid=de338ff9b1c030f6106b720bd64c0821")
        .retrieve()
        .bodyToMono(String.class);
    return responseSpec.block();
  }

  @Scheduled(cron = "0 0/5 * * * ?")
  public void getForecastScheduled() {
    var endpoints = endpointRepository.findAll();
    StreamSupport.stream(endpoints.spliterator(), true)
        .forEach(endpoint -> endpoint.setExpectedRainfall(
            requestForecast(endpoint.getLatitude(), endpoint.getLongitude())));
    endpointRepository.saveAll(endpoints);
  }

  private Double requestForecast(Double lat, Double lon) {
    WebClient client = WebClient.create();
    URI uri = URI.create(
        "https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lon
            + "&daily=rain_sum&timezone=Europe%2FBerlin");
    return client.get()
        .uri(uri)
        .retrieve()
        .bodyToMono(JsonNode.class)
        .map(s -> s.at("/daily/rain_sum").get(0).asDouble())
        .block();

  }


  public <T> void dump(T[] table) {
    for (T t : table) {
      System.out.println(t);
    }
  }
}


