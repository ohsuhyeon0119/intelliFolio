package com.suhyeon.intelli_folio.module.project;

import com.suhyeon.intelli_folio.module.project.domain.Project;
import com.suhyeon.intelli_folio.module.project.dto.CreateProjectCommand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectMapper {
    int insert(CreateProjectCommand cmd);
    Long findOwnerUserId(@Param("projectId") long projectId);
    List<Project> findByUserId(@Param("userId") long userId);

}