export class ConfigClass {
  public static getEnvironmentVariable(value) {
    let ambiente: string;
    let protocolo: string;
    let data = {};
    ambiente = window.location.hostname;
    protocolo = window.location.protocol;
    const objConexion = $ENV.API_RESOURCES;
    console.log(ambiente);
    switch (ambiente) {
      case objConexion.OMEXT_REVIEW_HOSTNAME:
        console.log('ambiente', 'Dev');
        data = {
          API_NAME_PERFILAMIENTO: 'OMEXT',
          ID_CLIENTE: objConexion.OMEXT_REVIEW_ID_CLIENTE,
          SECRET_KEY: objConexion.OMEXT_REVIEW_SECRET_KEY,
          API_IDP: objConexion.REVIEW_API_IDP,
          API_ENTE_RECEPTOR: objConexion.REVIEW_API_KONG + '/declaranet',
          API_IDP_AUTH: objConexion.REVIEW_API_KONG + '/identidad/login',
          API_IDP_REGISTRO: objConexion.REVIEW_API_KONG + '/identidad/registro',
          API_RENOVAR: objConexion.REVIEW_API_KONG + '/renovarToken',
          API_BASE: objConexion.REVIEW_API_KONG + '/omext/',
          API_NOLOCALIZADOS: objConexion.REVIEW_API_KONG + '/nolocalizados',
          API_CONSULTAR_ENTES: objConexion.REVIEW_API_KONG + '/declaranet/consultar-ente',
          API_INICIAR: objConexion.REVIEW_API_KONG + '/omext-login/iniciar',
          API_KONG: objConexion.REVIEW_API_KONG,
          LOGGER: true,
          LOCALDEV: 'localhost'

        };
        break;
        case objConexion.OMEXT_STAGING_HOSTNAME:
          console.log('ambiente', 'Dev');
          data = {
            API_NAME_PERFILAMIENTO: 'OMEXT',
            ID_CLIENTE: objConexion.OMEXT_STAGING_ID_CLIENTE,
            SECRET_KEY: objConexion.OMEXT_STAGING_SECRET_KEY,
            API_IDP: objConexion.STAGING_API_IDP,
            API_ENTE_RECEPTOR: objConexion.STAGING_API_KONG + '/declaranet',
            API_IDP_AUTH: objConexion.STAGING_API_KONG + '/identidad/login',
            API_IDP_REGISTRO: objConexion.STAGING_API_KONG + '/identidad/registro',
            API_RENOVAR: objConexion.STAGING_API_KONG + '/renovarToken',
            API_BASE: objConexion.STAGING_API_KONG + '/omext/',
            API_NOLOCALIZADOS: objConexion.STAGING_API_KONG + '/nolocalizados',
            API_CONSULTAR_ENTES: objConexion.STAGING_API_KONG + '/declaranet/consultar-ente',
          API_INICIAR: objConexion.STAGING_API_KONG + '/omext-login/iniciar',
          API_KONG: objConexion.STAGING_API_KONG,
            LOGGER: true,
            LOCALDEV: 'staging'
          };
          break;
      case objConexion.OMEXT_HOSTNAME:
        console.log('ambiente', 'ProdOmext');
        data = {
          API_NAME_PERFILAMIENTO: 'OMEXT',
          ID_CLIENTE: objConexion.OMEXT_ID_CLIENTE,
          SECRET_KEY: objConexion.OMEXT_SECRET_KEY,
          API_IDP: objConexion.API_IDP,
          API_ENTE_RECEPTOR: objConexion.API_KONG + '/declaranet',
          API_IDP_AUTH: objConexion.API_KONG + '/identidad/login',
          API_IDP_REGISTRO: objConexion.API_KONG + '/identidad/registro',
          API_RENOVAR: objConexion.API_KONG + '/renovarToken',
          API_BASE: objConexion.API_KONG + '/omext/',
          API_NOLOCALIZADOS: objConexion.API_KONG + '/nolocalizados',
          API_CONSULTAR_ENTES: objConexion.API_KONG + '/declaranet/consultar-ente',
          API_INICIAR: objConexion.API_KONG + '/omext-login/iniciar',
          API_KONG: objConexion.API_KONG,
          LOGGER: false,
          LOCALDEV: 'produccion'
        };
        break;
      case objConexion.OMEXT_PRES_HOSTNAME:
        console.log('ambiente', 'ProdOmext');
        data = {
          API_NAME_PERFILAMIENTO: 'OMEXT',
          ID_CLIENTE: objConexion.OMEXT_PRES_ID_CLIENTE,
          SECRET_KEY: objConexion.OMEXT_PRES_SECRET_KEY,
          API_IDP: objConexion.PRES_API_IDP,
          API_ENTE_RECEPTOR: objConexion.PRES_API_KONG + '/declaranet',
          API_IDP_AUTH: objConexion.PRES_API_KONG + '/identidad/login',
          API_IDP_REGISTRO: objConexion.PRES_API_KONG + '/identidad/registro',
          API_RENOVAR: objConexion.PRES_API_KONG + '/renovarToken',
          API_BASE: objConexion.PRES_API_KONG + '/omext/',
          API_NOLOCALIZADOS: objConexion.PRES_API_KONG + '/nolocalizados',
          API_CONSULTAR_ENTES: objConexion.PRES_API_KONG + '/declaranet/consultar-ente',
          API_INICIAR: objConexion.PRES_API_KONG + '/omext-login/iniciar',
          API_KONG: objConexion.PRES_API_KONG,
          LOGGER: false,
          LOCALDEV: 'produccion'
        };
        break;

      default:
        console.log('Error en el enviroment!!');
    }
    return data[value];
  }
}
export const environment = {
  production: true,
  apiRegister: ConfigClass.getEnvironmentVariable('API_REGISTER'),
  ID_CLIENTE: ConfigClass.getEnvironmentVariable('ID_CLIENTE'),
  SECRET_KEY: ConfigClass.getEnvironmentVariable('SECRET_KEY'),
  API_IDP: ConfigClass.getEnvironmentVariable('API_IDP'),
  API_ENTE_RECEPTOR: ConfigClass.getEnvironmentVariable('API_ENTE_RECEPTOR'),
  API_IDP_AUTH: ConfigClass.getEnvironmentVariable('API_IDP_AUTH'),
  API_IDP_REGISTRO: ConfigClass.getEnvironmentVariable('API_IDP_REGISTRO'),
  API_RENOVAR: ConfigClass.getEnvironmentVariable('API_RENOVAR'),
  API_BASE: ConfigClass.getEnvironmentVariable('API_BASE'),
  API_CONSULTAR_ENTES: ConfigClass.getEnvironmentVariable('API_CONSULTAR_ENTES'),
  API_NOLOCALIZADOS: ConfigClass.getEnvironmentVariable('API_NOLOCALIZADOS'),
  LOGGER: ConfigClass.getEnvironmentVariable('LOGGER'),
  LOCALDEV: ConfigClass.getEnvironmentVariable('LOCALDEV'),
  API_NAME_PERFILAMIENTO: ConfigClass.getEnvironmentVariable('API_NAME_PERFILAMIENTO'),
  API_KONG: ConfigClass.getEnvironmentVariable('API_KONG'),
  API_INICIAR: ConfigClass.getEnvironmentVariable('API_INICIAR')


  // API_ENTES: ConfigClass.getEnvironmentVariable('API_ENTES'),
  // API_CONSULTAR_ENTES: ConfigClass.getEnvironmentVariable('API_CONSULTAR_ENTES'),
  // API_RENOVAR: ConfigClass.getEnvironmentVariable('API_RENOVAR'),  
  // API_SIGN: ConfigClass.getEnvironmentVariable('API_SIGN'), 
  // LOGGER: ConfigClass.getEnvironmentVariable('LOGGER')  
};
