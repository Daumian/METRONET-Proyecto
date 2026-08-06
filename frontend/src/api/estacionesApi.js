import { solicitar } from './httpCliente.js';

export function obtenerEstaciones() {
  return solicitar('/estaciones');
}

export function crearEstacionEnBackend(estacion) {
  return solicitar('/estaciones', {
    method: 'POST',
    body: JSON.stringify(estacion),
  });
}

export function actualizarEstacionEnBackend(id, cambios) {
  return solicitar(`/estaciones/${id}`, {
    method: 'PUT',
    body: JSON.stringify(cambios),
  });
}

export function eliminarEstacionEnBackend(id) {
  return solicitar(`/estaciones/${id}`, { method: 'DELETE' });
}
