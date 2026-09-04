package com.datalyze.alquileres.api.dto.request;

public record ClienteRequestDTO(
        String dniCe,
        String nombres,
        String apellidos,
        String telefono,
        String email,
        String contactoEmergencia
) {
}
