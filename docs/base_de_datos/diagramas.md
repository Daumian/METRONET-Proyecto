# Diagramas

## Diagrama Entidad-Relación (actual)

```text
┌───────────────────┐          ┌───────────────────┐
│     estaciones     │          │     conexiones      │
├───────────────────┤          ├───────────────────┤
│ id (PK)            │◄────┐    │ id (PK)             │
│ nombre              │     │    │ origen_id (FK)      │──┐
│ pos_x                │     └───│ destino_id (FK)     │──┘
│ pos_y                │          └───────────────────┘
└───────────────────┘
```

`origen_id` y `destino_id` referencian ambos a `estaciones.id`, con borrado en cascada.
