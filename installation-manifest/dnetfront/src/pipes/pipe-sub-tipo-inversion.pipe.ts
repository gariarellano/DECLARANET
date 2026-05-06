import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'pipeSubTipoInversion',
  pure: true
})
export class PipeSubTipoInversionPipe implements PipeTransform {

  transform(value: any, ...args: any[]): any {
    return this.getPipeSubTipoInversion(value);
  }

  getPipeSubTipoInversion(params: any): string {
    if(params)console.log("params ",params.valor);
    
    return params ?params.valor : 'POR DEFINIR ';
  }

}
