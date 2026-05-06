export interface Adeudos {
    id: number,
    idPosicionVista: number,
    registroHistorico: boolean,
    verificar:boolean,
    tipoOperacion: string,
    titular: {
        id: number,
        valorUno: string,
        valor: string
    },
    tipoAdeudo: {
        id: number,
       
        valor: string
    },
    tipoAdeudoOtro:string,
    numeroCuentaContrato: string,
    fechaAdquisicion: string,
    montoOriginal: {
        //@montoMoneda
        moneda: {
            id: number,
            
            valor: string
        },
        monto: string
    },
    saldoInsoluto: number,
    terceros: [//al menos uno
        {
            id: number,
            idPosicionVista: string,
            tipoPersona: string, //Fisica, Moral
            personaFisica: {
                //@persona
                nombre: string,
                primerApellido: string,
                segundoApellido: string,
                rfc: string
            },
            personaMoral: {
                //@personaMoral
                nombre: string,
                rfc: string
            }
        }
    ],
    otorganteCredito: {
        id: string,
        tipoPersona: string, //Fisica, Moral
        personaFisica: {
            //@persona
            nombre: string,
            primerApellido: string,
            segundoApellido: string,
            rfc: string
        },
        personaMoral: {
            //@personaMoral
            nombre: string,
            rfc: string
        }
    },
    paisAdeudo: {
        id: number,
       
        valor: string
    }
}