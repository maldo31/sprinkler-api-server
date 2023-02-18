package com.example.sprinkler.apiserver.controllers;

import com.example.sprinkler.apiserver.dtos.AddSprinklingTaskDto;
import com.example.sprinkler.apiserver.services.EndpointScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "schedules")
public class EndpointScheduleController {

  @Autowired
  EndpointScheduleService endpointScheduleService;

  @GetMapping("/getSchedules")
  public String getSchedules() {
    return endpointScheduleService.getSchedules();
  }

  @GetMapping("/getSchedulesForEndpoint")
  public String getSchedulesForEndpoint(@RequestParam String endpointName) {
    return endpointScheduleService.getSchedulesForEndpoint(endpointName);

  }

  @PostMapping("/scheduleCronTask")
  public String scheduleTask(@RequestBody AddSprinklingTaskDto addSprinklingTaskDto) {
    return endpointScheduleService.scheduleCronTask(addSprinklingTaskDto);
  }

  @DeleteMapping("/deleteSchedule")
  public String deleteTask(@RequestParam Integer taskId) {
    return endpointScheduleService.removeScheduledTask(taskId);
  }

}
