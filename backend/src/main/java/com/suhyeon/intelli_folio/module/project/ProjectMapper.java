package com.suhyeon.intelli_folio.module.project;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProjectMapper {
    void insert(@Param("userId") long userId,
                @Param("name") String name,
                @Param("summary") String summary);

    Long lastInsertId();

    Long findOwnerUserId(@Param("projectId") long projectId);
}