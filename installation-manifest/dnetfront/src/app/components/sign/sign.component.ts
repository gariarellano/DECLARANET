import { Component, OnInit, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { NgbModal, NgbModalConfig } from '@ng-bootstrap/ng-bootstrap';
import { environment } from 'src/environments/environment';
import { SessionService } from '../../services/session.service';
import { DatosPersonales } from '../../interfaces/datosPersonales';
import { IdpService } from '../../services/idp.service';
import { first } from 'rxjs/operators';
import { NgForm } from '@angular/forms';
import { AlertService } from '../../services/alert.service';
import { Router } from '@angular/router';
import { Declaracion } from 'src/app/interfaces/declaracion';
import { DeclaracionService } from '../../services/declaracion.service';
import { Globals } from '../../globals';
import { PreviewComponent } from '../modals/preview/preview.component';
import { LocationStrategy } from '@angular/common';

declare var setSignValue: any;
declare var getValuesTransaction: any;
declare var $: any;
declare const prepareMenuCuenta: any;
@Component({
  selector: 'app-sign',
  templateUrl: './sign.component.html',
  styleUrls: ['./sign.component.scss']
})
export class SignComponent implements OnInit, AfterViewInit {
  cl: string;
  sk: string;
  pswd: string;
  curp: string;
  usuario: DatosPersonales;
  submitted = false;
  loading: boolean;
  declaracionLoaded: any;
  idUsu: number;
  numeroDeclaracion: string;
  tipoDeclaracion: string;
  collName: number;
  digest: string;
  rfc: string;
  firmaRealizada: boolean;
  modalRef: any;
  aceptado: boolean;
  institucionReceptora: any;
  prefijoInstitucion: string;
  errorFirma: string;
  notaTemp: any;
  preventDuplicate: boolean;
  pintaFirmaFIEL: boolean;
  // pruebaImpresion = true;
  @ViewChild('modalFIELButton') modalFIELButton: ElementRef;
  @ViewChild('modalFielNotaButton') modalFielNotaButton: ElementRef;
  @ViewChild('modalFUPButton') modalFUPButton: ElementRef;
  @ViewChild('formFirmaFUP') formFirmaFUP: NgForm;

  constructor(private elementRef: ElementRef,
    private sessionService: SessionService,
    private idpService: IdpService,
    private alertService: AlertService,
    private router: Router,
    private declaracionService: DeclaracionService,
    private globals: Globals,
    config: NgbModalConfig,
    private modalService: NgbModal,
    private locationStrategy: LocationStrategy
  ) {
    this.loading = false;
    this.cl = `${environment.ID_CLIENTE_SIGN}`;
    this.sk = `${environment.SECRET_KEY_SIGN}`;
    this.pintaFirmaFIEL = this.cl.trim() !== '' || this.sk.trim() !== '';
    if (!this.sessionService.currentUserValue) {
      this.logout();
    }
    // if (this.pruebaImpresion !== true) {
    this.declaracionLoaded = JSON.parse(JSON.stringify(this.globals.declaracionLoaded)) || JSON.parse(sessionStorage.getItem('declaracionLoaded'));
    // }
    this.firmaRealizada = false;
    this.institucionReceptora = JSON.parse(sessionStorage.getItem('institucionReceptora'));
    this.prefijoInstitucion = JSON.parse(sessionStorage.getItem('enteReceptor')).prefijo.toLowerCase();
    this.blockBackButton();
  }

  private blockBackButton() {
    history.pushState(null, null, window.location.href);
    this.locationStrategy.onPopState(() => {
      history.pushState(null, null, window.location.href);
    });
  }

  ngOnInit() {
    this.globals.component = '070809';
    console.log(JSON.stringify(this.sessionService.currentUserValue.datosPersonales));
    this.usuario = this.sessionService.currentUserValue.datosPersonales;
    prepareMenuCuenta();

    this.idUsu = this.declaracionLoaded.encabezado.usuario.idUsuario;
    this.numeroDeclaracion = this.declaracionLoaded.encabezado.numeroDeclaracion;
    this.collName = this.declaracionLoaded.institucionReceptora.collName;
    this.tipoDeclaracion = this.declaracionLoaded.encabezado.tipoDeclaracion;

    this.digest = this.globals.objIniciaFirma.digest;
    this.rfc = this.globals.objIniciaFirma.rfc;


  }

  getTransact1() {

    if (!this.preventDuplicate) {
      this.preventDuplicate = true;
    } else {
      return;
    }
    const t = JSON.parse($('#ngGetTransact').val());

    if (t.transaccion !== 'cancel') {
      // console.log(t);
      const obj = {
        token: null,
        tipoFirma: 'FIEL',
        transaccion: t.transaccion
      };
      this.validaFirmado(obj);

    } else {
      $('#modalFIEL').modal('hide');
    }



  }

  firmaFIEL() {
    this.preventDuplicate = false;
    setSignValue({
      cadena: this.digest,
      rfc: this.rfc
    });
    this.modalFIELButton.nativeElement.click();
    $('#modalFIEL').modal('show');
  }



  preparaFirmaFUP() {

    this.idpService.Authorize()
      .pipe(first())
      .subscribe(
        data => {
          if (data.status === 200) {
            sessionStorage.setItem('authIdp', data.headers.get('Authorization'));

            this.formFirmaFUP.reset();
            $('#modalFUP').modal('show');
            // this.modalFUPButton.nativeElement.click();

          }
        },
        error => {

        }
      );
  }

  firmarFUP() {
    this.loading = true;
    if (this.formFirmaFUP.invalid) {
      this.loading = false;
      return;
    }
    this.submitted = true;
    this.idpService.login(this.usuario.curp, this.pswd)
      .pipe(first())
      .subscribe(
        data => {
          if (data.status === 200) {
            switch (data.body.codigo) {
              case 1:
                this.alertService.success(
                  'Firmado correctamente, validando, espere...'
                );
                const obj = {
                  token: data.headers.get('Authorization'),
                  tipoFirma: 'FUP',
                  transaccion: data.body.transaccion
                };
                this.validaFirmado(obj);
                break;
              case 3:
                this.alertService.error('Contraseña incorrecta');
                this.loading = false;
                break;
              default:
                this.alertService.error('Ha ocurrido un error, codigo de usuario: ' + data.body.codigo);
                this.loading = false;
                break;
            }
          }
        },
        error => {
          this.alertService.error('Error en la api de firmado FUP');
          this.loading = false;
        }
      );
  }

  ngAfterViewInit() {
    if (this.pintaFirmaFIEL) {
      if (!document.getElementById('sfpSignScript')) {
        const s = document.createElement('script');
        s.type = 'text/javascript';
        s.src = this.getSrcSign();;
        s.id = 'sfpSignScript';
        this.elementRef.nativeElement.appendChild(s);

      }

      if (!document.getElementById('sfpSignScript1')) {
        const s1 = document.createElement('script');
        s1.text = `
    function getTransact() {
      var t = getValuesTransaction();
      $('#ngGetTransact').val(t.bodyReq);
      $('#getTransactButton').click();
      console.log(t);

    }
  `;
        s1.id = 'sfpSignScript1';
        this.elementRef.nativeElement.appendChild(s1);
      }
    }
  }

  validaFirmado(obj: any) {
    let declaracion: any;
    declaracion = {
      tipoFirma: obj.tipoFirma,
      transaccion: obj.transaccion,
      token: obj.token,
      rfc: this.rfc,
      numeroDeclaracion: this.numeroDeclaracion,
      idUsuario: this.idUsu,
      collName: this.collName,
      digest: this.digest
    };
    this.declaracionService.validaFirmaDeclaracion(declaracion)
      .pipe(first())
      .subscribe(
        data => {
          if (data.status === 200) {
            console.log(data);
            if (data.statusText === 'OK') {
              this.firmaRealizada = true;
              $('#modalFIEL').modal('hide');
              $('#modalFUP').modal('hide');

            }
          } else {
            console.log(data.body.mensajeError);
            this.errorFirma = `Ha ocurrido un error al realizar la firma, mensaje: ${data.body.mensajeError}`;
            $('#ModalError').modal('show');
            $('#modalFIEL').modal('hide');
            $('#modalFUP').modal('hide');
            this.loading = false;
          }
        },
        error => {
          this.errorFirma = `Ha ocurrido un error al realizar la firma, mensaje: ${error.error.mensajeError}`;
          $('#ModalError').modal('show');
          $('#modalFIEL').modal('hide');
          $('#modalFUP').modal('hide');
          this.loading = false;
        }
      );

  }

  previsualizar(tipoImpresion: string, titulo: string) {
    this.modalRef = this.modalService.open(PreviewComponent, {
      centered: true,
      windowClass: 'previewWindow'
    });
    this.modalRef.componentInstance.objPreview = {
      numeroDeclaracion: this.numeroDeclaracion,
      idUsuario: this.idUsu,
      collName: this.collName,
      tipoDeclaracion: this.tipoDeclaracion
    };
    this.modalRef.componentInstance.tipoImpresion = tipoImpresion;
    this.modalRef.componentInstance.titulo = titulo;


  }


  declaracion() {
    this.router.navigate(['/inicio']);
  }
  aviso() {
    this.router.navigate(['/aviso']);
  }

  logout() {
    const navOut = sessionStorage.getItem('enteReceptor') ? JSON.parse(sessionStorage.getItem('enteReceptor')).enteContext || '' : '';
    this.sessionService.logout();
    this.router.navigate(['/' + navOut]);
  }

  home() {
    this.router.navigate(['/inicio']);
  }
  nota() {
    this.router.navigate(['/nota']);
  }

  getSrcSign(): string {
    let scriptPath;
    switch (environment.API_SIGN) {
      case 'PRODUCTION':
        scriptPath = environment.API_SIGN_PROD;
        break;
      case 'PRODUCTION_PRES':
        scriptPath = environment.API_SIGN_PROD_PRES;
        break;
      case 'STAGING':
        scriptPath = environment.API_SIGN_STAGING;
        break;
      case 'REVIEW':
        scriptPath = environment.API_SIGN_REVIEW;
        break;
      case 'LOCALHOST':
        scriptPath = environment.API_SIGN_LOCALHOST;
        break;
      default:
        scriptPath = 'NoSignScriptData';
        break;
    }
    return scriptPath;;
  }
}
