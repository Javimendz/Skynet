package com.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.domain.Horario;
import com.backend.domain.Invitacion;
import com.backend.domain.Usuario;
import com.backend.dto.InvitacionRequestDto;
import com.backend.dto.InvitacionResponseDto;
import com.backend.exceptions.BusinessException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.repository.HorarioRepository;
import com.backend.repository.InvitacionRepository;
import com.backend.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvitacionServiceImpl implements IInvitacionService {

    private final InvitacionRepository invitacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final HorarioRepository horarioRepository;
    private final NotificacionService notificacionService; // reutilizamos tu servicio ya existente

    @Override
    @Transactional
    public InvitacionResponseDto crearInvitacion(InvitacionRequestDto dto) {
        log.info("Creando invitación de {} para {}", dto.getUsuarioInvitaId(), dto.getEmailInvitado());

        Usuario usuarioInvita = usuarioRepository.findById(dto.getUsuarioInvitaId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Horario horario = null;
        if (dto.getHorarioId() != null) {
            horario = horarioRepository.findById(dto.getHorarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Horario no encontrado"));

            if (invitacionRepository.existsByEmailInvitadoAndHorarioId(dto.getEmailInvitado(), dto.getHorarioId())) {
                throw new BusinessException("Ya existe una invitación pendiente para este email en esta clase");
            }
        }

        String token = java.util.UUID.randomUUID().toString();

        Invitacion invitacion = Invitacion.builder()
                .emailInvitado(dto.getEmailInvitado())
                .token(token)
                .usuarioInvita(usuarioInvita)
                .horario(horario)
                .build();

        Invitacion guardada = invitacionRepository.save(invitacion);

        // Reutilizamos tu sistema de notificaciones existente
        // (esto sería un email, no una notificación in-app, pero deja el gancho listo)
        log.info("Invitación creada con token: {}", token);

        String enlace = "https://skynet-app.com/invitacion/" + token;

        return InvitacionResponseDto.builder()
                .id(guardada.getId())
                .emailInvitado(guardada.getEmailInvitado())
                .estado(guardada.getEstado())
                .enlaceInvitacion(enlace)
                .build();
    }

}