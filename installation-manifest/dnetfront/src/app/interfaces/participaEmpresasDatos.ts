export interface participaEmpresas{
    id: null,
    idPosicionVista: number,
    registroHistorico:boolean,
    verificar:boolean,
    tipoOperacion: string,
    participante: string, //DECLARANETE, PAREJA, DEPENDIENTE ECONOMICO
    nombreEmpresaSociedadAsociacion: {
        //@personaMoral
        nombre: string,
        rfc: any
    },
    porcentajeParticipacion: number,
    tipoParticipacion: { //catalogo tipo participacion
        id: any,
        valor: any
    },
    tipoParticipacionOtro: string,
    recibeRemuneracion: string,
    montoMensual: {
        //@montoMoneda
        moneda: {
            id: any,                    
            valor: any
        },
        monto: any
    },
    localizacion: {
        //@localizacion
        ubicacion: string,
        domicilioMexico: { //uno u otro
            entidadFederativa: {
                id: any,                        
                valor: string
            }
        },

        domicilioExtranjero: { //uno u otro
            pais: {
                id: any,
                valor: string
            }
        },
    
    sector: {
        id: any,
        valor: any
    },
    sectorOtro: string
    }
}

export interface ParticipaEmpresasDatos {
    ninguno: false,
    participaciones: Array<participaEmpresas>,
    aclaracionesObservaciones: string    
}