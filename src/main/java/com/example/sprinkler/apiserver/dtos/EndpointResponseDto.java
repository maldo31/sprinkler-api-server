package com.example.sprinkler.apiserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndpointResponseDto {
    private Integer id;
    private String name;
    private String address;
    private String city;
    private double latitude;
    private double longitude;
    private double expectedRainfall;
    private double expectedMinimalWatering;
    private boolean flowMeter;
    private boolean moistureSensor;
    private boolean electroValve;
    private String owner;
}
