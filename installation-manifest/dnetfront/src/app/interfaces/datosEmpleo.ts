import {EmpleoCargo} from './../interfaces/empleoCargo';

export interface DatosEmpleo{
    ninguno: boolean
    empleoCargoComision:Array<EmpleoCargo>,
    aclaracionesObservaciones:string
}