package com.perfumeria.registro_usuario.service;

import java.util.List;
import java.util.stream.Collectors;

import com.perfumeria.registro_usuario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import com.perfumeria.registro_usuario.dto.UsuarioDTO;
import com.perfumeria.registro_usuario.model.Usuario;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;


    UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
        private UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setRut(usuario.getRut());
        dto.setNombre(usuario.getNombre());
        dto.setP_apellido(usuario.getP_apellido());
        dto.setS_apellido(usuario.getS_apellido());
        dto.setUsuario(usuario.getUsuario());
        dto.setCorreo(usuario.getCorreo());
        dto.setDireccion(usuario.getDireccion());
        dto.setPermiso(usuario.getPermiso());
        dto.setEstado(usuario.isEstado());
        return dto;
    }
    public Usuario findById(int id) {
        return usuarioRepository.findById(id);
    }
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }
    public List<UsuarioDTO> findAllDto() {
        return usuarioRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

public Usuario createUsuario(Usuario usuario) {
    Usuario usuarioActual = usuarioRepository.findById(usuario.getId());
    System.out.println("Usuario actual: " + usuarioActual);

    if (usuarioActual != null) {
        if (usuarioActual.getRut().equals(usuario.getRut())) {
            System.out.println("El usuario con el RUT " + usuario.getRut() + " ya existe.");
            throw new IllegalArgumentException("El RUT ya existe.");
        }
        if (usuarioActual.getCorreo().equals(usuario.getCorreo())) {
            System.out.println("El correo ya existe");
            throw new IllegalArgumentException("El correo ya existe.");
        }
        if (usuarioActual.getUsuario().equals(usuario.getUsuario())) {
            System.out.println("El nombre de usuario ya existe");
            throw new IllegalArgumentException("El nombre de usuario ya existe.");
        }
    }

    return usuarioRepository.save(usuario);
}


public Usuario updateUsuario(Usuario usuarioActualizado) {
    Usuario usuarioExistente = usuarioRepository.findById(usuarioActualizado.getId());
    if (usuarioExistente == null) {
        System.out.println("Usuario no encontrado");
        throw new IllegalArgumentException("Usuario no encontrado.");
    }
    var permiso = usuarioExistente.getPermiso();
    if (permiso == permiso.CLIENTE) {
        if (usuarioActualizado.getNombre() != null) usuarioExistente.setNombre(usuarioActualizado.getNombre());
        if (usuarioActualizado.getP_apellido() != null) usuarioExistente.setP_apellido(usuarioActualizado.getP_apellido());
        if (usuarioActualizado.getS_apellido() != null) usuarioExistente.setS_apellido(usuarioActualizado.getS_apellido());
        if (usuarioActualizado.getContrasenna() != null) usuarioExistente.setContrasenna(usuarioActualizado.getContrasenna());
        if (usuarioActualizado.getCorreo() != null) usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
        if (usuarioActualizado.getDireccion() != null) usuarioExistente.setDireccion(usuarioActualizado.getDireccion());
        return usuarioRepository.save(usuarioExistente);
    }
    if (permiso == permiso.ADMINISTRADOR) {
        if (usuarioActualizado.getNombre() != null) usuarioExistente.setNombre(usuarioActualizado.getNombre());
        if (usuarioActualizado.getP_apellido() != null) usuarioExistente.setP_apellido(usuarioActualizado.getP_apellido());
        if (usuarioActualizado.getS_apellido() != null) usuarioExistente.setS_apellido(usuarioActualizado.getS_apellido());
        if (usuarioActualizado.getContrasenna() != null) usuarioExistente.setContrasenna(usuarioActualizado.getContrasenna());
        if (usuarioActualizado.getCorreo() != null) usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
        if (usuarioActualizado.getDireccion() != null) usuarioExistente.setDireccion(usuarioActualizado.getDireccion());
        if (usuarioActualizado.getPermiso() != null) usuarioExistente.setPermiso(usuarioActualizado.getPermiso());
        return usuarioRepository.save(usuarioExistente);
    }

    if (permiso == permiso.GERENTE) {
        if (usuarioActualizado.getContrasenna() != null) usuarioExistente.setContrasenna(usuarioActualizado.getContrasenna());
        if (usuarioActualizado.getCorreo() != null) usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
        if (usuarioActualizado.getDireccion() != null) usuarioExistente.setDireccion(usuarioActualizado.getDireccion());
        if (usuarioActualizado.getPermiso() != null) usuarioExistente.setPermiso(usuarioActualizado.getPermiso());        
        return usuarioRepository.save(usuarioExistente);
    }

    System.out.println("No tiene permiso para actualizar.");
    throw new IllegalArgumentException("No tiene permiso para actualizar");
}

public Usuario deleteUserById(int id) {
    Usuario usuario = usuarioRepository.findById(id);
    if (usuario == null) {
        System.out.println("Usuario no encontrado");
        throw new IllegalArgumentException("Usuario no encontrado.");
    }
    var permiso = usuario.getPermiso();
    if (permiso != permiso.ADMINISTRADOR && permiso != permiso.GERENTE) {
        System.out.println("No tiene permiso para eliminar.");
        throw new IllegalArgumentException("No tiene permiso para eliminar.");
    }
    usuarioRepository.delete(usuario);
    return usuario;
}

    public Usuario cambiarEstado(int id, Usuario nuevoEstado) {
        Usuario buscarUsuario = usuarioRepository.findById(id);
        if (buscarUsuario != null) {
            buscarUsuario.setEstado(nuevoEstado.isEstado());
            return usuarioRepository.save(buscarUsuario);
        }
        return null;
    }
    
}
