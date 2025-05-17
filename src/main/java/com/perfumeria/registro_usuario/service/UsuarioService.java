package com.perfumeria.registro_usuario.service;

import java.util.List;
import com.perfumeria.registro_usuario.repository.UsuarioRpository;
import org.springframework.stereotype.Service;

import com.perfumeria.registro_usuario.model.Usuario;

@Service
public class UsuarioService {

    private  UsuarioRpository usuarioRpository;

    UsuarioService(UsuarioRpository usuarioRpository) {
        this.usuarioRpository = usuarioRpository;
    }

    public List<Usuario> getAllUsuarios() {
        // TODO Auto-generated method stub
        return usuarioRpository.findAll();
    }

    public Usuario createUsuario(Usuario usuario) {
        // TODO Auto-generated method stub
        return usuarioRpository.save(usuario);
    }

    public Usuario findById(int id) {
        // TODO Auto-generated method stub
        return usuarioRpository.findById(id);
    }
    
}
