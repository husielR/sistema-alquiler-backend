package com.datalyze.alquileres.api.controller;

import com.datalyze.alquileres.api.dto.ClienteDTO;
import com.datalyze.alquileres.api.dto.ContratoDTO;
import com.datalyze.alquileres.api.dto.PropiedadDTO;
import com.datalyze.alquileres.api.dto.request.ContratoRequestDTO;
import com.datalyze.alquileres.api.dto.request.PropiedadRequestDTO;
import com.datalyze.alquileres.api.enumeration.ContratoEstado;
import com.datalyze.alquileres.api.enumeration.PropiedadEstado;
import com.datalyze.alquileres.api.service.ContratoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/contrato")
public class ContratoController {
    private ContratoService contratoService;

    @Autowired
    public ContratoController(ContratoService contratoService) {
        this.contratoService = contratoService;
    }

    @GetMapping
    public ResponseEntity<List<ContratoDTO>> getContratoEntity() {
        return ResponseEntity.ok(this.contratoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratoDTO> getContratoId(@PathVariable Integer id) {
        return ResponseEntity.ok(this.contratoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ContratoDTO> crearContrato(@RequestBody ContratoRequestDTO request) {
        return new ResponseEntity<>(this.contratoService.crear(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContratoDTO> actualizarContrato(@PathVariable Integer id, @RequestBody ContratoRequestDTO request) {
        return ResponseEntity.ok(this.contratoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> EliminarContrato(@PathVariable Integer id) {
        this.contratoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estados")
    public ResponseEntity<List<String>> obtenerEstados() {
        List<String> estados = Arrays.stream(ContratoEstado.values())
                .filter(estado -> estado != ContratoEstado.Anulado)
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(estados);
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<ContratoDTO> anularContrato(@PathVariable Integer id) {
        return ResponseEntity.ok(this.contratoService.anularContrato(id));
    }

    @GetMapping("/idCliente/{id}")
    public ResponseEntity<List<ContratoDTO>> getContratoClienteEntity(@PathVariable Integer id) {
        return ResponseEntity.ok(this.contratoService.obtenerContratoDisponibleCliente(id));
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<ContratoDTO> finalizarContrato(@PathVariable Integer id) {
        return ResponseEntity.ok(this.contratoService.finalizarContrato(id));
    }

    @PutMapping("/{id}/incumplir")
    public ResponseEntity<ContratoDTO> incumplirContrato(@PathVariable Integer id) {
        return ResponseEntity.ok(this.contratoService.incumplirContrato(id));
    }

    @GetMapping("/paginados")
    public ResponseEntity<Page<ContratoDTO>> getContratosPaginados(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String termino,
            @RequestParam(required = false) Integer idPropiedad,
            @RequestParam(required = false) ContratoEstado estado) {

        return ResponseEntity.ok(this.contratoService.buscarPaginados(page, size, termino, idPropiedad, estado));
    }

}
