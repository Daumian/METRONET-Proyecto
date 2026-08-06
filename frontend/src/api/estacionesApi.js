const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api';

async function solicitar(path, opciones) {
  const respuesta = await fetch(`${API_BASE_URL}${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...opciones,
  });

  if (!respuesta.ok) {
    throw new Error(`Error ${respuesta.status} al llamar a ${path}`);
  }

  return respuesta.status === 204 ? null : respuesta.json();
}

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
