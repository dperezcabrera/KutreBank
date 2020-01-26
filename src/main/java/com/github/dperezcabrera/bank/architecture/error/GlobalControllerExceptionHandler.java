package com.github.dperezcabrera.bank.architecture.error;

import com.github.dperezcabrera.bank.architecture.common.ForbiddenException;
import com.github.dperezcabrera.bank.architecture.common.FunctionalException;
import com.github.dperezcabrera.bank.architecture.common.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(value = {ForbiddenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public MessageDto forbiddenException(ForbiddenException ex) {
        log.error("forbiddenException ", ex);
        return new MessageDto(ex.getMessage());
    }

    @ExceptionHandler(value = {FunctionalException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageDto functionalException(FunctionalException ex) {
        log.error("functionalException ", ex);
        return new MessageDto(ex.getMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageDto internalServerError(Exception ex) {
        log.error("internalServerError ", ex);
        return new MessageDto("Internal server error");
    }
}
