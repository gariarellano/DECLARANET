export interface participaDecisiones {


    id: number,
    idPosicionVista: number,
    registroHistorico:boolean,
    verificar:boolean,
    tipoOperacion: string,
    participante: string, //DECLARANETE, PAREJA, DEPENDIENTE ECONOMICO
    tipoInstitucion: { //catalogo de tipo institucion
        id: any,

        valor: string
    },
    tipoInstitucionOtro: string,
    institucion: {
        //personaMoral
        nombre: string,
        rfc: any
    },
    puestoRol: string,
    fechaInicioParticipacion: string,
    recibeRemuneracion: true,
    montoMensual: {
        //@montoMoneda
        moneda: {
            id: any,

            valor: string
        },
        monto: number
    },
    localizacion: {
        //@localizacion
        ubicacion: string,
        localizacionMexico: { //uno u otro
            entidadFederativa: {
                id: any,

                valor: string
            }
        },

        localizacionExtranjero: { //uno u otro
            pais: {
                id: any,
                valor: string
            }
        }
    }


}

export interface participaDecisionesDatos{
    ninguno: boolean;
    participaciones: Array<participaDecisiones>;
    aclaracionesObservaciones: string;
}
