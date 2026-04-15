package com.datalyze.alquileres.api.dto;

import java.time.LocalDate;

public record ContratoDTO(
        Integer idContrato,
        ClienteResumenDTO cliente,
        PropiedadDTO propiedad,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Double montoGarantia,
        Double montoMensual,
        Integer diaPago,
        String estado

) {
}
