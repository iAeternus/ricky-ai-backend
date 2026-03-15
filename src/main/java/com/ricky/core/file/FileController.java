package com.ricky.core.file;

import com.ricky.common.api.ApiResponse;
import com.ricky.core.file.service.FileAppService;
import com.ricky.core.file.dto.resp.FileUploadResponse;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileAppService fileAppService;

    public FileController(FileAppService fileAppService) {
        this.fileAppService = fileAppService;
    }

    @PostMapping("/upload")
    public Mono<ApiResponse<FileUploadResponse>> upload(@RequestPart("file") FilePart filePart) {
        return fileAppService.upload(filePart)
                .map(ApiResponse::ok);
    }
}
