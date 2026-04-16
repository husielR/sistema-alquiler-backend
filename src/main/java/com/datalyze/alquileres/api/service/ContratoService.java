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
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        return this.contratoMapper.toDtoList(this.contratoRepository.findAll());
    }

    @Override
    public ContratoDTO obtenerPorId(Integer id) {
        ContratoEntity contratoEntity = this.contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado con ID: " + id));

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
        contratoEntity = this.contratoRepository.save(contratoEntity);

        long meses = MONTHS.between(
                contratoEntity.getFechaInicio(),
                contratoEntity.getFechaFin()
        );

        List<PagoEntity> pagos = new ArrayList<>();
        LocalDate fecha = contratoEntity.getFechaInicio();

        for (int i = 0; i < meses; i++) {
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
    public ContratoDTO actualizar(Integer id, ContratoRequestDTO request) {
        ContratoEntity contratoExistente = this.contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado con ID: " + id));

        if (contratoExistente.getEstado() == ContratoEstado.Anulado) {
            throw new RuntimeException("Operación denegada: Un Contrato Anulado no puede ser modificado.");
        }

        this.contratoMapper.updateEntity(request, contratoExistente);
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

}
