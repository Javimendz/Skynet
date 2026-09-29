package com.backend.service;

import java.time.LocalDate;
import com.backend.dto.LogroResponseDto;
import com.backend.mapper.LogroMapper;
import com.backend.domain.Logro;
import com.backend.domain.UsuarioLogro;
import com.backend.domain.enums.TipoCondicion;
import com.backend.repository.EntrenamientoRepository;
import com.backend.repository.LogroRepository;
import com.backend.repository.ReservaRepository;
import com.backend.repository.RutinaRepository;
import com.backend.repository.UsuarioLogroRepository;
import com.backend.repository.UsuarioRepository;
import com.backend.service.ILogroService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogroServiceImpl implements ILogroService {

    private final LogroRepository logroRepository;
    private final UsuarioLogroRepository usuarioLogroRepository;
    private final EntrenamientoRepository entrenamientoRepository;
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RutinaRepository rutinaRepository;

    @Override
    public List<LogroResponseDto> obtenerLogrosUsuario(String username) {
        Long usuarioId = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"))
                .getId();

        List<Logro> catalogo = logroRepository.findAll();

        return catalogo.stream()
                .map(logro -> construirDto(logro, usuarioId))
                .collect(Collectors.toList());
    }

    @Override
    public LogroResponseDto obtenerProximoLogro(String username) {
        List<LogroResponseDto> todos = obtenerLogrosUsuario(username);

        return todos.stream()
                .filter(l -> !l.isDesbloqueado())
                .max(Comparator.comparingDouble(l ->
                        (double) l.getProgresoActual() / l.getProgresoObjetivo()))
                .orElse(null); // null = ya se han desbloqueado todos
    }

    // -------------------------------------------------------------
    // Lógica interna de cálculo de progreso
    // -------------------------------------------------------------
   private LogroResponseDto construirDto(Logro logro, Long usuarioId) {
    int progreso = calcularProgreso(logro, usuarioId);
    boolean desbloqueado = progreso >= logro.getObjetivo();

    LocalDate fechaDesbloqueo = usuarioLogroRepository
            .findByUsuarioIdAndLogroId(usuarioId, logro.getId())
            .map(UsuarioLogro::getFechaDesbloqueo)
            .orElse(null);

    if (desbloqueado && fechaDesbloqueo == null) {
        fechaDesbloqueo = LocalDate.now();
        guardarDesbloqueo(usuarioId, logro.getId(), fechaDesbloqueo);
    }

    return LogroMapper.toDto(logro, progreso, desbloqueado, fechaDesbloqueo);
}

   private int calcularProgreso(Logro logro, Long usuarioId) {
        return switch (logro.getTipoCondicion()) {
            case SESIONES_TOTALES ->
                rutinaRepository.contarSesionesCompletadas(usuarioId);

            case SESIONES_POR_INTENSIDAD ->
                rutinaRepository.contarSesionesPorIntensidad(usuarioId, logro.getFiltroIntensidad());

            case RESERVAS_TOTALES ->
                reservaRepository.contarReservasConfirmadas(usuarioId);

            case RACHA_DIAS_CONSECUTIVOS ->
                calcularRachaActual(usuarioId);
        };
    }


   private int calcularRachaActual(Long usuarioId) {
        List<LocalDate> fechas = rutinaRepository.findFechasSesionesOrdenadas(usuarioId);
     
        int racha = 0;
        LocalDate esperado = LocalDate.now();

        for (LocalDate fecha : fechas) {
            if (fecha.equals(esperado)) {
                racha++;
                esperado = esperado.minusDays(1);
            } else if (fecha.isBefore(esperado)) {
                break; // se rompió la racha
            }
        }
        return racha;
    }

    private void guardarDesbloqueo(Long usuarioId, Long logroId, LocalDate fecha) {
        log.info("Usuario {} desbloqueó logro {}", usuarioId, logroId);
        // usuarioLogroRepository.save(new UsuarioLogro(...));
    }


    
}