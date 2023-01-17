package com.example.sprinkler.apiserver.tasks;

import java.util.Date;

public class SprinklingTask implements Runnable {

  private String message;

  public SprinklingTask(String message) {
    this.message = message;
  }

  @Override
  public void run() {
    System.out.println(
        new Date() + " Sprinkling task executed " + message + " on thread" + Thread.currentThread()
            .getName());
  }
}
