package com.backend.service;

import com.backend.dto.EjercicioHistorialRequestDto;
import com.backend.dto.EjercicioHistorialResponseDto;
import java.util.List;

public interface IEjercicioHistorialService {
    EjercicioHistorialResponseDto guardarHistorial(Long entrenamientoId, Long usuarioId, EjercicioHistorialRequestDto dto);
    List<EjercicioHistorialResponseDto> obtenerHistorial(Long entrenamientoId, Long usuarioId);
}