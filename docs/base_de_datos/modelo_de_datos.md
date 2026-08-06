# Modelo de Datos

## Objetivo

Describir las entidades que forman el modelo de datos actual de METRONET.

---

# Entidades

## Estación

Representa un punto del mapa que el usuario crea desde el Frontend.

* `id`: identificador único.
* `nombre`: nombre visible de la estación.
* `x`, `y`: posición en el mapa.

No hay restricción de unicidad sobre la posición: dos estaciones pueden ocupar el mismo lugar (solaparse).

## Conexión

Representa un tramo directo entre dos estaciones (una línea de metro simple, sin ramificaciones ni intermedios).

* `id`: identificador único.
* `origen`: estación de inicio del tramo.
* `destino`: estación de fin del tramo.

Al eliminar una estación, todas sus conexiones se eliminan automáticamente.

---

# Estado Actual

Implementado: `estaciones` y `conexiones`.

Pendiente de diseño: trenes y su movimiento sobre las conexiones.

Ver la definición SQL en `database/002_creacion_tablas.sql` y el detalle de columnas en `docs/base_de_datos/tablas.md`.
