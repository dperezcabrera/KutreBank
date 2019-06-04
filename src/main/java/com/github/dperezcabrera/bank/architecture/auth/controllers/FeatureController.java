package com.github.dperezcabrera.bank.architecture.auth.controllers;

import com.github.dperezcabrera.bank.architecture.auth.dtos.FeaturesDto;
import com.github.dperezcabrera.bank.architecture.auth.services.FeatureService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/features")
@AllArgsConstructor
public class FeatureController {

	private FeatureService featureService;

	@GetMapping("/all")
	@PreAuthorize("@roleChecker.isAdmin()")
	public ResponseEntity<List<String>> getAll() {
		return ResponseEntity.ok(featureService.getAllFeatures());
	}

	@GetMapping("/active")
	@PreAuthorize("@roleChecker.isAdmin()")
	public ResponseEntity<List<String>> getActives() {
		return ResponseEntity.ok(featureService.getActiveFeatures());
	}

	@PutMapping("/active")
	@PreAuthorize("@roleChecker.isAdmin()")
	public ResponseEntity<Void> actives(@RequestBody FeaturesDto features) {
		featureService.setActiveFeatures(features.getFeatures());
		return ResponseEntity.ok().body(null);
	}

	@PutMapping("/active-all")
	@PreAuthorize("@roleChecker.isAdmin()")
	public ResponseEntity<Void> activeAll() {
		featureService.setActiveFeatures(featureService.getAllFeatures());
		return ResponseEntity.ok().body(null);
	}

	@PutMapping("/deactivate")
	@PreAuthorize("@roleChecker.isAdmin()")
	public ResponseEntity<Void> deactivates(@RequestBody FeaturesDto features) {
		List<String> activeFeatures = featureService.getActiveFeatures().stream()
				.filter(f -> !features.getFeatures().contains(f))
				.collect(Collectors.toList());
		featureService.setActiveFeatures(activeFeatures);
		return ResponseEntity.ok().body(null);
	}
}
