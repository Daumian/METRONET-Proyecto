package com.metronet.backend.service;

import com.metronet.backend.model.Estacion;
import com.metronet.backend.repository.EstacionRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EstacionService {

    private final EstacionRepository repository;

    public EstacionService(EstacionRepository repository) {
        this.repository = repository;
    }

    public List<Estacion> listar() {
        return repository.findAll();
    }

    public Estacion crear(Estacion estacion) {
        estacion.setId(null);
        return repository.save(estacion);
    }

    public Estacion mover(Long id, double x, double y) {
        Estacion estacion = obtenerOFallar(id);
        estacion.setX(x);
        estacion.setY(y);
        return repository.save(estacion);
    }

    public void eliminar(Long id) {
        repository.delete(obtenerOFallar(id));
    }

    private Estacion obtenerOFallar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe una estación con id " + id));
    }
}
