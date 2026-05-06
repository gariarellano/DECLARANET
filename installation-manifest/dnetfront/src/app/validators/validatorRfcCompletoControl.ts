import {FormControl} from '@angular/forms';

export function validateRfcCompletoControl(control:FormControl):{[s:string]:boolean}{

    // RFC SIN HOMOCLAVE
    // var rfc = /^(([A-Z]{4})\d{2}((0[1-9]|1[012])(0[1-9]|1\d|2[0-8])|(0[13456789]|1[012])(29|30)|(0[13578]|1[02])31))$|^(([A-Z]{4})([02468][048]|[13579][26])0229)$/;
    // var rfc = /^([A-ZÑ\x26]{4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[A-Z\d]{3})?$/;
    var rfcCompleto = new RegExp('^(([A-Z|a-z]{3})\\d{2}((0[1-9]|1[012])(0[1-9]|1\\d|2[0-8])|(0[13456789]|1[012])(29|30)|(0[13578]|1[02])31)(\\w{2})([A|a|0-9]{1}))$|^(([A-Z|a-z]{3})([02468][048]|[13579][26])0229)(\\w{2})([A|a|0-9]{1})$');
    

    if(control.value.length >0 && !control.value.match(rfcCompleto)){
      
        return {
            validateRfcCompletoControl:true
          }
  }
    return null;
}