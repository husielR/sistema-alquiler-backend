package com.datalyze.alquileres.api.service;

import com.datalyze.alquileres.api.dto.ContratoDTO;
import com.datalyze.alquileres.api.dto.request.ContratoRequestDTO;
import com.datalyze.alquileres.api.entity.*;
import com.datalyze.alquileres.api.enumeration.ContratoEstado;
import com.datalyze.alquileres.api.enumeration.PropiedadEstado;
import com.datalyze.alquileres.api.mapper.ContratoMapper;
import com.datalyze.alquileres.api.repository.ContratoRepository;
import com.datalyze.alquileres.api.repository.PagoRepository;
import com.datalyze.alquileres.api.repository.PropiedadRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Habilita las anotaciones de Mockito
class ContratoServiceTest {

    @Mock
    private ContratoMapper contratoMapper;
    @Mock
    private ContratoRepository contratoRepository;
    @Mock
    private PagoRepository pagoRepository;
    @Mock
    private PropiedadRepository propiedadRepository;

    @InjectMocks
    private ContratoService contratoService;

    @Test
    void crearContrato_todoCorrecto() {
        ContratoRequestDTO contratoAleatorio = new ContratoRequestDTO(
                1024,                                 // idCliente
                2,                                  // idPropiedad
                LocalDate.of(2026, 10, 1),            // fechaInicio
                LocalDate.of(2027, 10, 1),            // fechaFin
                1500.00,                              // montoGarantia
                750.00,                               // montoMensual
                5,                                    // diaPago
                "ACTIVO"                              // estado
        );
        Integer id = 2;
        TipoPropiedadEntity tipoPropiedadMock = mock(TipoPropiedadEntity.class);
        UbicacionEntity ubicacionMock = mock(UbicacionEntity.class);
        // 2. Construimos la entidad principal
        PropiedadEntity propiedadFicticia = new PropiedadEntity();
        propiedadFicticia.setIdPropiedad(2);
        propiedadFicticia.setIdUbicacion(12);
        propiedadFicticia.setIdTipo(3);
        propiedadFicticia.setIdentificador("DEP-402-B");
        propiedadFicticia.setPrecioBase(850.00);
        propiedadFicticia.setEstado(PropiedadEstado.Disponible);
        propiedadFicticia.setTipoPropiedad(tipoPropiedadMock);
        propiedadFicticia.setUbicacion(ubicacionMock);

        PropiedadEntity propiedadOcupada = new PropiedadEntity();
        propiedadOcupada.setIdPropiedad(3);

        ContratoEntity contrato = ContratoEntity.builder()
                .idContrato(1)
                .idCliente(10)
                .idPropiedad(20)
                .fechaInicio(LocalDate.of(2026, 1, 1))
                .fechaFin(LocalDate.of(2026, 12, 31))
                .montoGarantia(1000.0)
                .montoMensual(1500.0)
                .diaPago(5)
                .estado(ContratoEstado.Anulado)
                .cliente(mock(ClienteEntity.class))
                .propiedad(mock(PropiedadEntity.class))
                .pago(List.of())
                .build();

        ContratoDTO contratoDTO = mock(ContratoDTO.class);

        when(propiedadRepository.findById(id)).thenReturn(Optional.of(propiedadFicticia));
        when(contratoMapper.toEntity(contratoAleatorio)).thenReturn((contrato));
        when(contratoRepository.save(any(ContratoEntity.class))).thenReturn(contrato);
        when(contratoMapper.toDto(any(ContratoEntity.class))).thenReturn(contratoDTO);

        ContratoDTO result = contratoService.crear(contratoAleatorio);
        assertNotNull(result);
        assertEquals(contratoDTO, result);
    }

    @Test
    void crearContrato_estadoOcupado() {

        Integer idPropiedadTest = 99;

        ContratoRequestDTO contratoAleatorio = new ContratoRequestDTO(
                1024,                                 // idCliente
                idPropiedadTest,                                  // idPropiedad
                LocalDate.of(2026, 10, 1),            // fechaInicio
                LocalDate.of(2027, 10, 1),            // fechaFin
                1500.00,                              // montoGarantia
                750.00,                               // montoMensual
                5,                                    // diaPago
                "ACTIVO"                              // estado
        );


        PropiedadEntity propiedadOcupada = new PropiedadEntity();
        propiedadOcupada.setIdPropiedad(idPropiedadTest);
        propiedadOcupada.setEstado(PropiedadEstado.Ocupado);

        when(propiedadRepository.findById(idPropiedadTest)).thenReturn(Optional.of(propiedadOcupada));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {contratoService.crear(contratoAleatorio);});

        assertEquals("La propiedad ya está ocupada.", exception.getMessage());
    }
}