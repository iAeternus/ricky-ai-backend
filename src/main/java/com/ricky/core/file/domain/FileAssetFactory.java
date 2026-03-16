package com.ricky.core.file.domain;

import org.springframework.stereotype.Component;

@Component
public class FileAssetFactory {

    public FileAsset create(Long userId, Long conversationId, String filename, String contentType, long sizeBytes, String storagePath) {
        return new FileAsset(null, userId, conversationId, filename, contentType, sizeBytes, storagePath, FileStatus.ACTIVE);
    }

}
