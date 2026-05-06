import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'pipeTipoInversion',
  pure: true
})
export class PipeTipoInversionPipe implements PipeTransform {

  // transform(value: any, ...args: any[]): any {
  //   return null;
  // }
  transform(value: any, ...args: any[]): any {
    return this.getPipeTipoInversion(value);
  }

  getPipeTipoInversion(param: any): string {
    return param ? param.valor : 'POR DEFINIR ';
  }

}
