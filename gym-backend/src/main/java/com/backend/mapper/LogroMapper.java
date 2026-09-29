package com.backend.mapper;


import com.backend.dto.LogroResponseDto;
import com.backend.domain.Logro;

import java.time.LocalDate;

public class LogroMapper {

    private LogroMapper() {
        // Clase de utilidad, no instanciable
    }

    public static LogroResponseDto toDto(Logro logro, int progresoActual, boolean desbloqueado, LocalDate fechaDesbloqueo) {
        return LogroResponseDto.builder()
                .id(logro.getId())
                .nombre(logro.getNombre())
                .descripcion(logro.getDescripcion())
                .icono(logro.getIcono())
                .progresoActual(Math.min(progresoActual, logro.getObjetivo()))
                .progresoObjetivo(logro.getObjetivo())
                .desbloqueado(desbloqueado)
                .fechaDesbloqueo(fechaDesbloqueo)
                .build();
    }

    
}