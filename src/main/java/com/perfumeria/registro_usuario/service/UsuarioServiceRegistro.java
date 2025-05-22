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
    public Usuario updateUsuario(Usuario usuarioActualizado) {
        Usuario buscar = findById(usuarioActualizado.getId());
        if (buscar != null) {
            if (usuarioActualizado != null) {
                buscar.setNombre(usuarioActualizado.getNombre());
            }
            if (usuarioActualizado != null) {
                buscar.setP_apellido(usuarioActualizado.getP_apellido());
            }
            if (usuarioActualizado != null) {
                buscar.setS_apellido(usuarioActualizado.getS_apellido());
            }
            if (usuarioActualizado != null) {
                buscar.setUsuario(usuarioActualizado.getUsuario());
            }
            if (usuarioActualizado != null) {
                buscar.setCorreo(usuarioActualizado.getCorreo());
            }
            if (usuarioActualizado != null) {
                buscar.setContrasenna(usuarioActualizado.getContrasenna());
            }
            if (usuarioActualizado != null) {
                buscar.setDireccion(usuarioActualizado.getDireccion());
            }
            if (usuarioActualizado != null) {
                buscar.setPermiso(usuarioActualizado.getPermiso());
            }
            usuarioRepository.save(buscar);
            return usuarioActualizado;
        } 
        
        return null;
    }
        public Usuario deleteUserById(int id) {
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario != null) {
            usuarioRepository.delete(usuario);
            return usuario;
        }
        return null;
    }
}
