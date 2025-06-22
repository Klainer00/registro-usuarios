package com.perfumeria.registro_usuario.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.perfumeria.registro_usuario.model.Usuario;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @SuppressWarnings("unchecked")
    
    Usuario save(Usuario usuario);
    
    Usuario findById(int id);
    Usuario findByRut(String rut);
    Usuario findByCorreo(String correo);
    Usuario findByUsuario(String usuario);


}