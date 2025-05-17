package com.perfumeria.registro_usuario.model;

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
    @Column(unique = true)//indica el atributo de abajo
    private int id;
    @Column (length = 30, nullable = false)
    private String nombre;
    @Column(length = 50, nullable =false)
    private String apellido;
    @Column(length = 20, nullable = false)
    private String usuario;
    @Column(length = 40, nullable = false)
    private String contrasenna;
    @Column(length = 13 , nullable = false)
    private String rut;
    @Column(length = 50, nullable =false)
    private String correo;
    @Column(length = 50, nullable =false)
    private String dereccion;    
    @Column(nullable = false)
    private int perfil;

}