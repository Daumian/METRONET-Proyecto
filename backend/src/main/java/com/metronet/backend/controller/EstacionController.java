package com.metronet.backend.controller;

import com.metronet.backend.model.Estacion;
import com.metronet.backend.service.EstacionService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estaciones")
public class EstacionController {

    private final EstacionService service;

    public EstacionController(EstacionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Estacion> listar() {
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<Estacion> crear(@RequestBody Estacion estacion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(estacion));
    }

    @PutMapping("/{id}")
    public Estacion mover(@PathVariable Long id, @RequestBody Estacion estacion) {
        return service.mover(id, estacion.getX(), estacion.getY());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
