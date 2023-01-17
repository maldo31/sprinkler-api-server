package com.example.sprinkler.apiserver.services;

import com.example.sprinkler.apiserver.dtos.EndpointLocationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CoordinatesService {

  @Value("${apiKeys.geo}")
  private String geoKey;

  public Point getCoordinates(String city) {
    WebClient client = WebClient.builder()
        .defaultHeader("X-Api-Key", geoKey)
        .build();
    var responseSpec = client.get()
        .uri("https://api.api-ninjas.com/v1/geocoding?city=" + city)
        .retrieve()
        .bodyToFlux(EndpointLocationDto.class)
        .blockFirst();
    if (responseSpec != null) {
      return new Point(Double.parseDouble(responseSpec.getLatitude()),
          Double.parseDouble(responseSpec.getLongitude()));
    } else {
      return new Point(0, 0);
    }
  }

}
