export interface ClientesPrincipales {
                id: number,
                idPosicionVista: number,
                registroHistorico:boolean,
                verificar:boolean,
                tipoOperacion: string,
                
                participante: string, //DECLARANETE, PAREJA, DEPENDIENTE ECONOMICO
                nombreEmpresaServicio: string,
                rfcEmpresa: string,
                clientePrincipal: {
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
                sector: {
                    id: number,
                    
                    valor: string
                },
                sectorOtro: string,
                montoAproximadoGanancia: {
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
                }   

}

export interface ClientesPrincipalesDatos {
    realizaActividadLucrativa: boolean,
    clientes: Array<ClientesPrincipales>
    aclaracionesObservaciones: string    
}