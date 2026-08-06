# Tablas

## estaciones

| Columna | Tipo             | Restricciones     |
|---------|-------------------|--------------------|
| id      | SERIAL            | PRIMARY KEY        |
| nombre  | VARCHAR(100)      | NOT NULL           |
| pos_x   | DOUBLE PRECISION  | NOT NULL           |
| pos_y   | DOUBLE PRECISION  | NOT NULL           |

`pos_x` y `pos_y` almacenan la posición de la estación en el mapa del Frontend. No hay restricción de unicidad sobre la posición: las estaciones pueden solaparse.

## conexiones

| Columna    | Tipo    | Restricciones                                       |
|------------|---------|------------------------------------------------------|
| id         | SERIAL  | PRIMARY KEY                                           |
| origen_id  | INTEGER | NOT NULL, REFERENCES estaciones(id) ON DELETE CASCADE |
| destino_id | INTEGER | NOT NULL, REFERENCES estaciones(id) ON DELETE CASCADE |

Representa un tramo directo entre dos estaciones. Si se elimina una estación, sus conexiones se eliminan en cascada.

Definidas en `database/002_creacion_tablas.sql`.
