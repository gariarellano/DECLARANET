
import { Directive, forwardRef } from '@angular/core';
import { NG_VALIDATORS, AbstractControl, ValidatorFn, Validator, FormControl } from '@angular/forms';


// validation function
function validateCurpNombreRfcFactory(): ValidatorFn {
  return (c: AbstractControl) => {
    const re = /^([A-ZÑ\x26]{4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[A-Z\d]{3})?$|^([A-ZÑ\x26]{4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1]))?$|^([A-Z][AEIOUX][A-Z]{2}\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\d])(\d)$|^((\b[a-zA-ZÑñÁÉÍÓÚáéíóú]{2,})\s*){2,7}$/;
    const isValid = !c.value || c.value.match(re) ;
    if (isValid) {
      return null;
    } else {
      return {
        datoValid: {
          valid: false
        }
      };
    }

  };
}


@Directive({
  selector: '[curpNombreRfcValid][ngModel]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: CurpNombreRfcValidator, multi: true }
  ]
})
export class CurpNombreRfcValidator implements Validator {
  validator: ValidatorFn;

  constructor() {
    this.validator = validateCurpNombreRfcFactory();
  }

  validate(c: FormControl) {
    return this.validator(c);
  }

}


