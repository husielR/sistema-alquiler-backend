package com.datalyze.alquileres.api.controller;

import com.datalyze.alquileres.api.dto.ClienteDTO;
import com.datalyze.alquileres.api.dto.ContratoDTO;
import com.datalyze.alquileres.api.dto.PagoDTO;
import com.datalyze.alquileres.api.dto.PagoResumenDTO;
import com.datalyze.alquileres.api.dto.request.ContratoRequestDTO;
import com.datalyze.alquileres.api.dto.request.PagoCompletoRequestDTO;
import com.datalyze.alquileres.api.dto.request.PagoParcialRequestDTO;
import com.datalyze.alquileres.api.dto.request.PagoRequestDTO;
import com.datalyze.alquileres.api.enumeration.ContratoEstado;
import com.datalyze.alquileres.api.enumeration.PagoEstado;
import com.datalyze.alquileres.api.enumeration.PagoTipoPago;
import com.datalyze.alquileres.api.enumeration.PropiedadEstado;
import com.datalyze.alquileres.api.service.ClienteService;
import com.datalyze.alquileres.api.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
                .filter(estado -> estado != PagoEstado.Anulado)
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

    @PutMapping("/{id}")
    public ResponseEntity<PagoDTO> actualizarPago(@PathVariable Integer id, @RequestBody PagoRequestDTO request) {
        return ResponseEntity.ok(this.pagoService.actualizar(id, request));
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<PagoDTO> anularPago(@PathVariable Integer id) {
        return ResponseEntity.ok(this.pagoService.anularPago(id));
    }


    @PostMapping("{id}/pago-parcial")
    public ResponseEntity<PagoDTO> crearPago(@PathVariable Integer id, @RequestBody PagoParcialRequestDTO request) {
        return new ResponseEntity<>(this.pagoService.pagoParcial(id,request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/pagar")
    public ResponseEntity<PagoDTO> registrarPagoCompleto(@PathVariable Integer id, @RequestBody PagoCompletoRequestDTO request) {
        return ResponseEntity.ok(this.pagoService.registrarPagoCompleto(id, request));
    }

    @GetMapping("/paginados")
    public ResponseEntity<Page<PagoDTO>> getPagosPaginados(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String termino,
            @RequestParam(required = false) Integer idPropiedad,
            @RequestParam(required = false) List<PagoEstado> estados, // Spring convierte automático "?estados=Pendiente,Atrasado"
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin) {

        return ResponseEntity.ok(this.pagoService.buscarPagosPaginados(page, size, termino, idPropiedad, estados, fechaInicio, fechaFin));
    }

}
