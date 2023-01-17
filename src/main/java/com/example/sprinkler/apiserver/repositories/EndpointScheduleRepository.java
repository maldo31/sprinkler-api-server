package com.example.sprinkler.apiserver.repositories;

import com.example.sprinkler.apiserver.entities.EndpointSchedule;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface EndpointScheduleRepository extends CrudRepository<EndpointSchedule, Integer> {

  List<EndpointSchedule> findEndpointScheduleByEndpointName(String endpointName);

  EndpointSchedule findEndpointScheduleByTaskId(Integer taskId);
}
