package com.example.sprinkler.apiserver.tasks;

import com.example.sprinkler.apiserver.entities.Endpoint;
import com.example.sprinkler.apiserver.services.EndpointService;
import java.util.Date;

public class SprinklingTask implements Runnable {

  private Endpoint endpoint;
  private long time;

  EndpointService endpointService;

  public SprinklingTask(Endpoint endpoint, EndpointService endpointService, long time) {
    this.endpoint = endpoint;
    this.endpointService = endpointService;
    this.time = time;
  }

  @Override
  public void run() {
    if (endpoint.getExpectedRainfall() < endpoint.getExpectedMinimalWatering()) {
      System.out.println(
          new Date() + " Sprinkling task executed on thread"
              + Thread.currentThread()
              .getName());
      sprinkle(endpoint, this.time);
      System.out.println(new Date() + "Sprinkling ended");

    } else {
      System.out.println(
          new Date() + " Expected rain, not sprinkling. Task started on thread "
              + Thread.currentThread()
              .getName());
    }
  }

  private void sprinkle(Endpoint endpoint, long sprinklingTime) {
    endpointService.turnOnLed(endpoint.getName());
    try {
      Thread.sleep(sprinklingTime);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    endpointService.turnOffLed(endpoint.getName());
  }
}
