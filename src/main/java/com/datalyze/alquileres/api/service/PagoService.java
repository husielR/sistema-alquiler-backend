package com.datalyze.alquileres.api.service;

import com.datalyze.alquileres.api.dto.ContratoDTO;
import com.datalyze.alquileres.api.dto.PagoDTO;
import com.datalyze.alquileres.api.dto.PagoResumenDTO;
import com.datalyze.alquileres.api.dto.request.ContratoRequestDTO;
import com.datalyze.alquileres.api.dto.request.PagoParcialRequestDTO;
import com.datalyze.alquileres.api.dto.request.PagoRequestDTO;
import com.datalyze.alquileres.api.entity.ContratoEntity;
import com.datalyze.alquileres.api.entity.PagoEntity;
import com.datalyze.alquileres.api.entity.PropiedadEntity;
import com.datalyze.alquileres.api.enumeration.PagoEstado;
import com.datalyze.alquileres.api.enumeration.PagoTipoPago;
import com.datalyze.alquileres.api.mapper.PagoMapper;
import com.datalyze.alquileres.api.repository.ContratoRepository;
import com.datalyze.alquileres.api.repository.PagoRepository;
import com.datalyze.alquileres.api.service.imp.CrudImp;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class PagoService implements CrudImp<PagoDTO, PagoRequestDTO> {
    private final PagoRepository pagoRepository;
    private final PagoMapper pagoMapper;
    private final ContratoRepository contratoRepository;

    @Override
    public List<PagoDTO> obtenerTodos() {
        List<PagoEntity> entidades = this.pagoRepository.findAll();
        return this.pagoMapper.toDtoList(entidades);
    }

    @Override
    public PagoDTO obtenerPorId(Integer id) {
        PagoEntity pagoEntity = this.pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
        return this.pagoMapper.toDto(pagoEntity);

    }

    @Override
    public PagoDTO crear(PagoRequestDTO request) {
        PagoEntity pagoEntity = this.pagoMapper.toEntity(request);
        pagoEntity = this.pagoRepository.save(pagoEntity);
        return this.pagoMapper.toDto(pagoEntity);
    }

    @Override
    public PagoDTO actualizar(Integer id, PagoRequestDTO request) {
        PagoEntity pagoExistente = this.pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));

        if (pagoExistente.getEstado() == PagoEstado.Anulado) {
            throw new RuntimeException("Operación denegada: Un recibo Anulado no puede ser modificado.");
        }

        this.pagoMapper.updateEntity(request, pagoExistente);
        pagoExistente = this.pagoRepository.save(pagoExistente);
        return this.pagoMapper.toDto(pagoExistente);
    }

    @Override
    public void eliminar(Integer id) {
        if (!this.pagoRepository.existsById(id)) {
            throw new RuntimeException("Pago no encontrado con ID: " + id);
        }
        this.pagoRepository.deleteById(id);
    }

    public List<PagoResumenDTO> obtenerTodosResumen() {
        List<PagoEntity> entidades = this.pagoRepository.findAll();
        return this.pagoMapper.toDtoResumenList(entidades);
    }

    @Transactional
    public PagoDTO pagoParcial(Integer id, PagoParcialRequestDTO request){
        PagoEntity pagoEntity = this.pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));

        if (pagoEntity.getEstado() != PagoEstado.Pendiente && pagoEntity.getEstado() != PagoEstado.Atrasado) {
            throw new RuntimeException("Solo se pueden hacer pagos parciales sobre recibos Pendientes o Atrasados.");
        }

        if (request.montoTotal() >= pagoEntity.getMontoPagado()) {
            throw new RuntimeException("El monto abonado debe ser menor a la deuda total.");
        }

            Double saldoPendiente = pagoEntity.getMontoPagado() - request.montoTotal();

            pagoEntity.setMontoPagado(request.montoTotal());
            pagoEntity.setEstado(PagoEstado.Pagado);
            pagoEntity.setFechaPago(LocalDate.now());
            pagoEntity = this.pagoRepository.save(pagoEntity);

            PagoEntity nuevoPagoEntity = new PagoEntity();
            nuevoPagoEntity.setIdContrato(pagoEntity.getIdContrato());
            nuevoPagoEntity.setMontoPagado(saldoPendiente);
            nuevoPagoEntity.setPeriodoAnio(pagoEntity.getPeriodoAnio());
            nuevoPagoEntity.setPeriodoMes(pagoEntity.getPeriodoMes());
            nuevoPagoEntity.setFechaVencimiento(LocalDate.now().plusDays(request.diasParaPagarResto()));
            nuevoPagoEntity.setTipoPago(PagoTipoPago.Deuda);
            nuevoPagoEntity.setEstado(PagoEstado.Pendiente);
           this.pagoRepository.save(nuevoPagoEntity);
            return this.pagoMapper.toDto(pagoEntity);

    }

    public PagoDTO anularPago(Integer id) {
        PagoEntity pagoExistente = this.pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));

        if (pagoExistente.getEstado() == PagoEstado.Anulado) {
            throw new RuntimeException("El pago ya se encuentra anulado.");
        }

        pagoExistente.setEstado(PagoEstado.Anulado);
        pagoExistente = this.pagoRepository.save(pagoExistente);

        return this.pagoMapper.toDto(pagoExistente);
    }

}
