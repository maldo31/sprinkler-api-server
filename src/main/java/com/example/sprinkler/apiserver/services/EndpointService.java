package com.example.sprinkler.apiserver.services;

import com.example.sprinkler.apiserver.entities.Endpoint;
import com.example.sprinkler.apiserver.repositories.EndpointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

@Service
public class EndpointService {

    @Autowired
            EndpointRepository endpointRepository;

    WebClient client = WebClient.create("http://192.168.88.200/?led=off");


    public void turnOffLed() {
        try {
            URL url = new URL("http://192.168.88.200/?relay=off");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.getResponseCode();
            System.out.println("test");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void turnOnLed(){
        try {
            URL url = new URL("http://192.168.88.200/?relay=on");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.getResponseCode();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void addEndpoint(String name,String address) {
        endpointRepository.save(Endpoint.builder().name(name).address(address).build());
        endpointRepository.findAll().forEach(System.out::println);
    }
}
