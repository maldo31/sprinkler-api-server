package com.example.sprinkler.apiserver.services;

import com.example.sprinkler.apiserver.dtos.AddEndpointDto;
import com.example.sprinkler.apiserver.dtos.ApiCallDto;
import com.example.sprinkler.apiserver.dtos.ExecuteSprinklingDto;
import com.example.sprinkler.apiserver.entities.Endpoint;
import com.example.sprinkler.apiserver.exceptions.NoSuchEndpointException;
import com.example.sprinkler.apiserver.exceptions.WrongResponseFromEndpoint;
import com.example.sprinkler.apiserver.repositories.EndpointRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
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
@Slf4j
public class EndpointService {

    @Autowired
    EndpointRepository endpointRepository;

    @Autowired
    CoordinatesService coordinatesService;

    @Autowired
    UsersService usersService;

    public void relayOn(String name, Authentication authentication) throws WrongResponseFromEndpoint, NoSuchEndpointException {
        var endpoint = getEndpoint(name, authentication);
        Map<String, Integer> jsonMap = new HashMap<>();
        jsonMap.put("relay", 0);
        int x = callEndpointApi(endpoint, ApiCallDto.builder()
                .path("/relay")
                .jsonBody(jsonMap)
                .build());
        if (x != 200) throw new WrongResponseFromEndpoint("Bad response from endpoint");
    }

    public void relayOn(Endpoint endpoint) {
        Map<String, Integer> jsonMap = new HashMap<>();
        jsonMap.put("relay", 0);
        callEndpointApi(endpoint, ApiCallDto.builder()
                .path("/relay")
                .jsonBody(jsonMap)
                .build());
    }

    public void relayOff(String name, Authentication authentication) throws NoSuchEndpointException, WrongResponseFromEndpoint {
        var endpoint = getEndpoint(name, authentication);

        Map<String, Integer> jsonMap = new HashMap<>();
        jsonMap.put("relay", 1);
        var x = callEndpointApi(endpoint, ApiCallDto.builder()
                .path("/relay")
                .jsonBody(jsonMap)
                .build());
        if (x != 200) {
            throw new WrongResponseFromEndpoint("Bad response from endpoint");
        }
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

    private String callApiGetResponse(Endpoint endpoint, ApiCallDto apiCallDto) {
        WebClient webClient = WebClient.create("http://" + endpoint.getAddress());
        return webClient.get()
                .uri(apiCallDto.getPath())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Transactional
    public Endpoint addEndpoint(AddEndpointDto addEndpointDto) {
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
                .build());
    }

    @Transactional
    public String registerEndpoint(String address) {
        return endpointRepository.save(Endpoint.builder().address(address).build()).toString();
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

    public String getMoistureForEndpoint(String name, Authentication authentication) throws NoSuchEndpointException {
        var endpoint = getEndpoint(name, authentication);
        return getMoisturePercentage(endpoint);
    }

    public String getMoisturePercentage(Endpoint endpoint) {
        try {
            JSONObject moisture = new JSONObject(callApiGetResponse(endpoint, ApiCallDto.builder()
                    .path("/moisture")
                    .build()));
            return moisture.getString("moisture");
        } catch (JSONException e) {
            log.error("Not a JSON format");
            return "";
        }
    }

    public void setTakeSensorIntoAccount(String name, Boolean takeSensorIntoAccount, Authentication authentication) throws WrongResponseFromEndpoint, NoSuchEndpointException {
        var endpoint = getEndpoint(name, authentication);
        Map<String, Boolean> jsonMap = new HashMap<>();
        jsonMap.put("moistureSensorEnabled", takeSensorIntoAccount);
        var responseStatus = callEndpointApi(endpoint, ApiCallDto.builder()
                .path("/sprinkleIfMoist")
                .jsonBody(jsonMap)
                .build());
        if (responseStatus != 200) throw new WrongResponseFromEndpoint("Bad response from endpoint");
    }

    public String getCurrentFlow(String name, Authentication authentication)
            throws NoSuchEndpointException {
        var endpoint = getEndpoint(name, authentication);
        return callApiGetResponse(endpoint, ApiCallDto.builder()
                .path("/current_flow")
                .build());
    }

    public String getTotalFlow(String name, Authentication authentication) throws NoSuchEndpointException {
        var endpoint = getEndpoint(name, authentication);
        return callApiGetResponse(endpoint, ApiCallDto.builder()
                .path("/current_flow")
                .build());
    }
}