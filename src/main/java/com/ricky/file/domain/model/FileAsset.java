package com.ricky.file.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class FileAsset {
    private final Long id;
    @NonNull
    private final Long userId;
    private final Long conversationId;
    @NonNull
    private final String filename;
    @NonNull
    private final String contentType;
    private final long sizeBytes;
    @NonNull
    private final String storagePath;
    @NonNull
    private final FileStatus status;
    @NonNull
    private final Instant createdAt;

    public FileAsset withStatus(FileStatus newStatus) {
        return new FileAsset(
                id,
                userId,
                conversationId,
                filename,
                contentType,
                sizeBytes,
                storagePath,
                newStatus,
                createdAt
        );
    }
}
