// import { AbstractControl } from '@angular/forms';

// export function ValidateCurp(control: AbstractControl) {
//   const re = /^([A-Z][AEIOUX][A-Z]{2}\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\d])(\d)$/
// // /^([A-ZÑ&]{3,4}) ?(?:- ?)?(\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])) ?(?:- ?)?([A-Z\d]{2})([A\d])$/;
// // regex para rfc completo
//   if (!control.value.match(re)) {
//     return { validCurp: true };
//   }
//   return null;
// }


import { Directive, forwardRef } from '@angular/core';
import { NG_VALIDATORS, AbstractControl, ValidatorFn, Validator, FormControl } from '@angular/forms';


// validation function
function validateCurpFactory(): ValidatorFn {
  return (c: AbstractControl) => {
    const re = /^([A-Z][AEIOUX][A-Z]{2}\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\d])(\d)$/;
    const isValid = !c.value || c.value.match(re) ;
    // const isValid = c.value === 'VARE880714';
    if (isValid) {
      return null;
    } else {
      return {
        curpValid: {
          valid: false
        }
      };
    }

  };
}


@Directive({
  selector: '[curpValid][ngModel]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: CurpValidator, multi: true }
  ]
})
export class CurpValidator implements Validator {
  validator: ValidatorFn;

  constructor() {
    this.validator = validateCurpFactory();
  }

  validate(c: FormControl) {
    return this.validator(c);
  }

}


