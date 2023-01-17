//package com.example.sprinkler.apiserver.configs;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.step.tasklet.Tasklet;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SprinklerJobConfig {
//  @Autowired
//  private JobBuilderFactory jobBuilders;
//
//  @Autowired
//  private StepBuilderFactory stepBuilders;
//
//  @Bean
//  public Job customerReportJob() {
//    return jobBuilders.get("customerReportJob")
//        .start(taskletStep())
//        .next(chunkStep())
//        .build();
//  }
//
//  @Bean
//  public Step taskletStep() {
//    return stepBuilders.get("taskletStep")
//        .tasklet(tasklet())
//        .build();
//  }
//
//  @Bean
//  public Tasklet tasklet() {
//    return (contribution, chunkContext) -> {
//      return RepeatStatus.FINISHED;
//    };
//  }
//
//
//}
