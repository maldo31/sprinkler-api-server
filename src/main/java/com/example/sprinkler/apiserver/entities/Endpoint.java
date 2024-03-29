package com.example.sprinkler.apiserver.entities;


import com.example.sprinkler.apiserver.dtos.EndpointResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Endpoint {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String address;
    private String city;
    private Double latitude;
    private Double longitude;
    private Double expectedRainfall;
    private Double expectedMinimalWatering;
    private boolean flowMeter;
    private boolean moistureSensor;
    private boolean electroValve;

    private boolean isAdded;
    @ManyToOne
    private User user;

    public boolean isOwner(Principal principal) {
        return this.user.getEmail().equals(((User) principal).getEmail());
    }

    public EndpointResponseDto toEndpointResponseDto() {
        return EndpointResponseDto.builder()
                .id(id)
                .name(name)
                .address(address)
                .city(city)
                .expectedRainfall(expectedRainfall)
                .expectedMinimalWatering(expectedMinimalWatering)
                .flowMeter(flowMeter)
                .moistureSensor(moistureSensor)
                .electroValve(electroValve)
                .owner(user.getEmail())
                .build();
    }


    public Double getSoilMoisture() {
        return 0.3;
    }
}
