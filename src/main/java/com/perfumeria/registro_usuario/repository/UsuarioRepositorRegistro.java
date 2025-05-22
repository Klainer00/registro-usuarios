package com.perfumeria.registro_usuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.perfumeria.registro_usuario.model.Usuario;
@Repository
public interface UsuarioRepositorRegistro extends JpaRepository<Usuario, Integer> {
    List<Usuario> findAll();
    @SuppressWarnings("unchecked")
    
    Usuario save(Usuario usuario);
    
    Usuario findById(int id);


}