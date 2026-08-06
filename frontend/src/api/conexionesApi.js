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

export function obtenerConexiones() {
  return solicitar('/conexiones');
}

export function crearConexionEnBackend(conexion) {
  return solicitar('/conexiones', {
    method: 'POST',
    body: JSON.stringify(conexion),
  });
}

export function eliminarConexionEnBackend(id) {
  return solicitar(`/conexiones/${id}`, { method: 'DELETE' });
}
