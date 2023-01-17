package com.example.sprinkler.apiserver.services;

import com.example.sprinkler.apiserver.configs.ThreadPoolTaskSchedulerConfig;
import com.example.sprinkler.apiserver.dtos.AddSprinklingTaskDto;
import com.example.sprinkler.apiserver.entities.EndpointSchedule;
import com.example.sprinkler.apiserver.repositories.EndpointRepository;
import com.example.sprinkler.apiserver.repositories.EndpointScheduleRepository;
import com.example.sprinkler.apiserver.tasks.SprinklingTask;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;


@Service
public class EndpointScheduleService {

  @Autowired
  EndpointScheduleRepository endpointScheduleRepository;

  @Autowired
  EndpointRepository endpointRepository;

  @Autowired
  EndpointService endpointService;

  Map<Integer, ScheduledFuture<?>> tasksMap = new HashMap<>();

  ThreadPoolTaskSchedulerConfig threadPoolTaskSchedulerConfig = new ThreadPoolTaskSchedulerConfig();

  ThreadPoolTaskScheduler scheduler = threadPoolTaskSchedulerConfig.threadPoolTaskScheduler();

  int taskId = 0;

  public String getSchedulesForEndpoint(String endpointName) {
    return endpointScheduleRepository.findEndpointScheduleByEndpointName(endpointName).toString();
  }

  public String getSchedules() {
    return endpointScheduleRepository.findAll().toString();
  }

  public String scheduleCronTask(AddSprinklingTaskDto addSprinklingTaskDto) {
    var endpoint = endpointRepository.findById(addSprinklingTaskDto.getEndpointId()).orElseThrow();
    CronTrigger cronTrigger = new CronTrigger(executionDateToCronExpression(addSprinklingTaskDto));
    ScheduledFuture<?> scheduledTask = scheduler.schedule(new SprinklingTask(endpoint, endpointService, addSprinklingTaskDto.getSprinklingDuration()),
        cronTrigger);
    tasksMap.put(taskId, scheduledTask);

    var endpointSchedule = EndpointSchedule.builder()
        .endpoint(endpoint)
        .day(addSprinklingTaskDto.getDay())
        .hour(addSprinklingTaskDto.getHour())
        .minute(addSprinklingTaskDto.getMinute())
        .taskId(taskId).build();
    endpointScheduleRepository.save(endpointSchedule);
    taskId += 1;
    return "Created task id=" + (taskId - 1);
  }

  public String removeScheduledTask(Integer taskId) {
    ScheduledFuture<?> scheduledTask = tasksMap.get(taskId);
    if (scheduledTask != null) {
      scheduledTask.cancel(true);
      tasksMap.put(taskId, null);
      endpointScheduleRepository.delete(
          endpointScheduleRepository.findEndpointScheduleByTaskId(taskId));
      return "Removed task: " + taskId;
    }
    return "Couldn't remove task: " + taskId;
  }

  private String executionDateToCronExpression(AddSprinklingTaskDto addSprinklingTaskDto) {
    String cronPattern = "0 %s %s ? * %s";
    return String.format(cronPattern, addSprinklingTaskDto.getMinute(),
        addSprinklingTaskDto.getHour(),
        addSprinklingTaskDto.getDay());
  }
}
