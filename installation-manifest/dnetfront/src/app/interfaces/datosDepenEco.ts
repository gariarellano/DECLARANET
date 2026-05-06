export interface DatosDepenEco {
    // "id": string,
    // idPosicionVista: number,
    // registroHistorico:boolean,
    // verificar:boolean,
    // "tipoOperacion": string, //AGREGAR MODIFICAR SIN CAMBIO (BAJA="BORRADO LOGICO")
    //----------------------------------------------------------------------------------
    "id": number,
    "verificar": boolean,
    "idPosicionVista": number,
    "registroHistorico": boolean,
    "ninguno": false,
    "tipoOperacion": string, //AGREGAR MODIFICAR SIN CAMBIO (BAJA="BORRADO LOGICO")
    "datosPersonales": {
        //@persona
        "nombre": string,
        "primerApellido": string,
        "segundoApellido": string,
        "rfc": string, //solo obligatorio para el declaranete
        "fechaNacimiento": string
    },
    "parentescoRelacion": {
        "id": string,
        "valor": string
    },
    "parentescoRelacionOtro":string,
    "ciudadanoExtranjero": {
        "esExtranjero": boolean, //SI =true, NO= false
        "curp": string //vendra solo si es false
    },
    // "esDependienteEconomico": boolean,
    "habitaDomicilioDeclarante": false,
    "domicilioDependienteEconomico": {
        "lugarDondeReside": string,
        "domicilio": { //solo si es false el habitaMismoDomDeclarante
            //@ domicilio
            "ubicacion": string, //México, Extranjero
            "domicilioMexico": { //uno u otro
                "calle": string,
                "codigoPostal": string,
                "coloniaLocalidad": string,
                "entidadFederativa": {
                    "id": number,
                    "valor": string,
                },
                "municipioAlcaldia": {
                    "fk": number,
                    "id": number,
                    "valor": string,
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
    },
    "actividadLaboral": {

        //@actividadLaboral
        "ambitoSector": {
            "id": number,
            "valor": string
        },
        "ambitoSectorOtro":"",
        "sectorPublico": {
            "nivelOrdenGobierno":string,
            "ambitoPublico":string,
            "nombreEntePublico": string,
            "areaAdscripcion": string,
            "empleoCargoComision": string,
            "funcionPrincipal": string
        },
        "sectorPrivadoOtro": {
            "nombreEmpresaSociedadAsociacion": string,
            "rfc": string,
            "area": string,
            "empleoCargo": string,
            "sectorOtro":string,
            "sector": {
                "id": number,
                "valor": string
            },
            "proveedorContratistaGobierno": string
        },
        "fechaIngreso": string,
        "salarioMensualNeto": number
    }
}
