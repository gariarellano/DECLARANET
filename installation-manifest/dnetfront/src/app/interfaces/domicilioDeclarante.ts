export interface DomicilioDeclarante {
    'verificar': boolean,
    'domicilio': {
        'ubicacion': string, // México, Extranjero
        'domicilioMexico'?: { // uno u otro
          'calle': string,
          'codigoPostal': string,
          'coloniaLocalidad': string,
          'entidadFederativa': {
            'id': number,
            'valor': string
          },
          'municipioAlcaldia': {
            'id': number,
            'valor': string,
            'fk': string
          },
          'numeroExterior': string,
          'numeroInterior': string
        },
        'domicilioExtranjero'?: { // uno u otro
          'calle': string,
          'ciudadLocalidad': string,
          'codigoPostal': string,
          'estadoProvincia': string,
          'numeroExterior': string,
          'numeroInterior': string,
          'pais': {
            'id': number,
            'valor': string
          }
        }
      };
      'aclaracionesObservaciones': string;
}
