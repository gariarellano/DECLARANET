import { Directive, forwardRef } from '@angular/core';
import { NG_VALIDATORS, AbstractControl, ValidatorFn, Validator, FormControl } from '@angular/forms';


// validation function
function validatePasswordFactory(): ValidatorFn {
  return (c: AbstractControl) => {
    // const re = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;
    const re = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d\W]{8,}$/;
    const isValid = !c.value || c.value.match(re) ;
    // const isValid = c.value === 'VARE880714';
    if (isValid) {
      return null;
    } else {
      return {
        passwordInvalid: true
      };
    }

  };
}


@Directive({
  selector: '[passwordValid][ngModel]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: PasswordValidator, multi: true }
  ]
})
export class PasswordValidator implements Validator {
  validator: ValidatorFn;

  constructor() {
    this.validator = validatePasswordFactory();
  }

  validate(c: FormControl) {
    return this.validator(c);
  }

}


