import {FormControl} from '@angular/forms';

export function validateRfcControl(control:FormControl):{[s:string]:boolean}{

    // RFC SIN HOMOCLAVE
    // var rfc = /^(([A-Z]{4})\d{2}((0[1-9]|1[012])(0[1-9]|1\d|2[0-8])|(0[13456789]|1[012])(29|30)|(0[13578]|1[02])31))$|^(([A-Z]{4})([02468][048]|[13579][26])0229)$/;
    // var rfc = /^([A-ZÑ\x26]{4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[A-Z\d]{3})?$/;
    var rfc = new RegExp('^(([A-Z]{4})\\d{2}((0[1-9]|1[012])(0[1-9]|1\\d|2[0-8])|(0[13456789]|1[012])(29|30)|(0[13578]|1[02])31))$|^(([A-Z]{4})([02468][048]|[13579][26])0229)$');
    

    if(control.value.length >12 || !control.value.match(rfc)){
      
        return {
            validateRfcControl:true
          }
  }
    return null;
}