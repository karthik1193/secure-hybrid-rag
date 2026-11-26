package com.ai.secure_hybrid_rag.ingestion;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChunkRepository extends JpaRepository<ChunkEntity, UUID> {

}
