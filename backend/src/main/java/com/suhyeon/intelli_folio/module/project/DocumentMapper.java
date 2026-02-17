package com.suhyeon.intelli_folio.module.project;

import com.suhyeon.intelli_folio.module.project.dto.CreateDocumentCommand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DocumentMapper {
    int insert(CreateDocumentCommand cmd);

}