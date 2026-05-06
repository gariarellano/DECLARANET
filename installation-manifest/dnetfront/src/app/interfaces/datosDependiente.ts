import {DatosDepenEco} from './../interfaces/datosDepenEco';

export interface DatosDependiente {
    ninguno: boolean,
    datosDependientesEconomicos:Array<DatosDepenEco>,
    aclaracionesObservaciones: string
}
