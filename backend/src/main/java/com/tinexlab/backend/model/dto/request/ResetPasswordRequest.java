package com.tinexlab.backend.model.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
public class ResetPasswordRequest {
    
    @Pattern(regexp = "^(?=.{8,})(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=]).*$", message = "La clave debe " +
            "tener mínimo 8 caracteres y al menos un caracter especial, no debe tener espacios y debe tener al menos " +
            "una letra mayúscula.")
    private String password;
    private String resetPasswordToken;
}
