package com.perfulandia.registro.assemblers;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.perfulandia.registro.controller.UsuarioController;
import com.perfulandia.registro.model.Usuario;
@Component
public class UsuarioAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

@Override
public EntityModel<Usuario> toModel(Usuario usuario) {
    return EntityModel.of(usuario,
            linkTo(methodOn(UsuarioController.class).getUsuarioById(usuario.getId())).withSelfRel(),
            linkTo(methodOn(UsuarioController.class).getAll()).withRel("usuarios")

);
    }

}
