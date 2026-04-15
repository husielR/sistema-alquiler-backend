package com.datalyze.alquileres.api.dto;

import java.time.LocalDate;

public record ContratoResumenDTO(
        Integer idContrato,
        PropiedadDTO propiedad,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Double montoGarantia,
        Double montoMensual,
        Integer diaPago,
        String estado
) {
}
