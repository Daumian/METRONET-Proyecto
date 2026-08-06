package com.metronet.backend.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metronet.backend.model.Conexion;
import com.metronet.backend.model.Estacion;
import com.metronet.backend.service.ConexionService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ConexionController.class)
class ConexionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ConexionService service;

    @Test
    void listarDevuelve200ConLasConexiones() throws Exception {
        Estacion origen = new Estacion(1L, "A", 0, 0);
        Estacion destino = new Estacion(2L, "B", 10, 10);
        when(service.listar()).thenReturn(List.of(new Conexion(1L, origen, destino)));

        mockMvc.perform(get("/api/conexiones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].origen.id").value(1))
                .andExpect(jsonPath("$[0].destino.id").value(2));
    }

    @Test
    void crearDevuelve201ConLaConexionCreada() throws Exception {
        Estacion origen = new Estacion(1L, "A", 0, 0);
        Estacion destino = new Estacion(2L, "B", 10, 10);
        when(service.crear(eq(1L), eq(2L))).thenReturn(new Conexion(5L, origen, destino));

        mockMvc.perform(post("/api/conexiones")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new ConexionRequest(1L, 2L))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void eliminarDevuelve204() throws Exception {
        mockMvc.perform(delete("/api/conexiones/{id}", 1L)).andExpect(status().isNoContent());
    }
}
