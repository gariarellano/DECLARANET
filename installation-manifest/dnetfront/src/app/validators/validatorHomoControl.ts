import {FormControl} from '@angular/forms';

export function validateHomoControl(control:FormControl):{[s:string]:boolean}{

    // RFC SIN HOMOCLAVE
    var homoclave = /^([A-Z0-9]{2})([A|a|0-9]{1})$/;
    var homoclave = new RegExp('([A-Z0-9]{2})([A|a|0-9]{1})');
    

  if(control.value.length >3 || !control.value.match(homoclave)){
      
        return {
            validateHomoControl:true
          }
  }
    return null;
}