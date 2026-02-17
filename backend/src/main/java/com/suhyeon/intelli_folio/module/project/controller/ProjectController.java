package com.suhyeon.intelli_folio.module.project.controller;

import com.suhyeon.intelli_folio.core.config.OpenApiConfig;
import com.suhyeon.intelli_folio.module.project.dto.CreateProjectRequest;
import com.suhyeon.intelli_folio.module.project.dto.CreateProjectResponse;
import com.suhyeon.intelli_folio.module.project.dto.MyProjectsResponse;
import com.suhyeon.intelli_folio.module.project.dto.UploadDocumentsResponse;
import com.suhyeon.intelli_folio.module.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    @PostMapping
    public CreateProjectResponse createProject(
            @Parameter(hidden = true)
            @RequestAttribute("userId") long userId,
            @RequestBody CreateProjectRequest req
    ) {
        long projectId = projectService.createProject(userId, req.name(), req.summary());
        return new CreateProjectResponse(projectId);
    }

    @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
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

    @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    @GetMapping("/me")
    public MyProjectsResponse myProjects(@Parameter(hidden = true)
                                             @RequestAttribute("userId") long userId){
        return projectService.getMyProjects(userId);
    }

}