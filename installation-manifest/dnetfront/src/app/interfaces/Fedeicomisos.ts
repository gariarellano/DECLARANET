export interface Fideicomisos{
                id: number,
                idPosicionVista: number,
                registroHistorico:boolean,
                verificar:boolean,
                tipoOperacion: string,
                participante: string, //DECLARANETE, PAREJA, DEPENDIENTE ECONOMICO
                tipoFideicomiso: string, //PUBLICO, PROVADO, MIXTO
                tipoParticipacion: string, //FIDEICOMITENTE, FIDUCIARIO, FIDEICOMISARIO, COMITE TECNICO
                rfcFideicomiso: string,
                fideicomitente: {
                    tipoPersona: string, //Fisica, Moral
                    personaFisica: {
                        //@persona
                        nombre: string
                        primerApellido: string
                        segundoApellido: string
                        rfc: string
                    },
                    personaMoral: {
                        //@personaMoral
                        nombre: string,
                        rfc: string
                    }
                },
                fiduciario: {
                    //@personaMoral
                    nombre: string,
                    rfc: string
                },
                fideicomisario: {
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
                localizacion: string //MÉXICO, EXTRANJERO


}

export interface FideicomisosDatos{
    ninguno: false,
    fideicomisos: Array<Fideicomisos>,
    aclaracionesObservaciones: string
}
