import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'getPipeBieMue',
  pure: true
})
export class GetPipeBieMue implements PipeTransform {

  transform(value: any, ...args: any[]): any {
    return this.getPipeBieMue(value);
  }

  getPipeBieMue(param: any): string {
    return param ? param.valor : 'POR DEFINIR ';
  }
}
