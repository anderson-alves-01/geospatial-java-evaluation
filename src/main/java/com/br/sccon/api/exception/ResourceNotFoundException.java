package com.br.sccon.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    // Construtor privado para forçar o uso do Factory Method (opcional)
    private ResourceNotFoundException(String msg) {
        super(msg); // Essencial: passa a mensagem para a RuntimeException
    }

    // Factory Method: torna a criação mais semântica
    public static ResourceNotFoundException of(String msg) {
        return new ResourceNotFoundException(msg);
    }

    // Sobrecarga útil para buscar por ID
    public static ResourceNotFoundException of(Long id) {
        return new ResourceNotFoundException("Recurso não encontrado com o ID: " + id);
    }
}