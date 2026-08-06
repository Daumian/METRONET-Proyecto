import Phaser from 'phaser';
import {
  obtenerEstaciones,
  crearEstacionEnBackend,
  actualizarEstacionEnBackend,
  eliminarEstacionEnBackend,
} from '../src/api/estacionesApi.js';

const RADIO_ESTACION = 18;
const COLOR_ESTACION = 0xff5533;
const COLOR_FONDO = 0x102030;

export default class MapaScene extends Phaser.Scene {
  constructor() {
    super('MapaScene');
    this.contadorEstaciones = 0;
  }

  create() {
    this.add
      .rectangle(0, 0, this.scale.width, this.scale.height, COLOR_FONDO)
      .setOrigin(0, 0)
      .setInteractive()
      .on('pointerdown', (pointer) => this.crearEstacion(pointer.x, pointer.y));

    this.cargarEstaciones();
  }

  async cargarEstaciones() {
    try {
      const estaciones = await obtenerEstaciones();
      estaciones.forEach((estacion) => this.dibujarEstacion(estacion));
      this.contadorEstaciones = estaciones.length;
    } catch (error) {
      console.warn('No se pudieron cargar las estaciones desde el backend.', error);
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
      .setStrokeStyle(2, 0xffffff)
      .setInteractive({ draggable: true, useHandCursor: true });

    circulo.setData('id', datos.id ?? null);
    circulo.setData('nombre', datos.nombre);

    const etiqueta = this.add
      .text(datos.x, datos.y - RADIO_ESTACION - 12, datos.nombre, {
        fontSize: '12px',
        color: '#ffffff',
      })
      .setOrigin(0.5);

    circulo.on('drag', (_pointer, dragX, dragY) => {
      circulo.setPosition(dragX, dragY);
      etiqueta.setPosition(dragX, dragY - RADIO_ESTACION - 12);
    });

    circulo.on('dragend', () => this.guardarPosicion(circulo));

    circulo.on('pointerdown', (pointer, _x, _y, event) => {
      if (pointer.rightButtonDown()) {
        event.stopPropagation();
        this.eliminarEstacion(circulo, etiqueta);
      }
    });

    return circulo;
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
