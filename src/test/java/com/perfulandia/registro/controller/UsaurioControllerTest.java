package com.perfulandia.registro.controller;

import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.registro.dto.UsuarioDTO;
import com.perfulandia.registro.model.EnumRol;
import com.perfulandia.registro.model.Usuario;
import com.perfulandia.registro.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
public class UsaurioControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UsuarioService usuarioService;
    @Autowired
    private ObjectMapper objectMapper;

    public UsaurioControllerTest() {
    }

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testGetAllUsuarios() throws Exception {
        Usuario usuario1 = new Usuario(1, "12345678-9", "Juan", "Pérez", "Gómez", "juanperez", "contrasena123",
                "juan.perez@example.com", "Calle Falsa 123", EnumRol.CLIENTE, true, "987654321");
        Usuario usuario2 = new Usuario(2, "98765432-1", "Maria", "López", "Fernández", "marial", "securePass456",
                "maria.lopez@example.com", "Avenida Siempre Viva 742", EnumRol.VENDEDOR, true, "123456789");

        List<Usuario> usuarios = List.of(usuario1, usuario2);

        Mockito.when(usuarioService.findAll()).thenReturn(usuarios);

        mockMvc.perform(get("/api/usuarios/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[1].nombre").value("Maria"));
    }

    @Test
    public void testGetAllUsuariosDto() throws Exception {
        UsuarioDTO usuarioDto1 = new UsuarioDTO(
                "12345678-9", "Juan", "Pérez", "Gómez", "juanp", "juan.perez@example.com",
                "Calle Falsa 123", EnumRol.CLIENTE, true);
        UsuarioDTO usuarioDto2 = new UsuarioDTO(
                "98765432-1", "Maria", "López", "Fernández", "marial", "maria.lopez@example.com",
                "Avenida Siempre Viva 742", EnumRol.VENDEDOR, true);

        List<UsuarioDTO> usuariosDto = List.of(usuarioDto1, usuarioDto2);

        Mockito.when(usuarioService.findAllDto()).thenReturn(usuariosDto);

        mockMvc.perform(get("/api/usuarios/all-dto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[0].rut").value("12345678-9"))
                .andExpect(jsonPath("$[0].permiso").value("CLIENTE"))
                .andExpect(jsonPath("$[0].estado").value(true))
                .andExpect(jsonPath("$[1].nombre").value("Maria"))
                .andExpect(jsonPath("$[1].rut").value("98765432-1"))
                .andExpect(jsonPath("$[1].permiso").value("VENDEDOR"))
                .andExpect(jsonPath("$[1].estado").value(true));
    }

    @Test
    public void testCrearUsuario() throws Exception {
        Usuario usuario = new Usuario(
                1, "11223344-5", "Pedro", "Ramírez", "Soto", "pedror", "claveSegura",
                "pedro.ramirez@example.com", "Calle Nueva 456", EnumRol.CLIENTE, true, "555666777");

        Mockito.when(usuarioService.findById(usuario.getId())).thenReturn(null);
        Mockito.when(usuarioService.createUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/usuarios/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Pedro"))
                .andExpect(jsonPath("$.correo").value("pedro.ramirez@example.com"));
    }

    @Test
    public void testUpdateUsuario() throws Exception {
        Usuario usuarioBD = new Usuario(
                1, "12345678-9", "Juan", "Pérez", "Gómez", "juanp", "contrasena123",
                "juan.perez@example.com", "Calle Falsa 123", EnumRol.ADMINISTRADOR, true, "987654321");
        Usuario usuarioActualizado = new Usuario(
                1, "12345678-9", "Juan", "Pérez", "Gómez", "juanp", "nuevaClave123",
                "juan.perez@example.com", "Calle Falsa 123", EnumRol.ADMINISTRADOR, true, "987654321");

        Mockito.when(usuarioService.findById(1)).thenReturn(usuarioBD);
        Mockito.when(usuarioService.updateUsuario(Mockito.any(Usuario.class))).thenReturn(usuarioActualizado);

        mockMvc.perform(put("/api/usuarios/update/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contrasenna").value("nuevaClave123"))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    public void testGetUsuarioById() throws Exception {
        Usuario usuario = new Usuario(
                1, "12345678-9", "Juan", "Pérez", "Gómez", "juanp", "contrasena123",
                "juan.perez@example.com", "Calle Falsa 123", EnumRol.CLIENTE, true, "987654321");

        Mockito.when(usuarioService.findById(1)).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    public void testDeleteUserById_Exitoso() throws Exception {
        Usuario usuario = new Usuario(
                1, "12345678-9", "Juan", "Pérez", "Gómez", "juanp", "contrasena123",
                "juan.perez@example.com", "Calle Falsa 123", EnumRol.ADMINISTRADOR, true, "987654321");

        Mockito.when(usuarioService.findById(1)).thenReturn(usuario);
        Mockito.doNothing().when(usuarioService).deleteUsuarioById(1);

        mockMvc.perform(delete("/api/usuarios/delete/{id}", 1))
                .andExpect(status().isOk());
        verify(usuarioService, Mockito.times(1)).deleteUsuarioById(1);
    }

    @Test
    public void testCambiarEstado() throws Exception {
        
        Usuario usuarioConNuevoEstado = new Usuario(
                1, "12345678-9", "Juan", "Pérez", "Gómez", "juanp", "contrasena123",
                "juan.perez@example.com", "Calle Falsa 123", EnumRol.CLIENTE, true, "987654321");
        Usuario usuarioActualizado = new Usuario(
                1, "12345678-9", "Juan", "Pérez", "Gómez", "juanp", "contrasena123",
                "juan.perez@example.com", "Calle Falsa 123", EnumRol.CLIENTE, false, "987654321");

        Mockito.when(usuarioService.cambiarEstado(1, usuarioConNuevoEstado)).thenReturn(usuarioActualizado);

        mockMvc.perform(put("/api/usuarios/nuevo-estado/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioConNuevoEstado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value(false))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

}