package com.example.sprinkler.apiserver.services;

import com.example.sprinkler.apiserver.dtos.AddEndpointDto;
import com.example.sprinkler.apiserver.entities.Endpoint;
import com.example.sprinkler.apiserver.entities.User;
import com.example.sprinkler.apiserver.exceptions.NoSuchEndpointException;
import com.example.sprinkler.apiserver.repositories.EndpointRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Service
public class EndpointService {

    @Autowired
    EndpointRepository endpointRepository;

    @Autowired
    CoordinatesService coordinatesService;

    @Autowired
    UsersService usersService;

    public String turnOffLed(String name, Authentication authentication) {
        var endpoint = endpointRepository.findEndpointByNameAndUser(name, getUserFromAuthentication(authentication));
        if (endpoint.isPresent()) {
            int x = callEndpointApi(endpoint.get(), "/?relay=off");
            if (x != 200) return "Bad response";
            return "Turned Off led";
        }
        return "No such endpoint";
    }

    public void turnOffLed(Endpoint endpoint) {
        callEndpointApi(endpoint, "/?relay=off");
    }

    public String turnOnLed(String name, Authentication authentication) {
        var endpoint = endpointRepository.findEndpointByNameAndUser(name, getUserFromAuthentication(authentication));
        if (endpoint.isPresent()) {
            int x = callEndpointApi(endpoint.get(), "/?relay=on");
            if (x != 200) return "Bad response";
            return "Turned On led";
        }
        return "No such endpoint";
    }

    public void turnOnLed(Endpoint endpoint) {
        callEndpointApi(endpoint, "/?relay=off");
    }

    private int callEndpointApi(Endpoint endpoint, String apiCall) {
        try {
            URL url = new URL("http://" + endpoint.getAddress() + apiCall);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            return con.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                .user(getUserFromAuthentication(authentication))
                .build()).toString();
    }

    public Endpoint getEndpoint(String name, Authentication authentication) throws NoSuchEndpointException {
        return endpointRepository.findEndpointByNameAndUser(name, getUserFromAuthentication(authentication)).orElseThrow(NoSuchEndpointException::new);
    }

    @Transactional
    public void deleteEndpoint(String name, Authentication authentication) {
        endpointRepository.deleteByNameAndUser(name,getUserFromAuthentication(authentication));
    }

    public List<Endpoint> getEndpoints(Authentication authentication) {
        return endpointRepository.findAllByUser(getUserFromAuthentication(authentication));
    }

    private User getUserFromAuthentication(Authentication authentication) {
        return usersService.getUserByUsername(authentication.getName());
    }

}
