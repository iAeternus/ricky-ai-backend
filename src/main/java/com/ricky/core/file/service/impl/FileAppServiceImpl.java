package com.ricky.core.file.service.impl;

import com.ricky.common.exception.BizException;
import com.ricky.common.exception.ErrorCode;
import com.ricky.core.file.dto.resp.FileUploadResponse;
import com.ricky.core.file.infra.FileStorage;
import com.ricky.core.file.service.FileAppService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class FileAppServiceImpl implements FileAppService {
    private final FileStorage fileStorage;
    private final long maxSizeBytes;

    public FileAppServiceImpl(FileStorage fileStorage, @Value("${file.upload.max-size-bytes:10485760}") long maxSizeBytes) {
        this.fileStorage = fileStorage;
        this.maxSizeBytes = maxSizeBytes;
    }

    @Override
    public Mono<FileUploadResponse> upload(FilePart filePart) {
        return filePart.content()
                .reduce(new ByteArrayCollector(), ByteArrayCollector::append)
                .flatMap(collector -> {
                    if (collector.size() > maxSizeBytes) {
                        return Mono.error(new BizException(ErrorCode.FILE_TOO_LARGE));
                    }
                    String contentType = filePart.headers().getContentType() == null
                            ? "application/octet-stream"
                            : filePart.headers().getContentType().toString();
                    return fileStorage.save(filePart.filename(), collector.toBytes())
                            .map(path -> new FileUploadResponse(null, filePart.filename(), collector.size(), contentType));
                });
    }

    private static class ByteArrayCollector {
        private byte[] data = new byte[0];

        ByteArrayCollector append(DataBuffer buffer) {
            int readable = buffer.readableByteCount();
            byte[] bytes = new byte[readable];
            buffer.read(bytes);
            byte[] merged = new byte[data.length + bytes.length];
            System.arraycopy(data, 0, merged, 0, data.length);
            System.arraycopy(bytes, 0, merged, data.length, bytes.length);
            data = merged;
            return this;
        }

        int size() {
            return data.length;
        }

        byte[] toBytes() {
            return data;
        }
    }
}
