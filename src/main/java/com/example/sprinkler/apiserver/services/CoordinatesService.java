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

    @Value("${apiKeys.geocodinguri")
    private String geoUri;

    public Point getCoordinates(String city) {
      EndpointLocationDto responseSpec = callAPIAndGetLocation(city);
      if (responseSpec != null) {
            return new Point(Double.parseDouble(responseSpec.getLatitude()),
                    Double.parseDouble(responseSpec.getLongitude()));
        } else {
            return new Point(0, 0);
        }
    }

  private EndpointLocationDto callAPIAndGetLocation(String city) {
    WebClient client = WebClient.builder()
            .defaultHeader("X-Api-Key", geoKey)
            .build();
    return client.get()
            .uri(geoUri + city)
            .retrieve()
            .bodyToFlux(EndpointLocationDto.class)
            .blockFirst();
  }

}
