package com.datalyze.alquileres.api.dto;

import java.time.LocalDate;

public record PagoDTO(
    Integer idPago,
    ContratoDTO contrato,
    Integer periodoMes,
    Integer periodoAnio,
    Double montoPagado,
    LocalDate fechaPago,
    LocalDate fechaVencimiento,
    String estado,
    String tipoPago

) {
}
