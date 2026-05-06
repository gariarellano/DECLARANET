import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'getPipeVeh',
  pure: true
})
export class GetPipeVeh implements PipeTransform {

  transform(value: any, ...args: any[]): any {
    return this.getPipeVeh(value);
  }

  getPipeVeh(param: any): string {
    return param ? param.valor : 'POR DEFINIR ';
  }
}
