package com.github.dperezcabrera.bank.architecture.auth.services;

import com.github.dperezcabrera.bank.architecture.auth.dtos.Features;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FeatureService {

	private Set<String> allFeatures = Arrays.stream(Features.values()).map(Features::name).collect(Collectors.toSet());
	private Set<String> activeFeatures = new HashSet<>();

	public List<String> getAllFeatures() {
		return new ArrayList<>(allFeatures);
	}

	public synchronized List<String> getActiveFeatures() {
		return new ArrayList<>(activeFeatures);
	}

	public synchronized void setActiveFeatures(@NonNull List<String> activeFeatures) {
		this.activeFeatures = activeFeatures.stream().filter(allFeatures::contains).collect(Collectors.toSet());
	}

	public synchronized boolean isActive(@NonNull Features feature) {
		return activeFeatures.contains(feature.name());
	}
}
