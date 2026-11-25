CREATE TABLE chunks (
                        id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        document_id     UUID NOT NULL REFERENCES documents(id) ON DELETE CASCADE,
                        chunk_index     INT NOT NULL,
                        content         TEXT NOT NULL,
                        department      TEXT NOT NULL,
                        clearance_level SMALLINT NOT NULL CHECK (clearance_level BETWEEN 0 AND 3),
                        created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
                        UNIQUE (document_id, chunk_index)
);

CREATE INDEX idx_chunks_access ON chunks (department, clearance_level);