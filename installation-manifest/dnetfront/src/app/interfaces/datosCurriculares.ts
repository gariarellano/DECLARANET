import {Escolaridad} from './../interfaces/datosCurriculares-escolaridad';

export interface DatosCurricularesDeclarante {
    ninguno?: boolean ;
    'escolaridad': Array<Escolaridad>;
    'aclaracionesObservaciones': string;
}
