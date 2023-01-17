package com.example.sprinkler.apiserver.services;

import com.example.sprinkler.apiserver.controllers.EndpointController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class SprinklerTaskService {

  @Autowired
  EndpointController endpointController;


  @Scheduled(cron = "0 15 10 15 * ?")
  public void scheduleTaskUsingCronExpression() {

    long now = System.currentTimeMillis() / 1000;
    System.out.println(
        "schedule tasks using cron jobs - " + now);
  }
}
