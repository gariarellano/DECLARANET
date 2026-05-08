import { Component, OnInit, ViewChild, Output, EventEmitter, ElementRef, Input } from '@angular/core';

import { NgbModal, NgbModalConfig } from '@ng-bootstrap/ng-bootstrap';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { Globals } from 'src/app/globals';
import { Encabezado } from '../../interfaces/encabezados';
import { first } from 'rxjs/operators';
import { DeclaracionService } from 'src/app/services/declaracion.service';
import { SessionService } from '../../services/session.service';
import { Idle } from '@ng-idle/core';
import { DatosPersonales } from '../../interfaces/datosPersonales';
import { CatalogoService } from '../../services/catalogo.service';
import { PreviewComponent } from '../modals/preview/preview.component';
import { HistorialNotasComponent } from '../modals/historial-notas/historial-notas.component';
import { EnteReceptorService } from '../../services/ente-receptor.service';
import { LocationStrategy } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import * as moment from 'moment';
import { NgxUiLoaderService } from 'ngx-ui-loader';
declare var $: any;
declare const prepareMenuCuenta: any;

@Component({
  selector: 'app-start',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.scss'],
  providers: [NgbModalConfig, NgbModal]
})

export class StartComponent implements OnInit {
  @ViewChild('formPresentar') formPresentar: NgForm;
  @ViewChild('modalError') modalError: ElementRef;
  @Output() eliminar = new EventEmitter();
  @Output() prueba = new EventEmitter();
  fecha: any;
  fechaMin: string = "01-01-2015";
  initLoad: any;
  filteredAnios: any;
  filteredAniosAnt: any;
  declaracionPendiente: boolean;
  fechaMaxima: string;
  tipoDeclara: {
    id: string;
    anio: string;
    fechaEncargo: Date;
    tipoFormato: string;
    nivelJerarquico: any;
  };
  modalErrorMsj: string;
  modalErrorMsjArr: any;
  usuario: DatosPersonales;
  today: any;
  minAviso: any;
  numeroDeclaracion: any;
  historial: any;
  historialCargado: boolean;
  modalRef: any;
  enteImage: string;
  presentando: boolean;
  msjPresentarDelaracion: string;
  flagGetCatalogos: boolean;
  flagGetMuns: boolean;
  flagGetInstituciones: boolean;
  spinnerText: string;
  spinner = {
    fgsType: 'fading-circle',
    fgsColor: '#ae0c0c',
  };
  constructor(private router: Router,
    public globals: Globals,
    private declaracionService: DeclaracionService,
    private sessionService: SessionService,
    private idle: Idle,
    private modalService: NgbModal,
    private catalogoService: CatalogoService,
    private enteReceptorService: EnteReceptorService,
    private locationStrategy: LocationStrategy,
    private toastService: ToastrService,
    private readonly ngxService: NgxUiLoaderService,

  ) {
    this.fecha = {
      fechaMax: "",
      fechaMin: "2015-01-01"
    };
    this.spinnerText = 'Preparando entorno';
    this.ngxService.start();
    this.fecha = JSON.stringify(this.fecha);
    this.historialCargado = false;
    this.historial = null;
    this.filteredAniosAnt = null;
    if (!this.sessionService.currentUserValue) {
      this.logout();
    }
    this.enteImage = JSON.parse(sessionStorage.getItem('enteReceptor')).enteImage;
    this.blockBackButton();
  }

  private blockBackButton() {
    history.pushState(null, null, window.location.href);
    this.locationStrategy.onPopState(() => {
      history.pushState(null, null, window.location.href);
    });
  }

