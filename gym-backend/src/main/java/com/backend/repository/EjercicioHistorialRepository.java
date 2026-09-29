package com.backend.repository;

import com.backend.domain.EjercicioHistorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EjercicioHistorialRepository extends JpaRepository<EjercicioHistorial, Long> {
    
    // Busca el historial filtrado por el ejercicio/entrenamiento y el usuario, ordenado cronológicamente
    List<EjercicioHistorial> findByEntrenamientoIdAndUsuarioIdOrderByFechaAsc(Long entrenamientoId, Long usuarioId);
}