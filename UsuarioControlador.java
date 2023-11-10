package com.example.demo.controlador;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.error.ProductoNotFoundException;
import com.example.demo.error.UsuarioNotFoundException;
import com.example.demo.modelo.Usuario;
import com.example.demo.repos.UsuarioRepositorio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/api/usuario")
public class UsuarioControlador {

    private final UsuarioRepositorio usuarioRepositorio;
    public UsuarioControlador(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @GetMapping("/")
    public List<UsuarioDTO> getUsuarios() {
        List<UsuarioDTO> resultado = new ArrayList<>();
        for (Usuario usuario: usuarioRepositorio.findAll()) resultado.add(new UsuarioDTO(usuario));
        return resultado;
    }

    @PostMapping("/")
    public ResponseEntity<Usuario> createUsuario(@Valid @RequestBody Usuario usuario) {
        Usuario createdUsuario = usuarioRepositorio.save(usuario);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUsuario.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        return usuarioRepositorio.findById(id)
                .map(existingUsuario -> {
                    existingUsuario.setName(usuario.getName());
                    existingUsuario.setEmail(usuario.getEmail());
                    usuarioRepositorio.save(existingUsuario);

                    // Include the URI of the updated resource in the response
                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(id)
                            .toUri();

                    return ResponseEntity.ok().location(location).body(existingUsuario);
                })
                .orElseThrow(UsuarioNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUsuario(@PathVariable Long id) {
        return usuarioRepositorio.findById(id)
                .map(usuario -> {
                    usuarioRepositorio.delete(usuario);

                    // Include the URI to retrieve all elements in the response
                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/")
                            .build()
                            .toUri();

                    return ResponseEntity.noContent().location(location).build();
                })
                .orElseThrow(UsuarioNotFoundException::new);
    }
}
