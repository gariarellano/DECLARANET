import { Directive, forwardRef } from '@angular/core';
import { NG_VALIDATORS, AbstractControl, ValidatorFn, Validator, FormControl } from '@angular/forms';




function validateCadenaFactory(): ValidatorFn {
    return (ca: AbstractControl) => {
        console.log("ca ", ca);

        let valido = true;
        // const reg = /^([A-ZÑ\x26]{3}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[A-Z\d]{3})?$/;
        let reg = /^[.*;_:)(+-/&%$?¿!¿¡`"|¨]$/;

        for (var i = 0; i < ca.value.length; i++) {
            var caracter = ca.value.charAt(i);
            if (caracter.match(reg)) {
                valido = false;
            }
        }

        if (valido) {
            return null;
        } else {
            return {
                cadenaValid: {
                    valid: false
                }
            }
        }

        /*const isValid = !ca.value || ca.value.match(reg);
         if (isValid) {
             return null;
         } else {
             return {
                 cadenaValid: {
                     valid: false
                 }
             };
         }*/

    };
}

@Directive({
    selector: '[cadenaValid][ngModel]',
    providers: [
        { provide: NG_VALIDATORS, useExisting: validadorCadena, multi: true }
    ]
})



export class validadorCadena implements Validator {
    validator: ValidatorFn;

    constructor() {
        this.validator = validateCadenaFactory();
    }

    validate(ca: FormControl) {
        return this.validator(ca);
    }

}
