package com.datalyze.alquileres.api.dto;

public record ClienteResumenDTO(
        String dniCe,
        String nombres,
        String apellidos,
        String telefono,
        String contactoEmergencia
) {
}
