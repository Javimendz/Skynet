package com.backend.service;

import com.backend.dto.InvitacionResponseDto;
import com.backend.dto.InvitacionRequestDto;

public interface IInvitacionService {
    InvitacionResponseDto crearInvitacion(InvitacionRequestDto dto);
}