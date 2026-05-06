export interface Inversion {

    id: number,
    idPosicionVista: number,
    registroHistorico:boolean,
    verificar:boolean,
    tipoOperacion: string,
    tipoInversion: {
        id: number,
        clavePdn: string,
        valor: string,
       
    },
    subTipoInversion: {
        id: number,
        fk: number,
        valor: string
    },
    titular: {
        id: number,
        clavePdn: string,
        valor: string
    },
    terceros: [//al menos uno
        {
            id: string,
            idPosicionVista: number,
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
    numeroCuentaContrato: string,
    ubicacion: string,
    localizacionInversion: {
        pais: {
            id: string,
            valor: string
        },
        institucionRazonSocial: {
            //@personaMoral
            nombre: string,
            rfc: string
        }
    },
    saldo: {
        //@montoMoneda
        moneda: {
            id: number,
            valor: string
        },
        monto: number
    }

}