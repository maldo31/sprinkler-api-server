package com.example.sprinkler.apiserver.controllers;

import com.example.sprinkler.apiserver.services.EndpointScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "schedules")
public class EndpointScheduleController {

    @Autowired
    EndpointScheduleService endpointScheduleService;

    @PostMapping("/add_schedule")
    public String addSchedule(@RequestParam Integer id, @RequestParam String sprinklingHour, @RequestParam String sprinklingMinute, @RequestParam Integer dayOfWeek) {
        return endpointScheduleService.addSchedule(id, sprinklingHour, sprinklingMinute, dayOfWeek);
    }

    @GetMapping("/get_schedules")
    public String getSchedules(){
        return endpointScheduleService.getSchedules();
    }

    @GetMapping("/get_schedules_for_endpoint")
    public String getSchedulesForEndpoint(@RequestParam String endpointName){
        return endpointScheduleService.getSchedulesForEndpoint(endpointName);

    }

    @PostMapping("/schedule_task")
    public String scheduleTask(@RequestParam Integer period, @RequestParam String text){
        return endpointScheduleService.scheduleTask(period,text);
    }

}
