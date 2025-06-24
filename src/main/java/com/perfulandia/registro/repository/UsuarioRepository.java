package com.perfulandia.registro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.perfulandia.registro.model.Usuario;
@Repository
public interface UsuarioRepository  extends JpaRepository<Usuario, Integer> {
    Usuario findById(int id);
    Usuario findByRut(String rut);
    Usuario findByCorreo(String correo);
    Usuario findByUsuario(String usuario);

}
