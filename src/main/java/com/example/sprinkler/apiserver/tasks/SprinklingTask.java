package com.example.sprinkler.apiserver.tasks;

import com.example.sprinkler.apiserver.entities.Endpoint;
import java.util.Date;

public class SprinklingTask implements Runnable {

  private String message;
  private Endpoint endpoint;

  public SprinklingTask(String message, Endpoint endpoint) {
    this.message = message;
    this.endpoint = endpoint;
  }

  @Override
  public void run() {
    if (endpoint.getExpectedRainfall() > endpoint.getExpectedMinimalWatering()) {
      System.out.println(
          new Date() + " Sprinkling task executed " + message + " on thread"
              + Thread.currentThread()
              .getName());
    } else {
      System.out.println(
          new Date() + " Expected waterfall not sprinkling: " + message + " on thread"
              + Thread.currentThread()
              .getName());
    }
  }
}
