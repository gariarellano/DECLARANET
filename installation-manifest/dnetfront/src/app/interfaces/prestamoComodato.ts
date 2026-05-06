import {Prestamo} from './prestamoComodato-prestamo';

export interface PrestamoComodato {
    ninguno: boolean,
    prestamo: Array<Prestamo>,
    aclaracionesObservaciones: string
}