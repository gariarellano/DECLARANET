import { Encabezado } from './encabezados';
import { InstitucionReceptora } from './institucionReceptora';
import { Declaracion } from './declaracion';
import { Aviso } from './aviso';
export interface Recepcion {
  encabezado: Encabezado;
  firmada: boolean;
  institucionReceptora: InstitucionReceptora;
  declaracion: Declaracion;
  numeroDeclaracionPrecarga: string;
}


export interface RecepcionNota {
  encabezado: Encabezado;
  firmada: boolean;
  institucionReceptora: InstitucionReceptora;
  declaracion: Declaracion;
}


export interface RecepcionAviso {
  encabezado: Encabezado;
  firmada: boolean;
  institucionReceptora: InstitucionReceptora;
  declaracion: Aviso;
}
