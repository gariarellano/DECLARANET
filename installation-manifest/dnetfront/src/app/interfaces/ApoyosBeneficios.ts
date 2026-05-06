export interface ApoyosoBeneficios {
                id: number,
                idPosicionVista: number,
                registroHistorico:boolean,
                verificar:boolean,
                tipoOperacion: string,
                beneficiarioPrograma: {
                    id: any,
                   
                    valor: string
                },
                nombrePrograma: string,
                institucionOtorgante: string,
                nivelOrdenGobierno: string, // FEDERAL, ESTATAL, MUNICIPAL / ALCALDÍA
                tipoApoyo: {
                    id: any,
                   
                    valor: string
                },
                tipoApoyoOtro: string,
                formaRecepcion: string, //Monetario, Especie
                montoApoyoMensual: {
                    //@montoMoneda
                    moneda: {
                        id: any,
                       
                        valor: string
                    },
                    monto: number
                },
                especifiqueApoyo: string 
}

export interface ApoyosoBeneficiosDatos{
    ninguno: false;
    apoyos: Array<ApoyosoBeneficios>;
    aclaracionesObservaciones: string
}