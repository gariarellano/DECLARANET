export interface Producto {
  id: number;
  nombre: string;
  valorSiEsSecretaria: string;
  obligados: string;
  cumplieron: string;
  extemporaneos: string;
  pendientes: string;
  porcentCumplieron: string;
}

export interface Lista {
  id: number;
  nombre: string;
}

export interface Detalle {
  ente: string;
  servidorPublico: string;
  puesto: string;
  estatus: string;
}
