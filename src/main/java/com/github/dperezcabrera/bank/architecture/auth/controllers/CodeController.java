package com.github.dperezcabrera.bank.architecture.auth.controllers;

import com.github.dperezcabrera.bank.architecture.auth.services.CodeService;
import com.github.dperezcabrera.bank.architecture.auth.dtos.CodeDto;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/codes")
@AllArgsConstructor
public class CodeController {

	private CodeService codeService;

	@GetMapping
	@PreAuthorize("@roleChecker.isAdmin()")
	public ResponseEntity<List<CodeDto>> getAll() {
		return ResponseEntity.ok(codeService.getAll());
	}

	@PostMapping
	@PreAuthorize("@roleChecker.isAdmin()")
	public ResponseEntity<String> createCode(@RequestBody CodeDto codeDto) {
		codeService.createCode(codeDto.getCode(), codeDto.getAmount());
		return ResponseEntity.ok().build();
	}
}
