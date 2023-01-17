package com.example.sprinkler.apiserver.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Endpoint {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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


}
