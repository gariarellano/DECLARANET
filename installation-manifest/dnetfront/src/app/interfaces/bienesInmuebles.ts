import {BienInmueble} from './bienesInmuebles-inmueble';

export interface BienesInmuebles{
        ninguno: boolean,
        bienesInmuebles: Array<BienInmueble>,
        aclaracionesObservaciones: string
}