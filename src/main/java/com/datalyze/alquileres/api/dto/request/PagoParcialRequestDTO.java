package com.datalyze.alquileres.api.dto.request;

public record PagoParcialRequestDTO(
        Double montoTotal,
        Integer diasParaPagarResto
) {
}
