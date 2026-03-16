package com.ricky.core.chat.infra.repo;

import com.ricky.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Table("messages")
@EqualsAndHashCode(callSuper = true)
public class MessageEntity extends BaseEntity {

    @Column("conversation_id")
    private Long conversationId;

    @Column("user_id")
    private Long userId;

    private String role;

    private String content;

    @Column("token_count")
    private Integer tokenCount;

    private String status;

    @Column("created_at")
    private Instant createdAt;

}
