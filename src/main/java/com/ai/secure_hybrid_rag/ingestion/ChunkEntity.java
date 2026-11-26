package com.ai.secure_hybrid_rag.ingestion;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "chunks")
@NoArgsConstructor
public class ChunkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)   // new UUID on insert
    private UUID id;

    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    @Column(name = "chunk_index", nullable = false)
    private int chunkIndex;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String department;

    @Column(name = "clearance_level", nullable = false)
    private short clearanceLevel;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    public ChunkEntity(UUID documentId, int chunkIndex, String content, String department, short clearanceLevel) {
        this.documentId = documentId;
        this.chunkIndex = chunkIndex;
        this.content = content;
        this.department = department;
        this.clearanceLevel = clearanceLevel;
    }
}
