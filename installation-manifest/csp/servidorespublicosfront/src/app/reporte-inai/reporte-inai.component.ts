import { Component, OnInit } from '@angular/core';
import { DeclaracionService } from '../routing/declaracion.service';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { AppComponent } from '../app.component';
import { EnteReceptorService } from '../routing/servicioentes.service';

@Component({
  selector: 'app-reporte-inai',
  templateUrl: './reporte-inai.component.html',
  styleUrls: ['./reporte-inai.component.css']
})
export class ReporteInaiComponent implements OnInit {
  enteImage: string;
  nombreEnte: string;
  flagEnte: boolean;
  spinnerText = 'Cargando historial de declaraciones, por favor espera...';
  spinner = {
    fgsType: 'square-jelly-box',
    fgsColor: '#902444',
  };
  dl: string;
  base64: string;
  height: '100%';
  zoom = 'page-width';
  navSafari: boolean;
  errorCarga: boolean;
  cargaFinalizada: boolean;
  fileDownload = 'declaracion';

  constructor(private readonly declaracionService: DeclaracionService,
              private readonly ngxService: NgxUiLoaderService,
              private readonly enteReceptorService: EnteReceptorService,
              private readonly router: Router,
              private route: ActivatedRoute,
              public myapp: AppComponent) {
    this.navSafari = false;
    this.errorCarga = false;
    this.spinnerText = 'Preparando declaracion, por favor espera...';
    this.ngxService.start();
    this.dl = encodeURIComponent(this.route.snapshot.queryParamMap.get('dI'));
    console.log(this.dl);
  
  }


  ngOnInit(): void {
    let ente: any = null;
    ente = this.route.snapshot.params.ente || null;
    this.myapp.sfpEnteContext(ente);

    this.enteReceptorService
    .access(ente)
    .pipe(first())
    .subscribe(
      data => {

        if (data.status === 200) {
          console.log(data.body);
          this.enteImage = data.body.frontInfo.imagenB64;
          sessionStorage.setItem('enteImage', JSON.stringify(this.enteImage));
          this.nombreEnte = data.body.frontInfo.nombre;
          this.ngxService.stop();

          if (!this.nombreEnte) {
            this.router.navigate(['/error'], { queryParams: { error: 'badEnte' } });
          }
          sessionStorage.setItem('institucionReceptora', JSON.stringify(data.body.institucionReceptora));
          const enteReceptor = {
            enteImage: data.body.frontInfo.imagenB64,
            enteContext: ente,
            prefijo: data.body.frontInfo.prefijo
          };
          sessionStorage.setItem('enteReceptor', JSON.stringify(enteReceptor));
          this.flagEnte = true;

        }
      },
      error => {
        this.ngxService.stop();
        alert('Error de comunicación... Favor de intentarlo mas tarde.');

      }
    );

    const ua = navigator.userAgent.toLowerCase();
    // alert(ua);
    if (ua.indexOf('safari') !== -1) {
      if (ua.indexOf('chrome') > -1) {
        this.navSafari = false; // Chrome
      } else {
        this.navSafari = true; // Safari
      }
    }
    this.viewDeclaracion();
  }


  viewDeclaracion() {

      this.declaracionService.
        previewDeclaracionInai(this.dl)
        .pipe(first())
        .subscribe(
          data => {
            if (data.status === 200) {
              // this.src = `data:application/pdf;base64, ${data.body}`;
              // this.documentElement.nativeElement.setAttribute('src', this.src);
              
              this.base64 = data.body;
              this.cargaFinalizada = true;
              this.ngxService.stop();
            }
          },
          error => {
            this.errorCarga = true;
            this.ngxService.stop();
          }
        );
  }

  imprimeSafari() {
    const a = document.createElement('a');


    // decode base64 string, remove space for IE compatibility
    const binary = atob(this.base64.replace(/\s/g, ''));
    const len = binary.length;
    const buffer = new ArrayBuffer(len);
    const view = new Uint8Array(buffer);
    for (let i = 0; i < len; i++) {
        view[i] = binary.charCodeAt(i);
    }

    // create the blob object with content-type "application/pdf"
    const blob = new Blob( [view], { type: 'application/pdf' });
    const url = URL.createObjectURL(blob);

    // a.href = `data:application/pdf;base64, ${this.base64}`;
    a.href = url;
    a.target = '_blank';
    // a.download = `${this.noComprobante}.pdf`;
    a.download = `prueba.pdf`;
    // a.click();
    window.open(url);
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
  

}
