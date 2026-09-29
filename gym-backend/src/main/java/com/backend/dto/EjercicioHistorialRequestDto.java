package com.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EjercicioHistorialRequestDto {
    private Double peso;
    private Integer repeticiones;
}