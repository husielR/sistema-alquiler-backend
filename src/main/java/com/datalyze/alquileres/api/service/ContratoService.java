package com.datalyze.alquileres.api.service;

import com.datalyze.alquileres.api.dto.ContratoDTO;
import com.datalyze.alquileres.api.dto.request.ContratoRequestDTO;
import com.datalyze.alquileres.api.entity.ContratoEntity;
import com.datalyze.alquileres.api.entity.PagoEntity;
import com.datalyze.alquileres.api.entity.PropiedadEntity;
import com.datalyze.alquileres.api.enumeration.ContratoEstado;
import com.datalyze.alquileres.api.enumeration.PagoEstado;
import com.datalyze.alquileres.api.enumeration.PagoTipoPago;
import com.datalyze.alquileres.api.enumeration.PropiedadEstado;
import com.datalyze.alquileres.api.mapper.ContratoMapper;
import com.datalyze.alquileres.api.repository.ContratoRepository;
import com.datalyze.alquileres.api.repository.PagoRepository;
import com.datalyze.alquileres.api.repository.PropiedadRepository;
import com.datalyze.alquileres.api.service.imp.CrudImp;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.MONTHS;

@Service
@AllArgsConstructor
public class ContratoService implements CrudImp<ContratoDTO, ContratoRequestDTO> {
    private final ContratoMapper contratoMapper;
    private final ContratoRepository contratoRepository;
    private final PagoRepository pagoRepository;
    private final PropiedadRepository propiedadRepository;

    @Override
    public List<ContratoDTO> obtenerTodos() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isEncargado = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ENCARGADO"));

        List<ContratoEntity> entidades;

        if (isEncargado) {
            @SuppressWarnings("unchecked")
            List<Integer> misSedes = (List<Integer>) auth.getDetails();
            if (misSedes == null || misSedes.isEmpty()) {
                misSedes = List.of(-1); // Código defensivo
            }
            // Usamos la nueva consulta anidada
            entidades = this.contratoRepository.findByPropiedad_IdUbicacionIn(misSedes);
        } else {
            // ADMIN ve todo
            entidades = this.contratoRepository.findAll();
        }

