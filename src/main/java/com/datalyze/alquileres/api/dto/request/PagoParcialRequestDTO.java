package com.datalyze.alquileres.api.dto.request;

import java.time.LocalDate;

public record PagoParcialRequestDTO(
        Double montoTotal,
        Integer diasParaPagarResto,
        LocalDate fechaPago
) {
}
