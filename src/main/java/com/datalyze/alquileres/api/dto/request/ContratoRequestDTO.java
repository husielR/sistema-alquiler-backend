package com.datalyze.alquileres.api.dto.request;

import java.time.LocalDate;

public record ContratoRequestDTO(
        Integer idCliente,
        Integer idPropiedad,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Double montoGarantia,
        Double montoMensual,
        Integer diaPago,
        String estado
) {
}
