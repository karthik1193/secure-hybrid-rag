package com.ai.secure_hybrid_rag.ingestion;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity                          
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor  
public class DocumentEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.UUID)   // new UUID on insert
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String source;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;              

    public DocumentEntity(String title, String source) {
        this.title = title;
        this.source = source;
    }
}
