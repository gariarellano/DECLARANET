export interface Usuario {
    pwdEnc: string;
    nombre: string;
    primerApellido: string;
    segundoApellido: string;
    curp: string;
    rfc: string;
    homoclave: string;
    numCelular: string;
    email: string;
    emailAlt: string;
    // confirmCel: string;

}


export interface UsuarioEdit {
  curp: string;
  email: string;
  emailAlt: string;
  idUsuario: number;
  nombre: string;
  primerApellido: string;
  segundoApellido: string;
}
