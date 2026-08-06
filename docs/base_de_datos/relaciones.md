# Relaciones

## estaciones → conexiones

Una estación puede participar en varias conexiones, tanto como origen como destino (relación 1:N en ambos sentidos). Una conexión pertenece exactamente a dos estaciones.

```text
estaciones (1) ──< origen_id  ── conexiones
estaciones (1) ──< destino_id ── conexiones
```

`origen_id` y `destino_id` tienen `ON DELETE CASCADE`: si se elimina una estación, sus conexiones desaparecen con ella para no dejar referencias huérfanas.

No existe restricción que impida crear más de una conexión entre el mismo par de estaciones.
