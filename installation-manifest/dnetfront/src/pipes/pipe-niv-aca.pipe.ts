import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'getPipeNivAcaPipe',
  pure: true
})
export class GetPipeNivAcaPipe implements PipeTransform {

  transform(value: any, ...args: any[]): any {
    return this.getPipeNivAcaPipe(value);
  }

  getPipeNivAcaPipe(nivAca: any): string {
    return nivAca ? nivAca.valor : 'POR DEFINIR ';
  }
}
