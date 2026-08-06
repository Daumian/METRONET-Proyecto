package com.metronet.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metronet.backend.model.Estacion;
import com.metronet.backend.service.EstacionService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EstacionController.class)
class EstacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EstacionService service;

    @Test
    void listarDevuelve200ConLasEstaciones() throws Exception {
        when(service.listar()).thenReturn(List.of(new Estacion(1L, "Central", 10, 20)));

        mockMvc.perform(get("/api/estaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Central"));
    }

    @Test
    void crearDevuelve201ConLaEstacionCreada() throws Exception {
        Estacion solicitada = new Estacion(null, "Central", 10, 20);
        Estacion guardada = new Estacion(1L, "Central", 10, 20);
        when(service.crear(any())).thenReturn(guardada);

        mockMvc.perform(post("/api/estaciones")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(solicitada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void eliminarDevuelve204() throws Exception {
        mockMvc.perform(delete("/api/estaciones/{id}", 1L)).andExpect(status().isNoContent());
    }

    @Test
    void moverDevuelveLaEstacionActualizada() throws Exception {
        Estacion cambios = new Estacion(null, "Central", 100, 200);
        Estacion actualizada = new Estacion(1L, "Central", 100, 200);
        when(service.mover(eq(1L), eq(100.0), eq(200.0))).thenReturn(actualizada);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .put("/api/estaciones/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(cambios)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.x").value(100))
                .andExpect(jsonPath("$.y").value(200));
    }
}
