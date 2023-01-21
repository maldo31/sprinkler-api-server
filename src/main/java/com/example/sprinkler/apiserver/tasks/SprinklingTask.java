package com.example.sprinkler.apiserver.tasks;

import com.example.sprinkler.apiserver.entities.Endpoint;
import com.example.sprinkler.apiserver.services.EndpointService;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@RequiredArgsConstructor
public class SprinklingTask implements Runnable {

    private final Endpoint endpoint;
    private final long time;

    final EndpointService endpointService;


    @Override
    public void run() {
        if (endpoint.getExpectedRainfall() < endpoint.getExpectedMinimalWatering()) {
            System.out.println(
                    new Date() + " Sprinkling task executed on thread"
                            + Thread.currentThread()
                            .getName());
            sprinkle(endpoint, this.time);
            System.out.println(new Date() + "Sprinkling ended");

        } else {
            System.out.println(
                    new Date() + " Expected rain, not sprinkling. Task started on thread "
                            + Thread.currentThread()
                            .getName());
        }
    }

    private void sprinkle(Endpoint endpoint, long sprinklingTime) {
        endpointService.relayOff(endpoint);
        try {
            Thread.sleep(sprinklingTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        endpointService.relayOn(endpoint);
    }
}
