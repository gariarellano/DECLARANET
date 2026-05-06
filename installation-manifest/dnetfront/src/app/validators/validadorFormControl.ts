import { FormControl, ValidatorFn } from '@angular/forms';
import * as moment from 'moment';

export function validateCaracteres(control: FormControl): { [s: string]: boolean } {

    if (validaCadena(control)) {
        return {
            validateCaracteres: true
        }
    }
    return null;
}


function validaCadena(cadena): boolean {
    let valido = true;
    var regex = new RegExp("^[.*;_:)(+-/&%$?¿!¿¡`|¨]'$");
    if (cadena.value) {
        for (var i = 0; i < cadena.value.length; i++) {
            var caracter = cadena.value.charAt(i);
            if (caracter.match(regex)) {
                valido = false;
            }
        }
    }

    if (valido) {
        return null;
    } else {
        return true;
    }
}
export function validaFecha(control: FormControl): { [s: string]: boolean } {

    if (validaFechaProcess(control)) {
        return {
            validaFecha: true
        }
    }
    return null;
}


function validaFechaProcess(cadena): boolean {
    let valido = true;
    const fechaSelect = moment(cadena.value).format("YYYY-MM-DD");;
    const today = moment().format("YYYY-MM-DD");

    if (fechaSelect > today) {
        valido = false;
    }

    if (valido) {
        return null;
    } else {
        return true;
    }
}

export function validaFechaLimit(fechaMax): ValidatorFn {
    return (control: FormControl): { [s: string]: boolean } => {

        if (validaFechaProcessLimit(control, fechaMax)) {
            return {
                validaFechaLimit: true
            };
        }
        return null;
    }
}


function validaFechaProcessLimit(cadena, fechaMax): boolean {
    let valido = true;
    const fechaSelect = moment(cadena.value).format('YYYY-MM-DD');
    // const today = moment().format("YYYY-MM-DD");
    const fechaSelectYear = moment(cadena.value).year();
    const fechaTodayYear = moment(fechaMax).year();

    if (fechaSelect > fechaMax) {
        valido = false;
    }

    if (valido) {
        return null;
    } else {
        return true;
    }
}