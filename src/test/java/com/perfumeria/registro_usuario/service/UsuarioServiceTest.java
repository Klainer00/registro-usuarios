package com.perfumeria.registro_usuario.service;

import com.perfumeria.registro_usuario.dto.UsuarioDTO;
import com.perfumeria.registro_usuario.model.EnumRol;
import com.perfumeria.registro_usuario.model.Usuario;
import com.perfumeria.registro_usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setRut("12345678-9");
        usuario.setNombre("Juan");
        usuario.setP_apellido("Perez");
        usuario.setS_apellido("Gomez");
        usuario.setUsuario("juanperez");
        usuario.setContrasenna("password123");
        usuario.setCorreo("juan@gmail.com");
        usuario.setDireccion("Calle Falsa 123");
        usuario.setPermiso(EnumRol.CLIENTE);
        usuario.setEstado(true);
        usuario.setNumero("+56912345678");

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setRut(usuario.getRut());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setP_apellido(usuario.getP_apellido());
        usuarioDTO.setS_apellido(usuario.getS_apellido());
        usuarioDTO.setUsuario(usuario.getUsuario());
        usuarioDTO.setCorreo(usuario.getCorreo());
        usuarioDTO.setDireccion(usuario.getDireccion());
        usuarioDTO.setPermiso(usuario.getPermiso());
        usuarioDTO.setEstado(usuario.isEstado());
    }

    @Test
    void testFindById_Exito() {
        when(usuarioRepository.findById(1)).thenReturn(usuario);

        Usuario result = usuarioService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("juan@gmail.com", result.getCorreo());
        verify(usuarioRepository).findById(1);
    }

    @Test
    void testFindById_NoEncontrado() {
        when(usuarioRepository.findById(1)).thenReturn(null);

        Usuario result = usuarioService.findById(1);

        assertNull(result);
        verify(usuarioRepository).findById(1);
    }

    @Test
    void testFindAll_Exito() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));

        List<Usuario> result = usuarioService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getNombre());
        verify(usuarioRepository).findAll();
    }

    @Test
    void testFindAll_ListaVacia() {
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        List<Usuario> result = usuarioService.findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(usuarioRepository).findAll();
    }

    @Test
    void testFindAllDto_Exito() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));

        List<UsuarioDTO> result = usuarioService.findAllDto();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("juan@gmail.com", result.get(0).getCorreo());
        verify(usuarioRepository).findAll();
    }
    @Test
    void testCreateUsuario_Exito() {
        when(usuarioRepository.findById(1)).thenReturn(null);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.createUsuario(usuario);

        assertNotNull(result);
        assertEquals("12345678-9", result.getRut());
        verify(usuarioRepository).findById(1);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testCreateUsuario_RutDuplicado() {
        Usuario existingUsuario = new Usuario();
        existingUsuario.setId(1);
        existingUsuario.setRut("12345678-9");
        when(usuarioRepository.findById(1)).thenReturn(existingUsuario);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.createUsuario(usuario);
        });

        assertEquals("El RUT ya existe.", exception.getMessage());
        verify(usuarioRepository).findById(1);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testCreateUsuario_CorreoDuplicado() {
        Usuario existingUsuario = new Usuario();
        existingUsuario.setId(1);
        existingUsuario.setCorreo("juan@gmail.com");
        when(usuarioRepository.findById(1)).thenReturn(existingUsuario);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.createUsuario(usuario);
        });

        assertEquals("El correo ya existe.", exception.getMessage());
        verify(usuarioRepository).findById(1);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testCreateUsuario_UsuarioDuplicado() {
        Usuario existingUsuario = new Usuario();
        existingUsuario.setId(1);
        existingUsuario.setUsuario("juanperez");
        when(usuarioRepository.findById(1)).thenReturn(existingUsuario);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.createUsuario(usuario);
        });

        assertEquals("El nombre de usuario ya existe.", exception.getMessage());
        verify(usuarioRepository).findById(1);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testUpdateUsuario_Exito_Cliente() {
        usuario.setPermiso(EnumRol.CLIENTE);
        when(usuarioRepository.findById(1)).thenReturn(usuario);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1);
        usuarioActualizado.setNombre("Pedro");
        usuarioActualizado.setCorreo("pedro@example.com");

        Usuario result = usuarioService.updateUsuario(usuarioActualizado);

        assertNotNull(result);
        assertEquals("Pedro", result.getNombre());
        assertEquals("pedro@example.com", result.getCorreo());
        verify(usuarioRepository).findById(1);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testUpdateUsuario_Exito_Administrador_CambiarPermiso() {
        usuario.setPermiso(EnumRol.ADMINISTRADOR);
        when(usuarioRepository.findById(1)).thenReturn(usuario);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1);
        usuarioActualizado.setPermiso(EnumRol.GERENTE);

        Usuario result = usuarioService.updateUsuario(usuarioActualizado);

        assertNotNull(result);
        assertEquals(EnumRol.GERENTE, result.getPermiso());
        verify(usuarioRepository).findById(1);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testUpdateUsuario_Exito_Gerente() {
        usuario.setPermiso(EnumRol.GERENTE);
        when(usuarioRepository.findById(1)).thenReturn(usuario);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1);
        usuarioActualizado.setCorreo("pedro@example.com");
        usuarioActualizado.setPermiso(EnumRol.CLIENTE);

        Usuario result = usuarioService.updateUsuario(usuarioActualizado);

        assertNotNull(result);
        assertEquals("pedro@example.com", result.getCorreo());
        assertEquals(EnumRol.CLIENTE, result.getPermiso());
        verify(usuarioRepository).findById(1);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testUpdateUsuario_NoEncontrado() {
        when(usuarioRepository.findById(1)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.updateUsuario(usuario);
        });

        assertEquals("Usuario no encontrado.", exception.getMessage());
        verify(usuarioRepository).findById(1);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testUpdateUsuario_NoAutorizado() {
        usuario.setPermiso(EnumRol.VENDEDOR);
        when(usuarioRepository.findById(1)).thenReturn(usuario);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.updateUsuario(usuario);
        });

        assertEquals("No tiene permiso para actualizar.", exception.getMessage());
        verify(usuarioRepository).findById(1);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testDeleteUserById_Exito() {
        usuario.setPermiso(EnumRol.ADMINISTRADOR);
        when(usuarioRepository.findById(1)).thenReturn(usuario);

        Usuario result = usuarioService.deleteUserById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(usuarioRepository).findById(1);
        verify(usuarioRepository).delete(usuario);
    }
    @Test
    void testCambiarEstado_Exito() {
        when(usuarioRepository.findById(1)).thenReturn(usuario);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario nuevoEstado = new Usuario();
        nuevoEstado.setEstado(false);

        Usuario result = usuarioService.cambiarEstado(1, nuevoEstado);

        assertNotNull(result);
        assertFalse(result.isEstado());
        verify(usuarioRepository).findById(1);
        verify(usuarioRepository).save(any(Usuario.class));
    }


}