import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'pipeSubTipoInversion2',
  pure: true
})
export class PipeSubTipoInversionPipe2 implements PipeTransform {

  transform(value: any, ...args: any[]): any {
    return this.getPipeSubTipoInversion2(value);
  }

  getPipeSubTipoInversion2(subtipo: any): string {
    if(subtipo){console.log("params subtipo inver",subtipo);}
    
    return subtipo ? subtipo.valor : 'POR DEFINIR';
  }
 
}
