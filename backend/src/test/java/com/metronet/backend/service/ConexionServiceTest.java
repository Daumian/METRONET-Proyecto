package com.metronet.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.metronet.backend.model.Conexion;
import com.metronet.backend.model.Estacion;
import com.metronet.backend.repository.ConexionRepository;
import com.metronet.backend.repository.EstacionRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ConexionServiceTest {

    @Mock
    private ConexionRepository repository;

    @Mock
    private EstacionRepository estacionRepository;

    @InjectMocks
    private ConexionService service;

    @Test
    void crearRechazaConectarUnaEstacionConsigoMisma() {
        assertThatThrownBy(() -> service.crear(1L, 1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("400");
    }

    @Test
    void crearLanza404SiElOrigenNoExiste() {
        when(estacionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.crear(1L, 2L)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void crearGuardaLaConexionSiAmbasEstacionesExisten() {
        Estacion origen = new Estacion(1L, "A", 0, 0);
        Estacion destino = new Estacion(2L, "B", 10, 10);
        when(estacionRepository.findById(1L)).thenReturn(Optional.of(origen));
        when(estacionRepository.findById(2L)).thenReturn(Optional.of(destino));
        when(repository.save(any())).thenAnswer(invocacion -> invocacion.getArgument(0));

        Conexion creada = service.crear(1L, 2L);

        assertThat(creada.getOrigen()).isEqualTo(origen);
        assertThat(creada.getDestino()).isEqualTo(destino);
    }

    @Test
    void eliminarLanza404SiLaConexionNoExiste() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> service.eliminar(1L)).isInstanceOf(ResponseStatusException.class);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void eliminarBorraLaConexionExistente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }
}
