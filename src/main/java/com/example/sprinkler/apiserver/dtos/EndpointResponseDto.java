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
    private Double latitude;
    private Double longitude;
    private Double expectedRainfall;
    private Double expectedMinimalWatering;
    private boolean flowMeter;
    private boolean moistureSensor;
    private boolean electroValve;
    private String owner;
}
