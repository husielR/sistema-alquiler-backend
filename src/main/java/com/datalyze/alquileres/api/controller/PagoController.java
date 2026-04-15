package com.datalyze.alquileres.api.controller;

import com.datalyze.alquileres.api.dto.ClienteDTO;
import com.datalyze.alquileres.api.dto.ContratoDTO;
import com.datalyze.alquileres.api.dto.PagoDTO;
import com.datalyze.alquileres.api.dto.PagoResumenDTO;
import com.datalyze.alquileres.api.dto.request.ContratoRequestDTO;
import com.datalyze.alquileres.api.dto.request.PagoRequestDTO;
import com.datalyze.alquileres.api.enumeration.PagoEstado;
import com.datalyze.alquileres.api.enumeration.PagoTipoPago;
import com.datalyze.alquileres.api.enumeration.PropiedadEstado;
import com.datalyze.alquileres.api.service.ClienteService;
import com.datalyze.alquileres.api.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/pago")
public class PagoController {
    private PagoService pagoService;

    @Autowired
    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public ResponseEntity<List<PagoDTO>> getPagoEntity() {
        return ResponseEntity.ok(this.pagoService.obtenerTodos());
    }

    @GetMapping("/resumen")
    public ResponseEntity<List<PagoResumenDTO>> getPagoResumenEntity() {
        return ResponseEntity.ok(this.pagoService.obtenerTodosResumen());
    }

    @GetMapping("/estados")
    public ResponseEntity<List<String>> obtenerEstados() {
        List<String> estados = Arrays.stream(PagoEstado.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(estados);
    }
    @GetMapping("/tipo-pago")
    public ResponseEntity<List<String>> obtenerTipoPago() {
        List<String> estados = Arrays.stream(PagoTipoPago.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(estados);
    }

    @PostMapping
    public ResponseEntity<PagoDTO> crearPago(@RequestBody PagoRequestDTO request) {
        return new ResponseEntity<>(this.pagoService.crear(request), HttpStatus.CREATED);
    }

}
