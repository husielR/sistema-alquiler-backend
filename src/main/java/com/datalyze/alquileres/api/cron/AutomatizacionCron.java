package com.datalyze.alquileres.api.cron;
import com.datalyze.alquileres.api.entity.ContratoEntity;
import com.datalyze.alquileres.api.entity.PagoEntity;
import com.datalyze.alquileres.api.enumeration.ContratoEstado;
import com.datalyze.alquileres.api.enumeration.PagoEstado;
import com.datalyze.alquileres.api.enumeration.PagoTipoPago;
import com.datalyze.alquileres.api.repository.ContratoRepository;
import com.datalyze.alquileres.api.repository.PagoRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class AutomatizacionCron {

    private final PagoRepository pagoRepository;
    private final ContratoRepository contratoRepository;

    // Se ejecuta todos los días a las 00:01:00
    @Scheduled(cron = "0 1 0 * * ?")
    @Transactional
    public void procesoNocturnoDiario() {
        LocalDate hoy = LocalDate.now();

        // 1. CAZADOR DE MOROSOS: Pasa de Pendiente a Atrasado si ya venció
        List<PagoEntity> pendientes = pagoRepository.findByEstado(PagoEstado.Pendiente);
        for (PagoEntity pago : pendientes) {
            if (pago.getFechaVencimiento().isBefore(hoy)) {
                pago.setEstado(PagoEstado.Atrasado);
                pagoRepository.save(pago);
            }
        }


        List<ContratoEntity> informalesActivos = contratoRepository.findByEstadoAndFechaFinIsNull(ContratoEstado.Activo);

        for (ContratoEntity contrato : informalesActivos) {
            PagoEntity ultimoPago = pagoRepository.findFirstByIdContratoOrderByPeriodoAnioDescPeriodoMesDesc(contrato.getIdContrato());

            if (ultimoPago != null) {
                boolean necesitaNuevoRecibo = false;

                if (hoy.getYear() > ultimoPago.getPeriodoAnio()) {
                    necesitaNuevoRecibo = true;
                } else if (hoy.getYear() == ultimoPago.getPeriodoAnio() && hoy.getMonthValue() >= ultimoPago.getPeriodoMes()) {
                    necesitaNuevoRecibo = true;
                }

                if (necesitaNuevoRecibo) {
                    int nuevoMes = ultimoPago.getPeriodoMes() == 12 ? 1 : ultimoPago.getPeriodoMes() + 1;
                    int nuevoAnio = ultimoPago.getPeriodoMes() == 12 ? ultimoPago.getPeriodoAnio() + 1 : ultimoPago.getPeriodoAnio();

                    PagoEntity nuevoPago = new PagoEntity();
                    nuevoPago.setIdContrato(contrato.getIdContrato());
                    nuevoPago.setPeriodoAnio(nuevoAnio);
                    nuevoPago.setPeriodoMes(nuevoMes);
                    nuevoPago.setMontoPagado(contrato.getMontoMensual());
                    nuevoPago.setEstado(PagoEstado.Pendiente);
                    nuevoPago.setTipoPago(PagoTipoPago.Mensualidad);

                    // Calculamos la fecha ideal y le damos 7 días de tolerancia
                    int diaIdeal = Math.min(contrato.getDiaPago(), LocalDate.of(nuevoAnio, nuevoMes, 1).lengthOfMonth());
                    LocalDate fechaIdealDePago = LocalDate.of(nuevoAnio, nuevoMes, diaIdeal);
                    nuevoPago.setFechaVencimiento(fechaIdealDePago.plusDays(7));

                    pagoRepository.save(nuevoPago);
                }
            }
        }
    }
}