# METRONET

Simulador de una red de metro: Frontend en Phaser, Backend en Spring Boot y PostgreSQL como base de datos.

```text
Usuario → Frontend (Phaser) → Backend (Spring Boot) → PostgreSQL
```

## Ejecutar en desarrollo

```bash
# Backend (requiere PostgreSQL con la base metronet_proyecto, ver database/)
cd backend
./mvnw spring-boot:run

# Frontend
cd frontend
npm install
npm start
```

El backend expone la API en `http://localhost:8080/api` y el frontend corre en `http://localhost:5173`.

## API

| Método | Ruta                  | Descripción                     |
|--------|-----------------------|----------------------------------|
| GET    | `/api/estaciones`     | Listar estaciones                |
| POST   | `/api/estaciones`     | Crear una estación                |
| PUT    | `/api/estaciones/{id}`| Mover una estación                |
| DELETE | `/api/estaciones/{id}`| Eliminar una estación             |
| GET    | `/api/conexiones`     | Listar conexiones entre estaciones|
| POST   | `/api/conexiones`     | Crear una conexión                |
| DELETE | `/api/conexiones/{id}`| Eliminar una conexión             |

## Documentación

La documentación detallada del proyecto (arquitectura, base de datos, configuración inicial y trabajo en equipo) se encuentra en la carpeta [`docs`](docs).
