package com.tinexlab.backend.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GenericResponse<T> {
    private int httpCode;
    private String message;
    private T data;

    public static <T> GenericResponse<T> getResponse(int code, String message, T data){
        return GenericResponse.<T>builder()
                .httpCode(code)
                .message(message)
                .data(data)
                .build();
    }
}
