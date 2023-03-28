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

    private int waterQuantiny;
    private String minute = "00";
    private String hour;
    private String day;
    private int endpointId;
    private boolean smart = true;
    private long sprinklingDuration;
}
