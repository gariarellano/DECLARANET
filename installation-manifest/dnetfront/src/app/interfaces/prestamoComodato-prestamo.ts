export interface Prestamo {
    id: string,
    idPosicionVista: number,
    registroHistorico: boolean,
    verificar: boolean,
    tipoOperacion: string,
    tipoBien: string,
    relacionConTitular:string,
    inmueble: {
        tipoInmueble: {
            id: string,
            valor: string
        },
        tipoInmuebleOtro:string,
        //@domicilio
        domicilio: {
            ubicacion: string, //México, Extranjero
            domicilioMexico: { //uno u otro
                calle: string,
                codigoPostal: string,
                coloniaLocalidad: string,
                entidadFederativa: {
                    id: number,
                    valor: string,
                },
                municipioAlcaldia: {
                    fk: number,
                    id: number,
                    valor: string
                }
                numeroExterior: string,
                numeroInterior: string
            },

            domicilioExtranjero: { //uno u otro
                calle: string,
                ciudadLocalidad: string,
                codigoPostal: string,
                estadoProvincia: string,
                numeroExterior: string,
                numeroInterior: string,
                pais: {
                    id: number,
                    valor: string
                }
            }
        }
    },
    vehiculo: {
        tipoVehiculo: {
            id: string,
            valor: string
        },
        tipoVehiculoOtro:string,
        marca: string,
        modelo: string,
        anio: string,
        numeroSerieRegistro: string,
        lugarRegistro: {
            //@localizacion

            ubicacion: string,
            localizacionMexico: { //uno u otro
                entidadFederativa: {
                    id: string,
                    valor: string
                }
            },

            localizacionExtranjero: { //uno u otro
                pais: {
                    id: string,
                    valor: string
                }
            }

        }
    },
    duenoTitular: {
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
        },
        id: string,
        idPosicionVista: string,
        verificar: boolean
    }
}