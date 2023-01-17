package com.example.sprinkler.apiserver.repositories;

import com.example.sprinkler.apiserver.entities.EndpointSchedule;
import java.util.List;
import org.springframework.data.repository.CrudRepository;



public interface EndpointScheduleRepository extends CrudRepository<EndpointSchedule, Integer> {

  List<EndpointSchedule> findEndpointScheduleByEndpointName(String endpointName);

  EndpointSchedule findEndpointScheduleByTaskId(Integer taskId);
}
