import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'getPipeAde2',
  pure: true
})
export class GetPipeAde2 implements PipeTransform {

  transform(value: any, ...args: any[]): any {
    return this.getPipeAde2(value);
  }

  getPipeAde2(param: any): string {
    console.log("param ", param);
    if (param != null) {
      if (param.tipoPersona == 'PERSONA_FISICA') {
        return param.personaFisica.nombre = param.tipoPersona == 'PERSONA_FISICA' && param.personaFisica.nombre == null ? param.personaFisica.nombre = 'POR DEFINIR' : param.personaFisica.nombre;
      }
      if (param.tipoPersona == 'PERSONA_MORAL') {
        return param.personaMoral.nombre = param.tipoPersona == 'PERSONA_MORAL' && param.personaMoral.nombre == null ? param.personaMoral.nombre = 'POR DEFINIR' : param.personaMoral.nombre;
      }
    } else {
      return param = 'POR DEFINIR';
    }
  }
}
