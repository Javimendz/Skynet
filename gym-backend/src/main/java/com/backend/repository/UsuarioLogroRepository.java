package com.backend.repository;


import com.backend.domain.UsuarioLogro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioLogroRepository extends JpaRepository<UsuarioLogro, Long> {

    Optional<UsuarioLogro> findByUsuarioIdAndLogroId(Long usuarioId, Long logroId);
}