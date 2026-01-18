package com.example.apibatch.weather.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenWeatherClient {
    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchRawJson(String url) {
        return restTemplate.getForObject(url, String.class);
    }
}
