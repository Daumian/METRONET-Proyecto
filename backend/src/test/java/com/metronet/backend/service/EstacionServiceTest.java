package com.metronet.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.metronet.backend.model.Estacion;
import com.metronet.backend.repository.EstacionRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class EstacionServiceTest {

    @Mock
    private EstacionRepository repository;

    @InjectMocks
    private EstacionService service;

    @Test
    void listarDevuelveTodasLasEstaciones() {
        Estacion estacion = new Estacion(1L, "Central", 10, 20);
        when(repository.findAll()).thenReturn(List.of(estacion));

        assertThat(service.listar()).containsExactly(estacion);
    }

    @Test
    void crearIgnoraElIdRecibidoYGuarda() {
        Estacion solicitada = new Estacion(999L, "Central", 10, 20);
        when(repository.save(any())).thenAnswer(invocacion -> invocacion.getArgument(0));

        Estacion creada = service.crear(solicitada);

        assertThat(creada.getId()).isNull();
        assertThat(creada.getNombre()).isEqualTo("Central");
    }

    @Test
    void moverActualizaLaPosicionDeUnaEstacionExistente() {
        Estacion estacion = new Estacion(1L, "Central", 10, 20);
        when(repository.findById(1L)).thenReturn(Optional.of(estacion));
        when(repository.save(any())).thenAnswer(invocacion -> invocacion.getArgument(0));

        Estacion movida = service.mover(1L, 100, 200);

        assertThat(movida.getX()).isEqualTo(100);
        assertThat(movida.getY()).isEqualTo(200);
    }

    @Test
    void moverLanza404SiLaEstacionNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.mover(1L, 100, 200))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
    }

    @Test
    void eliminarLanza404SiLaEstacionNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.eliminar(1L)).isInstanceOf(ResponseStatusException.class);
        verify(repository, never()).delete(any());
    }

    @Test
    void eliminarBorraLaEstacionExistente() {
        Estacion estacion = new Estacion(1L, "Central", 10, 20);
        when(repository.findById(1L)).thenReturn(Optional.of(estacion));

        service.eliminar(1L);

        verify(repository).delete(estacion);
    }
}
