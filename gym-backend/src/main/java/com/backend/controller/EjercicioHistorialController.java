package com.backend.controller;

import com.backend.dto.EjercicioHistorialRequestDto;
import com.backend.dto.EjercicioHistorialResponseDto;
import com.backend.service.IEjercicioHistorialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ejercicios")
@RequiredArgsConstructor
public class EjercicioHistorialController {

    private final IEjercicioHistorialService historialService;

    // Endpoint POST para guardar al terminar el entrenamiento
    @PostMapping("/{id}/historial/usuario/{uId}")
    public ResponseEntity<EjercicioHistorialResponseDto> guardarHistorial(
            @PathVariable("id") Long entrenamientoId,
            @PathVariable("uId") Long usuarioId,
            @RequestBody EjercicioHistorialRequestDto dto) {
        
        EjercicioHistorialResponseDto response = historialService.guardarHistorial(entrenamientoId, usuarioId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Endpoint GET para que el resumen dibuje las gráficas
    @GetMapping("/{id}/historial/usuario/{uId}")
    public ResponseEntity<List<EjercicioHistorialResponseDto>> obtenerHistorial(
            @PathVariable("id") Long entrenamientoId,
            @PathVariable("uId") Long usuarioId) {
        
        List<EjercicioHistorialResponseDto> response = historialService.obtenerHistorial(entrenamientoId, usuarioId);
        return ResponseEntity.ok(response);
    }
}