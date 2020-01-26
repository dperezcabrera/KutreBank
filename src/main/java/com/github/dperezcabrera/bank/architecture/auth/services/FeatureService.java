package com.github.dperezcabrera.bank.architecture.auth.services;

import com.github.dperezcabrera.bank.architecture.auth.dtos.FeatureDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.Features;
import com.github.dperezcabrera.bank.architecture.common.ForbiddenException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FeatureService {

    private static final int DEFAULT_MAX_COUNTER = 10;
    private Set<String> allFeatures = Arrays.stream(Features.values()).map(Features::name).collect(Collectors.toSet());
    private Set<String> activeFeatures = new HashSet<>(allFeatures);
    private Map<String, Integer> featuresCounter = new HashMap<>();
    private Map<String, Integer> featuresMaxCounter = new HashMap<>();
    private Map<String, LocalDateTime> featuresInactiveDate = new HashMap<>();

    public synchronized List<FeatureDto> getAllFeatures() {
        return allFeatures.stream().map(n -> new FeatureDto(n, featuresInactiveDate.get(n))).collect(Collectors.toList());
    }

    public synchronized List<String> getActiveFeatures() {
        return new ArrayList<>(activeFeatures);
    }

    public synchronized void resetFeatureCounters() {
        featuresCounter = new HashMap<>();
        featuresInactiveDate = new HashMap<>();
    }

    public synchronized boolean isActive(@NonNull Features feature) {
        return activeFeatures.contains(feature.name());
    }

    public synchronized void useFeature(@NonNull Features feature) {
        incrementFeatureCounter(feature.name());
    }

    private void incrementFeatureCounter(String featureName){
        int counter = featuresCounter.getOrDefault(featureName, 0) + 1;
        int maxCounter = featuresMaxCounter.getOrDefault(featureName, DEFAULT_MAX_COUNTER);
        if (counter >= maxCounter) {
            activeFeatures.remove(featureName);
            featuresInactiveDate.put(featureName, LocalDateTime.now());
        }
        featuresCounter.put(featureName, counter);
    }
    
    public synchronized void checkFeature(@NonNull Features feature) {
        if (!activeFeatures.contains(feature.name())) {
            throw new ForbiddenException("Permiso denegado");
        }
        incrementFeatureCounter(feature.name());
    }
}
