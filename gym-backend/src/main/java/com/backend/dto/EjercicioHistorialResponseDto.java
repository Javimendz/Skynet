package com.backend.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class EjercicioHistorialResponseDto {
    private Long id;
    private Long usuarioId;
    private Long entrenamientoId;
    private Double peso;
    private Integer repeticiones;
    private LocalDateTime fecha;
}