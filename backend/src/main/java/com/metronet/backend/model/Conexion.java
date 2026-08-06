package com.metronet.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "conexiones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Conexion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "origen_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Estacion origen;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "destino_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Estacion destino;
}
