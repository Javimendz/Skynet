package com.backend.service;

import com.backend.domain.EjercicioHistorial;
import com.backend.domain.Entrenamiento;
import com.backend.domain.Usuario;
import com.backend.dto.EjercicioHistorialRequestDto;
import com.backend.dto.EjercicioHistorialResponseDto;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.mapper.EjercicioHistorialMapper;
import com.backend.repository.EjercicioHistorialRepository;
import com.backend.repository.EntrenamientoRepository;
import com.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EjercicioHistorialServiceImpl implements IEjercicioHistorialService {

    private final EjercicioHistorialRepository historialRepository;
    private final EntrenamientoRepository entrenamientoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EjercicioHistorialMapper historialMapper;

    @Override
    @Transactional
    public EjercicioHistorialResponseDto guardarHistorial(Long entrenamientoId, Long usuarioId, EjercicioHistorialRequestDto dto) {
        log.info("Guardando historial para el entrenamiento/ejercicio ID: {} y usuario ID: {}", entrenamientoId, usuarioId);

        Entrenamiento entrenamiento = entrenamientoRepository.findById(entrenamientoId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrenamiento no encontrado con ID: " + entrenamientoId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + usuarioId));

        EjercicioHistorial historial = EjercicioHistorial.builder()
                .entrenamiento(entrenamiento)
                .usuario(usuario)
                .peso(dto.getPeso())
                .repeticiones(dto.getRepeticiones())
                .build();

        EjercicioHistorial saved = historialRepository.save(historial);
        return historialMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EjercicioHistorialResponseDto> obtenerHistorial(Long entrenamientoId, Long usuarioId) {
        log.info("Obteniendo historial de rendimiento para el ejercicio ID: {} del usuario ID: {}", entrenamientoId, usuarioId);

        List<EjercicioHistorial> historiales = historialRepository.findByEntrenamientoIdAndUsuarioIdOrderByFechaAsc(entrenamientoId, usuarioId);

        return historiales.stream()
                .map(historialMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}