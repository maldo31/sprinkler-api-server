package com.example.sprinkler.apiserver.services;

import com.example.sprinkler.apiserver.dtos.AddEndpointDto;
import com.example.sprinkler.apiserver.entities.Endpoint;
import com.example.sprinkler.apiserver.entities.User;
import com.example.sprinkler.apiserver.repositories.EndpointRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.geo.Point;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class EndpointServiceTest {
    @Mock
    private EndpointRepository endpointRepository;
    @Mock
    private CoordinatesService coordinatesService;
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
    void getEndpoint() {
    }

    @Test
    void deleteEndpoint() {
    }

    @Test
    void getEndpoints() {
    }
}