package com.example.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsuarioNotFoundException extends RuntimeException  {
    private static final long serialVersionUID = 43876691117506211L;

    public ResponseEntity<String> UsuarioNotFoundException(Long id ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario not found with the id: " + id);
    }
}
