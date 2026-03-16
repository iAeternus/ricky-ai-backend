package com.ricky.core.file.domain;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class FileAssetFactory {

    public FileAsset create(Long userId, Long conversationId, String filename, String contentType, long sizeBytes, String storagePath) {
        Instant now = Instant.now();
        return new FileAsset(null, userId, conversationId, filename, contentType, sizeBytes, storagePath, FileStatus.ACTIVE, now);
    }

}
