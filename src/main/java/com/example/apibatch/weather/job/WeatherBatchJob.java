package com.example.apibatch.weather.job;

import com.example.apibatch.weather.client.OpenWeatherClient;
import com.example.apibatch.weather.processor.WeatherProcessor;
import com.example.apibatch.weather.writer.WeatherFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class WeatherBatchJob {

    private static final AtomicBoolean RUNNING = new AtomicBoolean(false);

    private final OpenWeatherClient client;
    private final WeatherProcessor processor;
    private final WeatherFileWriter writer;

    public WeatherBatchJob(OpenWeatherClient client,
                           WeatherProcessor processor,
                           WeatherFileWriter writer) {
        this.client = client;
        this.processor = processor;
        this.writer = writer;
    }

    @Value("${app.openweather.base-url}")
    private String baseUrl;

    @Value("${app.openweather.api-key}")
    private String apiKey;

    @Value("${app.openweather.city}")
    private String city;

    @Value("${app.openweather.units}")
    private String units;

    @Value("${app.openweather.lang}")
    private String lang;

    @Value("${app.storage.base-dir}")
    private String baseDir;

    @Value("${app.storage.file-name}")
    private String fileName;


    private static final Logger log = LoggerFactory.getLogger(WeatherBatchJob.class);

    @Scheduled(cron = "${batch.cron}")
    public void run() {
        if (!RUNNING.compareAndSet(false, true)) {
            log.warn("Previous job is still running. Skip this execution");
            return;
        }
        long startTime = System.currentTimeMillis();

        try {
            String raw = client.fetchRawJson(buildUrl());
            log.info("Raw weather json = {}", raw);

            String processed = processor.process(raw);
            Path saved = writer.write(processed);

            log.info("Processed weather saved to {}", saved.toAbsolutePath());

        } catch (Exception e) {
            log.error("Weather job failed", e);
        } finally {
            RUNNING.set(false);

            long elapsed = System.currentTimeMillis() - startTime;
            log.info("Weather job finished. elapsed={}ms", elapsed);
        }
    }

    private String buildUrl() {
        return UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .path("/weather")
                .queryParam("q", city)
                .queryParam("appid", apiKey)
                .queryParam("units", units)
                .queryParam("lang", lang)
                .build(true)
                .toUriString();
    }

}
