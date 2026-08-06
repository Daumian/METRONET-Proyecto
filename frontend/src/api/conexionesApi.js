import { solicitar } from './httpCliente.js';

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
