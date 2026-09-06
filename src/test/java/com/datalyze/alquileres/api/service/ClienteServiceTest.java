package com.datalyze.alquileres.api.service;

import com.datalyze.alquileres.api.dto.ClienteDTO;
import com.datalyze.alquileres.api.dto.request.ClienteRequestDTO;
import com.datalyze.alquileres.api.entity.ClienteEntity;
import com.datalyze.alquileres.api.mapper.ClienteMapper;
import com.datalyze.alquileres.api.repository.ClienteRepository;
import org.hibernate.boot.internal.Extends;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Habilita las anotaciones de Mockito
class ClienteServiceTest {

    // 1. FINGIMOS LAS HERRAMIENTAS (Mocks)
    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    // 2. INYECTAMOS LOS FAKES EN EL SERVICIO REAL
    @InjectMocks
    private ClienteService clienteService;


    @Test
    void crearCliente_DebeRetornarDtoGuardado() {
        // --- GIVEN (Preparar el escenario y programar los fakes) ---
        ClienteRequestDTO request = new ClienteRequestDTO("70123456", "Husiel", "Ruiz", "999888777", "husiel@mail.com", "999111222");

        ClienteEntity entidadSinId = new ClienteEntity();
        entidadSinId.setNombres("Husiel");

        ClienteEntity entidadGuardada = new ClienteEntity();
        entidadGuardada.setIdCliente(1); // La BD simulada le asigna un ID
        entidadGuardada.setNombres("Husiel");

        ClienteDTO dtoEsperado = new ClienteDTO(1, "70123456", "Husiel", "Ruiz", "999888777", "husiel@mail.com", "999111222", Collections.emptyList());

        // Le decimos a los fakes exactamente qué responder cuando el servicio los llame
        when(clienteMapper.toEntity(any(ClienteRequestDTO.class))).thenReturn(entidadSinId);
        when(clienteRepository.save(any(ClienteEntity.class))).thenReturn(entidadGuardada);
        when(clienteMapper.toDto(any(ClienteEntity.class))).thenReturn(dtoEsperado);

        // --- WHEN (Actuar: Llamamos al método real) ---
        ClienteDTO resultado = clienteService.crearCliente(request);

        // --- THEN (Verificar: Comprobamos que hizo su trabajo) ---
        assertNotNull(resultado, "El DTO retornado no debe ser nulo");
        assertEquals(1, resultado.idCliente(), "El ID del cliente debe ser el generado por la BD");
        assertEquals("Husiel", resultado.nombres());

        // Verificamos que el repositorio intentó guardar en la BD exactamente 1 vez
        Mockito.verify(clienteRepository, Mockito.times(1)).save(any(ClienteEntity.class));
    }


    @Test
    void actualizarCliente() {
        ClienteRequestDTO request = new ClienteRequestDTO("70123456", "Husiel", "Ruiz", "999888777", "husiel@mail.com", "999111222");
        ClienteEntity entidadConId = new ClienteEntity();
        entidadConId.setIdCliente(1);
        entidadConId.setNombres("Husiel");
        entidadConId.setApellidos("Huaranca");
        ClienteDTO dtoEsperado = new ClienteDTO(entidadConId.getIdCliente(),"70123456", "Husiel", "Ruiz", "999888777", "husiel@mail.com", "999111222",Collections.emptyList());
        Integer idCliente = 1;

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(entidadConId));
        when(clienteRepository.save(any(ClienteEntity.class))).thenReturn(entidadConId);
        when(clienteMapper.toDto(any(ClienteEntity.class))).thenReturn(dtoEsperado);
        ClienteDTO resultado = clienteService.actualizarCliente(idCliente, request);

        assertNotNull(resultado, "El dto debe tener ser no nulo");
        assertEquals(entidadConId.getIdCliente() ,resultado.idCliente(), "El ID del cliente debe ser igual");

    }

    @Test
    void actualizarCliente_idNulo() {
        Integer id = null;
        ClienteRequestDTO request = new ClienteRequestDTO(
                "70123456",
                "Husiel",
                "Ruiz",
                "999888777",
                "husiel@mail.com",
                "999111222"
        );

        assertThrows(RuntimeException.class, () -> clienteService.actualizarCliente(id, request));

    }

    @Test
    void actualizarCliente_noExiste() {
        Integer id = 2;
        ClienteRequestDTO request = new ClienteRequestDTO(
                "70123456",
                "Husiel",
                "Ruiz",
                "999888777",
                "husiel@mail.com",
                "999111222"
        );

        when(clienteRepository.findById(id)).thenReturn(Optional.ofNullable(mock(ClienteEntity.class)));
        assertThrows(RuntimeException.class, () -> clienteService.actualizarCliente(id, request));

    }

}