package com.github.dperezcabrera.bank.architecture.common;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String string) {
        super(string);
    }
}
