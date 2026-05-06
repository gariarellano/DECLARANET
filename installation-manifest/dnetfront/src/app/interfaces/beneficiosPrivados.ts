export interface BeneficiosPrivados{
                id: number,
                idPosicionVista: number,
                registroHistorico:boolean,
                verificar:boolean,
                tipoOperacion: number, //AGREGAR, MODIFICAR, SIN CAMBIO ó BAJA
                tipoBeneficio: { //catalogo de tipo de sorteo SORTEO CONCLUSIÓN DONACIÓN OTRO Especifique
                    id: number,
                    
                    valor: string
                },
                tipoBeneficioOtro: string,
                beneficiario: { //catalogo beneficiario (familiar)
                    id: number,
                    
                    valor: string
                },
                otorgante: {
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
                formaRecepcion: string, //Monetario, Especie
                especifiqueBeneficio: string,
                montoMensualAproximado: {
                    //@montoMoneda
                    moneda: {
                        id: number,
                        
                        valor: string
                    },
                    monto: number
                },
                sector: { //catalogo sector productivo.
                    id: number,
                    
                    valor: string
                }
                sectorOtro: string,

}

export interface BeneficiosPrivadosDatos{
    ninguno: boolean,
    beneficios: Array<BeneficiosPrivados>,
    aclaracionesObservaciones: string
}