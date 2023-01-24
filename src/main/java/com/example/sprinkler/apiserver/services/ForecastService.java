package com.example.sprinkler.apiserver.services;

import com.example.sprinkler.apiserver.entities.Endpoint;
import com.example.sprinkler.apiserver.repositories.EndpointRepository;
import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class ForecastService {

  @Autowired
  EndpointRepository endpointRepository;

  @Value("${apiKeys.forecast")
  private String forecastKey;

  public String getForecast(String city) {
    WebClient client = WebClient.create();
    var responseSpec = client.get()
        .uri("https://api.openweathermap.org/data/2.5/forecast?q=" + city
            + forecastKey)
        .retrieve()
        .bodyToMono(String.class);
    return responseSpec.block();
  }

  // At second :00, every 5 minutes starting at minute :00, of every hour
  @Scheduled(cron = "0 0/5 * * * ?")
  public void getForecastScheduled() {
    var endpoints = endpointRepository.findAll();
    StreamSupport.stream(endpoints.spliterator(), true)
        .filter(hasLatitude.and(hasLongitude))
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

  Predicate<Endpoint> hasLatitude = x -> x.getLatitude() != null;
  Predicate<Endpoint> hasLongitude = x -> x.getLongitude() != null;

  public <T> void dump(T[] table) {
    for (T t : table) {
      log.info(t.toString());
    }
  }
}


