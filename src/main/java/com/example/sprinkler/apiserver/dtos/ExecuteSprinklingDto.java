package com.example.sprinkler.apiserver.dtos;

import com.example.sprinkler.apiserver.entities.Endpoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteSprinklingDto {
    Duration duration;
    String endpointName;
}