  ngOnInit() {
    this.fechaMaxima = "";
    //this.fechaMaxima = "30/03/1995";
    // this.flagGetCatalogos = false;
    // this.flagGetMuns = false;
    // this.flagGetInstituciones = false;
    this.globals.component = '010203';
    this.today = new Date().toJSON().split('T')[0];
    this.minAviso = new Date('05/01/2018').toJSON().split('T')[0];
    let institucionReceptora: any;
    this.tipoDeclara = {
      id: null,
      anio: null,
      fechaEncargo: null,
      tipoFormato: null,
      nivelJerarquico: { id: null }
    };

    this.usuario = this.sessionService.currentUserValue.datosPersonales;
    $('#modalNuevaDeclaracion').on('show.bs.modal', (e) => {
      this.formPresentar.reset();
    });

    $('#modalNuevaDeclaracion').on('hidden.bs.modal', function () {
      $(this).find('form').trigger('reset');
    });
    // console.log(this.globals.systemLog);
    if (sessionStorage.getItem('systemLog')) {
      this.initLoad = JSON.parse(sessionStorage.getItem('systemLog'));
      sessionStorage.setItem('initLoad', sessionStorage.getItem('systemLog'));
      sessionStorage.removeItem('systemLog');
      institucionReceptora = this.initLoad.declaracionPendiente === true ? this.initLoad.declaracion.institucionReceptora : this.initLoad.institucionReceptora;
    } else {
      this.initLoad = JSON.parse(sessionStorage.getItem('initLoad'));
      const enc = this.initLoad.declaracionPendiente === true ? this.initLoad.declaracion.encabezado : this.initLoad.encabezado;
      // tslint:disable-next-line: max-line-length
      institucionReceptora = this.initLoad.declaracionPendiente === true ? this.initLoad.declaracion.institucionReceptora : this.initLoad.institucionReceptora;
      // const datosGenerales =  this.initLoad.declaracionPendiente === true ? this.initLoad.declaracion.declaracion.datosGenerales : this.initLoad.datosGenerales;
      const datosPersonales = this.usuario;
      this.declaracionService
        .verificarDeclaracion(enc, institucionReceptora, datosPersonales)
        .pipe(first())
        .subscribe(
          data => {
            if (data.status === 200) {
              const respuesta = data.body.declaracion.respuesta;
              if (respuesta.estatus) {
                sessionStorage.setItem('initLoad', JSON.stringify(respuesta));
                this.initLoad = JSON.parse(sessionStorage.getItem('initLoad'));
                this.declaracionPendiente = this.initLoad.declaracionPendiente;
              } else {
                this.router.navigate(['/error']);
              }
            }
          },
          error => {

          }
        );



    }

    // this.historialDeclaracion();

    console.log("initLoad", this.initLoad);
    this.declaracionPendiente = this.initLoad.declaracionPendiente;

    prepareMenuCuenta();
    this.preparaCatalogos(institucionReceptora);

  }

  preparaCatalogos(institucionReceptora) {
 
      this.getCatalogos();
      this.getMuns();
      this.getInstituciones(institucionReceptora);
    
  }

  terminaProceso() {
    if (this.flagGetCatalogos && this.flagGetInstituciones && this.flagGetMuns) {
      this.ngxService.stop();

    }

  }

  mensajePresentarDeclaracion() {
    const ente = sessionStorage.getItem('enteReceptor') ? JSON.parse(sessionStorage.getItem('enteReceptor')) : null;
    if (ente.parametrosEspecificos && ente.parametrosEspecificos.hasOwnProperty('avisoNuevaDeclaracion') && ente.parametrosEspecificos.avisoNuevaDeclaracion !== null) {
      if (ente.parametrosEspecificos.avisoNuevaDeclaracion !== '') {
        this.msjPresentarDelaracion = this.globals.htmlDecode(ente.parametrosEspecificos.avisoNuevaDeclaracion);
        $('#modalPrevMsj').modal('show');
      } else {
        $('#modalNuevaDeclaracion').modal('show');

      }
 } else {
      this.msjPresentarDelaracion = `<p class="text-black">
      <b>A LAS PERSONAS SERVIDORAS PÚBLICAS DECLARANTES USUARIAS DEL SISTEMA Declara
      <span class="DnetRojo font-weight-bold">Net</span></b>
   </p>
   <p class="text-black">
       Para el año 2022 presentarán la <b>declaración “simplificada”</b>, cuando su nivel jerárquico sea <b>menor al de jefe de departamento</b>
   </p>
   <p class="text-black">
       Deberán presentar la <b>declaración "completa"</b>, cuando su nivel jerárquico sea <b>igual o
      mayor al de jefe de departamento</b>.
   </p>
   <p class="text-black">
       En caso de duda pueden dirigirse al área de recursos humanos de su dependencia, entidad o empresa productiva del Estado.
   </p>`;
      $('#modalPrevMsj').modal('show');
    }
  }

