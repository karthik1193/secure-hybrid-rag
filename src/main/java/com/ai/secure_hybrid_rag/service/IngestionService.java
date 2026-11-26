package com.ai.secure_hybrid_rag.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.secure_hybrid_rag.ingestion.ChunkEntity;
import com.ai.secure_hybrid_rag.ingestion.ChunkRepository;
import com.ai.secure_hybrid_rag.ingestion.DocumentEntity;
import com.ai.secure_hybrid_rag.ingestion.DocumentRepository;
import com.ai.secure_hybrid_rag.ingestion.IngestionResult;

@Service
public class IngestionService {

	private final DocumentRepository documentRepository; 
	private final ChunkRepository chunkRepository;
	private final TokenTextSplitter splitter =  TokenTextSplitter.builder().withChunkSize(500).build();
	
	public IngestionService(DocumentRepository documentRepository, ChunkRepository chunkRepository) {
		this.documentRepository = documentRepository;
		this.chunkRepository = chunkRepository;
	}
	
	@Transactional
	public IngestionResult ingest(Resource pdf, String title, String department, short clearanceLevel) {
		
		var reader = new PagePdfDocumentReader(pdf);
		List<Document> pages = reader.get();
		//7c0038b1-9903-4edc-b719-d75d438571ef
		//d22ea987-5146-49dc-8648-31105c75eef8
		List<Document> docs = pages.stream()
				.map(d -> new Document(d.getText().replaceAll("\\s+", " ").trim(), d.getMetadata()))
				.toList();
		
		List<Document> chunks =  splitter.split(docs);
		//"d41264e7-b716-459e-a8d7-cd963c1eb0a7"
		DocumentEntity docEntity = documentRepository.save(new DocumentEntity(title, pdf.getFilename()));
		
		List<ChunkEntity> chunkEntity = new ArrayList<>();
		
		for (int i = 0; i <chunks.size(); i++) {
			chunkEntity.add(new ChunkEntity(docEntity.getId(), i, chunks.get(i).getText(), department, clearanceLevel));
		}
		
		 chunkRepository.saveAll(chunkEntity);
		return new IngestionResult(docEntity.getId(), chunkEntity.size());
	}
	
}
