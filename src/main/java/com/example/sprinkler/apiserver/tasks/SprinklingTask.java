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
        if (!addSprinklingTaskDto.getSmart() ||
                endpoint.getExpectedRainfall() < endpoint.getExpectedMinimalWatering() ||
                endpoint.getSoilMoisture() < 0.6
        ) {
            log.info(
                    new Date() + " Sprinkling task executed on thread"
                            + Thread.currentThread()
                            .getName());
            if (addSprinklingTaskDto.waterQuantiny != null) {
                sprinkle(endpoint, addSprinklingTaskDto.waterQuantiny);
            } else {
                sprinkle(endpoint, addSprinklingTaskDto.getSprinklingDuration());
            }

            log.info(new Date() + "Sprinkling dispatched to sprinkler");

        } else {
            log.info(
                    new Date() + " Expected rain, not sprinkling. Task started on thread "
                            + Thread.currentThread()
                            .getName());
        }
    }

    private void sprinkle(Endpoint endpoint, long sprinklingTime) {
        endpointService.relayOn(endpoint);
        try {
            Thread.sleep(sprinklingTime);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        endpointService.relayOn(endpoint);
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
