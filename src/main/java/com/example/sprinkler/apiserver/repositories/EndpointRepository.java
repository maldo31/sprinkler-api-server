package com.example.sprinkler.apiserver.repositories;

import com.example.sprinkler.apiserver.entities.Endpoint;
import com.example.sprinkler.apiserver.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface EndpointRepository extends CrudRepository<Endpoint, Integer> {

  Optional<Endpoint> findEndpointByNameAndUser(String name, User user);
  Optional<Endpoint> findEndpointByNameAndUser(String name, String user);

  void deleteByNameAndUser(String name, User user);

  List<Endpoint> findAllByUser(User user);

  List<Endpoint> findAllByUserIsNull();
}
