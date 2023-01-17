package com.example.sprinkler.apiserver.services;

import com.example.sprinkler.apiserver.dtos.AddEndpointDto;
import com.example.sprinkler.apiserver.entities.Endpoint;
import com.example.sprinkler.apiserver.repositories.EndpointRepository;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EndpointService {

  @Autowired
  EndpointRepository endpointRepository;

  @Autowired
  CoordinatesService coordinatesService;

  public String turnOffLed(String name) {
    String endpointAddress = endpointRepository.findEndpointByName(name).getAddress();
    try {
      URL url = new URL("http://" + endpointAddress + "/?relay=off");
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      con.getResponseCode();
      System.out.println("test");
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    } catch (ConnectException e) {
      return "Endpoint doesn't respond";
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return "Turned off led";

  }

  public String turnOnLed(String name) {
    String endpointAddress = endpointRepository.findEndpointByName(name).getAddress();
    try {
      URL url = new URL("http://" + endpointAddress + "/?relay=on");
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      con.getResponseCode();
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    } catch (ConnectException e) {
      return "Endpoint doesn't respond";
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return "Turned off led";
  }

  @Transactional
  public String addEndpoint(AddEndpointDto addEndpointDto) {
    var endpointLocation = coordinatesService.getCoordinates(addEndpointDto.getCity());
    return endpointRepository.save(Endpoint.builder()
        .name(addEndpointDto.getName())
        .address(addEndpointDto.getAddress())
        .city(addEndpointDto.getCity())
        .latitude(endpointLocation.getX())
        .longitude(endpointLocation.getY())
        .expectedMinimalWatering(addEndpointDto.getExpectedMinimalWatering())
        .build()).toString();
  }

  public String getEndpoint(String name) {
    return endpointRepository.findEndpointByName(name).toString();
  }

  @Transactional
  public void deleteEndpoint(String name) {
    endpointRepository.deleteByName(name);
  }

  public String getEndpoints() {
    return endpointRepository.findAll().toString();
  }

}
