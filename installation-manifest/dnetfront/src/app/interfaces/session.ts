import { DatosPersonales } from './datosPersonales';

export interface Session {
  auth: string;
  datosPersonales: DatosPersonales;
}
