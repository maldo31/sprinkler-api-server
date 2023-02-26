package com.example.sprinkler.apiserver.services;

import com.example.sprinkler.apiserver.controllers.EndpointController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SprinklerTaskService {

  @Autowired
  EndpointController endpointController;


  @Scheduled(cron = "0 15 10 15 * ?")
  public void scheduleTaskUsingCronExpression() {

    long now = System.currentTimeMillis() / 1000;
    log.info(
        "Schedule tasks using cron jobs - " + now);
  }
}
