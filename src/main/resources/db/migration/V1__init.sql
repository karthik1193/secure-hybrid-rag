-- Enable pgvector: adds the VECTOR column type and similarity operators.
CREATE EXTENSION IF NOT EXISTS vector;

-- One row per source document (a PDF or a Confluence page).
-- Chunks, embeddings and access tags get their own table in phase 2.
CREATE TABLE documents (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title       TEXT        NOT NULL,
    source      TEXT        NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);
