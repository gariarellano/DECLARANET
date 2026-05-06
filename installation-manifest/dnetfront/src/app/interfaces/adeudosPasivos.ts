import {Adeudos} from './adeudosPasivos-adeudos';

export interface AdeudosPasivos{
    ninguno:boolean,
    adeudos:Array<Adeudos>,
    aclaracionesObservaciones:string
}