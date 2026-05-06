import { Directive, forwardRef } from '@angular/core';
import { NG_VALIDATORS, AbstractControl, ValidatorFn, Validator, FormControl } from '@angular/forms';




function validateEmailFactory(): ValidatorFn {
  return (c: AbstractControl) => {

    // const re = new RegExp('([A-Z][AEIOUX][A-Z]{2}\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\\d])(\\d)');
    const re = new RegExp('[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[-.:;?¡]*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.][a-zA-Z]{1,5}');
    // var email = new RegExp('[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[-.:;?¡]*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.][a-zA-Z]{1,5}');

    const isValid = !c.value || c.value.match(re);
    if (isValid) {
      return null;
    } else {
      return {
        emailValid: {
          valid: false
        }
      };
    }

  };
}

@Directive({
  selector: '[emailValid][ngModel]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: validadorEmail, multi: true }
  ]
})



export class validadorEmail implements Validator {
  validator: ValidatorFn;

  constructor() {
    this.validator = validateEmailFactory();
  }

  validate(c: FormControl) {
    return this.validator(c);
  }

}
