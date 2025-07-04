package com.perfulandia.registro.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name =  "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor


public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // para marcar que la id sea auto increment
    private int id;
    @Column(length = 13 , nullable = false ,unique = true) //rut
    private String rut;
    @Column (length = 30, nullable = false)
    private String nombre;
    @Column(length = 30, nullable =false)
    private String p_apellido;
    @Column(length = 30, nullable =false)
    private String s_apellido;
    @Column(length = 20, nullable = false)
    private String usuario;
    @Column(length = 40, nullable = false)
    private String contrasenna;
    @Column(length = 50, nullable =false, unique = true) 
    private String correo;
    @Column(length = 50, nullable =false)
    private String direccion;    
    @Column(nullable = true)
    private EnumRol permiso; // 1 = cliente, 2 = vendedor, 3 = gerente ,4 = administrador
    @Column(nullable = false)   
    private boolean estado = true; // true = activo, false = inactivo
    @Column(length = 14, nullable = false)
    private String numero;
}