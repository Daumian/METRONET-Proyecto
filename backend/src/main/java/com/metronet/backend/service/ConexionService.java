package com.metronet.backend.service;

import com.metronet.backend.model.Conexion;
import com.metronet.backend.model.Estacion;
import com.metronet.backend.repository.ConexionRepository;
import com.metronet.backend.repository.EstacionRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ConexionService {

    private final ConexionRepository repository;
    private final EstacionRepository estacionRepository;

    public ConexionService(ConexionRepository repository, EstacionRepository estacionRepository) {
        this.repository = repository;
        this.estacionRepository = estacionRepository;
    }

    public List<Conexion> listar() {
        return repository.findAll();
    }

    public Conexion crear(Long origenId, Long destinoId) {
        if (origenId.equals(destinoId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Una estación no puede conectarse consigo misma");
        }
        Estacion origen = obtenerEstacionOFallar(origenId);
        Estacion destino = obtenerEstacionOFallar(destinoId);
        return repository.save(new Conexion(null, origen, destino));
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe una conexión con id " + id);
        }
        repository.deleteById(id);
    }

    private Estacion obtenerEstacionOFallar(Long id) {
        return estacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe una estación con id " + id));
    }
}
