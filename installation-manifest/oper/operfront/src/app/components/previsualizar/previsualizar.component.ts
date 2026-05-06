import { Component, OnInit, ElementRef, ViewChild, AfterViewInit, Input } from '@angular/core';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { first } from 'rxjs/operators';
import { Globals } from 'src/app/globals';
import { DeclaracionService } from 'src/app/services/declaracion.service';

@Component({
  selector: 'app-previsualizar',
  templateUrl: './previsualizar.component.html',
  styleUrls: ['./previsualizar.component.css']
})
export class PrevisualizarComponent implements OnInit, AfterViewInit {
   spinner: boolean;
   base64: string;
   height: '100%';
   zoom = 'page-width';
   navSafari: boolean;
   errorCarga: boolean;
  public noComprobante: string;
  public tipoDeclaracion: string;
  constructor(private readonly modalService: NgbModal, private readonly activeModal: NgbActiveModal,
              private readonly declaracionService: DeclaracionService,
              private readonly globals: Globals,) { 
                this.spinner = false;
                this.navSafari = false;
                this.errorCarga = false;
              }

    src: string;
    @ViewChild('preview') documentElement: ElementRef;
    @Input() public objPreview;
    @Input() public tipoImpresion;
    @Input() public titulo;
    @Input() public anio;
    @Input() public justificacion;
    comprobanteCadena; string;
    @Input() set comprobante(value: string) {
      this.comprobanteCadena = value;
      if (this.tipoImpresion === 'declaracion') {
        this.viewDeclaracion();
      } else if (this.tipoImpresion === 'acuse') {
        this.viewAcuse();
      }
    }

    ngAfterViewInit(){
      console.log('En busca del usuario perdido',this.globals);
      
    }

  ngOnInit(): void {
    const ua = navigator.userAgent.toLowerCase();
    // alert(ua);
    if (ua.indexOf('safari') !== -1) {
      if (ua.indexOf('chrome') > -1) {
        this.navSafari = false; // Chrome
      } else {
        this.navSafari = true; // Safari
      }
    }
    // alert(`safari: ${this.navSafari}`);
  }

  viewDeclaracion() {    
    const usuario = this.globals.usuario;
    // {
    //   "idUsuario" : 2000100,
    //   "nombre" : "EDER JAVIER",
    //   "primerApellido" : "VARGAS",
    //   "segundoApellido" : "RAMÍREZ",
    //   "curp" : "VARE880714HDFRMD03",
    //   "rfc" : "VARE880714",
    //   "homoclave" : "DH9",
    //   "email" : "ejvargas@funcionpublica.gob.mx"
    // };
    if (this.objPreview) {
      this.noComprobante = this.comprobanteCadena;
      this.tipoDeclaracion = `Declaracion de ${this.formatString(this.objPreview.tipoDeclaracion
        .replace('MODIFICACION', 'MODIFICACIÓN').replace('CONCLUSION', 'CONCLUSIÓN'))} ${this.objPreview.tipoDeclaracion === 'MODIFICACION' ? `${this.anio}` : '' }`;
      this.declaracionService.
        previewDeclaracion(this.objPreview, usuario, this.justificacion)
        .pipe(first())
        .subscribe(
          data => {
            if (data.status === 200) {
              console.log('fer64',data);
              
              // this.src = `data:application/pdf;base64, ${data.body}`;
              // this.documentElement.nativeElement.setAttribute('src', this.src);

              this.base64 = data.body.data;
              this.spinner = true;
            }
          },
          error => {
            this.errorCarga = true;
            this.spinner = true;
          }
        );
    }
  }

  viewAcuse() {    
    const usuario = this.globals.usuario;
    if (this.objPreview) {
      this.noComprobante = this.comprobanteCadena;
      this.tipoDeclaracion = `Declaracion de ${this.formatString(this.objPreview.tipoDeclaracion
        .replace('MODIFICACION', 'MODIFICACIÓN').replace('CONCLUSION', 'CONCLUSIÓN'))} ${this.objPreview.tipoDeclaracion === 'MODIFICACION' ? `${this.anio}` : '' }`;
      this.declaracionService.
        previewAcuse(this.objPreview, usuario, this.justificacion)
        .pipe(first())
        .subscribe(
          data => {
            if (data.status === 200) {
              console.log('fer64',data);
              this.base64 = data.body.data;
              this.spinner = true;
            }
          },
          error => {
            this.errorCarga = true;
            this.spinner = true;
          }
        );
    }
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
    a.download = `${this.noComprobante}.pdf`;
    // a.click();
    window.open(url);
  }

  close() {
    this.activeModal.close();
  }

  formatString(str) {
    return str
    .replace(/(\B)[^ ]*/g ,match => (match.toLowerCase()))
    .replace(/^[^ ]/g, match => (match.toUpperCase()));
  }
  download(filename, text) {
    // const element = document.createElement('a');
    // element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
    // element.setAttribute('download', filename);

    // element.style.display = 'none';
    // document.body.appendChild(element);

    // element.click();

    // document.body.removeChild(element);
  }

  imprimeSafari1() {  
    this.download('hello.txt', 'This is the content of my file :)');
  }

}
