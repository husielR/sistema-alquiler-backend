package com.datalyze.alquileres.api.dto.request;

import java.time.LocalDate;

public record PagoRequestDTO(
        String idContrato,
        Integer periodoMes,
        Integer periodoAnio,
        Double montoPagado,
        LocalDate fechaPago,
        LocalDate fechaVencimiento,
        String estado,
        String tipoPago
) {
}
