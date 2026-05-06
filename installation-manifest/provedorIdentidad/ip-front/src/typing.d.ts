// so the typescript compiler doesn't complain about the global config object
declare var $ENV: Env;

interface Env {
  API_RESOURCES: any;
}