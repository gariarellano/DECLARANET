
import { Directive, forwardRef, Input } from '@angular/core';
import { NG_VALIDATORS, AbstractControl, ValidatorFn, Validator, FormControl } from '@angular/forms';


// validation function



@Directive({
  selector: '[curpNombreRfcIncompletoValid][ngModel]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: CurpNombreRfcIncompletoValidator, multi: true }
  ]
})
export class CurpNombreRfcIncompletoValidator implements Validator {
  tipo: string;
  validator: ValidatorFn;
  @Input('curpNombreRfcIncompletoValid') tipoValidacion: string;

  constructor() {
    this.validator = this.validateCurpNombreRfcIncompletoFactory();
  }

  validate(c: FormControl) {
    this.tipo = this.tipoValidacion;
    return this.validator(c);
  }
  
  validateCurpNombreRfcIncompletoFactory(): ValidatorFn {
    return (c: AbstractControl) => {
      let re;
      switch(this.tipo) {
        case 'rfc':
          re = /^([A-ZÑ\x26]{4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[A-Z\d]{3})?$/;
          break;
        case 'curp':
          re = /^([A-Z][AEIOUX][A-Z]{2}\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\d])(\d)$/;
          break;
        case 'nombre':
          re = /^((\b[a-zA-ZÑñÁÉÍÓÚáéíóú]{2,})\s*){2,7}$/;
          break;
      }
      //  = /^([A-ZÑ\x26]{4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[A-Z\d]{3})?$|^([A-ZÑ\x26]{4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1]))?$|^([A-Z][AEIOUX][A-Z]{2}\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\d])(\d)$|^((\b[a-zA-ZÑñÁÉÍÓÚáéíóú]{2,})\s*){2,7}$/;
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
}


