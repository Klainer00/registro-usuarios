package com.perfulandia.registro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import jakarta.persistence.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.perfulandia.registro.model.EnumRol;
import com.perfulandia.registro.model.Usuario;
import com.perfulandia.registro.repository.UsuarioRepository;

public class UsuarioServiceTest {
    @Mock // sirve para simular el repository
    private UsuarioRepository usuarioRepository;
    @InjectMocks // simula el service del
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUsuario() {
        Usuario usuario = new Usuario();
        usuario.setRut("12345678-9");
        usuario.setNombre("Juan");
        usuario.setP_apellido("Pérez");
        usuario.setS_apellido("Gómez");
        usuario.setUsuario("juanperez");
        usuario.setCorreo("juanPerez@gmail.com");
        usuario.setDireccion("Calle Falsa 123");
        usuario.setPermiso(EnumRol.CLIENTE);
        usuario.setEstado(true);
        usuario.setNumero("1234567890");
        Usuario usuario2 = new Usuario();
        usuario2.setId(1); 
        usuario2.setRut("12345678-9");
        usuario2.setNombre("Juan");
        usuario2.setP_apellido("Pérez");
        usuario2.setS_apellido("Gómez");
        usuario2.setUsuario("juanperez");
        usuario2.setCorreo("juanPerez@gmail.com");
        usuario2.setDireccion("Calle Falsa 123");
        usuario2.setPermiso(EnumRol.CLIENTE);
        usuario2.setEstado(true);
        usuario2.setNumero("1234567890");
        when(usuarioRepository.save(usuario)).thenReturn(usuario2);
        Usuario usuarioGuardado = usuarioService.createUsuario(usuario);
        System.out.println(usuarioGuardado);

        assertThat(usuarioGuardado.getId()).isEqualTo(1);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testListarUsuarios() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1);
        usuario1.setRut("12345678-9");
        usuario1.setNombre("Juan");
        usuario1.setP_apellido("Pérez");
        usuario1.setS_apellido("Gómez");
        usuario1.setUsuario("juanperez");
        usuario1.setCorreo("juanPerez@gmail.com");
        usuario1.setDireccion("Calle Falsa 123");
        usuario1.setPermiso(EnumRol.CLIENTE);
        usuario1.setEstado(true);
        usuario1.setNumero("1234567890");
        Usuario usuario2 = new Usuario();
        usuario2.setId(2);
        usuario2.setRut("98765432-1");
        usuario2.setNombre("Ana");
        usuario2.setP_apellido("López");
        usuario2.setS_apellido("Martínez");
        usuario2.setUsuario("analopes");
        usuario2.setCorreo("analopez@gmail.com");
        usuario2.setDireccion("Avenida Siempre Viva 456");
        usuario2.setPermiso(EnumRol.VENDEDOR);
        usuario2.setEstado(true);
        usuario2.setNumero("0987654321");
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));
        List<Usuario> usuarios = usuarioService.findAll();
        assertThat(usuarios).hasSize(2).contains(usuario1, usuario2);

    }

    @Test
    void testFindById() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setRut("12345678-9");
        usuario.setNombre("Juan");
        usuario.setP_apellido("Pérez");
        usuario.setS_apellido("Gómez");
        usuario.setUsuario("juanperez");
        usuario.setCorreo("juanPerez@gmail.com");
        usuario.setDireccion("Calle Falsa 123");
        usuario.setPermiso(EnumRol.CLIENTE);
        usuario.setEstado(true);
        usuario.setNumero("1234567890");
        when(usuarioRepository.findById(1)).thenReturn(usuario);

        //falta asserts
        assertEquals(1, usuarioService.findById(1).getId());

    }

    @Test

    void testUpdateUsuario() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1);
        usuarioExistente.setRut("12345678-9");
        usuarioExistente.setNombre("Juan");
        usuarioExistente.setP_apellido("Pérez");
        usuarioExistente.setS_apellido("Gómez");
        usuarioExistente.setUsuario("juanperez");
        usuarioExistente.setCorreo("juanPerez@gmail.com");
        usuarioExistente.setDireccion("Calle Falsa 123");
        usuarioExistente.setPermiso(EnumRol.CLIENTE);
        usuarioExistente.setEstado(true);

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1);
        usuarioActualizado.setRut("12345678-9");
        usuarioActualizado.setNombre("Pedro"); // Cambiado
        usuarioActualizado.setP_apellido("Pérez");
        usuarioActualizado.setS_apellido("Gómez");
        usuarioActualizado.setUsuario("juanperez");
        usuarioActualizado.setCorreo("pedroPerez@gmail.com"); // Cambiado
        usuarioActualizado.setDireccion("Calle Falsa 123");
        usuarioActualizado.setPermiso(EnumRol.CLIENTE);
        usuarioActualizado.setEstado(true);
        when(usuarioRepository.findById(1)).thenReturn(usuarioExistente);
        
        when(usuarioRepository.save(usuarioActualizado)).thenReturn(usuarioActualizado);

        Usuario resultado = usuarioService.updateUsuario(usuarioActualizado);
        assertNotNull(resultado);
        assertEquals("Pedro", resultado.getNombre());
        assertEquals("pedroPerez@gmail.com", resultado.getCorreo());
        System.out.println("Usuario actualizado: " + resultado);
        verify(usuarioRepository).findById(1);
        verify(usuarioRepository).save(usuarioActualizado);
        System.out.println(usuarioActualizado);
    }

    @Test
    void testDeleteUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setRut("12345678-9");
        usuario.setNombre("Juan");
        usuario.setP_apellido("Pérez");
        usuario.setS_apellido("Gómez");
        usuario.setUsuario("juanperez");
        usuario.setCorreo("juanPerez@gmail.com");
        usuario.setDireccion("Calle Falsa 123");
        usuario.setPermiso(EnumRol.ADMINISTRADOR);
        usuario.setEstado(true);
        usuario.setNumero("1234567890");
        System.out.println(usuario);
        when(usuarioRepository.findById(1)).thenReturn(usuario);
        usuarioService.deleteUsuarioById(1);
        verify(usuarioRepository).findById(1);
        verify(usuarioRepository).delete(usuario);
        System.out.println(usuario.getId());
        
        
    }
    @Test
    void testCambiaEstadoUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEstado(true);
        System.out.println(usuario.isEstado());
        when(usuarioRepository.findById(1)).thenReturn(usuario);

        Usuario usuarioConNuevoEstado = new Usuario();
        usuarioConNuevoEstado.setEstado(false);
        usuarioService.cambiarEstado(1, usuarioConNuevoEstado);
        assertEquals(false, usuario.isEstado());
        verify(usuarioRepository).findById(1);
        verify(usuarioRepository).save(usuario);
        System.out.println(usuario.isEstado());
    }


}