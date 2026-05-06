import {FormControl} from '@angular/forms';

export function validateEmail(control:FormControl):{[s:string]:boolean}{

    // var email = new RegExp('[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[-.:;?¡]*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.][a-zA-Z]{1,5}');
    // var email = new RegExp('/^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;');
    const email = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;


    if(control.value) {
     if (!control.value.match(email)) {
        return {
            validateEmail: true
        }
    }
  }
    return null;
}
