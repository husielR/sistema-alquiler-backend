package com.datalyze.alquileres.api.dto;

public record ClienteResumenDTO(
        Integer idCliente,
        String dniCe,
        String nombres,
        String apellidos,
        String telefono,
        String contactoEmergencia
) {
}
