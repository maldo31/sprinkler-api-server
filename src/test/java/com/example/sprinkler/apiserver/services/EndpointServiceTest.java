package com.example.sprinkler.apiserver.services;

import com.example.sprinkler.apiserver.dtos.AddEndpointDto;
import com.example.sprinkler.apiserver.entities.Endpoint;
import com.example.sprinkler.apiserver.entities.User;
import com.example.sprinkler.apiserver.exceptions.NoSuchEndpointException;
import com.example.sprinkler.apiserver.repositories.EndpointRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.geo.Point;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
class EndpointServiceTest {
    @Mock
    private EndpointRepository endpointRepository;
    @Mock
    private CoordinatesService coordinatesService;

    @Mock
    private Authentication authentication;
    @Mock
    private UsersService usersService;

    @InjectMocks
    EndpointService endpointService;

    Endpoint endpoint;
    AddEndpointDto addEndpointDto;

    @BeforeEach
    public void setup() {
        String address = "192.168.1.1";
        String name = "test";
        endpoint = Endpoint.builder()
                .address(address)
                .name(name)
                .latitude(0.0)
                .longitude(0.0)
                .build();
        addEndpointDto = AddEndpointDto.builder()
                .address(address)
                .name(name)
                .build();
    }

    @Test
    void addEndpointPositive() {
        //Given
        given(endpointRepository.save(any(Endpoint.class))).willReturn(endpoint);
        given(coordinatesService.getCoordinates(any())).willReturn(new Point(0, 0));
        given(usersService.getUserFromAuthentication(any())).willReturn(User.builder().email("test").build());

        //When
        var endpointResponse = endpointService.addEndpoint(addEndpointDto);

        //Then
        Assertions.assertEquals(endpoint, endpointResponse);

    }

    @Test
    void registerEndpoint() {
    }

    @Test
    void getEndpoints() {
        List<Endpoint> expectedEndpoints =  Arrays.asList(new Endpoint(), new Endpoint());
        //Given
        var user = new User();
        given(endpointRepository.findAllByUser(user)).willReturn(expectedEndpoints);
        given(usersService.getUserFromAuthentication(authentication)).willReturn(user);

        //When
        var endpointsList = endpointService.getEndpoints(authentication);
        //Then
        Assertions.assertEquals(expectedEndpoints,endpointsList);
    }

    @Test
    void deleteEndpoint() {
    }

    @Test
    void getEndpoint() throws NoSuchEndpointException {
        //Given
        var user = new User();
        var endpointName = "endpointName";
        var expectedEndpoint = new Endpoint();
        given(endpointRepository.findEndpointByNameAndUser(endpointName,user)).willReturn(Optional.of(expectedEndpoint));
        given(usersService.getUserFromAuthentication(authentication)).willReturn(user);

        //When
        var returnedEndpoint = endpointService.getEndpoint(endpointName,authentication);
        //Then
        Assertions.assertEquals(expectedEndpoint,returnedEndpoint);
    }
}