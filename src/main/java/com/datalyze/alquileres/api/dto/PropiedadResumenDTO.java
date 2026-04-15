package com.datalyze.alquileres.api.dto;

public record PropiedadResumenDTO(
        Integer idPropiedad,
        String tipoPropiedadNombre,
        String identificador,
        Double precioBase,
        String estado
) {
}
