package com.ricky.core.file.infra.repo;

import com.ricky.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Table("file_assets")
@EqualsAndHashCode(callSuper = true)
public class FileAssetEntity extends BaseEntity {

    @Column("user_id")
    private Long userId;

    @Column("conversation_id")
    private Long conversationId;

    private String filename;

    @Column("content_type")
    private String contentType;

    @Column("size_bytes")
    private long sizeBytes;

    @Column("storage_path")
    private String storagePath;

    private String status;

    @Column("created_at")
    private Instant createdAt;

}
