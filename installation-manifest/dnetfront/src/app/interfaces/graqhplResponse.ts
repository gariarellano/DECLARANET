export interface GuardaDeclaracionResponse {
  estado: string;
  modulos: {
    modulo: string;
    errores: {
      listErrorMensajes: {
        errorId: string;
        mensaje: string;
        mensajeAlterno: string;
      }
      nombreCampo: string;
      propiedadValor: string;
    }

  };
}