  historialDeclaracion() {
    if (!this.historial) {
      const institucionReceptora = this.initLoad.declaracionPendiente === true ? this.initLoad.declaracion.institucionReceptora : this.initLoad.institucionReceptora;
      const enc = this.initLoad.declaracionPendiente === true ? this.initLoad.declaracion.encabezado : this.initLoad.encabezado;

      this.declaracionService
        .historialDeclaracion(institucionReceptora, enc.usuario.idUsuario)
        .pipe(first())
        .subscribe(
          data => {
            if (data.status === 200) {
              console.log('Historial', data);
              this.historialCargado = true;
              if (data.body.declaracion.respuesta.historial) {
                this.historial = data.body.declaracion.respuesta.historial;
              }
            }
          },
          error => {

          }
        );
    }
  }

  presentarDeclaracion() {
    const f = this.formPresentar;
    if (!f.valid) {
      return;
    }


    this.presentando = true;
    // console.log(this.tipoDeclara);
    // console.log(this.tipoDeclara.fechaEncargo.toString());

    const enc: Encabezado = {
      numeroDeclaracion: null,
      tipoDeclaracion: this.tipoDeclara.id,
      // tslint:disable-next-line: max-line-length
      anio: this.tipoDeclara.id === 'MODIFICACION' ? Number(this.tipoDeclara.anio) : new Date(this.tipoDeclara.fechaEncargo).getUTCFullYear(),
      fechaEncargo: this.tipoDeclara.fechaEncargo ? this.tipoDeclara.fechaEncargo.toString() : null,
      // fechaEncargo: this.tipoDeclara.fechaEncargo.toString(),
      fechaRegistro: null,
      fechaActualizacion: null,
      usuario: this.initLoad.encabezado.usuario,
      tipoFormato: null,
      versionDeclaracion: this.initLoad.encabezado.versionDeclaracion,
      nivelJerarquico: this.tipoDeclara.nivelJerarquico
    };


    const institucionReceptora: any = JSON.parse(sessionStorage.getItem('institucionReceptora'));
    this.declaracionService
      .crearDeclaracion(enc, institucionReceptora, this.initLoad.datosPersonales)
      .pipe(first())
      .subscribe(
        data => {
          if (data.status === 200) {
            const respuesta = data.body.declaracion.respuesta;
            // this.globals.systemLog = data.body;
            if (respuesta.estatus) {
              this.globals.declaracionLoaded = respuesta.declaracion;
              console.log('datosAcomparar', this.globals.declaracionLoaded);
              console.log(this.globals.declaracionLoaded.fechaEncargoRusp);
              console.log(this.globals.declaracionLoaded.encabezado.fechaEncargo);
              // Eliminar se}ssion en produccion
              sessionStorage.setItem('declaracionLoaded', JSON.stringify(this.globals.declaracionLoaded));
              if (this.globals.declaracionLoaded.fechaEncargoRusp && this.globals.declaracionLoaded.encabezado.fechaEncargo
                && this.globals.declaracionLoaded.fechaEncargoRusp !== this.globals.declaracionLoaded.encabezado.fechaEncargo) {
                $('#confirmarFechaEncargo').modal('show');
                $('#modalNuevaDeclaracion').modal('hide');
              } else {
                this.abrirDeclaracion(false);
              }

              // console.log(data);
              this.globals.flagPresentar1Vez = true;
              // if (data.body.declaracion.respuesta.declaracion.encabezado.tipoDeclaracion == 'AVISO') {
              //   this.router.navigate(['/aviso']);
              // } else {
              //   this.router.navigate(['/declaracion']);
              // }
              // $('#modalNuevaDeclaracion').modal('hide');
            } else {
              // this.globals.errorComponentText = respuesta.mensaje;
              // this.router.navigate(['/error'], { queryParams: { error: 'badCustom' }});

              this.toastService.error(
                'Favor de validar.',
                respuesta.mensaje,
                {
                  timeOut: 2000,
                  extendedTimeOut: 800,
                  closeButton: true,
                  tapToDismiss: true,
                  progressBar: true
                }
              );
            }
          }
          this.presentando = false;
        },
        error => {
          $('#modalNuevaDeclaracion').modal('hide');
          this.modalError.nativeElement.click();
          this.modalErrorMsj = error.error.declaracion.respuesta.mensaje;
          this.modalErrorMsjArr = [];
          const errores = JSON.parse(error.error.declaracion.respuesta.error);
          console.log(errores);
          for (const err of errores) {
            const nombreCampo = err.nombreCampo;
            for (const e of err.listErrorMensajes) {
              this.modalErrorMsjArr.push({ error: `${nombreCampo}: ${e.mensaje}` });
            }
          }
          this.presentando = false;
        }
      );


    // this.router.navigate(['/declaracion']);
  }

