package com.ricky.core.file.infra.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

@Component
public class LocalFileStorage implements FileStorage {
    private final Path baseDir;

    public LocalFileStorage(@Value("${file.upload.dir:uploads}") String uploadDir) {
        this.baseDir = Path.of(uploadDir);
    }

    @Override
    public Mono<String> save(String filename, byte[] content) {
        return Mono.fromCallable(() -> {
            Files.createDirectories(baseDir);
            String safeName = sanitize(filename);
            String storedName = Instant.now().toEpochMilli() + "_" + safeName;
            Path target = baseDir.resolve(storedName);
            Files.write(target, content);
            return target.toString();
        });
    }

    private String sanitize(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
