// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export class ConfigClass {

  public static getEnvironmentVariable(value) {
    let ambiente: string;
    let protocolo: string;
    let data = {};
    ambiente = window.location.hostname;
    protocolo = window.location.protocol;
    console.log(ambiente);
    const objConexion = $ENV.API_RESOURCES;
    // console.log(objConexion);
    switch (ambiente) {
      case objConexion.OPER_PRES_HOSTNAME:
        // console.log('ambiente', 'prodOper.decla');
        data = {
          ID_CLIENTE: objConexion.OPER_PRES_ID_CLIENTE,
          SECRET_KEY: objConexion.OPER_PRES_SECRET_KEY,
          API_IDP: objConexion.PRES_API_IDP,
          API_ENTE_RECEPTOR: objConexion.PRES_API_KONG + '/declaranet',
          API_IDP_AUTH: objConexion.PRES_API_KONG + '/identidad/login',
          API_IDP_REGISTRO: objConexion.PRES_API_KONG + '/identidad/registro',
          API_RENOVAR: objConexion.PRES_API_KONG + '/renovarToken',
          API_BASE: objConexion.PRES_API_KONG,
          API_INICIAR: objConexion.PRES_API_KONG + '/oper-login/iniciar',
          LOGGER: false,
          LOCALDEV: 'produccion',
          API_REGISTER: objConexion.PRES_API_KONG + '/api',
          API_NAME_PERFILAMIENTO: 'OPER',
        };
        break;
      case objConexion.OPER_HOSTNAME:
        // console.log('ambiente', 'prodOper.decla');
        data = {
          ID_CLIENTE: objConexion.OPER_ID_CLIENTE,
          SECRET_KEY: objConexion.OPER_SECRET_KEY,
          API_IDP: objConexion.API_IDP,
          API_ENTE_RECEPTOR: objConexion.API_KONG + '/declaranet',
          API_IDP_AUTH: objConexion.API_KONG + '/identidad/login',
          API_IDP_REGISTRO: objConexion.API_KONG + '/identidad/registro',
          API_RENOVAR: objConexion.API_KONG + '/renovarToken',
          API_BASE: objConexion.API_KONG,
          API_INICIAR: objConexion.API_KONG + '/oper-login/iniciar',
          LOGGER: false,
          LOCALDEV: 'produccion',
          API_REGISTER: objConexion.API_KONG + '/api',
          API_NAME_PERFILAMIENTO: 'OPER',
        };
        break;
      case objConexion.OPER_STAGING_HOSTNAME:
        // console.log('ambiente', 'staging');
        data = {
          ID_CLIENTE: objConexion.OPER_STAGING_ID_CLIENTE,
          SECRET_KEY: objConexion.OPER_STAGING_SECRET_KEY,
          API_IDP: objConexion.STAGING_API_IDP,
          API_ENTE_RECEPTOR: objConexion.STAGING_API_KONG + '/declaranet',
          API_IDP_AUTH: objConexion.STAGING_API_KONG + '/identidad/login',
          API_IDP_REGISTRO: objConexion.STAGING_API_KONG + '/identidad/registro',
          API_RENOVAR: objConexion.STAGING_API_KONG + '/renovarToken',
          API_BASE: objConexion.STAGING_API_KONG,
          API_INICIAR: objConexion.STAGING_API_KONG + '/oper-login/iniciar',
          LOGGER: true,
          LOCALDEV: 'staging',
          API_REGISTER: objConexion.STAGING_API_KONG + '/api',
          API_NAME_PERFILAMIENTO: 'OPER',

        };
        break;

      case objConexion.OPER_REVIEW_HOSTNAME:
        // console.log('ambiente', 'dev');
        data = {
          ID_CLIENTE: objConexion.OPER_REVIEW_ID_CLIENTE,
          SECRET_KEY: objConexion.OPER_REVIEW_SECRET_KEY,
          API_IDP: objConexion.REVIEW_API_IDP,
          API_ENTE_RECEPTOR: objConexion.REVIEW_API_KONG + '/declaranet',
          API_IDP_AUTH: objConexion.REVIEW_API_KONG + '/identidad/login',
          API_IDP_REGISTRO: objConexion.REVIEW_API_KONG + '/identidad/registro',
          API_RENOVAR: objConexion.REVIEW_API_KONG + '/renovarToken',
          API_BASE: objConexion.REVIEW_API_KONG,
          API_INICIAR: objConexion.REVIEW_API_KONG + '/oper-login/iniciar',
          LOGGER: true,
          LOCALDEV: 'REVIEW',
          API_REGISTER: objConexion.REVIEW_API_KONG + '/api',
          API_NAME_PERFILAMIENTO: 'OPER',

        };
        break;

      default:
        console.log('ambiente', 'defa');
        data = {
          ID_CLIENTE: 'SinDatos',
          SECRET_KEY: 'SinDatos',
          API_ENTE_RECEPTOR: 'SinDatos',
          API_RENOVAR: 'SinDatos',
          API_PERFILAMIENTO: 'SinDatos',
          API_IDP: 'SinDatos',
          API_IDP_AUTH: 'SinDatos',
          API_IDP_REGISTRO: 'SinDatos',
          API_BASE: 'SinDatos',
          API_INICIAR: 'SinDatos',
          API_REGISTER: 'SinDatos',
          LOGGER: true,
          LOCALDEV: 'localhost',
          API_NAME_PERFILAMIENTO: 'OPER',
        };
    }
    return data[value];
  }
}

export const environment = {
  production: true,
  API_REGISTER: ConfigClass.getEnvironmentVariable('API_REGISTER'),
  ID_CLIENTE: ConfigClass.getEnvironmentVariable('ID_CLIENTE'),
  SECRET_KEY: ConfigClass.getEnvironmentVariable('SECRET_KEY'),
  API_IDP: ConfigClass.getEnvironmentVariable('API_IDP'),
  API_ENTE_RECEPTOR: ConfigClass.getEnvironmentVariable('API_ENTE_RECEPTOR'),
  API_IDP_AUTH: ConfigClass.getEnvironmentVariable('API_IDP_AUTH'),
  API_RENOVAR: ConfigClass.getEnvironmentVariable('API_RENOVAR'),
  API_BASE: ConfigClass.getEnvironmentVariable('API_BASE'),
  API_IDP_REGISTRO: ConfigClass.getEnvironmentVariable('API_IDP_REGISTRO'),
  API_PERFILAMIENTO: ConfigClass.getEnvironmentVariable('API_PERFILAMIENTO'),
  API_NAME_PERFILAMIENTO: ConfigClass.getEnvironmentVariable('API_NAME_PERFILAMIENTO'),
  API_INICIAR: ConfigClass.getEnvironmentVariable('API_INICIAR'),
  LOCALDEV: ConfigClass.getEnvironmentVariable('LOCALDEV'),
  LOGGER: ConfigClass.getEnvironmentVariable('LOGGER')
};
