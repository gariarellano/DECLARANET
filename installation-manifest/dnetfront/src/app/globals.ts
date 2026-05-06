import { Injectable } from '@angular/core';
import { AbstractControl } from '@angular/forms';

export const timeTouchEvent = 500

@Injectable()
export class Globals {
  systemLog: any;
  declaracionLoaded: any;
  catalogos: any;
  flagScroll: boolean;
  enums: any;
  catalogoMun: any;
  catalogoEntes: any;
  fechaReferenciaMax: Date;
  textoSaldos: string;
  textoTitulos: string;
  textSubTitleEmpleo: string;
  component: string;
  flagPresentar1Vez: boolean;
  errorComponentText: string;
  precargaOracle: boolean;
  objIniciaFirma: {
    digest: string;
    rfc: string;
  };

  compareFn(c1: any, c2: any): boolean {
    return c1 && c2 ? c1.id === c2.id : c1 === c2;
  }


  getTipoOperacion(valid: boolean, regHis: boolean, tipoOperacion: string, seccion: string) { // seccion: P patrimonial, G generales/personales, I intereses
    if (!tipoOperacion || tipoOperacion === 'SIN_CAMBIO') {
      if (valid && regHis) {
        return this.enums.ENUM_TIPO_OPERACION.filter(( obj ) => {
          return obj.id !== 'AGREGAR';
        });
      }
      if (!valid && regHis) {
        return this.enums.ENUM_TIPO_OPERACION.filter(x => x.id === 'MODIFICAR' || x.id === 'BAJA');
      }
      if (!regHis) {
        if (this.declaracionLoaded.encabezado.tipoDeclaracion === 'INICIO' || seccion !== 'P') {
          return this.enums.ENUM_TIPO_OPERACION.filter(x => x.id === 'AGREGAR');
        } else {
          return this.enums.ENUM_TIPO_OPERACION.filter(x => x.id === 'AGREGAR' || x.id === 'BAJA');
        }
      }
    } else {
      return this.enums.ENUM_TIPO_OPERACION.filter(x => x.id === tipoOperacion);
    }
    return this.enums.ENUM_TIPO_OPERACION;
  }

  isNumber(value: string | number): boolean
{
   return ((value != null) &&
           (value !== '') &&
           !isNaN(Number(value.toString())));
}

verificaNivelJerarquico() {
  const ente = sessionStorage.getItem('enteReceptor') ? JSON.parse(sessionStorage.getItem('enteReceptor')) : null;
  if (ente.parametrosEspecificos && ente.parametrosEspecificos.noMuestraNivelJerarquico) {
      const idNiveles = ente.parametrosEspecificos.noMuestraNivelJerarquico;
      idNiveles.forEach(element => {
        const i = this.catalogos.CAT_NIVEL_JERARQUICO.map(e => e.id).indexOf(element);
        if(i !== -1) {
          this.catalogos.CAT_NIVEL_JERARQUICO.splice(i, 1);
        }
      });
  }
}


utf8_decode(strData) { // eslint-disable-line camelcase
  //  discuss at: https://locutus.io/php/utf8_decode/
  // original by: Webtoolkit.info (https://www.webtoolkit.info/)
  //    input by: Aman Gupta
  //    input by: Brett Zamir (https://brett-zamir.me)
  // improved by: Kevin van Zonneveld (https://kvz.io)
  // improved by: Norman "zEh" Fuchs
  // bugfixed by: hitwork
  // bugfixed by: Onno Marsman (https://twitter.com/onnomarsman)
  // bugfixed by: Kevin van Zonneveld (https://kvz.io)
  // bugfixed by: kirilloid
  // bugfixed by: w35l3y (https://www.wesley.eti.br)
  //   example 1: utf8_decode('Kevin van Zonneveld')
  //   returns 1: 'Kevin van Zonneveld'

  const tmpArr = [];
  let i = 0;
  let c1 = 0;
  let seqlen = 0;

  strData += '';

  while (i < strData.length) {
    c1 = strData.charCodeAt(i) & 0xFF;
    seqlen = 0;

    // https://en.wikipedia.org/wiki/UTF-8#Codepage_layout
    if (c1 <= 0xBF) {
      c1 = (c1 & 0x7F);
      seqlen = 1;
    } else if (c1 <= 0xDF) {
      c1 = (c1 & 0x1F);
      seqlen = 2;
    } else if (c1 <= 0xEF) {
      c1 = (c1 & 0x0F);
      seqlen = 3;
    } else {
      c1 = (c1 & 0x07);
      seqlen = 4;
    }

    for (let ai = 1; ai < seqlen; ++ai) {
      c1 = ((c1 << 0x06) | (strData.charCodeAt(ai + i) & 0x3F));
    }

    if (seqlen === 4) {
      c1 -= 0x10000;
      tmpArr.push(String.fromCharCode(0xD800 | ((c1 >> 10) & 0x3FF)));
      tmpArr.push(String.fromCharCode(0xDC00 | (c1 & 0x3FF)));
    } else {
      tmpArr.push(String.fromCharCode(c1));
    }

    i += seqlen;
  }

  return tmpArr.join('');
}


htmlDecode(input){
  const e = document.createElement('textarea');
  e.innerHTML = input;
  // handle case of empty input
  return e.childNodes.length === 0 ? '' : e.childNodes[0].nodeValue;
}
}
