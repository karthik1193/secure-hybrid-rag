package com.ai.secure_hybrid_rag;

import org.springframework.boot.SpringApplication;

public class TestSecureHybridRagApplication {

	public static void main(String[] args) {
		SpringApplication.from(SecureHybridRagApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
