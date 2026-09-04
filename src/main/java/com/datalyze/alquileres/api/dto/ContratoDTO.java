package com.datalyze.alquileres.api.dto;

import com.datalyze.alquileres.api.entity.PagoEntity;

import java.time.LocalDate;
import java.util.List;

public record ContratoDTO(
        Integer idContrato,
        ClienteResumenDTO cliente,
        PropiedadDTO propiedad,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Double montoGarantia,
        Double montoMensual,
        Integer diaPago,
        String estado,
        List<PagoResumenDTO> pago

) {
}
