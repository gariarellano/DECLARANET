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
                API_URL: 'https://api-gateway-staging.apps.funcionpublica.gob.mx/identidad/login',
                // API_REGISTER: 'https://api-gateway-staging.apps.funcionpublica.gob.mx/identidad/registro'
                // API_URL: 'https://52-review-testing-wf02bm.k8s.sfp.gob.mx/api',
                API_REGISTER: 'https://101-review-newtest-6nmvq2.k8s.sfp.gob.mx/api'
                // API_REGISTER: 'https://101-review-testing-yd06wj.200.34.175.120.nip.io/api'
              };
      }
    return data[value];
  }
}

export const environment = {
  production: false,
  LOGGER: true,
  TELEGRAM_URL: 'https://t.me/stagingNotifSfp_bot',
  // SECRET_RECAPTCHA: null,
  SECRET_RECAPTCHA: '6LdlqMkZAAAAALathOCoAeZu46GdlJEI0C09Z1HM',
  apiUrl: ConfigClass.getEnvironmentVariable('API_URL'),
  apiRegister: ConfigClass.getEnvironmentVariable('API_REGISTER')
};


/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
