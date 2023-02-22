package com.example.sprinkler.apiserver.tasks;

import com.example.sprinkler.apiserver.dtos.AddSprinklingTaskDto;
import com.example.sprinkler.apiserver.entities.Endpoint;
import com.example.sprinkler.apiserver.services.EndpointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class SprinklingTask implements Runnable {

    private final Endpoint endpoint;
    private final AddSprinklingTaskDto addSprinklingTaskDto;

    final EndpointService endpointService;

    @Override
    public void run() {
        if (!addSprinklingTaskDto.isSmart()) {
            if (endpoint.getExpectedRainfall() > endpoint.getExpectedMinimalWatering()) {
                log.info(
                        new Date() + "Expected rain, not sprinkling. Task started on thread "
                                + Thread.currentThread()
                                .getName());
                return;
            }
            if (Double.parseDouble(endpointService.getMoisturePercentage(endpoint)) > 0.95) {
                log.info(
                        new Date() + "Soil moisture is high enough, not Sprinkling. Task started on thread"
                                + Thread.currentThread()
                                .getName());
                return;
            }
            dispatchSprinklingTask();
        } else {
            dispatchSprinklingTask();
        }
    }

    private void dispatchSprinklingTask() {
        log.info(
                new Date() + "Dispatching sprinkling task to sprinkler.Task executed on thread"
                        + Thread.currentThread()
                        .getName());
        if (addSprinklingTaskDto.getWaterQuantiny() != 0) {
            sprinkle(endpoint, addSprinklingTaskDto.getWaterQuantiny());
        } else {
            sprinkle(endpoint, addSprinklingTaskDto.getSprinklingDuration());
        }
        log.info(new Date() + "Sprinkling dispatched to sprinkler");
    }

    private void sprinkle(Endpoint endpoint, long sprinklingTime) {
        endpointService.relayOn(endpoint);
        try {
            Thread.sleep(sprinklingTime);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        endpointService.relayOff(endpoint);
    }

    private void sprinkle(Endpoint endpoint, Integer waterQuantity) {
        endpointService.relayOn(endpoint);
        try {
            Thread.sleep(waterQuantity);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        endpointService.relayOn(endpoint);
    }
}
