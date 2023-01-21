package com.example.sprinkler.apiserver.services;

import com.example.sprinkler.apiserver.dtos.AddEndpointDto;
import com.example.sprinkler.apiserver.dtos.ApiCallDto;
import com.example.sprinkler.apiserver.dtos.ExecuteSprinklingDto;
import com.example.sprinkler.apiserver.entities.Endpoint;
import com.example.sprinkler.apiserver.exceptions.NoSuchEndpointException;
import com.example.sprinkler.apiserver.repositories.EndpointRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EndpointService {

    @Autowired
    EndpointRepository endpointRepository;

    @Autowired
    CoordinatesService coordinatesService;

    @Autowired
    UsersService usersService;

    public String relayOn(String name, Authentication authentication) {
        var endpoint = endpointRepository.findEndpointByNameAndUser(name, usersService.getUserFromAuthentication(authentication));
        if (endpoint.isPresent()) {
            Map<String, Integer> jsonMap = new HashMap<>();
            jsonMap.put("relay", 0);
            int x = callEndpointApi(endpoint.get(), ApiCallDto.builder()
                    .path("/relay")
                    .jsonBody(jsonMap)
                    .build());
            if (x != 200) return "Bad response";
            return "Turned Off led";
        }
        return "No such endpoint";
    }

    public void relayOn(Endpoint endpoint) {
        Map<String, Integer> jsonMap = new HashMap<>();
        jsonMap.put("relay", 0);
        callEndpointApi(endpoint, ApiCallDto.builder()
                .path("/relay")
                .jsonBody(jsonMap)
                .build());
    }

    public String relayOff(String name, Authentication authentication) {
        var endpoint = endpointRepository.findEndpointByNameAndUser(name, usersService.getUserFromAuthentication(authentication));
        if (endpoint.isPresent()) {
            Map<String, Integer> jsonMap = new HashMap<>();
            jsonMap.put("relay", 1);
            var x = callEndpointApi(endpoint.get(), ApiCallDto.builder()
                    .path("/relay")
                    .jsonBody(jsonMap)
                    .build());
            if (x != 200) return "Bad response";
            return "Turned On led";
        }
        return "No such endpoint";
    }

    public void relayOff(Endpoint endpoint) {
        Map<String, Integer> jsonMap = new HashMap<>();
        jsonMap.put("relay", 1);
        callEndpointApi(endpoint, ApiCallDto.builder()
                .path("/relay")
                .jsonBody(jsonMap)
                .build());
    }


    public void sprinkleWithDuration(ExecuteSprinklingDto executeSprinklingDto, Authentication authentication) throws NoSuchEndpointException {
        Map<String, Long> jsonMap = new HashMap<>();
        jsonMap.put("duration", executeSprinklingDto.getDuration().getSeconds());
        callEndpointApi(endpointRepository.findEndpointByNameAndUser(executeSprinklingDto.getEndpointName(), usersService.getUserFromAuthentication(authentication)).orElseThrow(NoSuchEndpointException::new), ApiCallDto.builder()
                .path("/sprinkle")
                .jsonBody(jsonMap)
                .build());
    }

    private int callEndpointApi(Endpoint endpoint, ApiCallDto apiCallDto) {

        WebClient webClient = WebClient.create("http://" + endpoint.getAddress());


        return webClient.post()
                .uri(apiCallDto.getPath())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(apiCallDto.getJsonBody()))
                .exchange()
                .map(ClientResponse::statusCode)
                .block()
                .value();
    }

    @Transactional
    public String addEndpoint(AddEndpointDto addEndpointDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var endpointLocation = coordinatesService.getCoordinates(addEndpointDto.getCity());
        return endpointRepository.save(Endpoint.builder()
                .name(addEndpointDto.getName())
                .address(addEndpointDto.getAddress())
                .city(addEndpointDto.getCity())
                .latitude(endpointLocation.getX())
                .longitude(endpointLocation.getY())
                .expectedMinimalWatering(addEndpointDto.getExpectedMinimalWatering())
                .user(usersService.getUserFromAuthentication(authentication))
                .build()).toString();
    }

    public Endpoint getEndpoint(String name, Authentication authentication) throws NoSuchEndpointException {
        return endpointRepository.findEndpointByNameAndUser(name, usersService.getUserFromAuthentication(authentication)).orElseThrow(NoSuchEndpointException::new);
    }

    @Transactional
    public void deleteEndpoint(String name, Authentication authentication) {
        endpointRepository.deleteByNameAndUser(name, usersService.getUserFromAuthentication(authentication));
    }

    public List<Endpoint> getEndpoints(Authentication authentication) {
        return endpointRepository.findAllByUser(usersService.getUserFromAuthentication(authentication));
    }

}
