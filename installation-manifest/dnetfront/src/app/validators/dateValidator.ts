import {FormControl} from '@angular/forms';

export function validateDate(minDate: string, maxDate: string) {
  return (control: FormControl): { [s: string]: boolean } => {
    if (!control.value || control.value === ''){
      return null;
    }
    console.log(new Date(control.value).toUTCString());

    const valDate = new Date(new Date(control.value).toUTCString());
    const minfecha = new Date(minDate);
    const maxfecha = new Date(maxDate);

    if (valDate < minfecha) {
          return {
            validateDate: true
          };
    }
    if (valDate > maxfecha) {
          return {
            validateDate: true
          };
    }
    return null;
    };
  }
