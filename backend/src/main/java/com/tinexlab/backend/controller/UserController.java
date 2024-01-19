package com.tinexlab.backend.controller;

import com.tinexlab.backend.model.dto.request.UserRequest;
import com.tinexlab.backend.model.dto.response.GenericResponse;
import com.tinexlab.backend.model.entity.User;
import com.tinexlab.backend.service.crud.impl.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @GetMapping("/users")
    // devuelve una lista completa o paginada si viajan parámetros de paginación
    public ResponseEntity<GenericResponse<?>> listarUsuarios(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("custom-status", "OK")
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.get(page, size)
                );
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @GetMapping("/users/{id}")
    public ResponseEntity<GenericResponse<User>> buscarUsuario(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("custom-status", "OK")
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getById(id)
                );
    }


    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PostMapping("/users")
    public ResponseEntity<GenericResponse<?>> guardarUsuario(@Valid @RequestBody UserRequest userRequest, BindingResult result){
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("custom-status", "OK")
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.save(userRequest, result)
                );
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PutMapping("/users/{id}")
    public ResponseEntity<GenericResponse<?>> editarUsuario(@Valid @RequestBody UserRequest userRequest, @PathVariable Long id, BindingResult result){
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("custom-status", "OK")
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.update(userRequest, id, result)
                );
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<GenericResponse<?>> borrarUsuario(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("custom-status", "OK")
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.delete(id)
                );
    }


    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @GetMapping("/dashboard")
    public ResponseEntity<GenericResponse<?>> getDashboard(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("custom-status", "OK")
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.dashboard()
                );
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PostMapping(value = "/csv", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<GenericResponse<?>> cargarDesdeCSV(@RequestPart(value = "archivo") MultipartFile archivo) throws IOException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("custom-status", "OK")
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.cargarDesdeCSV(archivo)
                );
    }

}
