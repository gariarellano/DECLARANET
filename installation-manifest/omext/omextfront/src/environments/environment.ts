// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.
export const environment = {
  LOCALDEV: "localhost",
  ID_CLIENTE: "21135",
  SECRET_KEY: "FITDJ92NF85NX7TKAN69",
  API_IDP:
    "https://70-review-testing-7qeaae.200.34.175.120.nip.io/assets/plugin/DnetTool.js",
    // "http://localhost:4300/assets/plugin/DnetTool.js",
  API_ENTE_RECEPTOR:
    "https://api-gateway-staging.apps.funcionpublica.gob.mx/declaranet",
  // API_IDP_AUTH: "https://52-review-testing-wf02bm.200.34.175.120.nip.io/api",
  API_IDP_AUTH: 'https://api-gateway-staging.apps.funcionpublica.gob.mx/identidad/login',
  // API_IDP_REGISTRO: 'https://101-review-newtest-6nmvq2.k8s.sfp.gob.mx/api',
  API_IDP_REGISTRO: 'https://api-gateway-staging.apps.funcionpublica.gob.mx/identidad/registro',

  API_RENOVAR:
    "https://api-gateway-staging.apps.funcionpublica.gob.mx/renovarToken",

  // API_PERFILAMIENTO: "https://dgti-ejc-permisos-staging.perfiles.apps.funcionpublica.gob.mx/auth/graphql",
  // API_PERFILAMIENTO: "https://dgti-ejc-permisos-staging.k8s.sfp.gob.mx/auth/graphql",
  // API_PERFILAMIENTO: "https://361-review-desarrollo-31hgw0.perfiles.apps.funcionpublica.gob.mx/auth/graphql",
  //API_BASE: "https://dnet-omext-omextback-staging.dkla8s.funcionpublica.gob.mx",
  API_BASE: "https://api-gateway-staging.apps.funcionpublica.gob.mx/omext/",
  API_NOLOCALIZADOS: "https://api-gateway-staging.apps.funcionpublica.gob.mx/nolocalizados",
  //API_BASE: 'https://dnet-omext-omextback.dkla8s.funcionpublica.gob.mx',
  API_NAME_PERFILAMIENTO: 'OMEXT',
  LOGGER: true,
  API_CONSULTAR_ENTES: 'https://api-gateway-staging.apps.funcionpublica.gob.mx/declaranet/consultar-ente',
  // API_INICIAR: 'https://686-review-inicio-1yifk0.dkla8s.sfp.gob.mx/inicio/iniciar',
  API_INICIAR: 'https://api-gateway-staging.apps.funcionpublica.gob.mx/omext-login/iniciar',
  API_KONG: 'https://api-gateway-staging.apps.funcionpublica.gob.mx',
  /* API_BASE: "https://api-gateway-staging.apps.funcionpublica.gob.mx/declaranet",
  API_ENTES: "https://api-gateway-staging.apps.funcionpublica.gob.mx/entes",
  API_CONSULTAR_ENTES:
    "https://api-gateway-staging.apps.funcionpublica.gob.mx/declaranet/consultar-ente",
  https://api-gateway-staging.apps.funcionpublica.gob.mx/identidad/login/public/validate-
response
  ID_CLIENTE: "21123",
  SECRET_KEY: "FDSG65498GFDSG543GFD",
  API_IDP:
    "https://70-review-testing-7qeaae.200.34.175.120.nip.io/assets/plugin/DnetTool.js",
  API_SIGN:
    "https://308-review-review-rcmeyv.apps.funcionpublica.gob.mx/assets/plugin/sfpsign.js",
  API_IDP_AUTH: "https://52-review-testing-wf02bm.200.34.175.120.nip.io/api",
  LOGGER: true, */

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
