package com.github.dperezcabrera.bank.architecture.error;

import com.github.dperezcabrera.bank.architecture.common.MessageDto;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(value = {FunctionalException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageDto functionalException(FunctionalException ex) {
        log.error("functionalException ", ex);
        return MessageDto.forbidden(ex.getMessage());
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageDto constraintViolationException(ConstraintViolationException ex) {
        log.error("constraintViolationException ", ex);
        return MessageDto.error("Bad request");
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageDto internalServerError(Exception ex) {
        log.error("internalServerError ", ex);
        return MessageDto.error("Internal error");
    }
}
