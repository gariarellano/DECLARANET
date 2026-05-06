import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'getPipeNonmbreDependientePipe',
  pure: true
})
export class GetPipeNonmbreDependientePipe implements PipeTransform {

  transform(value: any, ...args: any[]): any {
    return this.getPipeNonmbreDependientePipe(value);
  }

  getPipeNonmbreDependientePipe(param: any): string {
    console.log("paramFerst ", param);
    return param ? param : 'POR DEFINIR ';
  }
}