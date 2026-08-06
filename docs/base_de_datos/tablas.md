# Tablas

## estaciones

| Columna | Tipo             | Restricciones     |
|---------|-------------------|--------------------|
| id      | SERIAL            | PRIMARY KEY        |
| nombre  | VARCHAR(100)      | NOT NULL           |
| pos_x   | DOUBLE PRECISION  | NOT NULL           |
| pos_y   | DOUBLE PRECISION  | NOT NULL           |

`pos_x` y `pos_y` almacenan la posición de la estación en el mapa del Frontend. No hay restricción de unicidad sobre la posición: las estaciones pueden solaparse.

Definida en `database/002_creacion_tablas.sql`.
