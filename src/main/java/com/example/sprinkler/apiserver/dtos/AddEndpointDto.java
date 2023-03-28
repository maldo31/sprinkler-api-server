package com.example.sprinkler.apiserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddEndpointDto {
  private String name;
  private String address;
  private String city;
  private double expectedMinimalWatering = 10.0;
}


