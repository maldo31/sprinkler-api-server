package com.example.sprinkler.apiserver.services;

import com.example.sprinkler.apiserver.configs.ThreadPoolTaskSchedulerConfig;
import com.example.sprinkler.apiserver.entities.EndpointSchedule;
import com.example.sprinkler.apiserver.repositories.EndpointRepository;
import com.example.sprinkler.apiserver.repositories.EndpointScheduleRepository;
import com.example.sprinkler.apiserver.tasks.SprinklingTask;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.concurrent.ScheduledFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;


@Service
public class EndpointScheduleService {

  @Autowired
  EndpointScheduleRepository endpointScheduleRepository;

  @Autowired
  EndpointRepository endpointRepository;

  @Autowired
  ThreadPoolTaskSchedulerConfig threadPoolTaskSchedulerConfig;

  ThreadPoolTaskScheduler scheduler = threadPoolTaskSchedulerConfig.threadPoolTaskScheduler();

  public String addSchedule(Integer id, String sprinklingHour, String sprinklingMinute,
      Integer dayOfWeek) {
    var endpoint = endpointRepository.findById(id).orElseThrow();
    LocalTime sprinklingTime = LocalTime.of(Integer.parseInt(sprinklingHour),
        Integer.parseInt(sprinklingMinute));
    var endpointSchedule = EndpointSchedule.builder()
        .sprinklingTime(sprinklingTime)
        .endpoint(endpoint)
        .dayOfWeek(DayOfWeek.of(dayOfWeek))
        .zone("GMT")
        .build();
    endpointScheduleRepository.save(endpointSchedule);
    return "ok";
  }

  public String getSchedulesForEndpoint(String endpointName) {
    return endpointScheduleRepository.findEndpointScheduleByEndpointName(endpointName).toString();
  }

  public String getSchedules() {
    return endpointScheduleRepository.findAll().toString();
  }

  public String scheduleTask(Integer period, String text) {
    return scheduler.scheduleAtFixedRate(new SprinklingTask(text), period).toString();
  }
}
