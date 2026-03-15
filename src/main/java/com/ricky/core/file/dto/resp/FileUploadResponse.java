package com.ricky.core.file.dto.resp;

public record FileUploadResponse(
        Long fileId,
        String filename,
        long sizeBytes,
        String contentType
) {
}
