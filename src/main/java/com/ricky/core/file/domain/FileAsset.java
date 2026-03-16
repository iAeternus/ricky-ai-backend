package com.ricky.core.file.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import static com.ricky.common.utils.ValidationUtils.requireNonNull;

@Getter
@AllArgsConstructor
public class FileAsset {
    private Long id;
    @NonNull
    private Long userId;
    private Long conversationId;
    @NonNull
    private String filename;
    @NonNull
    private String contentType;
    private long sizeBytes;
    @NonNull
    private String storagePath;
    @NonNull
    private FileStatus status;

    public void changeStatus(FileStatus newStatus) {
        this.status = requireNonNull(newStatus, "newStatus");
    }
}