  abrirDeclaracion(reemplazar: boolean) {
    if (reemplazar) {
      this.globals.declaracionLoaded.declaracion.datosEmpleoCargoComision.empleoCargoComision[0].fechaEncargo = this.globals.declaracionLoaded.encabezado.fechaEncargo;
    }
    if (this.globals.declaracionLoaded.hasOwnProperty('fechaEncargoRusp')) {
      delete this.globals.declaracionLoaded.fechaEncargoRusp;
    }
    console.log('decla after delete', this.globals.declaracionLoaded);
    if (this.globals.declaracionLoaded.encabezado.tipoDeclaracion == 'AVISO') {
      this.router.navigate(['/aviso']);
    } else {
      this.router.navigate(['/declaracion']);
    }
    $('#modalNuevaDeclaracion').modal('hide');
  }

  abrirPresentar() {
    $('#modalNuevaDeclaracion').modal('show');
    $('#confirmarFechaEncargo').modal('hide');
  }

  abrirModalEliminar() {

  }

  eliminarDeclaracion() {

    const enc = this.initLoad.declaracion.encabezado;
    const institucionReceptora = this.initLoad.declaracion.institucionReceptora;
    const datosPersonales = this.initLoad.datosPersonales;

    this.declaracionService
      .eliminarDeclaracion(enc, institucionReceptora, datosPersonales)
      .pipe(first())
      .subscribe(
        data => {
          if (data.status === 200) {
            const respuesta = data.body.declaracion.respuesta;
            // this.globals.systemLog = data.body;
            if (respuesta.estatus) {
              // Eliminar session en produccion
              sessionStorage.setItem('systemLog', JSON.stringify(respuesta));
              // console.log(data);
              this.ngOnInit();
            } else {
              this.router.navigate(['/error']);
            }
          }
        },
        error => {

        }
      );


    // this.router.navigate(['/declaracion']);
  }

  continuarDeclaracion(declaracion) {
    this.globals.declaracionLoaded = declaracion;
    //Eliminar session en produccion
    sessionStorage.setItem('declaracionLoaded', JSON.stringify(this.globals.declaracionLoaded));
    if (declaracion.encabezado.tipoDeclaracion == 'AVISO') {
      this.router.navigate(['/aviso']);
    } else {
      this.router.navigate(['/declaracion']);
    }

  }

