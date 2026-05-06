export interface DatosGenerales {

    'datosPersonales': {
        'nombre': string,
        'primerApellido': string,
        'segundoApellido': string,
        'rfc': string,
        'curp': string,
    };
    'aclaracionesObservaciones': string;
    'aclara': string;
    'correoElectronico': {
        'institucional': string,
        'personalAlterno': string
    };
    'telefonoCasa': {
        'numero': string
    };
    'telefonoCelular': {
        'numero': string,
        'paisCelularPersonal'?: {
            'id': number,
            'valor': string
            // ,
            // 'lada': string
        }
    };
    'situacionPersonalEstadoCivil': {
        'id': number,
        'valor': string
    };
    'regimenMatrimonial': {
        'id': number,
        'valor': string
    };
    'regimenMatrimonialOtro'?: '';
    'paisNacimiento': {
        'id': number,
        'valor': string
    };
    'nacionalidad': {
        'id': number,
        'valor': string
    };
    verificar?: boolean;
}
