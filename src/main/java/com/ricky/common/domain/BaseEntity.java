package com.ricky.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Data
@ToString
@EqualsAndHashCode
public class BaseEntity {

    @Id
    Long id;

    @CreatedDate
    @Column("created_at")
    LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    LocalDateTime updatedAt;

    @CreatedBy
    @Column("created_by")
    Long createdBy;

    @LastModifiedBy
    @Column("updated_by")
    Long updatedBy;

}
