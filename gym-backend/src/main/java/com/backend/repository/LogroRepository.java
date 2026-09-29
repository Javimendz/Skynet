package com.backend.repository;


import com.backend.domain.Logro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogroRepository extends JpaRepository<Logro, Long> {
    // findAll() ya viene incluido de JpaRepository, es el catálogo completo
}
