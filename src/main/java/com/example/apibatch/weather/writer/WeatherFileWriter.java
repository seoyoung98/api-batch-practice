package com.example.apibatch.weather.writer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Component
public class WeatherFileWriter {

    @Value("${app.storage.base-dir}")
    private String baseDir;

    @Value("${app.storage.file-name}")
    private String fileName;

    public Path write(String json) throws Exception {
        Path dir = Paths.get(baseDir);
        Files.createDirectories(dir);

        Path file = dir.resolve(fileName);
        Files.write(
                file,
                json.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
        return file;
    }
}
