package com.example.apibatch.weather.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class WeatherProcessor {
    /*{
        "city": "Seoul",
            "temperature": -1.24,
            "humidity": 64,
            "weather": "clear sky",
            "collectedAt": "2026-01-16T23:40:00"
    }*/

    private final ObjectMapper mapper = new ObjectMapper();

    // 오직 가공만
    public String process(String rawJson) throws Exception {
        JsonNode root = mapper.readTree(rawJson);

        ObjectNode result = mapper.createObjectNode();
        result.put("city", root.path("name").asText());
        result.put("temperature", root.path("main").path("temp").asDouble());
        result.put("humidity", root.path("main").path("humidity").asInt());
        result.put("weather", root.path("weather").get(0).path("description").asText());
        result.put("collectedAt", LocalDateTime.now().toString());

        return mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(result);

    }

}
