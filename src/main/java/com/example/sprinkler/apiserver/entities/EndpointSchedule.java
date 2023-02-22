package com.example.sprinkler.apiserver.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class EndpointSchedule {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  private Endpoint endpoint;

  private String hour;

  private String minute;

  private String day;

  @Column(columnDefinition = "boolean default true")
  private boolean smart;
  
  private Integer taskId;

}
