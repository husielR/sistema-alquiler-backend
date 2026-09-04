package com.datalyze.alquileres.api.dto.request;

public record PropiedadRequestDTO(
        Integer idTipo,
        Integer idUbicacion,
        String identificador,
        Double precioBase,
        String estado
) {
}
