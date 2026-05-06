//  import { AbstractControl, ValidatorFn } from '@angular/forms';

//  export function ValidateRfcFactory(): ValidatorFn {
//   return (c: AbstractControl) => {
//   const re = /^([A-ZÑ&]{3,4}) ?(\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01]))$/;
// // /^([A-ZÑ&]{3,4}) ?(?:- ?)?(\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])) ?(?:- ?)?([A-Z\d]{2})([A\d])$/;
// // regex para rfc completo
//   if (!c.value.match(re)) {
//     return { validrfc: true };
//   }
//   return null;
//   };
// }


import { Directive, forwardRef } from '@angular/core';
import { NG_VALIDATORS, AbstractControl, ValidatorFn, Validator, FormControl } from '@angular/forms';


// validation function
function validateRfcFactory(): ValidatorFn {
  return (c: AbstractControl) => {
    const re = /^([A-ZÑ&]{3,4}) ?(\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01]))$/;
    // /^([A-ZÑ&]{3,4}) ?(?:- ?)?(\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])) ?(?:- ?)?([A-Z\d]{2})([A\d])$/;
    const isValid = !c.value || c.value.match(re) ;
    // const isValid = c.value === 'VARE880714';
    if (isValid) {
      return null;
    } else {
      return {
        rfcValid: {
          valid: false
        }
      };
    }

  };
}


@Directive({
  selector: '[rfcValid][ngModel]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: RfcValidator, multi: true }
  ]
})
export class RfcValidator implements Validator {
  validator: ValidatorFn;

  constructor() {
    this.validator = validateRfcFactory();
  }

  validate(c: FormControl) {
    return this.validator(c);
  }

}
