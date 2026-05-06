export interface EmpleoCargo {
    "tipoOperacion": string,
    "id": number,
    idPosicionVista: number,
    registroHistorico:boolean,
    verificar:boolean,
    "ente": {
        "id": number,
        "nombre": string,
        "nivelOrdenGobierno": {
            "nivelOrdenGobierno": string,
            "entidadFederativa": {
              "id": number,
              "valor": string,
              "municipioAlcaldia": {
                "id":number,
                "valor": string
              }
            }
        },
        "ambitoPublico": { //ANTES PODER
            "id": number,
            "valor": string
        }
    },
    "areaAdscripcion": string,
    "empleoCargoComision": string,
    "nivelJerarquico": { //Este dato no existe en el formato pero será el que defina el tipo de formato a desplegar completo - simplificado
        "id": number,
        "valor": string,
        "valorUno": string,
        "fk": number,
    },
    "nivelEmpleoCargoComision": string,
    "contratadoPorHonorarios": boolean,
    "remuneracionNeta": {
        //@montoMoneda
        "moneda": {
            "id": number,
            "valor": string
        },
        "monto": number
    },
    "tipoRemuneracion": string, //Mensual, Anual año anterior, Anual año actual
    "funcionPrincipal": string,
    "fechaTomaPosesion": string,
    "telefonoOficina": {
        //@telefono
        "numero": string,
        "extension": string
    },
    "domicilio": {
        //@domicilio
        "ubicacion": string, //México, Extranjero
        "domicilioMexico": { //uno u otro
            "calle": string,
            "codigoPostal": string,
            "coloniaLocalidad": string,
            "entidadFederativa": {
                "id": number,
                "valor": string
            },

            "municipioAlcaldia": {
                "id": number,
                "valor": string
            },
            "numeroExterior": string,
            "numeroInterior": string
        },

        "domicilioExtranjero": { //uno u otro
            "calle": string,
            "ciudadLocalidad": string,
            "codigoPostal": string,
            "estadoProvincia": string,
            "numeroExterior": string,
            "numeroInterior": string,
            "pais": {
                "id": number,
                "valor": string
            }
        }

    }

}
