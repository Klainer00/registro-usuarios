package com.perfumeria.registro_usuario.dto;

import com.perfumeria.registro_usuario.model.EnumRol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private String rut;
    private String nombre;
    private String p_apellido;
    private String s_apellido;
    private String usuario;
    private String correo;
    private String direccion;
    private EnumRol permiso;
    private boolean estado;
}