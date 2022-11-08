package com.example.sprinkler.apiserver.repositories;

import com.example.sprinkler.apiserver.entities.Endpoint;
import com.example.sprinkler.apiserver.entities.EndpointSchedule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface EndpointScheduleRepository extends CrudRepository<EndpointSchedule,Integer> {
    List<EndpointSchedule> findEndpointScheduleByEndpointName(String endpointName);
}