        return this.contratoMapper.toDtoList(entidades);
    }


    @Override
    public ContratoDTO obtenerPorId(Integer id) {
        ContratoEntity contratoEntity = this.contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado con ID: " + id));

        validarAccesoSede(contratoEntity.getPropiedad().getIdUbicacion());
        return this.contratoMapper.toDto(contratoEntity);
    }

    @Override
    @Transactional
    public ContratoDTO crear(ContratoRequestDTO request) {
        PropiedadEntity propiedad = this.propiedadRepository.findById(request.idPropiedad())
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));

        if (propiedad.getEstado() == PropiedadEstado.Ocupado) {
            throw new RuntimeException("La propiedad ya está ocupada.");
        }

        propiedad.setEstado(PropiedadEstado.Ocupado);
        this.propiedadRepository.save(propiedad);
        ContratoEntity contratoEntity = this.contratoMapper.toEntity(request);
        contratoEntity.setEstado(ContratoEstado.Activo);
        boolean esInformal = request.fechaFin() == null;
        if (esInformal) {
            contratoEntity.setFechaFin(LocalDate.of(2099, 12, 31));
        }
        contratoEntity = this.contratoRepository.save(contratoEntity);

        List<PagoEntity> pagos = new ArrayList<>();
        LocalDate fecha = contratoEntity.getFechaInicio();
        long mesesAGenerar = esInformal ? 1 : MONTHS.between(contratoEntity.getFechaInicio(), contratoEntity.getFechaFin());

        for (int i = 0; i < mesesAGenerar; i++) {
            PagoEntity pagoEntity = new PagoEntity();
            pagoEntity.setIdContrato(contratoEntity.getIdContrato());
            pagoEntity.setPeriodoAnio(fecha.getYear());
            pagoEntity.setPeriodoMes(fecha.getMonthValue());
            pagoEntity.setMontoPagado(contratoEntity.getMontoMensual());
            pagoEntity.setEstado(PagoEstado.Pendiente);
            pagoEntity.setTipoPago(PagoTipoPago.Mensualidad);
            int diaIdeal = Math.min(contratoEntity.getDiaPago(), fecha.lengthOfMonth());
            LocalDate fechaIdealDePago = fecha.withDayOfMonth(diaIdeal);
            pagoEntity.setFechaVencimiento(fechaIdealDePago.plusDays(7));
            pagos.add(pagoEntity);
            fecha = fecha.plusMonths(1);
        }

        this.pagoRepository.saveAll(pagos);

        return this.contratoMapper.toDto(contratoEntity);
    }

    @Override
    @Transactional
    public ContratoDTO actualizar(Integer id, ContratoRequestDTO request) {
        ContratoEntity contratoExistente = this.contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado con ID: " + id));

        if (contratoExistente.getEstado() == ContratoEstado.Anulado) {
            throw new RuntimeException("Operación denegada: Un Contrato Anulado no puede ser modificado.");
        }

        if (!Objects.equals(contratoExistente.getIdPropiedad(), request.idPropiedad())) {
            throw new RuntimeException("No se puede cambiar de propiedad en un contrato vigente. Finalice este contrato y cree uno nuevo para el otro cuarto.");
        }

        this.contratoMapper.updateEntity(request, contratoExistente);
        Integer idPropiedad = contratoExistente.getIdPropiedad();

        PropiedadEntity propiedad = propiedadRepository.findById(contratoExistente.getIdPropiedad())
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + idPropiedad));

        ContratoEstado nuevoEstado = contratoExistente.getEstado();

        if (nuevoEstado == ContratoEstado.Finalizado || nuevoEstado == ContratoEstado.Incumplido) {
            propiedad.setEstado(PropiedadEstado.Disponible);
        } else if (nuevoEstado == ContratoEstado.Activo) {
            propiedad.setEstado(PropiedadEstado.Ocupado);
        }

        this.propiedadRepository.save(propiedad);
        contratoExistente = this.contratoRepository.save(contratoExistente);

        return this.contratoMapper.toDto(contratoExistente);
    }

    @Override
    public void eliminar(Integer id) {
        if (!this.contratoRepository.existsById(id)) {
            throw new RuntimeException("Contrato no encontrado con ID: " + id);
        }
        this.contratoRepository.deleteById(id);
    }

    public List<ContratoDTO> obtenerContratoDisponibleCliente(Integer idCliente) {
        List<ContratoEntity> contratoEntity = this.contratoRepository.findByEstadoAndCliente_IdCliente(ContratoEstado.Activo, idCliente);

        return this.contratoMapper.toDtoList(contratoEntity);

    }

    @Transactional
    public ContratoDTO anularContrato(Integer id) {
        ContratoEntity contratoExistente = this.contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("contrato no encontrado con ID: " + id));

        if (contratoExistente.getEstado() == ContratoEstado.Anulado) {
            throw new RuntimeException("El contrato ya se encuentra anulado.");
        }

        contratoExistente.setEstado(ContratoEstado.Anulado);
        contratoExistente = this.contratoRepository.save(contratoExistente);

        List<PagoEntity> pagosContrato = this.pagoRepository.findAllByIdContrato(contratoExistente.getIdContrato());
        for (PagoEntity pago : pagosContrato) {
            if (pago.getEstado() == PagoEstado.Pendiente || pago.getEstado() == PagoEstado.Atrasado) {
                pago.setEstado(PagoEstado.Anulado);
            }
        }
        this.pagoRepository.saveAll(pagosContrato);

        PropiedadEntity propiedad = this.propiedadRepository.findById(contratoExistente.getIdPropiedad())
                .orElseThrow(() -> new RuntimeException("Propiedad vinculada no encontrada"));

        propiedad.setEstado(PropiedadEstado.Disponible);
        this.propiedadRepository.save(propiedad);

        return this.contratoMapper.toDto(contratoExistente);
    }

    @Transactional
    public ContratoDTO finalizarContrato(Integer id) {

        ContratoEntity contratoExistente = this.contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado con ID: " + id));

        if (contratoExistente.getEstado() != ContratoEstado.Activo) {
            throw new RuntimeException("Solo se pueden finalizar contratos que estén Activos.");
        }

        contratoExistente.setEstado(ContratoEstado.Finalizado);
        contratoExistente = this.contratoRepository.save(contratoExistente);

        List<PagoEntity> pagosContrato = this.pagoRepository.findAllByIdContrato(contratoExistente.getIdContrato());
        for (PagoEntity pago : pagosContrato) {
            if (pago.getEstado() == PagoEstado.Pendiente || pago.getEstado() == PagoEstado.Atrasado) {
                pago.setEstado(PagoEstado.Anulado);
            }
        }
        this.pagoRepository.saveAll(pagosContrato);

        PropiedadEntity propiedad = this.propiedadRepository.findById(contratoExistente.getIdPropiedad())
                .orElseThrow(() -> new RuntimeException("Propiedad vinculada no encontrada"));

        propiedad.setEstado(PropiedadEstado.Disponible);
        this.propiedadRepository.save(propiedad);

        return this.contratoMapper.toDto(contratoExistente);
    }

    @Transactional
    public ContratoDTO incumplirContrato(Integer id) {

        ContratoEntity contratoExistente = this.contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado con ID: " + id));

        if (contratoExistente.getEstado() != ContratoEstado.Activo) {
            throw new RuntimeException("Solo se pueden marcar como incumplido que estén Activos.");
        }

        contratoExistente.setEstado(ContratoEstado.Incumplido);
        contratoExistente = this.contratoRepository.save(contratoExistente);

        List<PagoEntity> pagosContrato = this.pagoRepository.findAllByIdContrato(contratoExistente.getIdContrato());
        for (PagoEntity pago : pagosContrato) {
            if (pago.getEstado() == PagoEstado.Pendiente || pago.getEstado() == PagoEstado.Atrasado) {
                pago.setEstado(PagoEstado.Anulado);
            }
        }
        this.pagoRepository.saveAll(pagosContrato);

        PropiedadEntity propiedad = this.propiedadRepository.findById(contratoExistente.getIdPropiedad())
                .orElseThrow(() -> new RuntimeException("Propiedad vinculada no encontrada"));

        propiedad.setEstado(PropiedadEstado.Disponible);
        this.propiedadRepository.save(propiedad);

        return this.contratoMapper.toDto(contratoExistente);
    }

    @Transactional()
    public Page<ContratoDTO> buscarPaginados(int page, int size, String termino, Integer idPropiedad, ContratoEstado estado) {
        Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size);

        boolean isAdmin = isUsuarioAdmin();
        List<Integer> misSedes = getSedesDelUsuario();

        Page<ContratoEntity> entityPage = this.contratoRepository.buscarConFiltrosAvanzados(
                termino, idPropiedad, estado, isAdmin, misSedes, pageable);

        return entityPage.map(this.contratoMapper::toDto);
    }

    private boolean isUsuarioAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private List<Integer> getSedesDelUsuario() {
        @SuppressWarnings("unchecked")
        List<Integer> sedes = (List<Integer>) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return (sedes == null || sedes.isEmpty()) ? List.of(-1) : sedes;
    }

    private void validarAccesoSede(Integer idUbicacionElemento) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isEncargado = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ENCARGADO"));

        if (isEncargado) {
            @SuppressWarnings("unchecked")
            List<Integer> misSedes = (List<Integer>) auth.getDetails();
            // Verificamos si la sede del contrato NO está en la lista de sedes permitidas
            if (misSedes == null || !misSedes.contains(idUbicacionElemento)) {
                throw new RuntimeException("Acceso denegado: Este contrato pertenece a otra sede.");
            }
        }
    }


}
