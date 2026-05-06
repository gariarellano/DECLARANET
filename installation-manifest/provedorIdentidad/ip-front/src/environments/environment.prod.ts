export class ConfigClass {

  public static getEnvironmentVariable(value) {
    let ambiente: string;
    let protocolo: string;
    let data = {};
    ambiente = window.location.hostname;
    protocolo = window.location.protocol;
    // console.log($ENV.API_RESOURCES);
    const objConexion = $ENV.API_RESOURCES;

    // console.log(ambiente);
    switch (ambiente) {
      case objConexion.IDP_HOSTNAME:
        data = {
          API_URL: objConexion.API_IDP_AUTH,
          API_REGISTER: objConexion.IDP_API_REGISTER,
          SECRET_RECAPTCHA: objConexion.IDP_SECRET_RECAPTCHA || null,
          TELEGRAM_URL: objConexion.IDP_TELEGRAM_URL,
          LOGGER: false
        };
        break;
      case objConexion.IDP_STAGING_HOSTNAME:
        data = {
          API_URL: objConexion.STAGING_API_IDP_AUTH,
          API_REGISTER: objConexion.IDP_STAGING_API_REGISTER,
          SECRET_RECAPTCHA: objConexion.IDP_STAGING_SECRET_RECAPTCHA || null,
          TELEGRAM_URL: objConexion.IDP_STAGING_TELEGRAM_URL,
          LOGGER: true
        };
        break;
      case objConexion.IDP_REVIEW_HOSTNAME:
        data = {
          API_URL: objConexion.REVIEW_API_IDP_AUTH,
          API_REGISTER: objConexion.IDP_REVIEW_API_REGISTER,
          SECRET_RECAPTCHA: objConexion.IDP_REVIEW_SECRET_RECAPTCHA || null,
          TELEGRAM_URL: objConexion.IDP_STAGING_TELEGRAM_URL,
          LOGGER: true
        };
        break;
      case objConexion.IDP_PRES_HOSTNAME:
        data = {
          API_URL: objConexion.PRES_API_IDP_AUTH,
          API_REGISTER: objConexion.IDP_PRES_API_REGISTER,
          SECRET_RECAPTCHA: objConexion.IDP_PRES_SECRET_RECAPTCHA || null,
          TELEGRAM_URL: objConexion.IDP_TELEGRAM_URL,
          LOGGER: true
        };
        break;
      default:
        data = {
          API_URL: '/apibad/api',
          API_REGISTER: '/apiregisterbad/api'
        };
    }
    return data[value];
  }
}

export const environment = {
  production: true,
  apiUrl: ConfigClass.getEnvironmentVariable('API_URL'),
  apiRegister: ConfigClass.getEnvironmentVariable('API_REGISTER'),
  TELEGRAM_URL: ConfigClass.getEnvironmentVariable('TELEGRAM_URL'),
  // SECRET_RECAPTCHA: '6LdO09waAAAAAPyUKRbkJb7dxckvZQSL3alufysq',
  SECRET_RECAPTCHA: ConfigClass.getEnvironmentVariable('SECRET_RECAPTCHA'),
  LOGGER: ConfigClass.getEnvironmentVariable('LOGGER')

};


