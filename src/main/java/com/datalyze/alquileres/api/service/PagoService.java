package com.datalyze.alquileres.api.service;

import com.datalyze.alquileres.api.dto.ContratoDTO;
import com.datalyze.alquileres.api.dto.PagoDTO;
import com.datalyze.alquileres.api.dto.PagoResumenDTO;
import com.datalyze.alquileres.api.dto.request.ContratoRequestDTO;
import com.datalyze.alquileres.api.dto.request.PagoCompletoRequestDTO;
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
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        pagoEntity.setEstado(PagoEstado.Pendiente);
        pagoEntity.setPeriodoAnio(LocalDate.now().getYear());
        pagoEntity.setPeriodoMes(LocalDate.now().getMonthValue());

        if (pagoEntity.getTipoPago() == PagoTipoPago.Mensualidad ||
                pagoEntity.getTipoPago() == PagoTipoPago.Deuda) {
            throw new RuntimeException("Solo puede generar manualmente cargos de tipo Penalidad o Garantía.");
        }

        if (pagoEntity.getIdContrato() == null) {
            throw new RuntimeException("Todo cargo debe estar asociado a un contrato.");
        }

        pagoEntity = this.pagoRepository.save(pagoEntity);
        return this.pagoMapper.toDto(pagoEntity);
    }

    @Override
    public PagoDTO actualizar(Integer id, PagoRequestDTO request) {
        PagoEntity pagoExistente = this.pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));

        if (pagoExistente.getEstado() == PagoEstado.Pagado || pagoExistente.getEstado() == PagoEstado.Anulado) {
            throw new RuntimeException("No se pueden editar recibos que ya están Pagados o Anulados.");
        }

        pagoExistente.setMontoPagado(request.montoPagado());
        pagoExistente.setFechaVencimiento(request.fechaVencimiento());

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
            pagoEntity.setFechaPago(request.fechaPago());
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

    @Transactional
    public PagoDTO reactivarPagoAnulado(Integer id) {
        PagoEntity pagoEntity = this.pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado."));

        if (pagoEntity.getEstado() != PagoEstado.Anulado) {
            throw new RuntimeException("Solo se pueden reactivar recibos que estén anulados.");
        }

        // Lo devolvemos a la vida
        pagoEntity.setEstado(PagoEstado.Pendiente);

        // Opcional: Si la fecha de vencimiento ya pasó, podrías ponerlo en Atrasado,
        // pero Pendiente es lo más seguro por ahora.

        pagoEntity = this.pagoRepository.save(pagoEntity);
        return this.pagoMapper.toDto(pagoEntity);
    }

    @Transactional
    public PagoDTO registrarPagoCompleto(Integer id, PagoCompletoRequestDTO request) {
        PagoEntity pagoExistente = this.pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));

        if (pagoExistente.getEstado() != PagoEstado.Pendiente && pagoExistente.getEstado() != PagoEstado.Atrasado) {
            throw new RuntimeException("Solo se pueden cobrar recibos Pendientes o Atrasados.");
        }

        pagoExistente.setEstado(PagoEstado.Pagado);
        pagoExistente.setFechaPago(request.fechaPago() != null ? request.fechaPago() : LocalDate.now());

        pagoExistente = this.pagoRepository.save(pagoExistente);
        return this.pagoMapper.toDto(pagoExistente);
    }

    @Transactional()
    public Page<PagoDTO> buscarPagosPaginados(int page, int size, String termino, Integer idPropiedad, List<PagoEstado> estados, LocalDate fechaInicio, LocalDate fechaFin) {
        Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size);
        if (estados == null || estados.isEmpty()) {
            estados = List.of(PagoEstado.values());
        }
        Page<PagoEntity> entityPage = this.pagoRepository.buscarPagosAvanzados(
                termino, idPropiedad, estados, fechaInicio, fechaFin, pageable);

        return entityPage.map(this.pagoMapper::toDto);
    }

}
