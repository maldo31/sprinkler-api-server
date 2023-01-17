package com.example.sprinkler.apiserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddSprinklingTaskDto {

  private String minute;
  private String hour;
  private String day;
  private Integer endpointId;
}