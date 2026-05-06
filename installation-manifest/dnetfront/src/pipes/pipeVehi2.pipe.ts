import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'getPipeVehD',
  pure: true
})
export class GetPipeVehD implements PipeTransform {

  transform(value: any, ...args: any[]): any {
    return this.getPipeVehD(value);
  }

  getPipeVehD(param: any): string {

    return param ? param : 'POR DEFINIR ';
  }
}