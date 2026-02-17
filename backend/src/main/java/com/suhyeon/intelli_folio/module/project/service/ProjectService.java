package com.suhyeon.intelli_folio.module.project.service;

import com.suhyeon.intelli_folio.core.rag.qdrant.PointIdBuilder;
import com.suhyeon.intelli_folio.module.project.domain.Document;
import com.suhyeon.intelli_folio.module.project.domain.Project;
import com.suhyeon.intelli_folio.module.project.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.suhyeon.intelli_folio.module.project.DocumentMapper;
import com.suhyeon.intelli_folio.module.project.ProjectMapper;
import com.suhyeon.intelli_folio.module.project.util.TextChunker;
import com.suhyeon.intelli_folio.module.rag.RagService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectMapper projectMapper;
    private final DocumentMapper documentMapper;
    private final RagService ragService;

    public long createProject(long userId, String name, String summary) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Project name is required.");
        }

        var cmd = new CreateProjectCommand();
        cmd.setUserId(userId);
        cmd.setName(name.trim());
        cmd.setSummary(summary);

        int rows = projectMapper.insert(cmd);
        if (rows != 1 || cmd.getId() == null) {
            throw new IllegalStateException("Failed to create project.");
        }
        return cmd.getId();
    }

    @Transactional
    public UploadDocumentsResponse uploadDocuments(long userId, long projectId, List<MultipartFile> files) {
        assertOwner(userId, projectId);

        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("No files uploaded.");
        }

        List<UploadedDocumentResult> results = new ArrayList<>();

        for (MultipartFile file : files) {
            validateTextFile(file);

            String filename = file.getOriginalFilename() == null ? "document.txt" : file.getOriginalFilename();
            String title = TextChunker.stripExtension(filename);

            String content;
            try {
                content = new String(file.getBytes(), StandardCharsets.UTF_8);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to read file: " + filename, e);
            }

            // 1) insert documents row
            var docCmd = new CreateDocumentCommand();
            docCmd.setProjectId(projectId);
            docCmd.setTitle(title);
            docCmd.setOriginalFilename(filename);
            docCmd.setContentType(file.getContentType());
            docCmd.setSizeBytes(file.getSize());

            int rows = documentMapper.insert(docCmd);
            if (rows != 1 || docCmd.getId() == null) {
                throw new IllegalStateException("Failed to create document row.");
            }
            Long docId = docCmd.getId();


            // 2) chunk & insert into vector db
            List<String> chunks = TextChunker.chunkByChars(content, 1600, 200);

            int inserted = 0;
            for (int i = 0; i < chunks.size(); i++) {
                String chunkText = chunks.get(i);

                String pointId = PointIdBuilder.forChunk(userId,projectId,docId,i);
                ragService.insertDataToVectorDB(
                        pointId,
                        userId,
                        projectId,
                        docId,
                        i,
                        filename,
                        chunkText
                );
                inserted++;
            }

            results.add(new UploadedDocumentResult(docId, filename, inserted));
        }

        return new UploadDocumentsResponse(projectId, results);
    }

    public MyProjectsResponse getMyProjects(long userId) {

        // 1) projects
        List<Project> projects = projectMapper.findByUserId(userId);
        if (projects.isEmpty()) {
            return new MyProjectsResponse(List.of());
        }

        // 2) documents by IN(projectIds)
        List<Long> projectIds = projects.stream().map(Project::getId).toList();
        List<Document> docs = documentMapper.findByProjectIds(projectIds);

        // 3) group docs by projectId
        Map<Long, List<DocumentSummary>> docsByProjectId = new HashMap<>();
        for (var d : docs) {
            docsByProjectId
                    .computeIfAbsent(d.getProjectId(), k -> new ArrayList<>())
                    .add(new DocumentSummary(d.getId(), d.getTitle(), d.getOriginalFilename(), d.getSizeBytes()));
        }

        // 4) compose response
        List<ProjectWithDocuments> result = new ArrayList<>();
        for (var p : projects) {
            var list = docsByProjectId.getOrDefault(p.getId(), List.of());
            result.add(new ProjectWithDocuments(p.getId(), p.getName(), p.getSummary(), list));
        }

        return new MyProjectsResponse(result);
    }




    private void assertOwner(long userId, long projectId) {
        Long ownerId = projectMapper.findOwnerUserId(projectId);
        if (ownerId == null) throw new IllegalArgumentException("Project not found: " + projectId);
        if (ownerId != userId) throw new SecurityException("Not project owner.");
    }

    private void validateTextFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Empty file.");
        }
        String name = file.getOriginalFilename();
        if (name == null) throw new IllegalArgumentException("File name is required.");

        String lower = name.toLowerCase();
        boolean ok = lower.endsWith(".md") || lower.endsWith(".txt");
        if (!ok) {
            throw new IllegalArgumentException("Only .md and .txt are allowed: " + name);
        }
    }
}