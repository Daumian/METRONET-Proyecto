package com.metronet.backend.controller;

import com.metronet.backend.model.Conexion;
import com.metronet.backend.service.ConexionService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conexiones")
public class ConexionController {

    private final ConexionService service;

    public ConexionController(ConexionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Conexion> listar() {
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<Conexion> crear(@RequestBody ConexionRequest solicitud) {
        Conexion creada = service.crear(solicitud.origenId(), solicitud.destinoId());
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
