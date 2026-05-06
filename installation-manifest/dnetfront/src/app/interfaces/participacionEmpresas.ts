export interface participaEmpresas{
            id: number,
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
            porcentajeParticipacion: string,
            tipoParticipacion: { //catalogo tipo participacion
                id: any,
                valor: any
            },
            recibeRemuneracion: string,
            montoMensual: {
                //@montoMoneda
                moneda: {
                    id: any,                    
                    valor: any
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
                },
            
            
        },
        sector: {
            id: any,
            valor: string
        }
}

