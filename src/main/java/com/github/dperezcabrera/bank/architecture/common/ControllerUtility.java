package com.github.dperezcabrera.bank.architecture.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ControllerUtility {

    private static final ResponseEntity<?> FORBIDDEN_RESPONSE = ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

    public static <T> ResponseEntity<T> forbidden() {
        return (ResponseEntity<T>) FORBIDDEN_RESPONSE;
    }
}
