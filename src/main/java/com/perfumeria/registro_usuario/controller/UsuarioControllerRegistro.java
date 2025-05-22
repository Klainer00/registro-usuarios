package com.perfumeria.registro_usuario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfumeria.registro_usuario.model.Usuario;
import com.perfumeria.registro_usuario.service.UsuarioServiceRegistro;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControllerRegistro {

    @Autowired
    private UsuarioServiceRegistro usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> getAll() {
        List<Usuario> usuarios = usuarioService.findAll();
        if (!usuarios.isEmpty()) {
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Usuario> postUsuario(@RequestBody Usuario usuario) {
        Usuario buscar = usuarioService.findById(usuario.getId());
        if (buscar == null) {
            return new ResponseEntity<>(usuarioService.createUsuario(usuario), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Usuario> putUsuario(@PathVariable int id, @RequestBody Usuario usuario) {
        // Buscar el usuario existente por ID
        Usuario buscar = usuarioService.findById(id);

        // Verificar si el usuario existe
        if (buscar == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Asegurar que el ID del usuario coincide con el de la URL
        if (usuario.getId() != id) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // IDs no coinciden
        }

        // Intentar actualizar el usuario
        Usuario usuarioActualizado = usuarioService.updateUsuario(usuario);

        // Validar si la actualización fue exitosa
        if (usuarioActualizado == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Fallo en la actualización
        }

        // Respuesta exitosa
        return ResponseEntity.ok(usuarioActualizado);
    }

}