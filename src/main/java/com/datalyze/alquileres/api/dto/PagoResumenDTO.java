package com.datalyze.alquileres.api.dto;

import java.time.LocalDate;

public record PagoResumenDTO(
    Integer idPago,
    Integer contratoIdContrato,
    Integer periodoMes,
    Integer periodoAnio,
    Double montoPagado,
    LocalDate fechaPago,
    LocalDate fechaVencimiento,
    String estado,
    String tipoPago

) {
}
