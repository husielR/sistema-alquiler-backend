package com.datalyze.alquileres.api.controller;

import com.datalyze.alquileres.api.dto.ClienteDTO;
import com.datalyze.alquileres.api.dto.ClienteResumenDTO;
import com.datalyze.alquileres.api.dto.request.ClienteRequestDTO;
import com.datalyze.alquileres.api.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
    private ClienteService  clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> getClienteEntity() {
        return ResponseEntity.ok(this.clienteService.getClienteEntity());
    }

    // GET: /cliente/5
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(this.clienteService.obtenerPorId(id));
    }

    // POST: /cliente (Enviando JSON en el Body)
    @PostMapping
    public ResponseEntity<ClienteDTO> crearCliente(@RequestBody ClienteRequestDTO request) {
        // Usamos HttpStatus.CREATED (201) que es la convención REST para inserciones
        return new ResponseEntity<>(this.clienteService.crearCliente(request), HttpStatus.CREATED);
    }

    // PUT: /cliente/5 (Enviando JSON en el Body)
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizarCliente(@PathVariable Integer id, @RequestBody ClienteRequestDTO request) {
        return ResponseEntity.ok(this.clienteService.actualizarCliente(id, request));
    }

    // DELETE: /cliente/5
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Integer id) {
        this.clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build(); // Devuelve un 204 No Content (Éxito sin devolver datos)
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ClienteResumenDTO>> getClienteActivoEntity() {
        return ResponseEntity.ok(this.clienteService.getClienteNotContract());
    }

    @GetMapping("/resumen")
    public ResponseEntity<List<ClienteResumenDTO>> getClienteResumenEntity() {
        return ResponseEntity.ok(this.clienteService.getClienteResumenEntity());
    }

}
