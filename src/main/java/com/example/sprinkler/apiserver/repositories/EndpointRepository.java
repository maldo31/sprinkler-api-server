package com.example.sprinkler.apiserver.repositories;

import com.example.sprinkler.apiserver.entities.Endpoint;
import org.springframework.data.repository.CrudRepository;



public interface EndpointRepository extends CrudRepository<Endpoint, Integer> {

  Endpoint findEndpointByName(String name);

  long deleteByName(String name);
}
