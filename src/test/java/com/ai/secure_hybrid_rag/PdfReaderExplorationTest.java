package com.ai.secure_hybrid_rag;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

public class PdfReaderExplorationTest {

	@Test
	void readSamplePdf() {
		var resource = new FileSystemResource("samples/hr-leave-policy.pdf");

		var reader = new PagePdfDocumentReader(resource);

		List<Document> pages = reader.get();

		// Clean layout padding: collapse every run of whitespace into one space.
		// Build a new Document so the metadata (page_number, file_name) is kept.
		List<Document> docs = pages.stream()
				.map(d -> new Document(d.getText().replaceAll("\\s+", " ").trim(), d.getMetadata()))
				.toList();

//		System.out.println("Documents returned: " + docs.size());
//		Document first = docs.get(0);
//		String text = first.getText();
//		System.out.println("Text: " + text.substring(0, Math.min(300, text.length())));
//		System.out.println("Metadata: " + first.getMetadata());
		
		var splitter = TokenTextSplitter.builder().withChunkSize(500).build();
		
		List<Document> chunks = splitter.split(docs);
		System.out.println("Chunks: " + chunks.size());
		// Document-wide counter: the splitter's chunk_index restarts on every page,
		// but chunks.chunk_index must be unique per document (UNIQUE (document_id, chunk_index)).
		int chunkIndex = 0;
		for (Document chunk: chunks) {
			System.out.println("chunk " + chunkIndex
					+ " | page " + chunk.getMetadata().get("page_number")
					+ " | splitter index " + chunk.getMetadata().get("chunk_index")
					+ " | " + chunk.getText().length() + " chars");
			chunkIndex++;
		}
	}

}
