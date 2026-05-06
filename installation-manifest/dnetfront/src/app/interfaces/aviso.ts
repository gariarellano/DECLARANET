import { DatosGenerales } from './datosGenerales';
import { DomicilioDeclarante } from './domicilioDeclarante';
import { DetalleAvisoCambioDependencia } from './avisoCambioDependencia';
export interface Aviso {
  datosGenerales: DatosGenerales;
  domicilioDeclarante?: DomicilioDeclarante;
  detalleAvisoCambioDependencia?: DetalleAvisoCambioDependencia;

}
