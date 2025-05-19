package com.perfumeria.registro_usuario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfumeria.registro_usuario.model.Usuario;
import com.perfumeria.registro_usuario.service.UsuarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping("/api/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    @GetMapping
    public ResponseEntity<List<Usuario>> getall(){
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        if (!usuarios.isEmpty()) {
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        
    }
    @PostMapping("/registro") 
    public ResponseEntity<Usuario> postUsuario(@RequestBody Usuario usuario){
        Usuario buscar = usuarioService.findById(usuario.getId());
        if (buscar == null) {
            return new ResponseEntity<>(usuarioService.createUsuario(usuario), HttpStatus.CREATED);
            
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable int id) {
        return new ResponseEntity<Usuario>(usuarioService.findById(id), HttpStatus.OK);
    }


}

