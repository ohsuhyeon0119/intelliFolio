package com.suhyeon.intelli_folio.controller;


import com.suhyeon.intelli_folio.rag.qdrant.QdrantClient;
import com.suhyeon.intelli_folio.service.RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class MethodTestController {
    private final RagService ragService;

    /**
     * 인덱싱 테스트:
     * pointId는 네가 임의로 유니크하게 넣으면 됨 (예: "u1_p10_d3_c7")
     */
    @PostMapping("/index")
    public ResponseEntity<Void> index(@RequestBody IndexRequest req) {
        ragService.insertDataToVectorDB(
                req.pointId(),
                req.userId(),
                req.projectId(),
                req.docId(),
                req.chunkId(),
                req.fileName(),
                req.chunkText()
        );
        return ResponseEntity.ok().build();
    }

    /**
     * 검색 테스트:
     * scope(userId/projectId/docId)는 null 허용(필터 조건 선택 적용)
     */
    @PostMapping("/search")
    public ResponseEntity<List<QdrantClient.SearchHit>> search(@RequestBody SearchRequest req) {
        var scope = new QdrantClient.SearchScope(req.userId(), req.projectId(), req.docId());
        var hits = ragService.search(req.query(), scope, req.limit());
        return ResponseEntity.ok(hits);
    }

    // --- DTOs ---

    public record IndexRequest(
            String pointId,
            long userId,
            long projectId,
            long docId,
            long chunkId,
            String fileName,
            String chunkText
    ) {}

    public record SearchRequest(
            String query,
            Long userId,      // nullable
            Long projectId,   // nullable
            Long docId,       // nullable
            int limit
    ) {}
}
