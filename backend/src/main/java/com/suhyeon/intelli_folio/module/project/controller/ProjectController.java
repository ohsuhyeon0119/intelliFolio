package com.suhyeon.intelli_folio.module.project.controller;

import com.suhyeon.intelli_folio.module.project.dto.CreateProjectRequest;
import com.suhyeon.intelli_folio.module.project.dto.CreateProjectResponse;
import com.suhyeon.intelli_folio.module.project.dto.UploadDocumentsResponse;
import com.suhyeon.intelli_folio.module.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public CreateProjectResponse createProject(
            @Parameter(hidden = true)
            @RequestAttribute("userId") long userId,
            @RequestBody CreateProjectRequest req
    ) {
        long projectId = projectService.createProject(userId, req.name(), req.summary());
        return new CreateProjectResponse(projectId);
    }


    @PostMapping(
            value = "/{projectId}/documents",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public UploadDocumentsResponse uploadDocuments(
            @Parameter(hidden = true)
            @RequestAttribute("userId") long userId,
            @PathVariable long projectId,
            @RequestPart("files") List<MultipartFile> files
    ) {
        return projectService.uploadDocuments(userId, projectId, files);
    }

}