package com.datalyze.alquileres.api.service;

import com.datalyze.alquileres.api.dto.ClienteDTO;
import com.datalyze.alquileres.api.dto.ClienteResumenDTO;
import com.datalyze.alquileres.api.dto.request.ClienteRequestDTO;
import com.datalyze.alquileres.api.entity.ClienteEntity;
import com.datalyze.alquileres.api.enumeration.ContratoEstado;
import com.datalyze.alquileres.api.mapper.ClienteMapper;
import com.datalyze.alquileres.api.repository.ClienteRepository;
import com.datalyze.alquileres.api.repository.ContratoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final ContratoRepository contratoRepository;

    // R - Read (Lista Completa)
    public List<ClienteDTO> getClienteEntity() {
        return this.clienteMapper.toDtoList(this.clienteRepository.findAll());
    }

    // R - READ (Por ID)
    public ClienteDTO obtenerPorId(Integer id) {
        ClienteEntity cliente = this.clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        return this.clienteMapper.toDto(cliente);
    }

    // C - CREATE
    public ClienteDTO crearCliente(ClienteRequestDTO request) {
        // 1. Convertimos el Request a Entidad
        ClienteEntity nuevaEntidad = this.clienteMapper.toEntity(request);
        // 2. Guardamos en Base de Datos
        nuevaEntidad = this.clienteRepository.save(nuevaEntidad);
        // 3. Devolvemos el DTO completo (ahora ya tiene su ID generado)
        return this.clienteMapper.toDto(nuevaEntidad);
    }

    // U - UPDATE
    public ClienteDTO actualizarCliente(Integer id, ClienteRequestDTO request) {
        // 1. Buscamos si existe
        ClienteEntity entidadExistente = this.clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));

        // 2. MapStruct actualiza la entidad existente con los datos nuevos automáticamente
        this.clienteMapper.updateEntity(request, entidadExistente);

        // 3. Guardamos y devolvemos
        entidadExistente = this.clienteRepository.save(entidadExistente);
        return this.clienteMapper.toDto(entidadExistente);
    }

    // D - DELETE
    public void eliminarCliente(Integer id) {
        if (!this.clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }

        boolean tieneContratos = this.contratoRepository.existsByCliente_IdCliente(id);

        if (tieneContratos) {
            throw new RuntimeException("No se puede eliminar: Este cliente tiene contratos en su historial. Por favor, desactívelo en su lugar.");
        } else {
            this.clienteRepository.deleteById(id);
        }
    }


    public List<ClienteResumenDTO> getClienteNotContract() {
        return this.clienteMapper.toDtoResumenList(this.clienteRepository.findClientesConContratoActivo(ContratoEstado.Activo));
    }

    public List<ClienteResumenDTO> getClienteResumenEntity() {
        return this.clienteMapper.toDtoResumenList(this.clienteRepository.findAll());
    }

    public Page<ClienteDTO> getClientePaginadosEntity(int page, int size, String termino) {
        Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size);
        Page<ClienteEntity> entityPage;
        if (termino == null || termino.trim().isEmpty()) {
            entityPage = this.clienteRepository.findAll(pageable);
        } else {
            entityPage = this.clienteRepository.buscarPorOmnibox(termino, pageable);
        }
        return entityPage.map(this.clienteMapper::toDto);    }

}
