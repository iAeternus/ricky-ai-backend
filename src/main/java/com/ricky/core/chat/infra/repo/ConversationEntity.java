package com.ricky.core.chat.infra.repo;

import com.ricky.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Table("conversations")
@EqualsAndHashCode(callSuper = true)
public class ConversationEntity extends BaseEntity {

    @Column("user_id")
    private Long userId;

    @Column("model_id")
    private Long modelId;

    private String title;

    private boolean pinned;

    private String status;

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

}
