import Phaser from 'phaser';
import {
  obtenerEstaciones,
  crearEstacionEnBackend,
  actualizarEstacionEnBackend,
  eliminarEstacionEnBackend,
} from '../src/api/estacionesApi.js';
import {
  obtenerConexiones,
  crearConexionEnBackend,
} from '../src/api/conexionesApi.js';

const RADIO_ESTACION = 18;
const COLOR_ESTACION = 0xff5533;
const COLOR_FONDO = 0x102030;
const COLOR_BORDE = 0xffffff;
const COLOR_BORDE_SELECCIONADA = 0x33ff77;
const COLOR_LINEA = 0x33ff77;

export default class MapaScene extends Phaser.Scene {
  constructor() {
    super('MapaScene');
    this.contadorEstaciones = 0;
    this.conexiones = [];
    this.estacionSeleccionada = null;
  }

  create() {
    this.add
      .rectangle(0, 0, this.scale.width, this.scale.height, COLOR_FONDO)
      .setOrigin(0, 0)
      .setDepth(-1)
      .setInteractive()
      .on('pointerdown', (pointer) => this.crearEstacion(pointer.x, pointer.y));

    this.add
      .text(
        10,
        10,
        'Click en vacío: crear estación · Click en dos estaciones: conectarlas · Arrastrar: mover · Click derecho: eliminar',
        { fontSize: '11px', color: '#8fa3b8' },
      )
      .setDepth(10);

    this.cargarEstaciones();
  }

  async cargarEstaciones() {
    const circulosPorId = new Map();
    try {
      const estaciones = await obtenerEstaciones();
      estaciones.forEach((estacion) => {
        const circulo = this.dibujarEstacion(estacion);
        circulosPorId.set(estacion.id, circulo);
      });
      this.contadorEstaciones = estaciones.length;
    } catch (error) {
      console.warn('No se pudieron cargar las estaciones desde el backend.', error);
      return;
    }
    await this.cargarConexiones(circulosPorId);
  }

  async cargarConexiones(circulosPorId) {
    try {
      const conexiones = await obtenerConexiones();
      conexiones.forEach((conexion) => {
        const origen = circulosPorId.get(conexion.origen.id);
        const destino = circulosPorId.get(conexion.destino.id);
        if (origen && destino) {
          this.dibujarConexion(origen, destino, conexion.id);
        }
      });
    } catch (error) {
      console.warn('No se pudieron cargar las conexiones desde el backend.', error);
    }
  }

  crearEstacion(x, y) {
    this.contadorEstaciones += 1;
    const nombre = `Estación ${this.contadorEstaciones}`;
    const circulo = this.dibujarEstacion({ nombre, x, y });

    crearEstacionEnBackend({ nombre, x, y })
      .then((creada) => circulo.setData('id', creada.id))
      .catch((error) => console.warn('No se pudo guardar la estación en el backend.', error));
  }

  dibujarEstacion(datos) {
    // Sin detección de colisiones: las estaciones pueden solaparse libremente.
    const circulo = this.add
      .circle(datos.x, datos.y, RADIO_ESTACION, COLOR_ESTACION)
      .setStrokeStyle(2, COLOR_BORDE)
      .setDepth(1)
      .setInteractive({ draggable: true, useHandCursor: true });

    circulo.setData('id', datos.id ?? null);
    circulo.setData('nombre', datos.nombre);
    circulo.setData('arrastrada', false);

    const etiqueta = this.add
      .text(datos.x, datos.y - RADIO_ESTACION - 12, datos.nombre, {
        fontSize: '12px',
        color: '#ffffff',
      })
      .setOrigin(0.5)
      .setDepth(2);

    circulo.on('pointerdown', (pointer) => {
      circulo.setData('arrastrada', false);
      if (pointer.rightButtonDown()) {
        this.eliminarEstacion(circulo, etiqueta);
      }
    });

    circulo.on('drag', (_pointer, dragX, dragY) => {
      circulo.setData('arrastrada', true);
      circulo.setPosition(dragX, dragY);
      etiqueta.setPosition(dragX, dragY - RADIO_ESTACION - 12);
      this.actualizarConexionesDe(circulo);
    });

    circulo.on('dragend', () => this.guardarPosicion(circulo));

    circulo.on('pointerup', () => {
      if (!circulo.getData('arrastrada')) {
        this.alternarSeleccion(circulo);
      }
    });

    return circulo;
  }

  alternarSeleccion(circulo) {
    if (this.estacionSeleccionada === circulo) {
      this.deseleccionar();
      return;
    }
    if (this.estacionSeleccionada) {
      this.crearConexion(this.estacionSeleccionada, circulo);
      this.deseleccionar();
      return;
    }
    this.estacionSeleccionada = circulo;
    circulo.setStrokeStyle(3, COLOR_BORDE_SELECCIONADA);
  }

  deseleccionar() {
    if (this.estacionSeleccionada) {
      this.estacionSeleccionada.setStrokeStyle(2, COLOR_BORDE);
    }
    this.estacionSeleccionada = null;
  }

  crearConexion(origen, destino) {
    const conexion = this.dibujarConexion(origen, destino);
    const origenId = origen.getData('id');
    const destinoId = destino.getData('id');
    if (origenId == null || destinoId == null) {
      return;
    }
    crearConexionEnBackend({ origenId, destinoId })
      .then((creada) => {
        conexion.id = creada.id;
      })
      .catch((error) => console.warn('No se pudo guardar la conexión en el backend.', error));
  }

  dibujarConexion(origen, destino, id = null) {
    const linea = this.add
      .line(0, 0, origen.x, origen.y, destino.x, destino.y, COLOR_LINEA)
      .setOrigin(0, 0)
      .setLineWidth(3)
      .setDepth(0);

    const conexion = { id, origen, destino, linea };
    this.conexiones.push(conexion);
    return conexion;
  }

  actualizarConexionesDe(circulo) {
    this.conexiones
      .filter((conexion) => conexion.origen === circulo || conexion.destino === circulo)
      .forEach((conexion) =>
        conexion.linea.setTo(conexion.origen.x, conexion.origen.y, conexion.destino.x, conexion.destino.y),
      );
  }

  guardarPosicion(circulo) {
    const id = circulo.getData('id');
    if (id == null) {
      return;
    }
    actualizarEstacionEnBackend(id, { x: circulo.x, y: circulo.y }).catch((error) =>
      console.warn('No se pudo actualizar la posición en el backend.', error),
    );
  }

  eliminarEstacion(circulo, etiqueta) {
    if (this.estacionSeleccionada === circulo) {
      this.estacionSeleccionada = null;
    }

    const conexionesRelacionadas = this.conexiones.filter(
      (conexion) => conexion.origen === circulo || conexion.destino === circulo,
    );
    conexionesRelacionadas.forEach((conexion) => conexion.linea.destroy());
    this.conexiones = this.conexiones.filter((conexion) => !conexionesRelacionadas.includes(conexion));

    const id = circulo.getData('id');
    circulo.destroy();
    etiqueta.destroy();

    if (id == null) {
      return;
    }
    eliminarEstacionEnBackend(id).catch((error) =>
      console.warn('No se pudo eliminar la estación en el backend.', error),
    );
  }
}
