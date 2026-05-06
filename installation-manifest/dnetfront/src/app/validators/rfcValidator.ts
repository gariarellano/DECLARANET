import {FormControl} from '@angular/forms';

export function validateRfc(control:FormControl):{[s:string]:boolean}{
    
    var rfc = /^([A-ZÑ\x26]{3,4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1]))((-)?([A-Z\d]{3}))?$/;
    var rfc = new RegExp('([A-ZÑ\x26]{4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1]))((-)?([A-Z\d]{3}))?');



  if(control.value.length > 10 || !control.value.match(rfc)){
      
        return {
            validateRfc:true
          }
  }
    return null;
}