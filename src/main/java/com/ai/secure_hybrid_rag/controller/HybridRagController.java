package com.ai.secure_hybrid_rag.controller;

import java.util.Locale;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.ai.secure_hybrid_rag.ingestion.IngestionResult;
import com.ai.secure_hybrid_rag.service.IngestionService;

/**
 * Upload endpoint for documents. Kept thin on purpose: it validates the
 * request (this is the security gate) and hands off to IngestionService.
 */
@RestController
@RequestMapping("/api/documents")
public class HybridRagController {

	// Only these departments may be used as access tags.
	private static final Set<String> ALLOWED_DEPARTMENTS = Set.of("finance", "hr", "engineering", "qa");

	private final IngestionService ingestionService;

	HybridRagController(IngestionService ingestionService) {
		this.ingestionService = ingestionService;
	}

	@PostMapping
	public ResponseEntity<IngestionResult> upload(
			@RequestParam("file") MultipartFile file,
			@RequestParam String title,
			@RequestParam String department,
			@RequestParam short clearanceLevel) {

		if (file.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "file is empty");
		}

		String dept = department.trim().toLowerCase(Locale.ROOT);
		if (!ALLOWED_DEPARTMENTS.contains(dept)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"unknown department: " + department + " (allowed: " + ALLOWED_DEPARTMENTS + ")");
		}

		if (clearanceLevel < 0 || clearanceLevel > 3) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "clearanceLevel must be between 0 and 3");
		}

		IngestionResult result = ingestionService.ingest(file.getResource(), title.trim(), dept, clearanceLevel);
		return ResponseEntity.status(HttpStatus.CREATED).body(result);
	}
}
