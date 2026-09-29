package com.backend.repository;

import com.backend.domain.RegistroPeso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegistroPesoRepository extends JpaRepository<RegistroPeso, Long> {

    @Query("SELECT r FROM RegistroPeso r " +
           "WHERE r.usuario.id = :usuarioId " +
           "ORDER BY r.fechaRegistro ASC")
    List<RegistroPeso> findEvolucionPorUsuario(@Param("usuarioId") Long usuarioId);

    @Query("SELECT r FROM RegistroPeso r " +
           "WHERE r.usuario.id = :usuarioId " +
           "ORDER BY r.fechaRegistro ASC")
    List<RegistroPeso> obtenerEvolucionPesoPorUsuario(@Param("usuarioId") Long usuarioId);

}