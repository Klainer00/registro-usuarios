package com.perfumeria.registro_usuario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfumeria.registro_usuario.dto.UsuarioDTO;
import com.perfumeria.registro_usuario.model.Usuario;
import com.perfumeria.registro_usuario.service.UsuarioService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/all")
    public ResponseEntity<List<Usuario>> getAll() {
        List<Usuario> usuarios = usuarioService.findAll();
        if (!usuarios.isEmpty()) {
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/all-dto")
    public ResponseEntity<List<UsuarioDTO>> getAllDto() {
        List<UsuarioDTO> usuarios = usuarioService.findAllDto();
        if (!usuarios.isEmpty()) {
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> postUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario buscar = usuarioService.findById(usuario.getId());
            if (buscar == null) {
                Usuario creado = usuarioService.createUsuario(usuario);
                return new ResponseEntity<>(creado, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("El usuario ya existe.", HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al crear el usuario: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable int id, @RequestBody Usuario usuarioact) {
        Usuario usuarioBD = usuarioService.findById(id);

        if (usuarioBD == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }

        if (usuarioBD.getPermiso() != usuarioBD.getPermiso().ADMINISTRADOR &&
                usuarioBD.getPermiso() != usuarioBD.getPermiso().GERENTE &&
                usuarioBD.getPermiso() != usuarioBD.getPermiso().CLIENTE) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Permiso denegado.");
        }

        if (usuarioBD.getId() != usuarioact.getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("IDs no coinciden.");
        }

        try {
            Usuario usuarioActualizado = usuarioService.updateUsuario(usuarioact);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al actualizar el usuario: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable int id) {
        Usuario usuario = usuarioService.findById(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable int id) {
        Usuario userActual = usuarioService.findById(id);
        if (userActual == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        if (userActual.getPermiso() == userActual.getPermiso().ADMINISTRADOR
                || userActual.getPermiso() == userActual.getPermiso().GERENTE) {
            usuarioService.deleteUserById(id);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permiso denegado");
        }
    }

    @PutMapping("/nuevo-estado/{id}")
    public ResponseEntity<Usuario> descativarUserById(
            @PathVariable int id,
            @RequestBody Usuario usuarioConNuevoEstado) {

        Usuario usuarioActualizado = usuarioService.cambiarEstado(id, usuarioConNuevoEstado);

        if (usuarioActualizado != null) {
            System.out.println("Usuario actualizado con nuevo estado: " + usuarioActualizado.isEstado());
            return ResponseEntity.ok(usuarioActualizado);
        } else {
            System.out.println("Usuario no encontrado o error al cambiar el estado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}