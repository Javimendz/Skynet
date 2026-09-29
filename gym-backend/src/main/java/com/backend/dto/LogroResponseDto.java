package com.backend.dto;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data              // ← necesario, genera los getters automáticamente
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogroResponseDto {
    private Long id;
    private String nombre;           // "Guerrero de Acero"
    private String descripcion;      // "Completa 10 sesiones de fuerza"
    private String icono;            // URL o nombre del icono/badge
    private int progresoActual;      // 8
    private int progresoObjetivo;    // 10
    private boolean desbloqueado;
    private LocalDate fechaDesbloqueo; // null si no está desbloqueado aún
    
}