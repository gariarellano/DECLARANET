import { Component, OnInit, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { NgbModal, NgbModalConfig } from '@ng-bootstrap/ng-bootstrap';
import * as $ from 'jquery';

// modals
import { DatosCurricularesModalComponent } from './../../components/modals/datos-curriculares-modal/datos-curriculares-modal.component';
import { DatosEmpleoModalComponent } from './../../components/modals/datos-empleo-modal/datos-empleo-modal.component';
import { ExperienciaLaboralModalComponent } from './../../components/modals/experiencia-laboral-modal/experiencia-laboral-modal.component';
import { DatosDependienteModalComponent } from './../../components/modals/datos-dependiente-modal/datos-dependiente-modal.component';
import { BienesInmueblesModalComponent } from './../../components/modals/bienes-inmuebles-modal/bienes-inmuebles-modal.component';
import { VehiculosModalComponent } from './../../components/modals/vehiculos-modal/vehiculos-modal.component';
import { BienesMueblesModalComponent } from './../../components/modals/bienes-muebles-modal/bienes-muebles-modal.component';
import { InversionesModalComponent } from './../../components/modals/inversiones-modal/inversiones-modal.component';
import { AdeudosModalComponent } from './../../components/modals/adeudos-modal/adeudos-modal.component';
import { PrestamoInmuebleModalComponent } from './../../components/modals/prestamo-inmueble-modal/prestamo-inmueble-modal.component';
import { PrestamoVehiculoModalComponent } from './../../components/modals/prestamo-vehiculo-modal/prestamo-vehiculo-modal.component';

// tslint:disable-next-line: max-line-length
import { ParticipacionEmpresasModalComponent } from './../../components/modals/participacion-empresas-modal/participacion-empresas-modal.component';
import { ParticipaDesicionesModalComponent } from '../modals/participa-desiciones-modal/participa-desiciones-modal.component';
import { ApoyosModalComponent } from '../modals/apoyos-modal/apoyos-modal.component';
import { RepresentacionModalComponent } from '../modals/representacion-modal/representacion-modal.component';
import { ClientesModalComponent } from '../modals/clientes-modal/clientes-modal.component';
import { BeneficiosModalComponent } from '../modals/beneficios-modal/beneficios-modal.component';
import { FideicomisosModalComponent } from '../modals/fideicomisos-modal/fideicomisos-modal.component';

// interfaces
import { DatosGenerales } from './../../interfaces/datosGenerales';
import { DomicilioDeclarante } from './../../interfaces/domicilioDeclarante';
import { DatosCurricularesDeclarante } from './../../interfaces/datosCurriculares';
import { Escolaridad } from './../../interfaces/datosCurriculares-escolaridad';
import { DatosEmpleo } from './../../interfaces/datosEmpleo';
import { ExperienciaLaboral } from './../../interfaces/experienciaLaboral';
import { DatosPareja } from './../../interfaces/datosPareja';
import { DatosDependiente } from './../../interfaces/datosDependiente';
import { BienesInmuebles } from '../../interfaces/bienesInmuebles';
import { BienesMuebles } from '../../interfaces/bienesMuebles';
import { Vehiculos } from '../../interfaces/vehiculos';
import { Inversiones } from '../../interfaces/inversiones';
import { AdeudosPasivos } from '../../interfaces/adeudosPasivos';
import { PrestamoComodato } from '../../interfaces/prestamoComodato';
import { ActividadLaboral } from '../../interfaces/actividadLaboral';
import { actividadAnualAnteriorDatos } from '../../interfaces/actividadAnualAnteriorDatos';

/*Seccion II interfaces*/
import { ParticipaEmpresasDatos } from './../../interfaces/participaEmpresasDatos';
import { participaDecisionesDatos } from './../../interfaces/participaDesicionesDatos';
import { ApoyosoBeneficiosDatos } from './../../interfaces/ApoyosBeneficios';
import { RepresenatcionDatos } from './../../interfaces/Representacion';
import { ClientesPrincipalesDatos } from './../../interfaces/clientesPrincipales';
import { BeneficiosPrivadosDatos } from './../../interfaces/beneficiosPrivados';
import { FideicomisosDatos } from './../../interfaces/Fedeicomisos';

// components
import { DatosCurricularesComponent } from '../datos-curriculares/datos-curriculares.component';
import { DatosEmpleoComponent } from '../datos-empleo/datos-empleo.component';
import { ExperienciaLaboralComponent } from '../experiencia-laboral/experiencia-laboral.component';
import { DatosDependienteComponent } from '../datos-dependiente/datos-dependiente.component';
import { BienesInmueblesComponent } from '../bienes-inmuebles/bienes-inmuebles.component';
import { VehiculosComponent } from '../vehiculos/vehiculos.component';
import { BienesMueblesComponent } from '../bienes-muebles/bienes-muebles.component';
import { InversionesComponent } from '../inversiones/inversiones.component';
import { AdeudosComponent } from '../adeudos/adeudos.component';
import { PrestamoBienInmuebleComponent } from '../prestamo-bien-inmueble/prestamo-bien-inmueble.component';
import { PrestamoBienVehiculoComponent } from '../prestamo-bien-vehiculo/prestamo-bien-vehiculo.component';

/*Seccion II Components*/
import { ParticipacionEmpresasComponent } from '../participacion-empresas/participacion-empresas.component';
import { ParticipaTomaDecisionesComponent } from '../participa-toma-decisiones/participa-toma-decisiones.component';
import { ApoyosBeneficiosComponent } from '../apoyos-beneficios/apoyos-beneficios.component';
import { RepresentacionComponent } from '../representacion/representacion.component';
import { ClientesPrincipalesComponent } from '../clientes-principales/clientes-principales.component';
import { BeneficiosPrivadosComponent } from '../beneficios-privados/beneficios-privados.component';
import { FideicomisosComponent } from '../fideicomisos/fideicomisos.component';

import { GraphqlService } from 'src/app/services/graphql.service';
import { Recepcion } from '../../interfaces/recepcion';
import { FirmaModalComponent } from '../modals/firma-modal/firma-modal.component';
import { Globals } from 'src/app/globals';
import { CatalogoService } from '../../services/catalogo.service';
import { first } from 'rxjs/operators';
import { SessionService } from 'src/app/services/session.service';
import { Idle } from '@ng-idle/core';
import { Router } from '@angular/router';
import { Encabezado } from '../../interfaces/encabezados';
import { DatosPersonales } from '../../interfaces/datosPersonales';
import { PreviewComponent } from '../modals/preview/preview.component';
import { sha3_512 } from 'js-sha3';



import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment';
import { DatosGeneralesComponent } from '../datos-generales/datos-generales.component';
import { DomicilioComponent } from '../domicilio/domicilio.component';
import { DatosParejaComponent } from '../datos-pareja/datos-pareja.component';
import { IngresosNetosComponent } from '../ingresos-netos/ingresos-netos.component';
import { ActividadAnualAnteriorComponent } from '../actividad-anual-anterior/actividad-anual-anterior.component';
import { EnteReceptorService } from 'src/app/services/ente-receptor.service';
import { LocationStrategy } from '@angular/common';
import { DeclaracionService } from 'src/app/services/declaracion.service';

const DECLARA_SIMPLE = [
  'item1',
  'item2',
  'item3',
  'item4',
  'item5',
  'item6',
  'item7',
  'item8',
  'item9'
];

const DECLARA_COMPLETA = [
  'item10',
  'item11',
  'item12',
  'item13',
  'item14',
  'item15',
  'item16',
  'item17',
  'item18',
  'item19',
  'item20',
  'item21',
  'item22',
];

const SECTIONS = [
  'datosGenerales',
  'domicilio',
  'datosCurriculares',
  'datosEmpleo',
  'expLaboral',
  'datosPareja',
  'datosDependienteEconom',
  'ingresosNetos',
  'actAnual',
  'sectionBienesInmuebles',
  'vehiculos',
  'binesMuebles',
  'inversiones',
  'adeudos',
  'prestamoInmueble',
  'participaEmpresa',
  'tomaDecisiones',
  'beneficios',
  'representacion',
  'clientesPrincipales',
  'beneficiosPrivados',
  'fideicomisos'
];


declare const prepareMenuCuenta: any;

@Component({
  selector: 'app-principal',
  templateUrl: './principal.component.html',
  styleUrls: ['./principal.component.scss'],
  providers: [NgbModalConfig, NgbModal]
})

export class PrincipalComponent implements OnInit, AfterViewInit {
  @ViewChild(DatosGeneralesComponent) generales: DatosGeneralesComponent;
  @ViewChild(DomicilioComponent) domicilio: DomicilioComponent;
  @ViewChild(DatosParejaComponent) pareja: DatosParejaComponent;
  @ViewChild(IngresosNetosComponent) ingresos: IngresosNetosComponent;
  @ViewChild(ActividadAnualAnteriorComponent) actAnualAnt: ActividadAnualAnteriorComponent;


  @ViewChild(DatosCurricularesComponent) curriculares: DatosCurricularesComponent;
  @ViewChild(DatosEmpleoComponent) empleo: DatosEmpleoComponent;
  @ViewChild(ExperienciaLaboralComponent) expLab: ExperienciaLaboralComponent;
  @ViewChild(DatosDependienteComponent) dependiente: DatosDependienteComponent;
  @ViewChild(BienesInmueblesComponent) inmueble: BienesInmueblesComponent;
  @ViewChild(VehiculosComponent) vehiculo: VehiculosComponent;
  @ViewChild(BienesMueblesComponent) mueble: BienesMueblesComponent;
  @ViewChild(InversionesComponent) inversion: InversionesComponent;
  @ViewChild(AdeudosComponent) adeudo: AdeudosComponent;
  @ViewChild(PrestamoBienInmuebleComponent) bienInmueble: PrestamoBienInmuebleComponent;
  @ViewChild(PrestamoBienVehiculoComponent) bienVehiculo: PrestamoBienVehiculoComponent;

  /*Seccion II Declaracion de intereses*/
  @ViewChild(ParticipacionEmpresasComponent) participacionEmpresas: ParticipacionEmpresasComponent;
  @ViewChild(ParticipaTomaDecisionesComponent) participaTomaDecisiones: ParticipaTomaDecisionesComponent;
  @ViewChild(ApoyosBeneficiosComponent) apoyos: ApoyosBeneficiosComponent;
  @ViewChild(RepresentacionComponent) representaciones: RepresentacionComponent;
  @ViewChild(ClientesPrincipalesComponent) clientesPrincipales: ClientesPrincipalesComponent;
  @ViewChild(BeneficiosPrivadosComponent) beneficiosPrivados: BeneficiosPrivadosComponent;
  @ViewChild(FideicomisosComponent) fideicomisos: FideicomisosComponent;
  @ViewChild('modalGuardarButton') modalGuardarButton: ElementRef;

  startTime: any;
  endTime: any;
  diff: any;
  totalSecctions: number;
  totalMaxSecctions = 22;
  tipoFormato: string;
  tDeclaracion: string;
  refresh: boolean;
  activeMenu: boolean;
  flagGuardar: boolean; // bandera para definir si el usuario puede guardar
  flagGuardando: boolean; // bandera para definir si el usuario puede guardar


  // Tipo de Declaración
  tipoDeclaracion = 'MODIFICACIÓN';
  fechaEncargo = '04/01/2020';
  currentSection = 0;
  countScrollWheel = 0;
  menuItems = DECLARA_SIMPLE;

  modalRef: any;
  numberOfClicks = 0;
  datosGenerales: DatosGenerales;
  domicilioDeclarante: DomicilioDeclarante;
  datosCurricularesDeclarante: DatosCurricularesDeclarante;
  datosEmpleo: DatosEmpleo;
  escolaridad: Escolaridad;
  datosPareja: DatosPareja;
  experienciaLab: ExperienciaLaboral;
  datosDependiente: DatosDependiente;
  /*Seccion II*/
  participaEmpresasDatos: ParticipaEmpresasDatos;
  bienesInmuebles: BienesInmuebles;
  vehiculos: Vehiculos;
  bienesMuebles: BienesMuebles;
  inversiones: Inversiones;
  adeudos: AdeudosPasivos;
  prestamoComodatos: PrestamoComodato;
  participaDecisionesDatos: participaDecisionesDatos;
  apoyosoBeneficiosDatos: ApoyosoBeneficiosDatos;
  representacionDatos: RepresenatcionDatos;
  clientesDatos: ClientesPrincipalesDatos;
  beneficiosPrivadosDatos: BeneficiosPrivadosDatos;
  fideicomisosDatos: FideicomisosDatos;
  actividadLaboral: ActividadLaboral;
  datosIngresos: any;
  datosActAnt: actividadAnualAnteriorDatos;
  // flagDeclaracion: FlagDeclaracion;
  declaracionLoaded: any;
  encabezado: Encabezado;
  usuario: DatosPersonales;
  guardarDeclaracionError: string;
  guardarDeclaracionErrorArr: any;
  errorCarga = false;
  mensajeCambioFormate: string;
  institucionReceptora: any;
  fechaActual: any;
  fechaEnc: Date;
  dayFechaEnc: string;
  swipeEvent(p) {
    if (p === 1) {
      document.getElementById('goForwardBtn').click();
    }
    if (p === 0) {
      document.getElementById('goBackBtn').click();
    }
  }

  constructor(config: NgbModalConfig,
    private readonly modalService: NgbModal,
    private readonly graphqlService: GraphqlService,
    public globals: Globals,
    private readonly catalogoService: CatalogoService,
    private readonly sessionService: SessionService,
    private readonly declaracionService: DeclaracionService,
    private readonly idle: Idle,
    private readonly router: Router,
    private readonly toastService: ToastrService,
    private readonly enteReceptorService: EnteReceptorService,
    private readonly locationStrategy: LocationStrategy
  ) {
    this.activeMenu = true;
    this.mensajeCambioFormate = null;
    config.backdrop = 'static';
    config.keyboard = false;
    this.refresh = false;
    this.flagGuardar = false;
    this.flagGuardando = false;

    if (!this.sessionService.currentUserValue) {
      this.logout();
    }
    this.getMuns();
    this.getCatalogos();
    this.institucionReceptora = JSON.parse(sessionStorage.getItem('institucionReceptora'));


    this.getInstituciones(this.institucionReceptora);



    this.blockBackButton();
  }

  private blockBackButton() {
    history.pushState(null, null, window.location.href);
    this.locationStrategy.onPopState(() => {
      history.pushState(null, null, window.location.href);
    });
  }

  // Desplegar toast (Nombre de la sección)
  showToast(section) {
    this.toastService.success(
      'No olvides guardar tus cambios.',
      `${section} ha sido capturada`,
      {
        timeOut: 2000,
        extendedTimeOut: 800,
        closeButton: true,
        tapToDismiss: true,
        progressBar: true
      }
    );
  }

  // Ir al ancla (Nombre ancla, Posición del menú)
  goToAnchor(currentSec) {
    this.currentSection = currentSec;
    document.getElementById(SECTIONS[currentSec]).scrollIntoView({ behavior: 'smooth' });
    $(`.item${this.currentSection + 1}`).addClass('menu-nav-active');

    for (let i = 1; i <= this.totalMaxSecctions; i++) {
      if (i !== this.currentSection + 1) {
        $(`.item${i}`).removeClass('menu-nav-active');
      }
    }
  }


  // Ir a la sección siguiente
  goForward() {
    if (!$('body').hasClass('disabled-onepage-scroll')) {
      this.startTime = new Date().getTime();
      if (this.endTime) {
        if (this.startTime - this.endTime > 100) {
          if (this.currentSection < (this.menuItems.length - 1)) {
            this.currentSection++;
            this.clickItem();
          }
        }
      } else {
        this.currentSection++;
        this.clickItem();
      }
    }
  }

  // Ir a la sección anterior
  goBack() {
    if (!$('body').hasClass('disabled-onepage-scroll')) {
      this.startTime = new Date().getTime();
      if (this.endTime) {
        if (this.startTime - this.endTime > 100) {
          if (this.currentSection > 0) {
            this.currentSection--;
            this.clickItem();
          }
        }
      } else {
        this.currentSection--;
        this.clickItem();
      }
    }
  }

  // Click en item del menú
  clickItem() {
    document.getElementById(this.menuItems[this.currentSection]).click();
    this.endTime = this.startTime + 100;
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

      case 'pareja':
        sessionStorage.setItem('datosPareja', JSON.stringify(mensaje));
        this.datosPareja = mensaje;
        this.showToast('Datos pareja');
        this.flagGuardar = true;
        break;

      case 'laboral':
        sessionStorage.setItem('actividadLaboral', JSON.stringify(mensaje));
        this.actividadLaboral = mensaje;
        this.datosPareja.datosParejas[0].actividadLaboral = mensaje;
        // Desplegar toast
        this.showToast('Actividad laboral');
        this.flagGuardar = true;
        break;

      case 'ingresos':
        sessionStorage.setItem('ingresos', JSON.stringify(mensaje));
        console.log(mensaje);

        this.datosIngresos = mensaje;
        // Desplegar toast
        this.showToast('Ingresos');
        this.flagGuardar = true;
        break;
      case 'ingresosAnt':
        sessionStorage.setItem('ingresosAnt', JSON.stringify(mensaje));

        this.datosActAnt = mensaje;
        // Desplegar toast
        this.showToast('Ingresos anteriores');
        this.flagGuardar = true;
        break;
    }

  }

  ngOnInit() {
    this.globals.component = '040506';
    this.fechaActual = new Date();
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
      if (!this.globals.declaracionLoaded) {
        this.globals.declaracionLoaded = JSON.parse(JSON.stringify(this.declaracionLoaded));
      }

      const numeroDeclaracionPrecarga = this.declaracionLoaded.numeroDeclaracionPrecarga || null;
      this.globals.precargaOracle = this.globals.isNumber(numeroDeclaracionPrecarga);

      if (this.tipoFormato === 'SIMPLIFICADO' && this.declaracionLoaded.encabezado.tipoFormato === 'COMPLETO') {
        this.mensajeCambioFormate = 'Se ha modificado el formato de tu declaración de acuerdo a tu encargo declarado.';
      }

      this.tipoFormato = this.declaracionLoaded.encabezado.tipoFormato;
      this.tDeclaracion = this.declaracionLoaded.encabezado.tipoDeclaracion;

      switch (this.tipoFormato) {
        case 'COMPLETO':
          if (this.tDeclaracion === 'INICIO' || this.tDeclaracion === 'CONCLUSION') {
            this.totalSecctions = 22;
          }
          if (this.tDeclaracion === 'MODIFICACION') {
            this.totalSecctions = 21;
          }
          break;

        case 'SIMPLIFICADO':
          if (this.tDeclaracion === 'INICIO' || this.tDeclaracion === 'CONCLUSION') {
            this.totalSecctions = 7;
          }
          if (this.tDeclaracion === 'MODIFICACION') {
            this.totalSecctions = 6;
          }
          break;
      }

      this.globals.fechaReferenciaMax = this.declaracionLoaded.encabezado.tipoDeclaracion === 'MODIFICACION' ?
        new Date(`${this.declaracionLoaded.encabezado.anio - 1}/12/31`).toISOString().split('T')[0] : this.declaracionLoaded.encabezado.fechaEncargo;

      this.fechaEnc = new Date(this.globals.fechaReferenciaMax);

      this.tipoDeclaracion = this.declaracionLoaded.encabezado.tipoDeclaracion;
      this.globals.textoSaldos = '';
      if (this.declaracionLoaded.encabezado.tipoDeclaracion === 'MODIFICACION') {
        this.globals.textoSaldos = `al 31 de diciembre del año inmediato anterior`;
        this.globals.textoTitulos = `(entre 1 de enero al 31 de diciembre del año inmediato anterior)`;
        this.globals.textSubTitleEmpleo = `(en el período a declarar)`;
        this.fechaEncargo = `AÑO DE DECLARACIÓN: ${this.declaracionLoaded.encabezado.anio}`;
      } else if (this.declaracionLoaded.encabezado.tipoDeclaracion === 'INICIO') {
        this.globals.textoSaldos = `(situación actual)`;
        this.globals.textoTitulos = `(situación actual)`;
        this.globals.textSubTitleEmpleo = `(que inicia)`;
        const parts = this.declaracionLoaded.encabezado.fechaEncargo.split('-');
        const customFechaEncargo = `${parts[2]}-${parts[1]}-${parts[0]}`;
        this.fechaEncargo = `FECHA DE INICIO DEL ENCARGO: ${customFechaEncargo}`;
        this.dayFechaEnc = customFechaEncargo;
      } else {
        this.globals.textoSaldos = `de conclusión del empleo`;
        this.globals.textoTitulos = ``;
        this.globals.textSubTitleEmpleo = `(que concluye)`;
        const parts = this.declaracionLoaded.encabezado.fechaEncargo.split('-');
        const customFechaEncargo = `${parts[2]}-${parts[1]}-${parts[0]}`;
        this.fechaEncargo = `FECHA DE CONCLUSIÓN DEL ENCARGO: ${customFechaEncargo}`;
        this.dayFechaEnc = customFechaEncargo;
      }
      // if (this.fechaEnc.getDate() <= 30) {
      //   // this.dayFechaEnc = this.fechaEnc.getDate() + 1;
      //   // this.dayFechaEnc = customFechaEncargo
      // } else {
      //   this.dayFechaEnc = this.fechaEnc.getDate();
      // }
      sessionStorage.setItem('fechaMax', JSON.stringify(this.globals.fechaReferenciaMax));
      // Eliminarpara porduccion el session
      console.log('declaracionLoaded', this.declaracionLoaded);
      console.log('decla', JSON.stringify(this.declaracionLoaded));
      console.log('sha', sha3_512(JSON.stringify(this.declaracionLoaded)));
      // modelo Datos Generales
      this.usuario = this.sessionService.currentUserValue.datosPersonales;

      this.datosGenerales = this.declaracionLoaded.declaracion.datosGenerales || this.datosGenerales;
      if (!this.datosGenerales.regimenMatrimonialOtro) {
        this.datosGenerales.regimenMatrimonialOtro = null;
      }
      this.encabezado = this.declaracionLoaded.encabezado;
      console.log(this.encabezado.tipoFormato);
      console.log(this.encabezado);


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
      // modelo Datos Curriculares
      this.datosCurricularesDeclarante = {
        ninguno: null,
        escolaridad: [],
        aclaracionesObservaciones: ''
      };
      this.datosCurricularesDeclarante = this.declaracionLoaded.declaracion.datosCurricularesDeclarante || this.datosCurricularesDeclarante;
      if (this.datosCurricularesDeclarante.escolaridad === null) {
        this.datosCurricularesDeclarante.escolaridad = [];
      }
      sessionStorage.setItem('datosCurricularesDeclarante', JSON.stringify(this.datosCurricularesDeclarante));

      // modelo Datos Empleo
      this.datosEmpleo = {
        empleoCargoComision: [],
        aclaracionesObservaciones: '',
        ninguno: null
      };
      this.datosEmpleo = this.declaracionLoaded.declaracion.datosEmpleoCargoComision || this.datosEmpleo;
      sessionStorage.setItem('datosEmpleo', JSON.stringify(this.datosEmpleo));

      // modelo Datos Experiencia laboral
      this.experienciaLab = {
        ninguno: null,
        experienciaLaboral: [],
        aclaracionesObservaciones: ''
      };
      this.declaracionLoaded.declaracion.experienciasLaborales = this.declaracionLoaded.declaracion.experienciasLaborales || this.experienciaLab;
      this.experienciaLab = this.declaracionLoaded.declaracion.experienciasLaborales;
      sessionStorage.setItem('experienciaLab', JSON.stringify(this.experienciaLab));


      // modelo Datos Pareja
      this.datosPareja = {
        ninguno: null,
        datosParejas: [{
          id: null,
          idPosicionVista: '1',
          registroHistorico: false,
          tipoOperacion: 'AGREGAR',
          datosPersonales: {
            nombre: '',
            primerApellido: '',
            segundoApellido: '',
            rfc: '',
            fechaNacimiento: '',
          },
          relacionConDeclarante: null,
          ciudadanoExtranjero: {
            esExtranjero: null,
            curp: ''
          },
          esDependienteEconomico: null,
          habitaDomicilioDeclarante: null,
          domicilioDependienteEconomico: {
            lugarDondeReside: '',
            domicilio: {
              ubicacion: '',
              domicilioMexico: {
                calle: '',
                codigoPostal: '',
                coloniaLocalidad: '',
                entidadFederativa: {
                  id: null,
                  valor: '',
                },
                municipioAlcaldia: {
                  id: null,
                  valor: '',
                  fk: null
                }
              },
              domicilioExtranjero: {
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
            }
          },
          ninguno: false,
          actividadLaboral: {
            ambitoSector: {
              id: null,
              valor: ''
            },
            ambitoSectorOtro: '',
            sectorPublico: {
              nivelOrdenGobierno: null,
              ambitoPublico: null,
              nombreEntePublico: '',
              areaAdscripcion: '',
              empleoCargoComision: '',
              funcionPrincipal: ''
            },
            sectorPrivadoOtro: {
              nombreEmpresaSociedadAsociacion: '',
              rfc: '',
              area: '',
              empleoCargo: '',
              sector: {
                id: null,
                valor: ''
              },
              sectorOtro: '',
              proveedorContratistaGobierno: null


            },
            fechaIngreso: '',
            salarioMensualNeto: 0
          },
          verificar: true,

        }],
        aclaracionesObservaciones: ''
      };


      this.declaracionLoaded.declaracion.datosParejas =
        this.declaracionLoaded.declaracion.datosParejas ||
        this.datosPareja;

      this.declaracionLoaded.declaracion.datosParejas.datosParejas = this.declaracionLoaded.declaracion.datosParejas.datosParejas ||
        this.datosPareja.datosParejas;


      this.declaracionLoaded.declaracion.datosParejas.datosParejas[0].domicilioDependienteEconomico =
        this.declaracionLoaded.declaracion.datosParejas.datosParejas[0].domicilioDependienteEconomico ||
        this.datosPareja.datosParejas[0].domicilioDependienteEconomico;

      this.declaracionLoaded.declaracion.datosParejas.datosParejas[0].domicilioDependienteEconomico.domicilio =
        this.declaracionLoaded.declaracion.datosParejas.datosParejas[0].domicilioDependienteEconomico.domicilio ||
        this.datosPareja.datosParejas[0].domicilioDependienteEconomico.domicilio;

      this.declaracionLoaded.declaracion.datosParejas.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioExtranjero =
        this.declaracionLoaded.declaracion.datosParejas.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioExtranjero ||
        this.datosPareja.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioExtranjero;

      this.declaracionLoaded.declaracion.datosParejas.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioExtranjero.pais =
        this.declaracionLoaded.declaracion.datosParejas.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioExtranjero.pais ||
        this.datosPareja.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioExtranjero.pais;

      this.declaracionLoaded.declaracion.datosParejas.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioMexico =
        this.declaracionLoaded.declaracion.datosParejas.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioMexico ||
        this.datosPareja.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioMexico;

      this.declaracionLoaded.declaracion.datosParejas.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioMexico.entidadFederativa =
        this.declaracionLoaded.declaracion.datosParejas.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioMexico.entidadFederativa ||
        this.datosPareja.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioMexico.entidadFederativa;

      this.declaracionLoaded.declaracion.datosParejas.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioMexico.municipioAlcaldia =
        this.declaracionLoaded.declaracion.datosParejas.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioMexico.municipioAlcaldia ||
        this.datosPareja.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioMexico.municipioAlcaldia;

      this.declaracionLoaded.declaracion.datosParejas.datosParejas[0].actividadLaboral =
        this.declaracionLoaded.declaracion.datosParejas.datosParejas[0].actividadLaboral ||
        this.datosPareja.datosParejas[0].actividadLaboral;



      this.declaracionLoaded.declaracion.datosParejas.datosParejas[0].actividadLaboral.sectorPrivadoOtro =
        this.declaracionLoaded.declaracion.datosParejas.datosParejas[0].actividadLaboral.sectorPrivadoOtro ||
        this.datosPareja.datosParejas[0].actividadLaboral.sectorPrivadoOtro;

      this.declaracionLoaded.declaracion.datosParejas.datosParejas[0].actividadLaboral.sectorPublico =
        this.declaracionLoaded.declaracion.datosParejas.datosParejas[0].actividadLaboral.sectorPublico ||
        this.datosPareja.datosParejas[0].actividadLaboral.sectorPublico;

      this.declaracionLoaded.declaracion.datosParejas.datosParejas = this.declaracionLoaded.declaracion.datosParejas.datosParejas
        || this.datosPareja.datosParejas;
      this.datosPareja = this.declaracionLoaded.declaracion.datosParejas || this.datosPareja;


      // modelo Datos Dependiente
      this.datosDependiente = {
        ninguno: null,
        datosDependientesEconomicos: [],
        aclaracionesObservaciones: ''
      };

      this.declaracionLoaded.declaracion.datosDependientesEconomicos = this.declaracionLoaded.declaracion.datosDependientesEconomicos || this.datosDependiente;
      this.datosDependiente = this.declaracionLoaded.declaracion.datosDependientesEconomicos;
      sessionStorage.setItem('datosDependiente', JSON.stringify(this.datosDependiente));


      // modelo Bienes Inmuebles
      this.bienesInmuebles = {
        ninguno: null,
        bienesInmuebles: [],
        aclaracionesObservaciones: ''
      };
      this.declaracionLoaded.declaracion.bienesInmuebles = this.declaracionLoaded.declaracion.bienesInmuebles || this.bienesInmuebles;
      this.bienesInmuebles = this.declaracionLoaded.declaracion.bienesInmuebles;
      sessionStorage.setItem('bienesInmuebles', JSON.stringify(this.bienesInmuebles));

      // modelo Vehiculos
      this.vehiculos = {
        ninguno: null,
        vehiculos: [],
        aclaracionesObservaciones: ''
      };
      this.declaracionLoaded.declaracion.vehiculos = this.declaracionLoaded.declaracion.vehiculos || this.vehiculos;
      this.vehiculos = this.declaracionLoaded.declaracion.vehiculos;
      sessionStorage.setItem('vehiculos', JSON.stringify(this.vehiculos));


      // modelo Bienes Muebles
      this.bienesMuebles = {
        ninguno: null,
        bienesMuebles: [],
        aclaracionesObservaciones: ''
      };
      this.declaracionLoaded.declaracion.bienesMuebles = this.declaracionLoaded.declaracion.bienesMuebles || this.bienesMuebles;
      this.bienesMuebles = this.declaracionLoaded.declaracion.bienesMuebles;
      sessionStorage.setItem('bienesMuebles', JSON.stringify(this.bienesMuebles));



      // modelo Inversiones
      this.inversiones = {
        ninguno: null,
        inversion: [],
        aclaracionesObservaciones: ''
      };
      this.declaracionLoaded.declaracion.inversionesCuentasValores = this.declaracionLoaded.declaracion.inversionesCuentasValores || this.inversiones;
      this.inversiones = this.declaracionLoaded.declaracion.inversionesCuentasValores;
      sessionStorage.setItem('inversiones', JSON.stringify(this.inversiones));

      // modelo Adeudos Pasivos
      this.adeudos = {
        ninguno: null,
        adeudos: [],
        aclaracionesObservaciones: ''
      };
      this.declaracionLoaded.declaracion.adeudosPasivos = this.declaracionLoaded.declaracion.adeudosPasivos || this.adeudos;
      this.adeudos = this.declaracionLoaded.declaracion.adeudosPasivos;
      sessionStorage.setItem('adeudos', JSON.stringify(this.adeudos));


      // modelo Prestamo Comodato
      this.prestamoComodatos = {
        ninguno: null,
        prestamo: [],
        aclaracionesObservaciones: ''
      };
      this.declaracionLoaded.declaracion.prestamoComodato = this.declaracionLoaded.declaracion.prestamoComodato || this.prestamoComodatos;
      this.prestamoComodatos = this.declaracionLoaded.declaracion.prestamoComodato;
      sessionStorage.setItem('prestamoComodato', JSON.stringify(this.prestamoComodatos));


      this.actividadLaboral = {
        ninguno: null,
        ambitoSector: {
          id: '',
          valor: ''
        },
        sectorPublico: {
          nivelOrdenGobierno: '', // FEDERAL, ESTATAL, MUNICIPAL / ALCALDÍA
          ambitoPublico: '', // EJECUTIVO, LEGISLATIVO, JUDICIAL, ÓRGANO AUTÓNOMO ANTES PODER
          nombreEntePublico: '',
          areaAdscripcion: '',
          empleoCargoComision: '',
          funcionPrincipal: ''
        },
        sectorPrivadoOtro: {
          nombreEmpresaSociedadAsociacion: '',
          rfc: '',
          area: '',
          empleoCargo: '',
          sector: {
            id: '',
            valor: ''
          }
        },
        fechaIngreso: '',
        salarioMensualNeto: '',
        verificar: true
      };

      /*Segunda Sección*/
      this.participaEmpresasDatos = {
        ninguno: null,
        participaciones: [],
        aclaracionesObservaciones: ''
      };
      this.declaracionLoaded.declaracion.participaEmpresasSociedadesAsociaciones =
        this.declaracionLoaded.declaracion.participaEmpresasSociedadesAsociaciones || this.participaEmpresasDatos;
      this.participaEmpresasDatos = this.declaracionLoaded.declaracion.participaEmpresasSociedadesAsociaciones;
      sessionStorage.setItem('participaEmpresas', JSON.stringify(this.participaEmpresasDatos));

      this.participaDecisionesDatos = {
        ninguno: null,
        participaciones: [],
        aclaracionesObservaciones: ''
      };
      this.declaracionLoaded.declaracion.participacionTomaDecisiones = this.declaracionLoaded.declaracion.participacionTomaDecisiones || this.participaDecisionesDatos;
      this.participaDecisionesDatos = this.declaracionLoaded.declaracion.participacionTomaDecisiones;
      sessionStorage.setItem('participaDesiciones', JSON.stringify(this.participaDecisionesDatos));

      this.apoyosoBeneficiosDatos = {
        ninguno: null,
        apoyos: [],
        aclaracionesObservaciones: ''
      };
      this.declaracionLoaded.declaracion.apoyos = this.declaracionLoaded.declaracion.apoyos || this.apoyosoBeneficiosDatos;
      this.apoyosoBeneficiosDatos = this.declaracionLoaded.declaracion.apoyos;
      sessionStorage.setItem('apoyosBeneficios', JSON.stringify(this.apoyosoBeneficiosDatos));

      this.representacionDatos = {
        ninguno: null,
        representaciones: [],
        aclaracionesObservaciones: ''
      };
      this.declaracionLoaded.declaracion.representaciones = this.declaracionLoaded.declaracion.representaciones || this.representacionDatos;
      this.representacionDatos = this.declaracionLoaded.declaracion.representaciones;
      sessionStorage.setItem('representaciones', JSON.stringify(this.representacionDatos));

      this.clientesDatos = {
        realizaActividadLucrativa: null,
        clientes: [],
        aclaracionesObservaciones: ''
      };
      this.declaracionLoaded.declaracion.clientesPrincipales = this.declaracionLoaded.declaracion.clientesPrincipales || this.clientesDatos;
      this.clientesDatos = this.declaracionLoaded.declaracion.clientesPrincipales;
      sessionStorage.setItem('clientesPrincipales', JSON.stringify(this.clientesDatos));

      this.beneficiosPrivadosDatos = {
        ninguno: null,
        beneficios: [],
        aclaracionesObservaciones: ''
      };

      this.declaracionLoaded.declaracion.beneficiosPrivados = this.declaracionLoaded.declaracion.beneficiosPrivados || this.beneficiosPrivadosDatos;
      this.beneficiosPrivadosDatos = this.declaracionLoaded.declaracion.beneficiosPrivados;
      sessionStorage.setItem('beneficiosPrivados', JSON.stringify(this.beneficiosPrivadosDatos));

      this.fideicomisosDatos = {
        ninguno: null,
        fideicomisos: [],
        aclaracionesObservaciones: ''
      };

      this.declaracionLoaded.declaracion.fideicomisos = this.declaracionLoaded.declaracion.fideicomisos || this.fideicomisosDatos;

      this.fideicomisosDatos = this.declaracionLoaded.declaracion.fideicomisos;

      sessionStorage.setItem('fideicomisos', JSON.stringify(this.fideicomisosDatos));

      this.datosIngresos = {
        tipoRemuneracion: '',
        remuneracionNetaCargoPublico: {
          // @montoMoneda
          monto: 0,
          moneda: {
            id: 96,
            valor: 'PESO MEXICANO'
          }
        },
        otrosIngresosTotal: {
          monto: 0,
          moneda: {
            id: 96,
            valor: 'PESO MEXICANO'
          }
        },
        actividadIndustrialComercialEmpresarial: [],
        actividadFinanciera: [],
        serviciosProfesionales: [],
        otrosIngresos: [],
        enajenacionBienes: [],
        ingresoNetoDeclarante: {
          remuneracion: {
            monto: 0,
            moneda: {
              id: 96,
              valor: 'PESO MEXICANO'
            }
          }
        },
        ingresoNetoParejaDependiente: {
          remuneracion: {
            monto: 0,
            moneda: {
              id: 96,
              valor: 'PESO MEXICANO'
            }
          }
        },
        totalIngresosNetos: {
          remuneracion: {
            monto: 0,
            moneda: {
              id: 96,
              valor: 'PESO MEXICANO'
            }
          }
        },
        aclaracionesObservaciones: ''
      };

      this.datosIngresos = this.declaracionLoaded.declaracion.ingresos || this.datosIngresos;
      sessionStorage.setItem('ingresos', JSON.stringify(this.datosIngresos));

      this.datosActAnt = {
        servidorPublicoAnioAnterior: null,
        actividadAnual: {
          fechaInicio: '',
          fechaConclusion: '',
          tipoRemuneracion: 'ANUAL_ANTERIOR',
          remuneracionNetaCargoPublico: {
            // @montoMoneda
            monto: 0,
            moneda: {
              id: 96,
              valor: 'PESO MEXICANO'
            }
          },
          otrosIngresosTotal: {
            monto: 0,
            moneda: {
              id: 96,
              valor: 'PESO MEXICANO'
            }
          },
          actividadIndustrialComercialEmpresarial: [],
          actividadFinanciera: [],
          serviciosProfesionales: [],
          otrosIngresos: [],
          enajenacionBienes: [],
          ingresoNetoDeclarante: {
            remuneracion: {
              monto: 0,
              moneda: {
                id: 96,
                valor: 'PESO MEXICANO'
              }
            }
          },
          ingresoNetoParejaDependiente: {
            remuneracion: {
              monto: 0,
              moneda: {
                id: 96,
                valor: 'PESO MEXICANO'
              }
            }
          },
          totalIngresosNetos: {
            remuneracion: {
              monto: 0,
              moneda: {
                id: 96,
                valor: 'PESO MEXICANO'
              }
            }
          },

        },
        aclaracionesObservaciones: ''
      };

      this.datosActAnt = this.declaracionLoaded.declaracion.actividadAnualAnterior || this.datosActAnt;
      sessionStorage.setItem('ingresosAnt', JSON.stringify(this.datosActAnt));



      // se crean los objetos en el sessionstorage
      sessionStorage.setItem('datosGenerales', JSON.stringify(this.datosGenerales));
      sessionStorage.setItem('domicilioDeclarante', JSON.stringify(this.domicilioDeclarante));

      sessionStorage.setItem('datosPareja', JSON.stringify(this.datosPareja));
      sessionStorage.setItem('inversiones', JSON.stringify(this.inversiones));
      sessionStorage.setItem('actividadLaboral', JSON.stringify(this.actividadLaboral));

      /*Seccion II Declaracion de intereses*/
      sessionStorage.setItem('participaEmpresas', JSON.stringify(this.participaEmpresasDatos));
      sessionStorage.setItem('participaDesiciones', JSON.stringify(this.participaDecisionesDatos));
      sessionStorage.setItem('apoyosBeneficios', JSON.stringify(this.apoyosoBeneficiosDatos));
      sessionStorage.setItem('representaciones', JSON.stringify(this.representacionDatos));
      sessionStorage.setItem('clientesPrincipales', JSON.stringify(this.clientesDatos));
      sessionStorage.setItem('beneficiosPrivados', JSON.stringify(this.beneficiosPrivadosDatos));
      sessionStorage.setItem('fideicomisos', JSON.stringify(this.fideicomisosDatos));
      sessionStorage.setItem('ingresos', JSON.stringify(this.datosIngresos));
      sessionStorage.setItem('ingresosAnt', JSON.stringify(this.datosActAnt));


      // Llenar menu items con Declaración completa
      // Se reinicia siempre a DECLARA_SIMPLE para evitar duplicados cuando ngOnInit se llama múltiples veces
      this.menuItems = DECLARA_SIMPLE;
      if (this.encabezado.tipoFormato === 'COMPLETO') {
        this.menuItems = this.menuItems.concat(DECLARA_COMPLETA);
      }
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
    if (this.tipoFormato === 'COMPLETO' && this.pareja) {
      this.pareja.ngOnInit();
    }
    this.ingresos.ngOnInit();
    if (this.tDeclaracion === 'INICIO' || this.tDeclaracion === 'CONCLUSION') {
      this.actAnualAnt.ngOnInit();
    }
  }

  renderView() {
    $.each($('section'), function (i) {
      const val = 100 * i;
      $(this).css({
        position: 'absolute',
        top: val + '%'
      }).addClass('section').attr('data-index', i + 1);
    });
  }

  ngAfterViewInit() {
    prepareMenuCuenta();
    this.renderView();
    if (this.globals.flagPresentar1Vez === true) {
      this.globals.flagPresentar1Vez = false;
    }

    // HOVER NAV-MENÚ
    let c;
    $('.menu-nav ul').hover(() => {
      c = false;
      $('.menu-nav').css('width', '300px');
      $('.opacidad').css('display', 'flex');
      $('.opacidad').css('top', '68px;');

      setTimeout(() => {
        if (!c) {
          $('.menu-nav > ul > li > span').css({ 'display': 'flex' });
        }
      }, 200);
    }, () => {
      c = true;
      $('.menu-nav > ul > li > span').css('display', 'none');
      $('.menu-nav').css('width', '40px');
      $('.opacidad').css('display', 'none');

    });

    if (this.globals.flagScroll !== true) {
      this.globals.flagScroll = true;
    }

    $('.btn-success,.observaciones').on('keydown', (event) => {
      if (event.which === 9) {
        this.goForward();
        event.preventDefault();

      }
    });


    const init = JSON.parse(sessionStorage.getItem('declaracionLoaded'));
    this.validaSemaforos(init.declaracion);

  }

  checarN(param) {
    this.flagGuardar = true;
    this.checar(param);
  }

  checar(param) {
    switch (param) {
      case 'curriculares':
        this.datosCurricularesDeclarante = JSON.parse(sessionStorage.getItem('datosCurricularesDeclarante'));
        this.curriculares.getDatos();
        this.modalService.dismissAll();
        break;
      case 'empleo':
        this.datosEmpleo = JSON.parse(sessionStorage.getItem('datosEmpleo'));
        this.empleo.getDatos();
        this.modalService.dismissAll();
        break;
      case 'experiencia':
        this.experienciaLab = JSON.parse(sessionStorage.getItem('experienciaLab'));
        this.expLab.getDatos();
        this.modalService.dismissAll();
        break;
      case 'dependiente':
        this.datosDependiente = JSON.parse(sessionStorage.getItem('datosDependiente'));
        this.dependiente.getDatos();
        this.modalService.dismissAll();
        break;
      case 'inmuebles':
        this.bienesInmuebles = JSON.parse(sessionStorage.getItem('bienesInmuebles'));
        this.inmueble.getDatos();
        this.modalService.dismissAll();
        break;
      case 'vehiculos':
        this.vehiculos = JSON.parse(sessionStorage.getItem('vehiculos'));
        this.vehiculo.getDatos();
        this.modalService.dismissAll();
        break;
      case 'muebles':
        this.bienesMuebles = JSON.parse(sessionStorage.getItem('bienesMuebles'));
        this.mueble.getDatos();
        this.modalService.dismissAll();
        break;
      case 'inversiones':
        this.inversiones = JSON.parse(sessionStorage.getItem('inversiones'));
        this.inversion.getDatos();
        this.modalService.dismissAll();
        break;
      case 'adeudos':
        this.adeudos = JSON.parse(sessionStorage.getItem('adeudos'));
        this.adeudo.getDatos();
        this.modalService.dismissAll();
        break;
      case 'bienInmueble':
        this.prestamoComodatos = JSON.parse(sessionStorage.getItem('prestamoComodato'));
        this.bienInmueble.getDatos();
        this.modalService.dismissAll();
        break;
      case 'bienVehiculo':
        this.prestamoComodatos = JSON.parse(sessionStorage.getItem('prestamoComodato'));
        this.bienVehiculo.getDatos();
        this.modalService.dismissAll();
        break;

      /*Segunda Seccion */
      case 'participaEmpresa':
        this.participaEmpresasDatos = JSON.parse(sessionStorage.getItem('participaEmpresas'));
        this.participacionEmpresas.getDatos();
        this.modalService.dismissAll();
        break;
      case 'participaTomaDecisiones':
        this.participaDecisionesDatos = JSON.parse(sessionStorage.getItem('participaDesiciones'));
        this.participaTomaDecisiones.getDatos();
        this.modalService.dismissAll();
        break;
      case 'apoyosBen':
        this.apoyosoBeneficiosDatos = JSON.parse(sessionStorage.getItem('apoyosBeneficios'));
        this.apoyos.getDatos();
        this.modalService.dismissAll();
        break;
      case 'representacion':
        this.representacionDatos = JSON.parse(sessionStorage.getItem('representaciones'));
        this.representaciones.getDatos();
        this.modalService.dismissAll();
        break;
      case 'clientesPrinc':
        this.clientesDatos = JSON.parse(sessionStorage.getItem('clientesPrincipales'));
        this.clientesPrincipales.getDatos();
        this.modalService.dismissAll();
        break;
      case 'beneficios':
        this.beneficiosPrivadosDatos = JSON.parse(sessionStorage.getItem('beneficiosPrivados'));
        this.beneficiosPrivados.getDatos();
        this.modalService.dismissAll();
        break;
      case 'fideicomisos':
        this.fideicomisosDatos = JSON.parse(sessionStorage.getItem('fideicomisos'));
        this.fideicomisos.getDatos();
        this.modalService.dismissAll();
    }
  }

  validaSemaforos(i: any) {

    if (i.datosGenerales.verificar === true) {
      $('.item1')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item1')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    if (i.domicilioDeclarante && i.domicilioDeclarante.verificar === true) {
      $('.item2')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item2')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    // DATOS CURRICULARES

    const emCaExiste = i.datosCurricularesDeclarante != null ? true : false;
    const emCaNinguno = i.datosCurricularesDeclarante != null && i.datosCurricularesDeclarante.ninguno === true ? true : false;
    let emCaLength;
    let emCaVerificar;
    if (emCaExiste) {
      emCaLength = i.datosCurricularesDeclarante.escolaridad != null && i.datosCurricularesDeclarante.escolaridad.length > 0 ? true : false;

      emCaVerificar = true;
      if (emCaLength) {
        const arr = i.datosCurricularesDeclarante.escolaridad;
        arr.forEach(element => {
          if (!element.verificar) {
            emCaVerificar = false;
          }
        });
      }
    } else {
      emCaLength = false;
      emCaVerificar = false;
    }

    if (emCaExiste && (emCaNinguno || (emCaLength && emCaVerificar))) {
      $('.item3')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item3')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    // EMPLEO CARGO COMISÍON


    const empCcExiste = i.datosEmpleoCargoComision != null ? true : false;
    const empCcNinguno = i.datosEmpleoCargoComision != null && i.datosEmpleoCargoComision.ninguno === true ? true : false;
    let empCcLength;
    let empCcVerificar;
    if (empCcExiste) {
      empCcLength = i.datosEmpleoCargoComision.empleoCargoComision != null && i.datosEmpleoCargoComision.empleoCargoComision.length > 0 ? true : false;

      empCcVerificar = true;
      if (empCcLength) {
        const arr = i.datosEmpleoCargoComision.empleoCargoComision;
        arr.forEach(element => {
          if (!element.verificar) {
            empCcVerificar = false;
          }
        });
      }
    } else {
      empCcLength = false;
      empCcVerificar = false;
    }

    if (empCcExiste && (empCcNinguno || (empCcLength && empCcVerificar))) {
      $('.item4')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item4')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    // EXPERIENCIA LABORAL

    const fExiste = i.experienciasLaborales != null ? true : false;
    const fNinguno = i.experienciasLaborales != null && i.experienciasLaborales.ninguno === true ? true : false;
    let fLength;
    let fVerificar;
    if (fExiste) {
      fLength = i.experienciasLaborales.experienciaLaboral != null && i.experienciasLaborales.experienciaLaboral.length > 0 ? true : false;

      fVerificar = true;
      if (fLength) {
        const arr = i.experienciasLaborales.experienciaLaboral;
        arr.forEach(element => {
          if (!element.verificar) {
            fVerificar = false;
          }
        });
      }
    } else {
      fLength = false;
      fVerificar = false;
    }

    if (fExiste && (fNinguno || (fLength && fVerificar))) {
      $('.item5')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item5')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    // DATOS PAREJA

    const parExiste = i.datosParejas != null && i.datosParejas.ninguno != null ? true : false;
    const parNinguno = i.datosParejas != null && i.datosParejas.ninguno === true ? true : false;
    let parLength;
    let parVerificar;
    if (parExiste) {
      parLength = i.datosParejas.datosParejas != null && i.datosParejas.datosParejas.length > 0 ? true : false;

      parVerificar = true;
      if (parLength) {
        const arr = i.datosParejas.datosParejas;
        arr.forEach(element => {
          if (!element.verificar) {
            parVerificar = false;
          }
        });

      }
    } else {
      parLength = false;
      parVerificar = false;
    }


    if (parExiste && (parNinguno || (parLength && parVerificar))) {
      $('.item6')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item6')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    // DATOS DEPENDIENTE ECONÓMICO

    const ecoExiste = i.datosDependientesEconomicos != null ? true : false;
    const ecoNinguno = i.datosDependientesEconomicos != null && i.datosDependientesEconomicos.ninguno === true ? true : false;
    let ecoLength;
    let ecoVerificar;
    if (ecoExiste) {
      ecoLength = i.datosDependientesEconomicos.datosDependientesEconomicos != null && i.datosDependientesEconomicos.datosDependientesEconomicos.length > 0 ? true : false;

      ecoVerificar = true;
      if (ecoLength) {
        const arr = i.datosDependientesEconomicos.datosDependientesEconomicos;
        arr.forEach(element => {
          if (!element.verificar) {
            ecoVerificar = false;
          }
        });
      }
    } else {
      ecoLength = false;
      ecoVerificar = false;
    }

    if (ecoExiste && (ecoNinguno || (ecoLength && ecoVerificar))) {
      $('.item7')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item7')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }


    // INGRESOS NETOS
    if (i.ingresos != null) {
      $('.item8')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item8')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    // ACTIVIDAD ANUAL ANTERIOR
    if (i.actividadAnualAnterior != null) {
      $('.item9')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item9')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    // BIENES INMUEBLES

    const bInmExiste = i.bienesInmuebles != null ? true : false;
    const bInmNinguno = i.bienesInmuebles != null && i.bienesInmuebles.ninguno === true ? true : false;
    let bInmLength;
    let bInmVerificar;
    if (bInmExiste) {
      bInmLength = i.bienesInmuebles.bienesInmuebles != null && i.bienesInmuebles.bienesInmuebles.length > 0 ? true : false;

      bInmVerificar = true;
      if (bInmLength) {
        const arr = i.bienesInmuebles.bienesInmuebles;
        arr.forEach(element => {
          if (!element.verificar) {
            bInmVerificar = false;
          }
        });
      }
    } else {
      bInmLength = false;
      bInmVerificar = false;
    }

    if (bInmExiste && (bInmNinguno || (bInmLength && bInmVerificar))) {
      $('.item10')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item10')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    // VEHICULOS

    const vehiExiste = i.vehiculos != null ? true : false;
    const vehiNinguno = i.vehiculos != null && i.vehiculos.ninguno === true ? true : false;
    let vehiLength;
    let vehiVerificar;
    if (vehiExiste) {
      vehiLength = i.vehiculos.vehiculos != null && i.vehiculos.vehiculos.length > 0 ? true : false;

      vehiVerificar = true;
      if (vehiLength) {
        const arr = i.vehiculos.vehiculos;
        arr.forEach(element => {
          if (!element.verificar) {
            vehiVerificar = false;
          }
        });
      }
    } else {
      vehiLength = false;
      vehiVerificar = false;
    }

    if (vehiExiste && (vehiNinguno || (vehiLength && vehiVerificar))) {
      $('.item11')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item11')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    // MUEBLES
    const muebExiste = i.bienesMuebles != null ? true : false;
    const muebNinguno = i.bienesMuebles != null && i.bienesMuebles.ninguno === true ? true : false;
    let muebLength;
    let muebVerificar;
    if (muebExiste) {
      muebLength = i.bienesMuebles.bienesMuebles != null && i.bienesMuebles.bienesMuebles.length > 0 ? true : false;

      muebVerificar = true;
      if (muebLength) {
        const arr = i.bienesMuebles.bienesMuebles;
        arr.forEach(element => {
          if (!element.verificar) {
            muebVerificar = false;
          }
        });
      }
    } else {
      muebLength = false;
      muebVerificar = false;
    }

    if (muebExiste && (muebNinguno || (muebLength && muebVerificar))) {
      $('.item12')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item12')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    // INVERSIONES
    const inverExiste = i.inversionesCuentasValores != null ? true : false;
    const inverNinguno = i.inversionesCuentasValores != null && i.inversionesCuentasValores.ninguno === true ? true : false;
    let inverLength;
    let inverVerificar;
    if (inverExiste) {
      inverLength = i.inversionesCuentasValores.inversion != null && i.inversionesCuentasValores.inversion.length > 0 ? true : false;

      inverVerificar = true;
      if (inverLength) {
        const arr = i.inversionesCuentasValores.inversion;
        arr.forEach(element => {
          if (!element.verificar) {
            inverVerificar = false;
          }
        });
      }
    } else {
      inverLength = false;
      inverVerificar = false;
    }

    if (inverExiste && (inverNinguno || (inverLength && inverVerificar))) {
      $('.item13')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item13')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    // ADEUDOS
    const adeuExiste = i.adeudosPasivos != null ? true : false;
    const adeuNinguno = i.adeudosPasivos != null && i.adeudosPasivos.ninguno === true ? true : false;
    let adeuLength;
    let adeuVerificar;
    if (adeuExiste) {
      adeuLength = i.adeudosPasivos.adeudos != null && i.adeudosPasivos.adeudos.length > 0 ? true : false;

      adeuVerificar = true;
      if (adeuLength) {
        const arr = i.adeudosPasivos.adeudos;
        arr.forEach(element => {
          if (!element.verificar) {
            adeuVerificar = false;
          }
        });
      }
    } else {
      adeuLength = false;
      adeuVerificar = false;
    }

    if (adeuExiste && (adeuNinguno || (adeuLength && adeuVerificar))) {
      $('.item14')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item14')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    // PRESTAMO COMODATO

    const pComoExiste = i.prestamoComodato != null ? true : false;
    const pComoNinguno = i.prestamoComodato != null && i.prestamoComodato.ninguno === true ? true : false;
    let pComoLength;
    let pComoVerificar;
    if (pComoExiste) {
      pComoLength = i.prestamoComodato.prestamo != null && i.prestamoComodato.prestamo.length > 0 ? true : false;

      pComoVerificar = true;
      if (pComoLength) {
        const arr = i.prestamoComodato.prestamo;
        arr.forEach(element => {
          if (!element.verificar) {
            pComoVerificar = false;
          }
        });
      }
    } else {
      pComoLength = false;
      pComoVerificar = false;
    }

    if (pComoExiste && (pComoNinguno || (pComoLength && pComoVerificar))) {
      $('.item15')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item15')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    // PARTICIPA EMPRESAS

    const paEmpExiste = i.participaEmpresasSociedadesAsociaciones != null ? true : false;
    const paEmpNinguno = i.participaEmpresasSociedadesAsociaciones != null && i.participaEmpresasSociedadesAsociaciones.ninguno === true ? true : false;
    let paEmpLength;
    let paEmpVerificar;
    if (paEmpExiste) {
      paEmpLength = i.participaEmpresasSociedadesAsociaciones.participaciones != null && i.participaEmpresasSociedadesAsociaciones.participaciones.length > 0 ? true : false;

      paEmpVerificar = true;
      if (paEmpLength) {
        const arr = i.participaEmpresasSociedadesAsociaciones.participaciones;
        arr.forEach(element => {
          if (!element.verificar) {
            paEmpVerificar = false;
          }
        });
      }
    } else {
      paEmpLength = false;
      paEmpVerificar = false;
    }

    if (paEmpExiste && (paEmpNinguno || (paEmpLength && paEmpVerificar))) {
      $('.item16')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item16')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    // PARTICIPA DESICIONES

    const parDeExiste = i.participacionTomaDecisiones != null ? true : false;
    const parDeNinguno = i.participacionTomaDecisiones != null && i.participacionTomaDecisiones.ninguno === true ? true : false;
    let parDeLength;
    let parDeVerificar;
    if (parDeExiste) {
      parDeLength = i.participacionTomaDecisiones.participaciones != null && i.participacionTomaDecisiones.participaciones.length > 0 ? true : false;

      parDeVerificar = true;
      if (parDeLength) {
        const arr = i.participacionTomaDecisiones.participaciones;
        arr.forEach(element => {
          if (!element.verificar) {
            parDeVerificar = false;
          }
        });
      }
    } else {
      parDeLength = false;
      parDeVerificar = false;
    }

    if (parDeExiste && (parDeNinguno || (parDeLength && parDeVerificar))) {
      $('.item17')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item17')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    // APOYOS
    const apoExiste = i.apoyos != null ? true : false;
    const apoNinguno = i.apoyos != null && i.apoyos.ninguno === true ? true : false;
    let apoLength;
    let apoVerificar;
    if (apoExiste) {
      apoLength = i.apoyos.apoyos != null && i.apoyos.apoyos.length > 0 ? true : false;

      apoVerificar = true;
      if (apoLength) {
        const arr = i.apoyos.apoyos;
        arr.forEach(element => {
          if (!element.verificar) {
            apoVerificar = false;
          }
        });
      }
    } else {
      apoLength = false;
      apoVerificar = false;
    }

    if (apoExiste && (apoNinguno || (apoLength && apoVerificar))) {
      $('.item18')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item18')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    // REPRESENTACIONES
    const repreExiste = i.representaciones != null ? true : false;
    const repreNinguno = i.representaciones != null && i.representaciones.ninguno === true ? true : false;
    let repreLength;
    let repreVerificar;
    if (repreExiste) {
      repreLength = i.representaciones.representaciones != null && i.representaciones.representaciones.length > 0 ? true : false;

      repreVerificar = true;
      if (repreLength) {
        const arr = i.representaciones.representaciones;
        arr.forEach(element => {
          if (!element.verificar) {
            repreVerificar = false;
          }
        });
      }
    } else {
      repreLength = false;
      repreVerificar = false;
    }

    if (repreExiste && (repreNinguno || (repreLength && repreVerificar))) {
      $('.item19')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item19')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    // CLIENTES PRINCIPALES

    const cliExiste = i.clientesPrincipales != null && i.clientesPrincipales.realizaActividadLucrativa != null ? true : false;
    const cliNinguno = i.clientesPrincipales != null && i.clientesPrincipales.realizaActividadLucrativa === true ? false : true;
    let cliLength;
    let cliVerificar;
    if (cliExiste) {
      cliLength = i.clientesPrincipales.clientes != null && i.clientesPrincipales.clientes.length > 0 ? true : false;

      cliVerificar = true;
      if (cliLength) {
        const arr = i.clientesPrincipales.clientes;
        arr.forEach(element => {
          if (!element.verificar) {
            cliVerificar = false;
          }
        });
      }
    } else {
      cliLength = false;
      cliVerificar = false;
    }

    if (cliExiste && (cliNinguno || (cliLength && cliVerificar))) {
      $('.item20')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item20')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }






    // BENEFICIOS PRIVADOS

    const beneExiste = i.beneficiosPrivados != null ? true : false;
    const beneNinguno = i.beneficiosPrivados != null && i.beneficiosPrivados.ninguno === true ? true : false;
    let beneLength;
    let beneVerificar;
    if (beneExiste) {
      beneLength = i.beneficiosPrivados.beneficios != null && i.beneficiosPrivados.beneficios.length > 0 ? true : false;

      beneVerificar = true;
      if (beneLength) {
        const arr = i.beneficiosPrivados.beneficios;
        arr.forEach(element => {
          if (!element.verificar) {
            beneVerificar = false;
          }
        });
      }
    } else {
      beneLength = false;
      beneVerificar = false;
    }


    if (beneExiste && (beneNinguno || (beneLength && beneVerificar))) {
      $('.item21')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item21')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }

    // FIDEISOMISOS
    const fideExiste = i.fideicomisos != null ? true : false;
    const fideNinguno = i.fideicomisos != null && i.fideicomisos.ninguno === true ? true : false;
    let fideLength;
    let fideVerificar;
    if (fideExiste) {
      fideLength = i.fideicomisos.fideicomisos != null && i.fideicomisos.fideicomisos.length > 0 ? true : false;

      fideVerificar = true;
      if (fideLength) {
        const arr = i.fideicomisos.fideicomisos;
        arr.forEach(element => {
          if (!element.verificar) {
            fideVerificar = false;
          }
        });
      }
    } else {
      fideLength = false;
      fideVerificar = false;
    }

    if (fideExiste && (fideNinguno || (fideLength && fideVerificar))) {
      $('.item22')
        .find('i').removeClass('fa-times-circle foco-rojo')
        .addClass('far fa-check-circle foco-verde');
    } else {
      $('.item22')
        .find('i').removeClass('foco-verde')
        .addClass('foco-rojo');
    }
  }

  aclaraciones(content, obj?: any) {
    switch (content) {
      case 1:
        this.datosCurricularesDeclarante.aclaracionesObservaciones = obj;
        this.curriculares.getDatos();
        this.flagGuardar = true;
        break;
      case 2:
        this.datosEmpleo.aclaracionesObservaciones = obj;
        this.empleo.getDatos();
        this.flagGuardar = true;
        break;
      case 3:
        this.experienciaLab.aclaracionesObservaciones = obj;
        this.expLab.getDatos();
        this.flagGuardar = true;
        break;
      case 4:
        this.datosDependiente.aclaracionesObservaciones = obj;
        this.dependiente.getDatos();
        this.flagGuardar = true;
        break;
      case 5:

        this.bienesInmuebles.aclaracionesObservaciones = obj;
        this.inmueble.getDatos();
        this.flagGuardar = true;
        break;
      case 6:
        this.vehiculos.aclaracionesObservaciones = obj;
        this.vehiculo.getDatos();
        this.flagGuardar = true;
        break;
      case 7:
        this.bienesMuebles.aclaracionesObservaciones = obj;
        this.mueble.getDatos();
        this.flagGuardar = true;
        break;
      case 8:
        this.inversiones.aclaracionesObservaciones = obj;
        this.inversion.getDatos();
        this.flagGuardar = true;
        break;
      case 9:
        this.adeudos.aclaracionesObservaciones = obj;
        this.adeudo.getDatos();
        this.flagGuardar = true;
        break;
      case 10:
        this.prestamoComodatos.aclaracionesObservaciones = obj;
        this.bienInmueble.getDatos();
        this.flagGuardar = true;
        break;
      /*Segunda Seccion */
      case 12:
        this.participaEmpresasDatos.aclaracionesObservaciones = obj;
        this.participacionEmpresas.getDatos();
        this.flagGuardar = true;
        break;
      case 13:
        this.participaDecisionesDatos.aclaracionesObservaciones = obj;
        this.participaTomaDecisiones.getDatos();
        this.flagGuardar = true;
        break;
      case 14:
        this.apoyosoBeneficiosDatos.aclaracionesObservaciones = obj;
        this.apoyos.getDatos();
        this.flagGuardar = true;

        break;
      case 15:
        this.representacionDatos.aclaracionesObservaciones = obj;
        this.representaciones.getDatos();
        this.flagGuardar = true;

        break;
      case 16:
        this.clientesDatos.aclaracionesObservaciones = obj;
        this.clientesPrincipales.getDatos();
        this.flagGuardar = true;

        break;
      case 17:
        this.beneficiosPrivadosDatos.aclaracionesObservaciones = obj;
        this.beneficiosPrivados.getDatos();
        this.flagGuardar = true;

        break;
      case 18:
        this.fideicomisosDatos.aclaracionesObservaciones = obj;
        this.fideicomisos.getDatos();
        this.flagGuardar = true;
        break;

    }
  }

  open(content, obj?: any) {

    $('body').addClass('disabled-onepage-scroll');

    switch (content) {

      case 1:
        this.modalRef = this.modalService.open(DatosCurricularesModalComponent, {
          centered: true,
          size: 'xl',
          scrollable: true
        });
        if (obj) {
          this.modalRef.componentInstance.objUpdate = JSON.parse(JSON.stringify(obj));
        }
        this.modalRef.result.then((result) => {
          if (result.result === 'success') {
            this.showToast('Datos curriculares');
            this.checar(result.form);
            this.flagGuardar = true;
          }
        }, (reason) => {
        });

        break;

      case 2:
        this.modalRef = this.modalService.open(DatosEmpleoModalComponent, {
          centered: true,
          size: 'xl',
          scrollable: true
        });
        if (obj) {
          console.log('desde switch', obj);

          this.modalRef.componentInstance.objUpdate = JSON.parse(JSON.stringify(obj));
        }
        this.modalRef.result.then((result) => {
          if (result.result === 'success') {
            this.showToast('Datos empleo');
            this.checar(result.form);
            this.flagGuardar = true;
          }
        }, (reason) => {
          this.checar(reason);
        });

        break;

      case 3:
        this.modalRef = this.modalService.open(ExperienciaLaboralModalComponent, {
          centered: true,
          size: 'xl',
          scrollable: true
        });
        if (obj) {
          this.modalRef.componentInstance.objUpdate = JSON.parse(JSON.stringify(obj));
        }
        this.modalRef.result.then((result) => {
          if (result.result === 'success') {
            this.showToast('Experiencia Laboral');
            this.checar(result.form);
            this.flagGuardar = true;
          }
        }, (reason) => {
          this.checar(reason);
        });

        break;

      case 4:
        this.modalRef = this.modalService.open(DatosDependienteModalComponent, {
          centered: true,
          size: 'xl',
          scrollable: true
        });
        if (obj) {
          this.modalRef.componentInstance.objUpdate = JSON.parse(JSON.stringify(obj));
        }
        this.modalRef.result.then((result) => {
          if (result.result === 'success') {
            this.showToast('Dependiente económico');
            this.checar(result.form);
            this.flagGuardar = true;
          }
        }, (reason) => {
          this.checar(reason);
        });

        break;

      case 5:
        this.modalRef = this.modalService.open(BienesInmueblesModalComponent, {
          centered: true,
          size: 'xl',
          scrollable: true
        });
        if (obj) {
          this.modalRef.componentInstance.objUpdate = JSON.parse(JSON.stringify(obj));
        }
        this.modalRef.result.then((result) => {
          if (result.result === 'success') {
            this.showToast('Bienes Inmuebles');
            this.checar(result.form);
            this.flagGuardar = true;
            this.calculaEnajenacion();
          }
        }, (reason) => {
          this.checar(reason);
        });

        break;

      case 6:
        this.modalRef = this.modalService.open(VehiculosModalComponent, {
          centered: true,
          size: 'xl',
          scrollable: true
        });
        if (obj) {
          this.modalRef.componentInstance.objUpdate = JSON.parse(JSON.stringify(obj));
        }
        this.modalRef.result.then((result) => {
          if (result.result === 'success') {
            this.showToast('Vehículos');
            this.checar(result.form);
            this.flagGuardar = true;
            this.calculaEnajenacion();
          }
        }, (reason) => {
          this.checar(reason);
        });

        break;

      case 7:
        this.modalRef = this.modalService.open(BienesMueblesModalComponent, {
          centered: true,
          size: 'xl',
          scrollable: true
        });
        if (obj) {
          this.modalRef.componentInstance.objUpdate = JSON.parse(JSON.stringify(obj));
        }
        this.modalRef.result.then((result) => {
          if (result.result === 'success') {
            this.showToast('Bienes muebles');
            this.checar(result.form);
            this.flagGuardar = true;
            this.calculaEnajenacion();
          }
        }, (reason) => {
          this.checar(reason);
        });

        break;

      case 8:
        this.modalRef = this.modalService.open(InversionesModalComponent, {
          centered: true,
          size: 'xl',
          scrollable: true
        });
        if (obj) {
          this.modalRef.componentInstance.objUpdate = JSON.parse(JSON.stringify(obj));
        }
        this.modalRef.result.then((result) => {
          if (result.result === 'success') {
            this.showToast('Inversiones');
            this.checar(result.form);
            this.flagGuardar = true;
          }
        }, (reason) => {
          this.checar(reason);
        });
        break;

      case 9:
        this.modalRef = this.modalService.open(AdeudosModalComponent, {
          centered: true,
          size: 'xl',
          scrollable: true
        });
        if (obj) {
          this.modalRef.componentInstance.objUpdate = JSON.parse(JSON.stringify(obj));
        }
        this.modalRef.result.then((result) => {
          if (result.result === 'success') {
            this.showToast('Adeudos');
            this.checar(result.form);
            this.flagGuardar = true;
          }
        }, (reason) => {
          this.checar(reason);
        });
        break;


      case 10:
        this.modalRef = this.modalService.open(PrestamoInmuebleModalComponent, {
          centered: true,
          size: 'xl',
          scrollable: true
        });
        if (obj) {
          this.modalRef.componentInstance.objUpdate = JSON.parse(JSON.stringify(obj));
        }
        this.modalRef.result.then((result) => {
          if (result.result === 'success') {
            this.showToast('Prestamo Comodato Inmueble');
            this.checar(result.form);
            this.flagGuardar = true;
          }
        }, (reason) => {
          this.checar(reason);
        });
        break;

      case 11:
        this.modalRef = this.modalService.open(PrestamoVehiculoModalComponent, {
          centered: true,
          size: 'xl',
          scrollable: true
        });
        if (obj) {
          this.modalRef.componentInstance.objUpdate = JSON.parse(JSON.stringify(obj));
        }
        this.modalRef.result.then((result) => {
          if (result.result === 'success') {
            this.showToast('Prestamo Comodato Vehículo');
            this.checar(result.form);
            this.flagGuardar = true;
          }
        }, (reason) => {
          this.checar(reason);
        });
        break;

      case 12:
        this.modalRef = this.modalService.open(ParticipacionEmpresasModalComponent, {
          centered: true,
          size: 'xl',
          scrollable: true
        });
        if (obj) {
          this.modalRef.componentInstance.objUpdate = JSON.parse(JSON.stringify(obj));
        }
        this.modalRef.result.then((result) => {
          if (result.result === 'success') {
            this.showToast('Participación empresas');
            this.checar(result.form);
            this.flagGuardar = true;
          }
        }, (reason) => {
          this.checar(reason);
        });

        break;

      case 13:
        this.modalRef = this.modalService.open(ParticipaDesicionesModalComponent, {
          centered: true,
          size: 'xl',
          scrollable: true
        });
        if (obj) {
          this.modalRef.componentInstance.objUpdate = JSON.parse(JSON.stringify(obj));
        }
        this.modalRef.result.then((result) => {
          if (result.result === 'success') {
            this.showToast('Participación decisiones');
            this.checar(result.form);
            this.flagGuardar = true;
          }
        }, (reason) => {
          this.checar(reason);
        });

        break;

      case 14:
        this.modalRef = this.modalService.open(ApoyosModalComponent, {
          centered: true,
          size: 'xl',
          scrollable: true
        });
        if (obj) {
          this.modalRef.componentInstance.objUpdate = JSON.parse(JSON.stringify(obj));
        }
        this.modalRef.result.then((result) => {
          if (result.result === 'success') {
            this.showToast('Apoyos');
            this.checar(result.form);
            this.flagGuardar = true;
          }
        }, (reason) => {
          this.checar(reason);
        });
        break;

      case 15:
        this.modalRef = this.modalService.open(RepresentacionModalComponent, {
          centered: true,
          size: 'xl',
          scrollable: true
        });
        if (obj) {
          this.modalRef.componentInstance.objUpdate = JSON.parse(JSON.stringify(obj));
        }
        this.modalRef.result.then((result) => {
          if (result.result === 'success') {
            this.showToast('Representación');
            this.checar(result.form);
            this.flagGuardar = true;
          }
        }, (reason) => {
          this.checar(reason);
        });
        break;

      case 16:
        this.modalRef = this.modalService.open(ClientesModalComponent, {
          centered: true,
          size: 'xl',
          scrollable: true
        });
        if (obj) {
          this.modalRef.componentInstance.objUpdate = JSON.parse(JSON.stringify(obj));
        }
        this.modalRef.result.then((result) => {
          if (result.result === 'success') {
            this.showToast('Clientes principales');
            this.checar(result.form);
            this.flagGuardar = true;
          }
        }, (reason) => {
          this.checar(reason);
        });
        break;

      case 17:
        this.modalRef = this.modalService.open(BeneficiosModalComponent, {
          centered: true,
          size: 'xl',
          scrollable: true
        });
        if (obj) {
          this.modalRef.componentInstance.objUpdate = JSON.parse(JSON.stringify(obj));
        }
        this.modalRef.result.then((result) => {
          if (result.result === 'success') {
            this.showToast('Beneficios');
            this.checar(result.form);
            this.flagGuardar = true;
          }
        }, (reason) => {
          this.checar(reason);
        });
        break;

      case 18:
        this.modalRef = this.modalService.open(FideicomisosModalComponent, {
          centered: true,
          size: 'xl',
          scrollable: true
        });
        if (obj) {
          this.modalRef.componentInstance.objUpdate = JSON.parse(JSON.stringify(obj));
        }
        this.modalRef.result.then((result) => {
          if (result.result === 'success') {
            this.showToast('Fideicomisos');
            this.checar(result.form);
            this.flagGuardar = true;
          }
        }, (reason) => {
          this.checar(reason);
        });
        break;

      case 19:
        this.modalRef = this.modalService.open(FirmaModalComponent, {
          centered: true,
          size: 'lg',
          scrollable: true
        });
        this.modalRef.result.then((result) => {
          if (result === 'success') {
          }
        }, (reason) => {
          this.checar(reason);
        });
        break;
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

  delete(content, obj: any) {

    switch (content) {

      case 1:
        this.datosCurricularesDeclarante.escolaridad.splice(this.datosCurricularesDeclarante.escolaridad.findIndex(i => i.idPosicionVista === obj.idPosicionVista), 1);
        this.datosCurricularesDeclarante.escolaridad.forEach(
          (element, index) => {
            this.datosCurricularesDeclarante.escolaridad[index].idPosicionVista = index + 1;
          });
        sessionStorage.setItem('datosCurricularesDeclarante', JSON.stringify(this.datosCurricularesDeclarante));
        this.curriculares.getDatos();
        this.flagGuardar = true;
        break;
      case 2:
        this.datosEmpleo.empleoCargoComision.splice(this.datosEmpleo.empleoCargoComision.findIndex(i => i.idPosicionVista === obj.idPosicionVista), 1);
        if (this.datosEmpleo.empleoCargoComision.length === 0) {
          this.datosEmpleo.ninguno = null;
        }
        this.datosEmpleo.empleoCargoComision.forEach(
          (element, index) => {
            this.datosEmpleo.empleoCargoComision[index].idPosicionVista = index + 1;
          });
        sessionStorage.setItem('datosEmpleo', JSON.stringify(this.datosEmpleo));
        this.empleo.getDatos();
        this.flagGuardar = true;
        break;
      case 3:
        this.experienciaLab.experienciaLaboral.splice(this.experienciaLab.experienciaLaboral.findIndex(i => i.idPosicionVista === obj.idPosicionVista), 1);
        if (this.experienciaLab.experienciaLaboral.length === 0) {
          this.experienciaLab.ninguno = null;
        }

        this.experienciaLab.experienciaLaboral.forEach(
          (element, index) => {
            this.experienciaLab.experienciaLaboral[index].idPosicionVista = index + 1;
          });
        sessionStorage.setItem('experienciaLab', JSON.stringify(this.experienciaLab));
        this.expLab.getDatos();
        this.flagGuardar = true;
        break;
      case 4:
        this.datosDependiente.datosDependientesEconomicos.splice(this.datosDependiente.datosDependientesEconomicos.findIndex(i => i.idPosicionVista === obj.idPosicionVista), 1);
        this.datosDependiente.datosDependientesEconomicos.forEach(
          (element, index) => {
            this.datosDependiente.datosDependientesEconomicos[index].idPosicionVista = index + 1;
          });

        sessionStorage.setItem('datosDependiente', JSON.stringify(this.datosDependiente));
        this.dependiente.getDatos();
        this.flagGuardar = true;
        break;
      case 5:
        this.bienesInmuebles.bienesInmuebles.splice(this.bienesInmuebles.bienesInmuebles.findIndex(i => i.idPosicionVista === obj.idPosicionVista), 1);
        this.bienesInmuebles.bienesInmuebles.forEach(
          (element, index) => {
            this.bienesInmuebles.bienesInmuebles[index].idPosicionVista = index + 1;
          });

        sessionStorage.setItem('bienesInmuebles', JSON.stringify(this.bienesInmuebles));
        this.inmueble.getDatos();
        this.flagGuardar = true;
        break;
      case 6:
        this.vehiculos.vehiculos.splice(this.vehiculos.vehiculos.findIndex(i => i.idPosicionVista === obj.idPosicionVista), 1);
        if (this.vehiculos.vehiculos.length === 0) {
          this.vehiculos.ninguno = null;
        }
        this.vehiculos.vehiculos.forEach(
          (element, index) => {
            this.vehiculos.vehiculos[index].idPosicionVista = index + 1;
          });

        sessionStorage.setItem('vehiculos', JSON.stringify(this.vehiculos));

        this.vehiculo.getDatos();
        this.flagGuardar = true;
        break;
      case 7:
        this.bienesMuebles.bienesMuebles.splice(this.bienesMuebles.bienesMuebles.findIndex(i => i.idPosicionVista === obj.idPosicionVista), 1);
        if (this.bienesMuebles.bienesMuebles.length === 0) {
          this.bienesMuebles.ninguno = null;
        }
        this.bienesMuebles.bienesMuebles.forEach(
          (element, index) => {
            this.bienesMuebles.bienesMuebles[index].idPosicionVista = index + 1;
          });

        sessionStorage.setItem('bienesMuebles', JSON.stringify(this.bienesMuebles));

        this.mueble.getDatos();
        this.flagGuardar = true;
        break;
      case 8:
        this.inversiones.inversion.splice(this.inversiones.inversion.findIndex(i => i.idPosicionVista === obj.idPosicionVista), 1);
        if (this.inversiones.inversion.length === 0) {
          this.inversiones.ninguno = null;
        }
        this.inversiones.inversion.forEach(
          (element, index) => {
            this.inversiones.inversion[index].idPosicionVista = index + 1;
          });

        sessionStorage.setItem('inversiones', JSON.stringify(this.inversiones));

        this.inversion.getDatos();
        this.flagGuardar = true;
        break;
      case 9:
        this.adeudos.adeudos.splice(this.adeudos.adeudos.findIndex(i => i.idPosicionVista === obj.idPosicionVista), 1);
        if (this.adeudos.adeudos.length === 0) {
          this.adeudos.ninguno = null;
        }
        this.adeudos.adeudos.forEach(
          (element, index) => {
            this.adeudos.adeudos[index].idPosicionVista = index + 1;
          });

        sessionStorage.setItem('adeudos', JSON.stringify(this.adeudos));

        this.adeudo.getDatos();
        this.flagGuardar = true;
        break;
      case 10:
        this.prestamoComodatos.prestamo.splice(this.prestamoComodatos.prestamo.findIndex(i => i.idPosicionVista === obj.idPosicionVista), 1);
        if (this.prestamoComodatos.prestamo.length === 0) {
          this.prestamoComodatos.ninguno = null;
        }
        this.prestamoComodatos.prestamo.forEach(
          (element, index) => {
            this.prestamoComodatos.prestamo[index].idPosicionVista = index + 1;
          });

        sessionStorage.setItem('prestamoComodato', JSON.stringify(this.prestamoComodatos));

        this.bienInmueble.getDatos();
        this.flagGuardar = true;
        break;


      case 12:
        this.participaEmpresasDatos.participaciones.splice(this.participaEmpresasDatos.participaciones.findIndex(i => i.idPosicionVista === obj.idPosicionVista), 1);
        if (this.participaEmpresasDatos.participaciones.length === 0) {
          this.participaEmpresasDatos.ninguno = null;
        }
        this.participaEmpresasDatos.participaciones.forEach(
          (element, index) => {
            this.participaEmpresasDatos.participaciones[index].idPosicionVista = index + 1;
          });

        sessionStorage.setItem('participaEmpresas', JSON.stringify(this.participaEmpresasDatos));
        this.participacionEmpresas.getDatos();
        this.flagGuardar = true;
        break;


      case 13:
        this.participaDecisionesDatos.participaciones.splice(this.participaDecisionesDatos.participaciones.findIndex(i => i.idPosicionVista === obj.idPosicionVista), 1);
        if (this.participaDecisionesDatos.participaciones.length === 0) {
          this.participaDecisionesDatos.ninguno = null;
        }
        this.participaDecisionesDatos.participaciones.forEach(
          (element, index) => {
            this.participaDecisionesDatos.participaciones[index].idPosicionVista = index + 1;
          });
        sessionStorage.setItem('participaDesiciones', JSON.stringify(this.participaDecisionesDatos));
        this.participaTomaDecisiones.getDatos();
        this.flagGuardar = true;
        break;

      case 14:
        this.apoyosoBeneficiosDatos.apoyos.splice(this.apoyosoBeneficiosDatos.apoyos.findIndex(i => i.idPosicionVista === obj.idPosicionVista), 1);
        if (this.apoyosoBeneficiosDatos.apoyos.length === 0) {
          this.apoyosoBeneficiosDatos.ninguno = null;
        }
        this.apoyosoBeneficiosDatos.apoyos.forEach(
          (element, index) => {
            this.apoyosoBeneficiosDatos.apoyos[index].idPosicionVista = index + 1;
          });

        sessionStorage.setItem('apoyosBeneficios', JSON.stringify(this.apoyosoBeneficiosDatos));
        this.apoyos.getDatos();
        this.flagGuardar = true;
        break;

      case 15:
        this.representacionDatos.representaciones.splice(this.representacionDatos.representaciones.findIndex(i => i.idPosicionVista === obj.idPosicionVista), 1);
        this.representacionDatos.representaciones.forEach(
          (element, index) => {
            this.representacionDatos.representaciones[index].idPosicionVista = index + 1;
          });
        sessionStorage.setItem('representaciones', JSON.stringify(this.representacionDatos));
        this.representaciones.getDatos();
        this.flagGuardar = true;
        break;

      case 16:
        this.clientesDatos.clientes.splice(this.clientesDatos.clientes.findIndex(i => i.idPosicionVista === obj.idPosicionVista), 1);
        this.clientesDatos.clientes.forEach(
          (element, index) => {
            this.clientesDatos.clientes[index].idPosicionVista = index + 1;
          });
        sessionStorage.setItem('clientesPrincipales', JSON.stringify(this.clientesDatos));
        this.clientesPrincipales.getDatos();
        this.flagGuardar = true;
        break;

      case 17:
        this.beneficiosPrivadosDatos.beneficios.splice(this.beneficiosPrivadosDatos.beneficios.findIndex(i => i.idPosicionVista === obj.idPosicionVista), 1);
        this.beneficiosPrivadosDatos.beneficios.forEach(
          (element, index) => {
            this.beneficiosPrivadosDatos.beneficios[index].idPosicionVista = index + 1;
          });

        sessionStorage.setItem('beneficiosPrivados', JSON.stringify(this.beneficiosPrivadosDatos));
        this.beneficiosPrivados.getDatos();
        this.flagGuardar = true;
        break;

      case 18:
        this.fideicomisosDatos.fideicomisos.splice(this.fideicomisosDatos.fideicomisos.findIndex(i => i.idPosicionVista === obj.idPosicionVista), 1);
        this.fideicomisosDatos.fideicomisos.forEach(
          (element, index) => {
            this.fideicomisosDatos.fideicomisos[index].idPosicionVista = index + 1;
          });

        sessionStorage.setItem('fideicomisos', JSON.stringify(this.fideicomisosDatos));
        this.fideicomisos.getDatos();
        this.flagGuardar = true;
        break;
    }
  }

  guardaDeclaracion() {
    $('#content-cargando').css('display', 'flex');
    this.flagGuardando = true;
    try {

      this.sessionService.renovarToken()
        .subscribe(
          ren => {
            const datosEmpleoJSON = JSON.stringify(this.datosEmpleo);
            let datosEmpleoObj: DatosEmpleo;
            if (this.datosEmpleo.ninguno !== null) {
              datosEmpleoObj = JSON.parse(datosEmpleoJSON);
              delete datosEmpleoObj.ninguno;
            }
            if (this.datosGenerales.verificar !== true) {
              $('#modalErrorDGButton').click();
              this.flagGuardar = false;
              this.flagGuardando = false;
              return;
            }
            this.mensajeCambioFormate = null;
            if (this.domicilioDeclarante.domicilio.ubicacion === 'MEXICO') {
              this.domicilioDeclarante.domicilio.domicilioExtranjero = null;
            } else if (this.domicilioDeclarante.domicilio.ubicacion === 'EXTRANJERO') {
              this.domicilioDeclarante.domicilio.domicilioMexico = null;
            }
            const datosCurricularesDeclaranteJSON = JSON.stringify(this.datosCurricularesDeclarante);
            let datosCurricularesDeclaranteObj: DatosCurricularesDeclarante;
            if (this.datosCurricularesDeclarante.ninguno !== null) {
              datosCurricularesDeclaranteObj = JSON.parse(datosCurricularesDeclaranteJSON);
              delete datosCurricularesDeclaranteObj.ninguno;
            }

            const datosParejaJSON = JSON.stringify(this.datosPareja);
            let datosParejaObj: DatosPareja;
            datosParejaObj = JSON.parse(datosParejaJSON);
            if (this.datosPareja.ninguno === true) {
              datosParejaObj.datosParejas = null;
            } else {
              if (this.datosPareja.datosParejas[0].habitaDomicilioDeclarante === true) {
                datosParejaObj.datosParejas[0].domicilioDependienteEconomico = null;
              } else {
                switch (datosParejaObj.datosParejas[0].domicilioDependienteEconomico.lugarDondeReside) {
                  case 'MEXICO':
                    datosParejaObj.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioExtranjero = null;
                    datosParejaObj.datosParejas[0].domicilioDependienteEconomico.domicilio.ubicacion = datosParejaObj.datosParejas[0].domicilioDependienteEconomico.lugarDondeReside;
                    datosParejaObj.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioMexico.entidadFederativa = datosParejaObj.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioMexico?.entidadFederativa?.id ?
                      datosParejaObj.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioMexico.entidadFederativa : null;
                    datosParejaObj.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioMexico.municipioAlcaldia = datosParejaObj.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioMexico?.municipioAlcaldia?.id ?
                      datosParejaObj.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioMexico.municipioAlcaldia : null;
                    break;
                  case 'EXTRANJERO':
                    datosParejaObj.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioMexico = null;
                    datosParejaObj.datosParejas[0].domicilioDependienteEconomico.domicilio.ubicacion = datosParejaObj.datosParejas[0].domicilioDependienteEconomico.lugarDondeReside;
                    datosParejaObj.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioExtranjero.pais = datosParejaObj.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioExtranjero.pais?.id ?
                      datosParejaObj.datosParejas[0].domicilioDependienteEconomico.domicilio.domicilioExtranjero.pais : null;
                    break;
                  case 'SE_DESCONOCE':
                    datosParejaObj.datosParejas[0].domicilioDependienteEconomico.domicilio = null;
                    break;
                }
              }
              if (this.datosPareja.datosParejas[0].ninguno === true) {
                datosParejaObj.datosParejas[0].actividadLaboral = null;
              } else if (datosParejaObj.datosParejas[0].actividadLaboral.ambitoSector.id === 2
                || datosParejaObj.datosParejas[0].actividadLaboral.ambitoSector.id === 9999) {
                datosParejaObj.datosParejas[0].actividadLaboral.sectorPublico = null;
              } else if (datosParejaObj.datosParejas[0].actividadLaboral.ambitoSector.id === 1) {
                datosParejaObj.datosParejas[0].actividadLaboral.sectorPrivadoOtro = null;
              }


            }
            this.limpiaVersion();
            let recepcion: Recepcion;
            if (this.tipoFormato === 'COMPLETO') {
              recepcion = {
                encabezado: this.declaracionLoaded.encabezado,
                numeroDeclaracionPrecarga: this.declaracionLoaded.numeroDeclaracionPrecarga || null,
                firmada: false,
                institucionReceptora: this.declaracionLoaded.institucionReceptora,
                declaracion: {
                  datosGenerales: this.datosGenerales,
                  domicilioDeclarante: this.domicilioDeclarante.domicilio.ubicacion ? this.domicilioDeclarante : null,
                  datosCurricularesDeclarante: this.datosCurricularesDeclarante.ninguno !== null ? datosCurricularesDeclaranteObj : null,
                  datosEmpleoCargoComision: this.datosEmpleo.empleoCargoComision.length > 0 ? datosEmpleoObj : null,
                  experienciasLaborales: this.experienciaLab.ninguno !== null ? this.experienciaLab : null,
                  ingresos: this.datosIngresos.tipoRemuneracion === '' ? null : this.datosIngresos,
                  actividadAnualAnterior: this.datosActAnt.servidorPublicoAnioAnterior === null ? null : this.datosActAnt,
                  bienesMuebles: this.bienesMuebles.ninguno !== null ? this.bienesMuebles : null,
                  inversionesCuentasValores: this.inversiones.ninguno !== null ? this.inversiones : null,
                  adeudosPasivos: this.adeudos.ninguno !== null ? this.adeudos : null,
                  fideicomisos: this.fideicomisosDatos.ninguno !== null ? this.fideicomisosDatos : null,
                  beneficiosPrivados: this.beneficiosPrivadosDatos.ninguno !== null ? this.beneficiosPrivadosDatos : null,
                  clientesPrincipales: this.clientesDatos.realizaActividadLucrativa !== null ? this.clientesDatos : null,
                  representaciones: this.representacionDatos.ninguno !== null ? this.representacionDatos : null,
                  apoyos: this.apoyosoBeneficiosDatos.ninguno !== null ? this.apoyosoBeneficiosDatos : null,
                  participacionTomaDecisiones: this.participaDecisionesDatos.ninguno !== null ? this.participaDecisionesDatos : null,
                  participaEmpresasSociedadesAsociaciones: this.participaEmpresasDatos.ninguno !== null ? this.participaEmpresasDatos : null,
                  datosParejas: this.datosPareja.ninguno !== null ? datosParejaObj : null,
                  vehiculos: this.vehiculos.ninguno !== null ? this.vehiculos : null,
                  datosDependientesEconomicos: this.datosDependiente.ninguno !== null ? this.datosDependiente : null,
                  bienesInmuebles: this.bienesInmuebles.ninguno !== null ? this.bienesInmuebles : null,
                  prestamoComodato: this.prestamoComodatos.ninguno !== null ? this.prestamoComodatos : null

                }
              };
            } else if (this.tipoFormato === 'SIMPLIFICADO') {
              recepcion = {
                encabezado: this.declaracionLoaded.encabezado,
                numeroDeclaracionPrecarga: this.declaracionLoaded.numeroDeclaracionPrecarga || null,
                firmada: false,
                institucionReceptora: this.declaracionLoaded.institucionReceptora,
                declaracion: {
                  datosGenerales: this.datosGenerales,
                  domicilioDeclarante: this.domicilioDeclarante.verificar ? this.domicilioDeclarante : null,
                  datosCurricularesDeclarante: this.datosCurricularesDeclarante.ninguno !== null ? datosCurricularesDeclaranteObj : null,
                  datosEmpleoCargoComision: this.datosEmpleo.empleoCargoComision.length > 0 ? datosEmpleoObj : null,
                  experienciasLaborales: this.experienciaLab.ninguno !== null ? this.experienciaLab : null,
                  ingresos: this.datosIngresos.tipoRemuneracion === '' ? null : this.datosIngresos,
                  actividadAnualAnterior: this.datosActAnt.servidorPublicoAnioAnterior === null ? null : this.datosActAnt,
                }
              };
            }


            this.guardarDeclaracionError = null;
            this.guardarDeclaracionErrorArr = null;
            this.graphqlService.guardaDeclaracion(recepcion).subscribe(respuesta => {
              console.log('respuesta', respuesta.data.guardarDeclaracion);
              if (respuesta.data.guardarDeclaracion) {

                switch (respuesta.data.guardarDeclaracion.estado) {
                  case 'CORRECTO':
                    this.modalGuardarButton.nativeElement.click();
                    const decla = JSON.parse(sessionStorage.getItem('declaracionLoaded'));
                    decla.declaracion = respuesta.data.guardarDeclaracion.declaracion.declaracion;
                    sessionStorage.setItem('declaracionLoaded', JSON.stringify(decla.declaracion));
                    this.globals.declaracionLoaded = decla.declaracion;
                    this.refresh = true;
                    this.activeMenu = false;
                    this.flagGuardar = false;
                    setTimeout(() => {
                      $('#modalGuardarClose').click();
                    }, 2000);

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
                    for (const mod of respuesta.data.guardarDeclaracion.modulos) {
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
  calculaEnajenacion() {
    if (this.tipoFormato !== 'SIMPLIFICADO') {
      let tv = 0;
      let tm = 0;
      let ti = 0;

      if (this.tDeclaracion !== 'INICIO') {
        if (this.vehiculos.vehiculos && this.vehiculos.vehiculos.length > 0) {
          this.vehiculos.vehiculos.forEach((item) => {
            if (item.tipoOperacion === 'BAJA' && item.motivoBaja.id === 1 && item.titular.id === 1) {

              tv += Number(item.valorVenta.monto);
            }
          });
        }
        if (this.bienesMuebles.bienesMuebles && this.bienesMuebles.bienesMuebles.length > 0) {
          this.bienesMuebles.bienesMuebles.forEach((item) => {
            if (item.tipoOperacion === 'BAJA' && item.motivoBaja.id === 1 && item.titular.id === 1) {

              tm += Number(item.valorVenta.monto);
            }
          });
        }
        if (this.bienesInmuebles.bienesInmuebles && this.bienesInmuebles.bienesInmuebles.length > 0) {
          this.bienesInmuebles.bienesInmuebles.forEach((item) => {
            if (item.tipoOperacion === 'BAJA' && item.motivoBaja.id === 1 && item.titular.id === 1) {

              ti += Number(item.valorVenta.monto);
            }
          });
        }
        setTimeout(() => {
          this.ingresos.guardaEnajenacion({ tv, tm, ti });
          this.datosIngresos.enajenacionBienes = this.ingresos.arreEnajenacionBienes;

          this.datosIngresos.otrosIngresosTotal.monto = this.ingresos.suma5;
          this.datosIngresos.ingresoNetoDeclarante.remuneracion.monto = this.ingresos.suma2;
          if (this.datosIngresos.totalIngresosNetos) {
            this.datosIngresos.totalIngresosNetos.remuneracion.monto = this.ingresos.sumaC;
          }

        }, this.ingresos ? 0 : 2000);
      }
    }
  }

  limpiaVersion() {

    if (this.datosIngresos.actividadFinanciera.length > 0) {

      this.datosIngresos.actividadFinanciera.find(x => x.tipoInstrumento.id === 9999).tipoInstrumentoOtro = 'OTRO (ESPECIFIQUE)';
      this.datosIngresos.actividadFinanciera.forEach((item) => {
        if (item.remuneracion.monto === null) { item.remuneracion.monto = 0; }
      });
    }
    if (this.datosIngresos.enajenacionBienes.length > 0) {
      this.datosIngresos.enajenacionBienes.forEach((item) => {
        if (item.remuneracion.monto === null) { item.remuneracion.monto = 0; }
      });
    }
    if (this.datosActAnt.actividadAnual) {
      if (this.datosActAnt.actividadAnual.actividadFinanciera.length > 0) {
        this.datosActAnt.actividadAnual.actividadFinanciera.find(x => x.tipoInstrumento.id === 9999).tipoInstrumentoOtro = 'OTRO (ESPECIFIQUE)';
        this.datosActAnt.actividadAnual.actividadFinanciera.forEach((item) => {
          if (item.remuneracion.monto === null) { item.remuneracion.monto = 0; }
        });
      }
      if (this.datosActAnt.actividadAnual.actividadFinanciera.length > 0) {
        this.datosActAnt.actividadAnual.enajenacionBienes.forEach((item) => {
          if (item.remuneracion.monto === null) { item.remuneracion.monto = 0; }
        });
      }
    }
    if (this.tipoFormato === 'COMPLETO') {
      const numeroDeclaracionPrecarga = this.declaracionLoaded.numeroDeclaracionPrecarga || null;

      if (this.fideicomisosDatos.fideicomisos && this.fideicomisosDatos.fideicomisos.length > 0) {
        this.fideicomisosDatos.fideicomisos.forEach((item) => {
          if (!numeroDeclaracionPrecarga && item.registroHistorico === true) {
            item.registroHistorico = false;
          }
        });
      }

      if (this.beneficiosPrivadosDatos.beneficios && this.beneficiosPrivadosDatos.beneficios.length > 0) {
        this.beneficiosPrivadosDatos.beneficios.forEach((item) => {
          if (!numeroDeclaracionPrecarga && item.registroHistorico === true) {
            item.registroHistorico = false;
          }
        });
      }

      if (this.clientesDatos.clientes && this.clientesDatos.clientes.length > 0) {
        this.clientesDatos.clientes.forEach((item) => {
          if (!numeroDeclaracionPrecarga && item.registroHistorico === true) {
            item.registroHistorico = false;
          }
        });
      }

      if (this.representacionDatos.representaciones && this.representacionDatos.representaciones.length > 0) {
        this.representacionDatos.representaciones.forEach((item) => {
          if (!numeroDeclaracionPrecarga && item.registroHistorico === true) {
            item.registroHistorico = false;
          }
        });
      }

      if (this.apoyosoBeneficiosDatos.apoyos && this.apoyosoBeneficiosDatos.apoyos.length > 0) {
        this.apoyosoBeneficiosDatos.apoyos.forEach((item) => {
          if (!numeroDeclaracionPrecarga && item.registroHistorico === true) {
            item.registroHistorico = false;
          }
        });
      }

      if (this.participaDecisionesDatos.participaciones && this.participaDecisionesDatos.participaciones.length > 0) {
        this.participaDecisionesDatos.participaciones.forEach((item) => {
          if (!numeroDeclaracionPrecarga && item.registroHistorico === true) {
            item.registroHistorico = false;
          }
        });
      }

      if (this.participaEmpresasDatos.participaciones && this.participaEmpresasDatos.participaciones.length > 0) {
        this.participaEmpresasDatos.participaciones.forEach((item) => {
          if (!numeroDeclaracionPrecarga && item.registroHistorico === true) {
            item.registroHistorico = false;
          }
        });
      }

      if (this.bienesInmuebles.bienesInmuebles && this.bienesInmuebles.bienesInmuebles.length > 0) {
        this.bienesInmuebles.bienesInmuebles.forEach((item) => {
          if (!numeroDeclaracionPrecarga && item.registroHistorico === true) {
            item.registroHistorico = false;
          }
        });
      }
    }
  }
}
