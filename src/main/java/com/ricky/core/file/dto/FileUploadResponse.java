package com.ricky.core.file.dto;

public record FileUploadResponse(
        Long fileId,
        String filename,
        long sizeBytes,
        String contentType
) {
}
