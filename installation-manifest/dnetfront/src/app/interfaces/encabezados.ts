export interface Encabezado {
    anio: number;
    fechaActualizacion: string;
    fechaEncargo: string;
    fechaRegistro: string;
    numeroDeclaracion: number;
    tipoDeclaracion: string;
    tipoFormato: string;
    usuario: {
      curp: string;
      idUsuario: number;
      id_movimiento: number;
    };
    nivelJerarquico: {
      id: number;
      valor: string;
      valorUno: string;
      fk: string;
    };
    versionDeclaracion: string;
}
