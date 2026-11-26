package com.ai.secure_hybrid_rag.ingestion;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IngestionResult {

	private UUID id;
	private int size;

    public IngestionResult(UUID id, int size) {
        this.id = id;
        this.size = size;
    }
}
