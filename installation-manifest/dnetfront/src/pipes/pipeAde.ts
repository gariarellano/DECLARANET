import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'getPipeAde',
  pure: true
})
export class GetPipeAde implements PipeTransform {

  transform(value: any, ...args: any[]): any {
    return this.getPipeAde(value);
  }

  getPipeAde(param: any): string {
    return param ? param.valor : 'POR DEFINIR ';
  }
}
