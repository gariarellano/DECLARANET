import { Directive, forwardRef } from '@angular/core';
import { NG_VALIDATORS, AbstractControl, ValidatorFn, Validator, FormControl } from '@angular/forms';




function validateCurpFactory(): ValidatorFn {
  return (c: AbstractControl) => {

    const re = new RegExp('([A-Z][AEIOUX][A-Z]{2}\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\\d])(\\d)');
    // const re = /^([A-Z][AEIOUX][A-Z]{2}\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\\d])(\\d)?$/;

    const isValid = !c.value || c.value.match(re);
    if (isValid) {
      return null;
    } else {
      return {
        CurpValid: {
          valid: false
        }
      };
    }

  };
}

@Directive({
  selector: '[curpValid][ngModel]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: validadorCurp, multi: true }
  ]
})



export class validadorCurp implements Validator {
  validator: ValidatorFn;

  constructor() {
    this.validator = validateCurpFactory();
  }

  validate(c: FormControl) {
    return this.validator(c);
  }

}
