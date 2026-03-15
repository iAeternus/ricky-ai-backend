package com.ricky.core.file.service;

import com.ricky.core.file.dto.resp.FileUploadResponse;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface FileAppService {

    Mono<FileUploadResponse> upload(FilePart filePart);
}
