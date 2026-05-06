import { Validator, AbstractControl, NG_VALIDATORS } from '@angular/forms';
import { Directive } from '@angular/core';

@Directive({
    selector: '[appSelectValidator]',
    providers: [{
        provide: NG_VALIDATORS,
        useExisting: SelectRequiredValidatorDirective,
        multi: true
    }]
})
export class SelectRequiredValidatorDirective implements Validator {
    validate(control: AbstractControl): { [key: string]: any } | null {
        // console.log(control.value);
        return control.value === null || control.value.id === null ? { defaultSelected: true } : null;
    }
}