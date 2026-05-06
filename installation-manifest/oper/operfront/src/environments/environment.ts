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
    switch (ambiente) {

          default:
              data = {
                API_REGISTER: 'https://api-gateway-staging.apps.funcionpublica.gob.mx/api'
              };
      }
    return data[value];
  }

}

export const environment = {
  apiRegister: ConfigClass.getEnvironmentVariable('API_REGISTER'),
  LOCALDEV: 'localhost',
  ID_CLIENTE: '21138',
  SECRET_KEY: '329ZWRRTX567SDG74D45F',
  // API_ENTE_RECEPTOR: 'https://api-gateway-staging.apps.funcionpublica.gob.mx/declaranet',
  API_RENOVAR: 'https://api-gateway-staging.apps.funcionpublica.gob.mx/renovarToken',
  // API_IDP: 'https://70-review-testing-7qeaae.200.34.175.120.nip.io/assets/plugin/DnetTool.js',
  API_IDP: "http://localhost:4300/assets/plugin/DnetTool.js",
  API_IDP_AUTH: 'https://api-gateway-staging.apps.funcionpublica.gob.mx/identidad/login',
  API_IDP_REGISTRO: 'https://api-gateway-staging.apps.funcionpublica.gob.mx/identidad/registro',
  API_REGISTER: 'https://api-gateway-staging.apps.funcionpublica.gob.mx/api',
  API_BASE: 'https://api-gateway-staging.apps.funcionpublica.gob.mx',
  API_NAME_PERFILAMIENTO: 'OPER',
  API_INICIAR: 'https://731-review-login-py9mn2.dkla8s.sfp.gob.mx/inicio/iniciar',
  LOGGER: true,
  production: false,
};
/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
