export class ConfigEnv {

  public static getEnvironmentVariable(value) {
    let ambiente: string;
    let protocolo: string;
    let data = {};
    ambiente = window.location.hostname;
    protocolo = window.location.protocol;
    const objConexion = $ENV.API_RESOURCES;
    console.log(objConexion);
    switch (ambiente) {
     

      case objConexion.SP_HOSTNAME:
        data = {
          API_BASE: objConexion.API_BASE,
          LOGGER: true,
         };
        break;
      case objConexion.SP_STAGING_HOSTNAME:
        data = {
          API_BASE: objConexion.STAGING_API_BASE,
          LOGGER: false,
         };
        break;
      default:
        data = {
          API_TRANSACT: '/SinDatos',
          API_LOGIN: '/SinDatos'
        };
    }
    return data[value];
  }
}


export const environment = {
  API_BASE: ConfigEnv.getEnvironmentVariable('API_BASE'),
  LOGGER: ConfigEnv.getEnvironmentVariable('LOGGER'),
  production: true
};