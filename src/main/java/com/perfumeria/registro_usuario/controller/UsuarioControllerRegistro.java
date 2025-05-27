package com.perfumeria.registro_usuario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfumeria.registro_usuario.model.Usuario;
import com.perfumeria.registro_usuario.service.UsuarioServiceRegistro;

import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ResponseEntity<Usuario> updateUserById(@PathVariable int id, @RequestBody Usuario usuarioact) {
        Usuario buscar = usuarioService.findById(id);
        System.out.println(usuarioact);
        if (buscar == null) {
            System.out.println("pase por buscar");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        System.out.println("usuario a actualizar: " + id + ", name: " + usuarioact.getNombre());
        System.out.println(" usuario a actualizar: " + id + " usuario recibido: " + usuarioact.getId());
        if (buscar.getId() != usuarioact.getId()) {
            System.out.println("pase por id no coinciden");
            System.out.println("ID del usuario a actualizar: " + id + ", ID del usuario encontrado: " + buscar.getId());
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // IDs no coinciden
         }
        if (buscar.getPermiso() != 3) {
            System.out.println("pase por permisos");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Permiso no válido
        }
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaa");
        Usuario usuarioActualizado = usuarioService.updateUsuario(usuarioact);
        if (usuarioActualizado == null) {
            System.out.println("pase pa aca usuarioActualizado");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Fallo en la actualización
        }
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable int id) {
        Usuario userActual = usuarioService.findById(id);
        if (userActual == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        if (userActual.getPermiso() == 3) {
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