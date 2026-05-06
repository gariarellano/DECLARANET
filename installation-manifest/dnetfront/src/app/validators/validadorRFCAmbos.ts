import { Directive, forwardRef } from '@angular/core';
import { NG_VALIDATORS, AbstractControl, ValidatorFn, Validator, FormControl } from '@angular/forms';




function validateRfcAmbosFactory(): ValidatorFn {
    return (c: AbstractControl) => {
      const re = /^([A-ZÑ\x26]{3,4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[A-Z\d]{3})?$/;
      // const re = /^([A-ZÑ\x26]{3}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1]))([A-Z\d]{3})?$/;
      
      // /^([A-ZÑ&]{3,4}) ?(?:- ?)?(\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])) ?(?:- ?)?([A-Z\d]{2})([A\d])$/;
      const isValid = !c.value || c.value.match(re) ;
      // const isValid = c.value === 'VARE880714';
      if (isValid) {
        return null;
      } else {
        return {
          rfcAmbosValid: {
            valid: false
          }
        };
      }
  
    };
  }

  @Directive({
    selector: '[rfcAmbosValid][ngModel]',
    providers: [
      { provide: NG_VALIDATORS, useExisting: validadorRFCAmbos, multi: true }
    ]
  })
  
  

export class validadorRFCAmbos implements Validator {
    validator: ValidatorFn;
  
    constructor() {
      this.validator = validateRfcAmbosFactory();
    }
  
    validate(c: FormControl) {
      return this.validator(c);
    }
  
  }