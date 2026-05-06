export interface Representacion{
                id: null,
                idPosicionVista: number,
                registroHistorico:boolean,
                verificar:boolean,
                tipoOperacion: string,
				participante:string, //Declarante, Pareja, Dependiente Economico
                tipoRepresentacion: string, //Representante, Representado
                fechaInicioRepresentacion: string,
                representanteRepresentado: {
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
                recibeRemuneracion: true,
                montoMensual: {
                    //@montoMoneda
                    moneda: {
                        id: number,
                        valor: string
                    },
                    monto: number
                },
                localizacion: {
                    //@localizacion
                    ubicacion: string,
                    localizacionMexico: { //uno u otro
                        entidadFederativa: {
                            id: number,
                            valor: string
                        }
                    },

                    localizacionExtranjero: { //uno u otro
                        pais: {
                            id: number,
                            valor: string
                        }
                    }
                },
                sector: {
                    id: number,
                    valor: string
                }
                sectorOtro: '',
}

export interface RepresenatcionDatos{
    ninguno: false,
    representaciones: Array<Representacion>,
    aclaracionesObservaciones: string
}