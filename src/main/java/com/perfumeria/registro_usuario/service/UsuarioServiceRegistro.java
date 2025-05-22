package com.perfumeria.registro_usuario.service;

import java.util.List;
import com.perfumeria.registro_usuario.repository.UsuarioRepositorRegistro;
import org.springframework.stereotype.Service;

import com.perfumeria.registro_usuario.model.Usuario;

@Service
public class UsuarioServiceRegistro {

    private  UsuarioRepositorRegistro usuarioRepository;

    UsuarioServiceRegistro(UsuarioRepositorRegistro usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario createUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario findById(int id) {
        return usuarioRepository.findById(id);
    }

}
