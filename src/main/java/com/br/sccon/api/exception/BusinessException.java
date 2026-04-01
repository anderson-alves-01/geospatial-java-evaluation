package com.br.sccon.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BusinessException extends RuntimeException {

    private BusinessException(String msg) {
        super(msg); // Repassa a mensagem para a RuntimeException
    }

    public static BusinessException of(String msg) {
        return new BusinessException(msg);
    }
}