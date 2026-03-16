package com.ricky.core.file.infra.mapper;

import com.ricky.core.file.domain.FileAsset;
import com.ricky.core.file.domain.FileStatus;
import com.ricky.core.file.infra.repo.FileAssetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FileAssetDataMapper {

    FileAssetEntity toEntity(FileAsset fileAsset);

    FileAsset toAggregateRoot(FileAssetEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void update(@MappingTarget FileAssetEntity entity, FileAsset fileAsset);

    default String toStatus(FileStatus status) {
        return status == null ? null : status.name();
    }

    default FileStatus toFileStatus(String status) {
        return status == null ? null : FileStatus.valueOf(status);
    }
}
