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
        Usuario buscar = this.findById(usuarioActualizado.getId());
            System.out.println("Servicio updateUsuario - ID del request: " + usuarioActualizado.getId() + 
                               ", ID del usuario encontrado (BD): " + buscar.getId());
        if (buscar != null) {
            if (usuarioActualizado.getNombre() != null) {
                buscar.setNombre(usuarioActualizado.getNombre());
            }
            if (usuarioActualizado.getP_apellido() != null) {
                buscar.setP_apellido(usuarioActualizado.getP_apellido());
            }
            if (usuarioActualizado.getS_apellido() != null) {
                buscar.setS_apellido(usuarioActualizado.getS_apellido());
            }
            if (usuarioActualizado.getUsuario() != null) {
                buscar.setUsuario(usuarioActualizado.getUsuario());
            }
            if (usuarioActualizado.getCorreo() != null) {
                buscar.setCorreo(usuarioActualizado.getCorreo());
            }
            if (usuarioActualizado.getContrasenna() != null) {
                buscar.setContrasenna(usuarioActualizado.getContrasenna());
            }
            if (usuarioActualizado.getDireccion() != null) {
                buscar.setDireccion(usuarioActualizado.getDireccion());
            }
            if (usuarioActualizado.isEstado()) {
                buscar.setPermiso(usuarioActualizado.getPermiso());
                buscar.setPermiso(usuarioActualizado.getPermiso());
                
            }
            usuarioRepository.save(buscar);
            return usuarioActualizado;
        } 
        
        return null;
    }
        public Usuario deleteUserById(int id) {
        Usuario usuario = usuarioRepository.findById(id);
            usuarioRepository.delete(usuario);
            return usuario;
    }
public Usuario cambiarEstado(int id, Usuario nuevoEstadoObj){ 
        Usuario buscarUsuario = this.findById(id); 
        if (buscarUsuario != null) {
            buscarUsuario.setEstado(nuevoEstadoObj.isEstado()); 
            return usuarioRepository.save(buscarUsuario); 
        }
        return null;
    }


    }


