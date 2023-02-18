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

    public Integer waterQuantiny;
    private String minute = "00";
  private String hour;
  private String day;
  private Integer endpointId;
  private Boolean smart = true;
  private long sprinklingDuration;
}