  filterAniosAnt(anio) {
    if (anio === 'AniosAnterior') {
      this.presentando = true;
      const institucionReceptora = this.initLoad.declaracionPendiente === true ? this.initLoad.declaracion.institucionReceptora : this.initLoad.institucionReceptora;
      const enc = this.initLoad.declaracionPendiente === true ? this.initLoad.declaracion.encabezado : this.initLoad.encabezado;
      this.declaracionService
        .consultaAniosAntModificacion(enc, institucionReceptora)
        .pipe(first())
        .subscribe(
          data => {
            if (data.status === 200) {
              // console.log('Anios', data);
              this.filteredAniosAnt = data.body.declaracion.respuesta.anios;
              this.filteredAnios = this.filteredAniosAnt;
              this.tipoDeclara.anio = null;
            }
            this.presentando = false;
          },
          error => {
            this.presentando = false;

          }
        );
    }
  }
  filterAnios(id) {
    if (this.filteredAniosAnt === null) {
      this.filteredAnios = this.initLoad.declaracionAPresentar.find(item => item.idTipoDecla === id).anios;
    }
    this.tipoDeclara = {
      id: this.tipoDeclara.id,
      anio: null,
      fechaEncargo: null,
      tipoFormato: null,
      nivelJerarquico: { id: null }
    };
    console.log(this.filteredAnios);

  }
  renovar() {
    this.sessionService
      .renovarToken()
      .pipe(first())
      .subscribe(
        data => {
          if (data.status === 200) {
            console.log('Renovado');
            this.idle.watch();
          }
        },
        error => {
          this.sessionService.logout();
          this.router.navigate(['/']);
        }
      );
  }

  renovar1() {
    this.reset();
  }

  logout() {
    const navOut = sessionStorage.getItem('enteReceptor') ? JSON.parse(sessionStorage.getItem('enteReceptor')).enteContext || '' : '';
    this.sessionService.logout();
    this.router.navigate(['/' + navOut]);
  }
  reset() {
    this.idle.watch();

  }
  getCatalogos() {
    if (!this.globals.catalogos || !this.globals.enums) {
      this.catalogoService.getCatalogosAll()
        .pipe(first())
        .subscribe(
          data => {
            if (data.status === 200) {
              this.globals.catalogos = data.body.catalogos;
              this.globals.enums = data.body.enums;
              this.globals.verificaNivelJerarquico();
              this.flagGetCatalogos = true;
              this.terminaProceso();
            }
          },
          error => {
            this.ngxService.stop();

          }
        );
    } else {
      this.flagGetCatalogos = true;
      this.terminaProceso();
    }

  }
  getMuns() {
    if (!this.globals.catalogoMun) {
      this.catalogoService.getCatalogoMun()
        .pipe(first())
        .subscribe(
          data => {
            if (data.status === 200) {
              this.globals.catalogoMun = data.body.CAT_MUNICIPIO_ALCALDIA;
              console.log('catMun', data);
              this.flagGetMuns = true;
              this.terminaProceso();

            }
          },
          error => {
            this.ngxService.stop();

          }
        );
    } else {
      this.flagGetMuns = true;
      this.terminaProceso();
    }
  }

  getInstituciones(ente: any) {
    this.enteReceptorService.getInstituciones(ente)
      .pipe(first())
      .subscribe(
        data => {
          if (data.status === 200) {
            this.globals.catalogoEntes = data.body.catalogo;
            this.flagGetInstituciones = true;
            this.terminaProceso();
            // console.log('enteNuevo', data);
          }
        },
        error => {
          this.ngxService.stop();

        }
      );
  }

  previsualizar(tipoImpresion: string, titulo: string, numeroDeclaracion: string, idUsuario: number, collName: number, tipoDeclaracion: string) {
    this.modalRef = this.modalService.open(PreviewComponent, {
      centered: true,
      windowClass: 'previewWindow'
    });
    this.modalRef.componentInstance.objPreview = {
      numeroDeclaracion,
      idUsuario,
      collName,
      tipoDeclaracion
    };
    this.modalRef.componentInstance.tipoImpresion = tipoImpresion;
    this.modalRef.componentInstance.titulo = titulo;

  }

  prevNotas(obj: any) {
    this.modalRef = this.modalService.open(HistorialNotasComponent, {
      centered: true,
      windowClass: 'previewWindow'
    });
    const institucionReceptora: any = JSON.parse(sessionStorage.getItem('institucionReceptora'));
    this.modalRef.componentInstance.objNotas = {
      registro: obj,
      institucionReceptora,
      datosPersonales: this.initLoad.datosPersonales
    }
  }


}
