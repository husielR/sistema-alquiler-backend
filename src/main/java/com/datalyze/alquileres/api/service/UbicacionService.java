package com.datalyze.alquileres.api.service;

import com.datalyze.alquileres.api.dto.UbicacionDTO;
import com.datalyze.alquileres.api.dto.request.UbicacionRequestDTO;
import com.datalyze.alquileres.api.entity.UbicacionEntity;
import com.datalyze.alquileres.api.mapper.UbicacionMapper;
import com.datalyze.alquileres.api.repository.UbicacionRepository;
import com.datalyze.alquileres.api.service.imp.CrudImp;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Service
@AllArgsConstructor
public class UbicacionService implements CrudImp <UbicacionDTO, UbicacionRequestDTO> {
    private final UbicacionRepository ubicacionRepository;
    private final UbicacionMapper ubicacionMapper;

    @Override
    public List<UbicacionDTO> obtenerTodos() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isEncargado = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ENCARGADO"));

        if (isEncargado) {
            @SuppressWarnings("unchecked")
            List<Integer> misSedes = (List<Integer>) auth.getDetails();

            // ---> CÓDIGO DEFENSIVO AQUÍ <---
            if (misSedes == null || misSedes.isEmpty()) {
                return List.of(); // Evita error SQL si no tiene sedes
            }

            return this.ubicacionMapper.toDtoList(this.ubicacionRepository.findAllById(misSedes));
        }

        return this.ubicacionMapper.toDtoList(this.ubicacionRepository.findAll());
    }

    @Override
    public UbicacionDTO obtenerPorId(Integer id) {
        UbicacionEntity ubicacion = this.ubicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicacion no encontrado con ID: " + id));
        return this.ubicacionMapper.toDto(ubicacion);
    }

    @Override
    public UbicacionDTO crear(UbicacionRequestDTO request) {
        return null;
    }

    @Override
    public UbicacionDTO actualizar(Integer id, UbicacionRequestDTO request) {
        return null;
    }

    @Override
    public void eliminar(Integer id) {

    }
}
