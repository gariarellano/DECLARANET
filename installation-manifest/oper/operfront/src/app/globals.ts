import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root',
})
export class Globals {
    /* Información de la sesión */
    tokenInicial: any;


    // user: string;
    // curp: string;
    usuario: any;
    permisos: Array<any>;
    profiles: Array<any>;
    profile: any;
    institutions: Array<any>;
    institution: any;
    menu: Array<any> = [];

    component: string;


    itemsPerPageLabel = 'Registros por página:';
    nextPageLabel = 'Siguiente página';
    previousPageLabel = 'Página anterior';
    firstPageLabel = 'Primer página';
    lastPageLabel = 'Última página';

    clean() {
        this.usuario = undefined;
        this.profiles = undefined;
        this.institutions = undefined;
        this.removeProfile();
    }

    removeProfile() {
        this.profile = undefined;
        this.institution = undefined;
        this.permisos = undefined;
        this.menu = undefined;
    }

     spanishRangeLabel(page: number, pageSize: number, length: number) {
        if (length === 0 || pageSize === 0) { return `0 de ${length}`; }
        length = Math.max(length, 0);
        const startIndex = page * pageSize;
        // If the start index exceeds the list length, do not try and fix the end index to the end.
        const endIndex = startIndex < length ?
            Math.min(startIndex + pageSize, length) :
            startIndex + pageSize;
        return `${startIndex + 1} - ${endIndex} de ${length}`;
      }

      getValueSession(_property) {
        return this[_property];
      }
    
      setValueSession(_property, _value) {
        this[_property] = _value;
      }
      compareFn(c1: any, c2: any): boolean {
        return c1 && c2 ? c1.id === c2.id : c1 === c2;
      }
      pick(obj, keys) {
        return keys.map(k => k in obj ? {
          [k]: obj[k]
        } : {})
          .reduce((res, o) => Object.assign(res, o), {});
      }
      clearObj(obj): any {
        for (const prop in obj) {
          if (obj[prop] === null || obj[prop] === undefined){
            delete obj[prop];
          }
        }
        return obj;
      }

}
