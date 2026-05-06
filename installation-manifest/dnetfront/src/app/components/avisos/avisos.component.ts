import { Component, OnInit, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { NgbModal, NgbModalConfig } from '@ng-bootstrap/ng-bootstrap';
import * as $ from 'jquery';

// interfaces
import { DatosGenerales } from '../../interfaces/datosGenerales';
import { DomicilioDeclarante } from '../../interfaces/domicilioDeclarante';

import { GraphqlService } from 'src/app/services/graphql.service';
import { RecepcionAviso } from '../../interfaces/recepcion';

import { Globals } from 'src/app/globals';
import { CatalogoService } from '../../services/catalogo.service';
import { first } from 'rxjs/operators';
import { SessionService } from 'src/app/services/session.service';
import { Idle } from '@ng-idle/core';
import { Router } from '@angular/router';
import { Encabezado } from '../../interfaces/encabezados';
import { DatosPersonales } from '../../interfaces/datosPersonales';
import { PreviewComponent } from '../modals/preview/preview.component';

import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment';
import { DatosGeneralesComponent } from '../datos-generales/datos-generales.component';
import { DomicilioComponent } from '../domicilio/domicilio.component';
import { DetalleAvisoCambioDependencia } from 'src/app/interfaces/avisoCambioDependencia';
import { EnteReceptorService } from 'src/app/services/ente-receptor.service';
import { LocationStrategy } from '@angular/common';
import { DeclaracionService } from 'src/app/services/declaracion.service';

const DECLARA_SIMPLE = [
  "item1",
  "item2",
  "item3",
]

const DECLARA_COMPLETA = [

]

const SECTIONS = [
  "datosGenerales",
  "domicilio",
  'avisoCambio'
]

declare const prepareMenuCuenta: any

@Component({
  selector: 'app-avisos',
  templateUrl: './avisos.component.html',
  styleUrls: ['./avisos.component.scss'],
  providers: [NgbModalConfig, NgbModal]
})

export class AvisoComponent implements OnInit, AfterViewInit {
  @ViewChild(DatosGeneralesComponent) generales: DatosGeneralesComponent;
  @ViewChild(DomicilioComponent) domicilio: DomicilioComponent;


  @ViewChild('modalGuardarButton') modalGuardarButton: ElementRef;

  startTime: any;
  endTime: any
  diff: any;
  totalSecctions: number;
  totalMaxSecctions = 3;
  tipoFormato: string;
  tDeclaracion: string;
  refresh: boolean;
  activeMenu: boolean;
  institucionReceptora: any;
  flagGuardando: boolean;
  flagGuardar: boolean;
  swipeEvent(_p) {
    if (_p === 1) 
    {document.getElementById('goForwardBtn').click()}
    if (_p === 0) 
    {document.getElementById('goBackBtn').click()}
  }

  // Tipo de Declaración
  tipoDeclaracion = 'MODIFICACIÓN'
  fechaEncargo = '04/01/2020'
  currentSection = 0
  countScrollWheel = 0
  menuItems = DECLARA_SIMPLE

  modalRef: any;
  numberOfClicks = 0;
  datosGenerales: DatosGenerales;
  domicilioDeclarante: DomicilioDeclarante;
  detalleAviso: DetalleAvisoCambioDependencia;

  declaracionLoaded: any;
  encabezado: Encabezado;
  usuario: DatosPersonales;
  guardarDeclaracionError: string;
  guardarDeclaracionErrorArr: any;
  errorCarga = false;
  mensajeCambioFormate: string;

  constructor(config: NgbModalConfig,
              private readonly modalService: NgbModal,
              private readonly graphqlService: GraphqlService,
              public globals: Globals,
              private readonly catalogoService: CatalogoService,
              private readonly sessionService: SessionService,
              private readonly idle: Idle,
              private readonly router: Router,
              private readonly toastService: ToastrService,
              private readonly declaracionService: DeclaracionService,
              private readonly enteReceptorService: EnteReceptorService,
              private readonly locationStrategy: LocationStrategy
  ) {
    this.activeMenu = true;
    this.mensajeCambioFormate = null;
    config.backdrop = 'static';
    config.keyboard = false;
    this.refresh = false;
    this.flagGuardando = false;
    this.flagGuardar = false;
    this.getMuns();
    this.getCatalogos();
    this.getInstituciones(this.institucionReceptora);
    this.institucionReceptora = JSON.parse(sessionStorage.getItem('institucionReceptora'));
    this.blockBackButton();
  }

  private blockBackButton() {
    history.pushState(null, null, window.location.href);
    this.locationStrategy.onPopState(() => {
      history.pushState(null, null, window.location.href);
    });
  }

  // Desplegar toast (Nombre de la sección)
  showToast(_section) {
    this.toastService.success(
      'No olvides guardar tus cambios.',
      `${_section} ha sido capturada`,
      {
        extendedTimeOut: 10000,
        closeButton: true,
        tapToDismiss: true
      }
    );
  }

  goToAnchor(_currentSection) {
    this.currentSection = _currentSection
    document.getElementById(SECTIONS[_currentSection]).scrollIntoView({ behavior: "smooth" })
    $(`.item${this.currentSection + 1}`).addClass("menu-nav-active");

    for (var i = 1; i <= this.totalMaxSecctions; i++) {
      if (i != this.currentSection + 1) {
        $(`.item${i}`).removeClass("menu-nav-active");
      }
    }
  }
  // Ir a la sección siguiente
  goForward() {
    this.startTime = new Date().getTime()
    if (this.endTime) {
      if (this.startTime - this.endTime > 100){
        if (this.currentSection < (this.menuItems.length - 1)) {
          this.currentSection++
          this.clickItem()
        }
      }
    } else {
      this.currentSection++
      this.clickItem()
    }
  }

  // Ir a la sección anterior
  goBack() {
    this.startTime = new Date().getTime()
    if (this.endTime) {
      if (this.startTime - this.endTime > 100){
        if (this.currentSection > 0) {
          this.currentSection--
          this.clickItem()
        }
      }
    } else {
      this.currentSection--
      this.clickItem()
    }
  }

  // Click en item del menú
  clickItem() {
    document.getElementById(this.menuItems[this.currentSection]).click()
    this.endTime = this.startTime + 100
  }

  procesaAceptar(mensaje, seccion) {

    switch (seccion) {
      case 'generales':
        sessionStorage.setItem('datosGenerales', JSON.stringify(mensaje));
        this.datosGenerales = mensaje;
        this.datosGenerales.verificar = true;
        // Desplegar toast
        this.showToast('Datos generales');
        this.flagGuardar = true;
        break;

      case 'domicilio':
        sessionStorage.setItem('domicilioDeclarante', JSON.stringify(mensaje));
        this.domicilioDeclarante = mensaje;
        this.domicilioDeclarante.verificar = true;
        // Desplegar toast
        this.showToast('Domicilio declarante');
         this.flagGuardar = true;
        break;

      case 'avisoCambio':
        sessionStorage.setItem('avisoCambio', JSON.stringify(mensaje));
        this.detalleAviso = mensaje;

        // Desplegar toast
        this.showToast('Aviso cambio dependencia');
         this.flagGuardar = true;
        break;
    }
  }

  ngOnInit() {
    this.globals.component = '040506';

    try {
      if (this.activeMenu) {
        $('.item1').addClass('menu-nav-active');
      }

      if (environment.production && !this.globals.declaracionLoaded) {
        this.router.navigate(['/inicio']);
      }
      let d: any = null;
      d = this.globals.declaracionLoaded ? JSON.parse(JSON.stringify(this.globals.declaracionLoaded)) : null;

      this.declaracionLoaded = d || JSON.parse(sessionStorage.getItem('declaracionLoaded'));

      this.totalSecctions = 3;

      this.globals.fechaReferenciaMax = this.declaracionLoaded.encabezado.fechaEncargo;      

      this.tipoDeclaracion = this.declaracionLoaded.encabezado.tipoDeclaracion;

      const parts = this.declaracionLoaded.encabezado.fechaEncargo.split('-');
      const customFechaEncargo = `${parts[2]}-${parts[1]}-${parts[0]}`;
      this.fechaEncargo = `FECHA DE EGRESO: ${customFechaEncargo}`;
      this.usuario = this.sessionService.currentUserValue.datosPersonales;

      this.datosGenerales = this.declaracionLoaded.declaracion.datosGenerales || this.datosGenerales;
      if (!this.datosGenerales.regimenMatrimonialOtro) {
        this.datosGenerales.regimenMatrimonialOtro = null;
      }
      this.encabezado = this.declaracionLoaded.encabezado;

      // modelo Domicilio Declarante
      this.domicilioDeclarante = {
        verificar: null,
        domicilio: {
          ubicacion: null, // México, Extranjero
          domicilioMexico: { // uno u otro
            calle: null,
            codigoPostal: null,
            coloniaLocalidad: '',
            entidadFederativa: {
              id: null,
              valor: ''
            },
            municipioAlcaldia: {
              id: null,
              valor: '',
              fk: ''
            },
            numeroExterior: '',
            numeroInterior: ''
          },
          domicilioExtranjero: { // uno u otro
            calle: '',
            ciudadLocalidad: '',
            codigoPostal: '',
            estadoProvincia: '',
            numeroExterior: '',
            numeroInterior: '',
            pais: {
              id: null,
              valor: ''
            }
          }
        },
        aclaracionesObservaciones: ''
      };
      if (this.declaracionLoaded.declaracion.domicilioDeclarante) {
        if (!this.declaracionLoaded.declaracion.domicilioDeclarante.domicilio.domicilioMexico) {
          this.declaracionLoaded.declaracion.domicilioDeclarante.domicilio.domicilioMexico =
            this.domicilioDeclarante.domicilio.domicilioMexico;
        }
        if (!this.declaracionLoaded.declaracion.domicilioDeclarante.domicilio.domicilioExtranjero) {
          this.declaracionLoaded.declaracion.domicilioDeclarante.domicilio.domicilioExtranjero =
            this.domicilioDeclarante.domicilio.domicilioExtranjero;
        }
        this.domicilioDeclarante = this.declaracionLoaded.declaracion.domicilioDeclarante;
      }
      sessionStorage.setItem('domicilioDeclarante', JSON.stringify(this.domicilioDeclarante));


      // modelo Aviso
      this.detalleAviso = {
        empleoCargoComisionConcluye: {
          ente: {
            ambitoPublico: null,
            id: null,
            nivelOrdenGobierno: {
              entidadFederativa: null,
              nivelOrdenGobierno: null
            },
            nombre: null
          },
          nivelJerarquico: this.declaracionLoaded.encabezado.nivelJerarquico,
          fechaConclusionEncargo: this.declaracionLoaded.encabezado.fechaEncargo
        },

        empleoCargoComisionInicia: {
          id: null,
          ente: {
            ambitoPublico: null,
            id: null,
            nivelOrdenGobierno: {
              entidadFederativa: {
                id: null,
                valor: null
              },
              nivelOrdenGobierno: null,
            },
            nombre: null
          },
          fechaInicioEncargo: null,
          empleoCargoComision: null,
          nivelJerarquico: {
            id: null,
            valor: null,
            fk: null,
            valorUno: null
          },
          contratadoPorHonorarios: null,
          nivelEmpleoCargoComision: null,
          areaAdscripcion: null,
          domicilio: {
            //@domicilionull       
            ubicacion: null, //México, Extranjero
            domicilioMexico: { //uno u otro
              calle: null,
              codigoPostal: null,
              coloniaLocalidad: null,
              entidadFederativa: {
                id: null,
                valor: null
              },
              municipioAlcaldia: {
                id: null,
                valor: null,
                fk: 0
              },
              numeroExterior: null,
              numeroInterior: null
            },
            domicilioExtranjero: { //uno u otro
              calle: null,
              ciudadLocalidad: null,
              codigoPostal: null,
              estadoProvincia: null,
              numeroExterior: null,
              numeroInterior: null,
              pais: {
                id: null,
                valor: null
              }
            }
          },
        },
        aclaracionesObservaciones: null,
      }

      if (this.declaracionLoaded.declaracion.detalleAvisoCambioDependencia) {
        if (!this.declaracionLoaded.declaracion.detalleAvisoCambioDependencia.empleoCargoComisionInicia.domicilio.domicilioMexico) {
          this.declaracionLoaded.declaracion.detalleAvisoCambioDependencia.empleoCargoComisionInicia.domicilio.domicilioMexico =
            this.detalleAviso.empleoCargoComisionInicia.domicilio.domicilioMexico;
        }
        if (!this.declaracionLoaded.declaracion.detalleAvisoCambioDependencia.empleoCargoComisionInicia.domicilio.domicilioExtranjero) {
          this.declaracionLoaded.declaracion.detalleAvisoCambioDependencia.empleoCargoComisionInicia.domicilio.domicilioExtranjero =
            this.detalleAviso.empleoCargoComisionInicia.domicilio.domicilioExtranjero;
        }
        this.detalleAviso = this.declaracionLoaded.declaracion.detalleAvisoCambioDependencia;
      }
      sessionStorage.setItem('avisoCambio', JSON.stringify(this.detalleAviso));


      // se crean los objetos en el sessionstorage
      sessionStorage.setItem('datosGenerales', JSON.stringify(this.datosGenerales));
      sessionStorage.setItem('avisoCambio', JSON.stringify(this.detalleAviso))


      // Llenar menu items con Declaración completa
      if (this.encabezado.tipoFormato === 'COMPLETO') 
        {this.menuItems = this.menuItems.concat(DECLARA_COMPLETA)}
      if (this.refresh === true) {
        this.cargaTablas();
        this.validaSemaforos(this.declaracionLoaded.declaracion);
      }


    } catch (error) {
      console.log('Error en la carga:' + error);
      this.guardarDeclaracionError = 'Error en la carga inicial, salga a la declaración e intente de nuevo';
      this.errorCarga = true;
      $('#modalErrorCargaButton').click();
    }


  }

  cargaTablas() {
    this.generales.ngOnInit();
    this.domicilio.ngOnInit();
  }

  renderView() {
    $.each($('section'), function (i) {
      let val = 100 * i
      $(this).css({
        position: "absolute",
        top: val + "%"
      }).addClass("section").attr("data-index", i + 1);
    })
  }

  ngAfterViewInit() {
    prepareMenuCuenta();
    this.renderView();
    if (this.globals.flagPresentar1Vez === true) {
      this.globals.flagPresentar1Vez = false;
    }


    if (this.globals.flagScroll !== true) {
      this.globals.flagScroll = true
    }

    $(".btn-success,.observaciones").on("keydown", (event) => {
      if (event.which == 9) {
        this.goForward();
        event.preventDefault();

      }
    });
    let c;
    $(".menu-nav ul").hover(function () {
      c = false;
      $(".menu-nav").css('width', '300px');
      $(".opacidad").css('display', 'flex');
      $(".opacidad").css('top', '68px;');

      setTimeout(() => {
        if (!c) {
          $(".menu-nav > ul > li > span").css({ 'display': 'flex' });
        }
      }, 200);
    }, function () {
      c = true;
      $(".menu-nav > ul > li > span").css('display', 'none');
      $(".menu-nav").css('width', '40px');
      $(".opacidad").css('display', 'none');
    });

    const init = JSON.parse(sessionStorage.getItem('declaracionLoaded'));
    this.validaSemaforos(init.declaracion);
  }

  validaSemaforos(i: any) {

    if (i.datosGenerales.verificar == true) {
      $('.item1')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item1')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    if (i.domicilioDeclarante && i.domicilioDeclarante.verificar == true) {
      $('.item2')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item2')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    // AVISO DEPENDENCIA
    if (this.detalleAviso.empleoCargoComisionConcluye.ente.id != null) {
      $('.item3')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item3')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }
  }
  firmar() {
    const declaracionLoaded = JSON.parse(JSON.stringify(this.globals.declaracionLoaded)) || JSON.parse(sessionStorage.getItem('declaracionLoaded'));
    const idUsu = declaracionLoaded.encabezado.usuario.idUsuario;
    const numeroDeclaracion = declaracionLoaded.encabezado.numeroDeclaracion;
    const collName = declaracionLoaded.institucionReceptora.collName;

    this.declaracionService.iniciaFirmaDeclaracion(declaracionLoaded, idUsu, numeroDeclaracion, collName)
      .pipe(first())
      .subscribe(
        data => {
          if (data.status === 200) {
            this.globals.objIniciaFirma = {
              digest: data.body.declaracion.digest,
              rfc: data.body.declaracion.rfc
            };
            this.router.navigate(['/firmar']);
          } else {
            this.toastService.error(
              `Mensaje: ${data.body.MENSAJE}`,
              `Error iniciar proceso de firma`,
              {
                timeOut: 5000,
                extendedTimeOut: 5000,
                closeButton: true,
                tapToDismiss: true,
                progressBar: true
              }
            );
          }
        },
        error => {
          if (error.status === 409) {
            this.toastService.error(
              `Mensaje: ${error.error.MENSAJE}`,
              `Error iniciar proceso de firma`,
              {
                timeOut: 5000,
                extendedTimeOut: 5000,
                closeButton: true,
                tapToDismiss: true,
                progressBar: true
              }
            );
          }
        }
      );

  }

  previsualizar() {
    this.modalRef = this.modalService.open(PreviewComponent, {
      centered: true,
      windowClass: 'previewWindow'
    });
    this.modalRef.componentInstance.objPreview = {
      numeroDeclaracion: this.declaracionLoaded.encabezado.numeroDeclaracion,
      idUsuario: this.declaracionLoaded.encabezado.usuario.idUsuario,
      collName: this.declaracionLoaded.institucionReceptora.collName,
      tipoDeclaracion: this.declaracionLoaded.encabezado.tipoDeclaracion
    };
    this.modalRef.componentInstance.tipoImpresion = 'declaracion';
    this.modalRef.componentInstance.titulo = 'Vista previa';
  }

  guardaDeclaracion() {
    $('#content-cargando').css('display', 'flex');
    this.flagGuardando = true;
    try {

      this.sessionService.renovarToken()
        .subscribe(
          ren => {

            if (this.datosGenerales.verificar !== true) {
              $('#modalErrorDGButton').click();
              return;
            }

            if (this.domicilioDeclarante.domicilio.ubicacion === 'MEXICO') {
              this.domicilioDeclarante.domicilio.domicilioExtranjero = null;
            } else if (this.domicilioDeclarante.domicilio.ubicacion === 'EXTRANJERO') {
              this.domicilioDeclarante.domicilio.domicilioMexico = null;
            }


            if (this.detalleAviso.empleoCargoComisionInicia.domicilio.ubicacion === 'MEXICO') {
              this.detalleAviso.empleoCargoComisionInicia.domicilio.domicilioExtranjero = null;
            } else if (this.detalleAviso.empleoCargoComisionInicia.domicilio.ubicacion === 'EXTRANJERO') {
              this.detalleAviso.empleoCargoComisionInicia.domicilio.domicilioMexico = null;
            }

            let recepcion: RecepcionAviso;

            recepcion = {
              encabezado: this.declaracionLoaded.encabezado,
              firmada: false,
              institucionReceptora: this.declaracionLoaded.institucionReceptora,
              declaracion: {
                datosGenerales: this.datosGenerales,
                domicilioDeclarante: this.domicilioDeclarante.verificar ? this.domicilioDeclarante : null,
                detalleAvisoCambioDependencia: this.detalleAviso.empleoCargoComisionConcluye.ente.id == null ? null : this.detalleAviso
              }
            }



            this.guardarDeclaracionError = null;
            this.guardarDeclaracionErrorArr = null;
            this.graphqlService.guardaAviso(recepcion).subscribe(respuesta => {
              console.log(respuesta.data);
              if (respuesta.data.guardarAviso) {
                switch (respuesta.data.guardarAviso.estado) {
                  case 'CORRECTO':
                    this.modalGuardarButton.nativeElement.click();
                    const decla = JSON.parse(sessionStorage.getItem('declaracionLoaded'));
                    decla.declaracion = respuesta.data.guardarAviso.declaracion.declaracion;
                    sessionStorage.setItem('declaracionLoaded', JSON.stringify(decla.declaracion));
                    this.globals.declaracionLoaded = decla.declaracion;
                    this.refresh = true;
                    this.activeMenu = false;
                    this.ngOnInit();
                    break;
                  case 'ERROR_DIGITO':
                    this.guardarDeclaracionError = 'Elemento no enviado';
                    this.modalGuardarButton.nativeElement.click();
                    break;
                  case 'ERROR':
                    this.guardarDeclaracionError = 'Error al guardar la información, por favor intente nuevamente, si el problema persiste, contacte al administrador.';
                    this.modalGuardarButton.nativeElement.click();
                    break;
                  case 'ERROR_COMUNICACION':
                    this.guardarDeclaracionError = 'Se ha presentado un problema de comunicación con los servidores, por favor intente mas tarde.';
                    this.modalGuardarButton.nativeElement.click();
                    break;
                  case 'MODULOS_INCORRECTOS':
                    this.guardarDeclaracionError = 'Error en la captura de módulos';
                    this.modalGuardarButton.nativeElement.click();
                    break;
                  case 'CON_OBSERVACIONES':
                    this.guardarDeclaracionErrorArr = [];
                    for (const mod of respuesta.data.guardarAviso.modulos) {
                      for (const e of mod.errores) {
                        this.guardarDeclaracionError = 'Error en la captura de módulos:';
                        this.guardarDeclaracionErrorArr.push({ error: `Error en el campo: ${e.nombreCampo} ${e.listErrorMensajes[0].mensaje}` });
                      }
                    }
                    this.modalGuardarButton.nativeElement.click();
                    break;
                  default:
                    console.log(respuesta.data.guardarDeclaracion.estado);
                    break;


                }
              }
              this.flagGuardando = false;

            },
              error => {
                this.guardarDeclaracionError = 'Error de incompatibilidad';
                this.modalGuardarButton.nativeElement.click();
                this.flagGuardando = false;
                console.log(error);
              });
          });
    } catch (e) {


    } finally {
      $('#content-cargando').css('display', 'none');
    }
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
            console.log('cat', data);
          }
        },
        error => {
        }
      );

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
            }
          },
          error => {

          }
        );
    }
  }

  getInstituciones(ente: any) {
    if (!this.globals.catalogoEntes) {
      this.enteReceptorService.getInstituciones(ente)
        .pipe(first())
        .subscribe(
          data => {
            if (data.status === 200) {
              this.globals.catalogoEntes = data.body.catalogo;
            }
          },
          error => {
          }
        );
    }
  }

  home() {
    this.router.navigate(['/inicio']);
  }
  logout() {
    const navOut = sessionStorage.getItem('enteReceptor') ? JSON.parse(sessionStorage.getItem('enteReceptor')).enteContext || '' : '';
    this.sessionService.logout();
    this.router.navigate(['/' + navOut]);
  }
  revisaTooltip() {
    return !this.flagGuardar ? 'Para poder guardar la declaración, deberá dar clic al botón \naceptar de los módulos en los que realizó cambios' : '';
  }
}
