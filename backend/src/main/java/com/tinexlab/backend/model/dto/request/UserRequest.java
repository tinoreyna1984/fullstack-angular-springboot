package com.tinexlab.backend.model.dto.request;

import com.tinexlab.backend.util.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String username;
    @Pattern(regexp = "^(?=.{8,})(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=]).*$", message = "La clave debe " +
            "tener mínimo 8 caracteres y al menos un caracter especial, no debe tener espacios y debe tener al menos " +
            "una letra mayúscula.")
    private String password;
    @Email
    private String email;
    @Size(min = 2, message = "El nombre debe tener como mínimo 2 caracteres.")
    private String name;
    @Size(min = 2, message = "El apellido debe tener como mínimo 2 caracteres.")
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Role role;
}
