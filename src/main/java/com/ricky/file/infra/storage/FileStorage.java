package com.ricky.file.infra.storage;

import reactor.core.publisher.Mono;

public interface FileStorage {
    Mono<String> save(String filename, byte[] content);
}
