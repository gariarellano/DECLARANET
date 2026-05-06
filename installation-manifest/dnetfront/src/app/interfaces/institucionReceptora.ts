export interface InstitucionReceptora {
    id: string;
    nombre: string;
    ambitoPublico: string;
    nivelOrdenGobierno: {
      id: number;
      valor: string;
    };
  }
