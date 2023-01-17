package com.example.sprinkler.apiserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndpointLocationDto {
  private String name;
  private String latitude;
  private String longitude;
  private String country;
  private String state;

}
