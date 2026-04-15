package com.datalyze.alquileres.api.dto;

public record PropiedadDTO(
        Integer idPropiedad,
        TipoPropiedadDTO tipoPropiedad,
        UbicacionDTO ubicacion,
        String identificador,
        Double precioBase,
        String estado

) {
}
