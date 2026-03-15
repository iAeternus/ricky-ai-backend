package com.ricky.core.file;

import com.ricky.core.file.service.impl.FileAppServiceImpl;
import com.ricky.core.file.dto.resp.FileUploadResponse;
import com.ricky.core.file.infra.FileStorage;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileAppServiceTest {

    @Test
    void uploadStoresFileAndReturnsResponse() {
        FileStorage storage = (filename, content) -> Mono.just("uploads/" + filename);
        FileAppServiceImpl service = new FileAppServiceImpl(storage, 1024);

        byte[] data = "hello".getBytes(StandardCharsets.UTF_8);
        TestFilePart part = new TestFilePart("file", "test.txt", MediaType.TEXT_PLAIN, data);

        Mono<FileUploadResponse> responseMono = service.upload(part);

        StepVerifier.create(responseMono)
                .assertNext(resp -> {
                    assertEquals("test.txt", resp.filename());
                    assertEquals(5L, resp.sizeBytes());
                    assertEquals("text/plain", resp.contentType());
                })
                .verifyComplete();
    }

    private static class TestFilePart implements FilePart {
        private final String name;
        private final String filename;
        private final HttpHeaders headers;
        private final byte[] data;

        private TestFilePart(String name, String filename, MediaType mediaType, byte[] data) {
            this.name = name;
            this.filename = filename;
            this.data = data;
            this.headers = new HttpHeaders();
            this.headers.setContentType(mediaType);
        }

        @Override
        public String filename() {
            return filename;
        }

        @Override
        public Mono<Void> transferTo(Path dest) {
            return Mono.error(new UnsupportedOperationException("Not supported in test"));
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public HttpHeaders headers() {
            return headers;
        }

        @Override
        public Flux<DataBuffer> content() {
            DataBuffer buffer = new DefaultDataBufferFactory().wrap(data);
            return Flux.just(buffer);
        }
    }
}
