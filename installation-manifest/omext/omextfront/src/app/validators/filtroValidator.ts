import { Directive, forwardRef } from '@angular/core';
import { NG_VALIDATORS, AbstractControl, ValidatorFn, Validator, FormControl } from '@angular/forms';




function validateCadenaFactory(): ValidatorFn {
    return (ca: AbstractControl) => {

        let valido = true;
        // let reg = /^(([a-zA-Z0-9]{2,100})+([ ])+([a-zA-Z0-9]{2,100}))*$|/;
        let reg = /^((\b[a-zA-ZÑñÁÉÍÓÚáéíóú]{2,})\s*){2,7}$|^([A-Z][AEIOUX][A-Z]{2}\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\d])(\d)$/;

        if (ca.value.match(reg)) {
            valido = true;
        } else {
            valido = false;
        }

        if (valido) {
            return null;
        } else {
            return {
                filtroValid: {
                    valid: false
                }
            }
        }

    };
}

@Directive({
    selector: '[filtroValid][ngModel]',
    providers: [
        { provide: NG_VALIDATORS, useExisting: validadorFiltro, multi: true }
    ]
})



export class validadorFiltro implements Validator {
    validator: ValidatorFn;

    constructor() {
        this.validator = validateCadenaFactory();
    }

    validate(ca: FormControl) {
        return this.validator(ca);
    }

}
