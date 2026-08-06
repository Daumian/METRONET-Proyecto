import Phaser from 'phaser';
import MapaScene from '../scenes/MapaScene.js';

const config = {
  type: Phaser.AUTO,
  parent: 'juego',
  width: 960,
  height: 600,
  backgroundColor: '#0b1420',
  disableContextMenu: true,
  scene: [MapaScene],
};

new Phaser.Game(config);
