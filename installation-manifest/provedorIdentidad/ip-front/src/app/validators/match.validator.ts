// import { AbstractControl, FormGroup } from '@angular/forms';

// export function validateMatching(key: string, confirmKey: string) {
//     return (group: FormGroup): {[key: string]: any} => {
//       const valor1 = group.controls[key];
//       const valor2 = group.controls[confirmKey];
//       if (valor1.value !== valor2.value) {
//         return {
//           mismatched: true
//         };
//       }
//     };
//   }
// import {FormControl} from '@angular/forms';


// export function matchOtherValidator(otherControlName: string) {

//   let thisControl: FormControl;
//   let otherControl: FormControl;

//   return function matchOtherValidate(control: FormControl) {

//     if (!control.parent) {
//       return null;
//     }

//     // Initializing the validator.
//     if (!thisControl) {
//       thisControl = control;
//       otherControl = control.parent.get(otherControlName) as FormControl;
//       if (!otherControl) {
//         throw new Error('matchOtherValidator(): No ha sido encontrado el otro control');
//       }
//       otherControl.valueChanges.subscribe(() => {
//         thisControl.updateValueAndValidity();
//       });
//     }

//     if (!otherControl) {
//       return null;
//     }

//     if (otherControl.value !== thisControl.value) {
//       return {
//         matchOther: true
//       };
//     }

//     return null;

//   };

// }



import { Directive, forwardRef, Attribute } from '@angular/core';
import { Validator, AbstractControl, NG_VALIDATORS } from '@angular/forms';

@Directive({
    selector: '[validateEqual][formControlName],[validateEqual][formControl],[validateEqual][ngModel]',
    providers: [
        { provide: NG_VALIDATORS,  useExisting: forwardRef(() => EqualValidator), multi: true},
    ]
})
export class EqualValidator implements Validator {
    constructor( @Attribute('validateEqual') public validateEqual: string,
                 @Attribute('reverse') public reverse: string) {

    }

    private get isReverse() {
        if (!this.reverse) { return false; }
        return this.reverse === 'true' ? true : false;
    }

    validate(c: AbstractControl): { [key: string]: any } {
        // self value
        const v = c.value;

        // control vlaue
        const e = c.root.get(this.validateEqual);

        // value not equal
        if (e && v !== e.value && !this.isReverse) {
          return {
            validateEqual: true
          };
        }

        // value equal and reverse
        if (e && v === e.value && this.isReverse) {
            delete e.errors['validateEqual'];
            if (!Object.keys(e.errors).length) { e.setErrors(null); }
        }

        // value not equal and reverse
        if (e && v !== e.value && this.isReverse) {
            e.setErrors({
                validateEqual: true
            });
        }

        return null;
    }
}