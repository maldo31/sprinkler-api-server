package com.example.sprinkler.apiserver.services;

import com.example.sprinkler.apiserver.entities.EndpointSchedule;
import com.example.sprinkler.apiserver.repositories.EndpointRepository;
import com.example.sprinkler.apiserver.repositories.EndpointScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;


@Service
public class EndpointScheduleService {

    @Autowired
    EndpointScheduleRepository endpointScheduleRepository;

    @Autowired
    EndpointRepository endpointRepository;

    public String addSchedule(Integer id, String sprinklingHour, String sprinklingMinute, Integer dayOfWeek) {
        var endpoint = endpointRepository.findById(id).orElseThrow();
        LocalTime sprinklingTime = LocalTime.of(Integer.parseInt(sprinklingHour), Integer.parseInt(sprinklingMinute));
        var endpointSchedule= EndpointSchedule.builder()
                .sprinklingTime(sprinklingTime)
                .endpoint(endpoint)
                .dayOfWeek(DayOfWeek.of(dayOfWeek))
                .zone("GMT")
                .build();
        endpointScheduleRepository.save(endpointSchedule);
        return "ok";
    }

    public String getSchedulesForEndpoint(String endpointName){
        return endpointScheduleRepository.findEndpointScheduleByEndpointName(endpointName).toString();
    }

    public String getSchedules() {
        return endpointScheduleRepository.findAll().toString();
    }
}
