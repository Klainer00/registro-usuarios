package com.perfulandia.registro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.perfulandia.registro.assemblers.UsuarioAssembler;
import com.perfulandia.registro.dto.UsuarioDTO;
import com.perfulandia.registro.model.Usuario;
import com.perfulandia.registro.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "API para la gestión de usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioAssembler assembler;

    @GetMapping("/all")
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "204", description = "No hay usuarios disponibles", content = @Content)
    })
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> getAll() {
        List<Usuario> usuarios = usuarioService.findAll();
        if (!usuarios.isEmpty()) {
            List<EntityModel<Usuario>> usuarioModels = usuarios.stream()
                    .map(assembler::toModel)
                    .toList();
            CollectionModel<EntityModel<Usuario>> collectionModel = CollectionModel.of(usuarioModels);
            return ResponseEntity.ok(collectionModel);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/all-dto")
    @Operation(summary = "Obtener todos los usuarios en formato DTO", description = "Devuelve una lista de usuarios en formato DTO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios DTO obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "204", description = "No hay usuarios disponibles", content = @Content)
    })
    public ResponseEntity<List<UsuarioDTO>> getAllDto() {
        List<UsuarioDTO> usuarios = usuarioService.findAllDto();
        if (!usuarios.isEmpty()) {
            return ResponseEntity.ok(usuarios);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/create")
    @Operation(summary = "Crear un nuevo usuario", description = "Crea un usuario nuevo con los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "406", description = "El usuario ya existe", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<?> postUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario buscar = usuarioService.findById(usuario.getId());
            if (buscar == null) {
                Usuario creado = usuarioService.createUsuario(usuario);
                return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(creado));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("El usuario ya existe.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al crear el usuario: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Actualizar un usuario por ID", description = "Actualiza los datos de un usuario existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Permiso denegado", content = @Content),
            @ApiResponse(responseCode = "400", description = "IDs no coinciden o datos inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<?> updateUserById(@PathVariable int id, @RequestBody Usuario usuarioact) {
        Usuario usuarioBD = usuarioService.findById(id);

        if (usuarioBD == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }

        if (usuarioBD.getPermiso() != usuarioBD.getPermiso().ADMINISTRADOR &&
                usuarioBD.getPermiso() != usuarioBD.getPermiso().GERENTE &&
                usuarioBD.getPermiso() != usuarioBD.getPermiso().CLIENTE) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permiso denegado.");
        }

        if (usuarioBD.getId() != usuarioact.getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("IDs no coinciden.");
        }

        try {
            Usuario usuarioActualizado = usuarioService.updateUsuario(usuarioact);
            return ResponseEntity.ok(assembler.toModel(usuarioActualizado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al actualizar el usuario: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario por ID", description = "Devuelve un usuario específico con enlaces HATEOAS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "204", description = "Usuario no encontrado", content = @Content)
    })
    public ResponseEntity<EntityModel<Usuario>> getUsuarioById(@PathVariable int id) {
        Usuario usuario = usuarioService.findById(id);
        if (usuario != null) {
            return ResponseEntity.ok(assembler.toModel(usuario));
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Eliminar un usuario por ID", description = "Elimina un usuario si tiene permisos adecuados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Permiso denegado", content = @Content)
    })
    public ResponseEntity<String> deleteUserById(@PathVariable int id) {
        Usuario userActual = usuarioService.findById(id);
        if (userActual == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        if (userActual.getPermiso() == userActual.getPermiso().ADMINISTRADOR
                || userActual.getPermiso() == userActual.getPermiso().GERENTE) {
            usuarioService.deleteUsuarioById(id);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permiso denegado");
        }
    }

    @PutMapping("/nuevo-estado/{id}")
    @Operation(summary = "Cambiar estado de un usuario", description = "Actualiza el estado (activo/inactivo) de un usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado del usuario actualizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    public ResponseEntity<EntityModel<Usuario>> descativarUserById(@PathVariable int id,
            @RequestBody Usuario usuarioConNuevoEstado) {
        Usuario usuarioActualizado = usuarioService.cambiarEstado(id, usuarioConNuevoEstado);
        if (usuarioActualizado != null) {
            return ResponseEntity.ok(assembler.toModel(usuarioActualizado));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}