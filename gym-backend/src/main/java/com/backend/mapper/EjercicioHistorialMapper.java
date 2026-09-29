package com.backend.mapper;

import com.backend.domain.EjercicioHistorial;
import com.backend.dto.EjercicioHistorialResponseDto;
import org.springframework.stereotype.Component;

@Component
public class EjercicioHistorialMapper {

    public EjercicioHistorialResponseDto toResponseDto(EjercicioHistorial historial) {
        if (historial == null) {
            return null;
        }
        return EjercicioHistorialResponseDto.builder()
                .id(historial.getId())
                .usuarioId(historial.getUsuario() != null ? historial.getUsuario().getId() : null)
                .entrenamientoId(historial.getEntrenamiento() != null ? historial.getEntrenamiento().getId() : null)
                .peso(historial.getPeso())
                .repeticiones(historial.getRepeticiones())
                .fecha(historial.getFecha())
                .build();
    }
}