package com.suhyeon.intelli_folio.module.project;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DocumentMapper {
    void insert(@Param("projectId") long projectId,
                @Param("title") String title,
                @Param("originalFilename") String originalFilename,
                @Param("contentType") String contentType,
                @Param("sizeBytes") long sizeBytes);

    Long lastInsertId();
}