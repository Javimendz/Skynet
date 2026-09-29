package com.backend.repository;


import com.backend.domain.Invitacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitacionRepository extends JpaRepository<Invitacion, Long> {
    Optional<Invitacion> findByToken(String token);
    boolean existsByEmailInvitadoAndHorarioId(String email, Long horarioId);
}