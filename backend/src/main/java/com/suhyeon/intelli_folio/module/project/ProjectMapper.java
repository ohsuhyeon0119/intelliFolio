package com.suhyeon.intelli_folio.module.project;

import com.suhyeon.intelli_folio.module.project.dto.CreateProjectCommand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProjectMapper {
    int insert(CreateProjectCommand cmd);
    Long findOwnerUserId(@Param("projectId") long projectId);
}