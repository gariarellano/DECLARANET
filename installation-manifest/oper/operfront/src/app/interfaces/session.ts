import { Usuario } from './usuario';

export interface Session {
  auth: string;
  datosPersonales: Usuario;
}
