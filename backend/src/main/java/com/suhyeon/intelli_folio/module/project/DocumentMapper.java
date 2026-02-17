package com.suhyeon.intelli_folio.module.project;

import com.suhyeon.intelli_folio.module.project.domain.Document;
import com.suhyeon.intelli_folio.module.project.dto.CreateDocumentCommand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DocumentMapper {
    int insert(CreateDocumentCommand cmd);
    List<Document> findByProjectIds(@Param("projectIds") List<Long> projectIds);
}

