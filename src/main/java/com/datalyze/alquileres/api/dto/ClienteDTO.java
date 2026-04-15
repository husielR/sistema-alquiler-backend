package com.datalyze.alquileres.api.dto;

import java.util.List;

public record ClienteDTO(
         String dniCe,
         String nombres,
         String apellidos,
         String telefono,
         String email,
         String contactoEmergencia,
         List<ContratoResumenDTO> contrato
) {
}
