export class ConfigEnv {

  public static getEnvironmentVariable(value) {
    let ambiente: string;
    let protocolo: string;
    let data = {};
    ambiente = window.location.hostname;
    protocolo = window.location.protocol;
    // console.log(objConexion);
    // console.log($ENV.API_RESOURCES);
    const objConexion = $ENV.API_RESOURCES;
    // objConexion.API_RESOURCES = 'prueba';
    // console.log(objConexion.API_RESOURCES);
    
    switch (ambiente) {
      case objConexion.HOSTNAME:
        data = {
          API_BASE: objConexion.API_KONG + '/declaranet',
          API_ENTES: objConexion.API_KONG + '/entes',
          API_CONSULTAR_ENTES: objConexion.API_KONG + '/declaranet/consultar-ente',
          API_RENOVAR: objConexion.API_KONG + '/renovarToken',
          ID_CLIENTE: objConexion.ID_CLIENTE,
          SECRET_KEY: objConexion.SECRET_KEY,
          ID_CLIENTE_SIGN: objConexion.ID_CLIENTE_SIGN,
          SECRET_KEY_SIGN: objConexion.SECRET_KEY_SIGN,
          API_IDP: objConexion.API_IDP,
          API_SIGN: objConexion.API_SIGN,
          API_IDP_AUTH: objConexion.API_IDP_AUTH,
          LOGGER: objConexion.LOGGER
        };
        break;
      case objConexion.PRES_HOSTNAME:
        data = {
          API_BASE: objConexion.PRES_API_KONG + '/declaranet',
          API_ENTES: objConexion.PRES_API_KONG + '/entes',
          API_CONSULTAR_ENTES: objConexion.PRES_API_KONG + '/declaranet/consultar-ente',
          API_RENOVAR: objConexion.PRES_API_KONG + '/renovarToken',
          ID_CLIENTE: objConexion.PRES_ID_CLIENTE,
          SECRET_KEY: objConexion.PRES_SECRET_KEY,
          ID_CLIENTE_SIGN: objConexion.PRES_ID_CLIENTE_SIGN,
          SECRET_KEY_SIGN: objConexion.PRES_SECRET_KEY_SIGN,
          API_IDP: objConexion.PRES_API_IDP,
          API_SIGN: objConexion.PRES_API_SIGN,
          API_IDP_AUTH: objConexion.PRES_API_IDP_AUTH,
          LOGGER: objConexion.PRES_LOGGER
        };
        break;
      case objConexion.STAGING_HOSTNAME:
        data = {
          API_BASE: objConexion.STAGING_API_KONG + '/declaranet',
          API_ENTES: objConexion.STAGING_API_KONG + '/entes',
          API_CONSULTAR_ENTES: objConexion.STAGING_API_KONG + '/declaranet/consultar-ente',
          API_RENOVAR: objConexion.STAGING_API_KONG + '/renovarToken',
          ID_CLIENTE: objConexion.STAGING_ID_CLIENTE,
          SECRET_KEY: objConexion.STAGING_SECRET_KEY,
          ID_CLIENTE_SIGN: objConexion.STAGING_ID_CLIENTE_SIGN,
          SECRET_KEY_SIGN: objConexion.STAGING_SECRET_KEY_SIGN,
          API_IDP: objConexion.STAGING_API_IDP,
          API_SIGN: objConexion.STAGING_API_SIGN,
          API_IDP_AUTH: objConexion.STAGING_API_IDP_AUTH,
          LOGGER: objConexion.STAGING_LOGGER
        };
        break;
      case objConexion.REVIEW_HOSTNAME:
        data = {
          API_BASE: objConexion.REVIEW_API_KONG + '/declaranet',
          API_ENTES: objConexion.REVIEW_API_KONG + '/entes',
          API_CONSULTAR_ENTES: objConexion.REVIEW_API_KONG + '/declaranet/consultar-ente',
          API_RENOVAR: objConexion.REVIEW_API_KONG + '/renovarToken',
          ID_CLIENTE: objConexion.REVIEW_ID_CLIENTE,
          SECRET_KEY: objConexion.REVIEW_SECRET_KEY,
          ID_CLIENTE_SIGN: objConexion.REVIEW_ID_CLIENTE_SIGN,
          SECRET_KEY_SIGN: objConexion.REVIEW_SECRET_KEY_SIGN,
          API_IDP: objConexion.REVIEW_API_IDP,
          API_SIGN: objConexion.REVIEW_API_SIGN,
          API_IDP_AUTH: objConexion.REVIEW_API_IDP_AUTH,
          LOGGER: objConexion.REVIEW_LOGGER
        };
        break;
      default:
        data = {
          // API_URL: 'http://172.29.100.24:8888/api',
          // API_REGISTER: 'http://172.29.100.24:8181/api'
          API_BASE: '/SinDatos',
          API_ENTES: '/SinDatos'
        };
    }
    return data[value];
  }
}


export const environment = {
  API_BASE: ConfigEnv.getEnvironmentVariable('API_BASE'),
  API_ENTES: ConfigEnv.getEnvironmentVariable('API_ENTES'),
  API_CONSULTAR_ENTES: ConfigEnv.getEnvironmentVariable('API_CONSULTAR_ENTES'),
  API_RENOVAR: ConfigEnv.getEnvironmentVariable('API_RENOVAR'),
  ID_CLIENTE: ConfigEnv.getEnvironmentVariable('ID_CLIENTE'),
  SECRET_KEY: ConfigEnv.getEnvironmentVariable('SECRET_KEY'),
  ID_CLIENTE_SIGN: ConfigEnv.getEnvironmentVariable('ID_CLIENTE_SIGN'),
  SECRET_KEY_SIGN: ConfigEnv.getEnvironmentVariable('SECRET_KEY_SIGN'),
  API_IDP: ConfigEnv.getEnvironmentVariable('API_IDP'),
  API_SIGN: ConfigEnv.getEnvironmentVariable('API_SIGN'),
  API_SIGN_LOCALHOST: 'https://308-review-review-rcmeyv.apps.funcionpublica.gob.mx/assets/plugin/sfpsign.js',
  API_SIGN_REVIEW: 'https://308-review-review-rcmeyv.apps.funcionpublica.gob.mx/assets/plugin/sfpsign.js',
  API_SIGN_STAGING: 'https://ejzpriv-firma-electronica-firmafront-staging.apps.funcionpublica.gob.mx/assets/plugin/sfpsign.js',
  API_SIGN_PROD: 'https://firma.apps.funcionpublica.gob.mx/assets/plugin/sfpsign.js',
  API_SIGN_PROD_PRES: 'https://firma-pres.declaranet.gob.mx/assets/plugin/sfpsign.js',
  API_IDP_AUTH: ConfigEnv.getEnvironmentVariable('API_IDP_AUTH'),
  LOGGER: ConfigEnv.getEnvironmentVariable('LOGGER'),
  production: true
};