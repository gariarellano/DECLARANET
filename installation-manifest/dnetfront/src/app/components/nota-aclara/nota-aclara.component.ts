
import { Component, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import * as $ from 'jquery';
import { Router } from '@angular/router';
import { GraphqlService } from 'src/app/services/graphql.service';
import { SessionService } from 'src/app/services/session.service';
import { Globals } from 'src/app/globals';
import { ToastrService } from 'ngx-toastr';
import { DatosPersonales } from 'src/app/interfaces/datosPersonales';
import { LocationStrategy } from '@angular/common';
import { first } from 'rxjs/operators';
import { DeclaracionService } from 'src/app/services/declaracion.service';
declare const prepareMenuCuenta: any;

const SECTIONS = [
  "datosGenerales",
  "domicilio",
  "datosCurriculares",
  "datosEmpleo",
  "expLaboral",
  "datosPareja",
  "datosDependienteEconom",
  "ingresosNetos",
  "actAnual",
  "sectionBienesInmuebles",
  "vehiculos",
  "binesMuebles",
  "inversiones",
  "adeudos",
  "prestamoInmueble",
  "participaEmpresa",
  "tomaDecisiones",
  "beneficios",
  "representacion",
  "clientesPrincipales",
  "beneficiosPrivados",
  "fideicomisos",
  "aviso"
]
@Component({
  selector: 'app-nota-aclara',
  templateUrl: './nota-aclara.component.html',
  styleUrls: ['./nota-aclara.component.scss']
})

export class NotaAclaraComponent implements OnInit, AfterViewInit {
  @ViewChild('modalGuardarButton') modalGuardarButton: ElementRef;

  currentSection = 0;
  totalMaxSecctions = 22;
  activeMenu: boolean;
  secciones: any;
  seccionesCopia: any;
  notaValid: boolean;
  arr: Array<any>;
  nota: any;
  tipoFormato: string;
  mensajeCambioFormate: string;
  declaracionLoaded: any;
  tDeclaracion: string;
  totalSecctions: number;
  guardarDeclaracionError: string;
  guardarDeclaracionErrorArr: any;
  declaraLoad: any;
  usuario: DatosPersonales;
  tipoDeclara: string;
  anio: any;
  nivelJe: any;
  fechaEncargo: any;
  institucionReceptora: any;
  notaTemp: any;
  tDeclaracionOrigen: any;
  flagGuardando: boolean;
  flagGuardar: boolean;
  constructor(private locationStrategy: LocationStrategy, 
              private router: Router,
              public globals: Globals,
              private graphqlService: GraphqlService,
              private sessionService: SessionService,
              private declaracionService: DeclaracionService,
              private toastService: ToastrService,
  ) {
    this.activeMenu = true;
    this.flagGuardando = false;
    this.flagGuardar = false;
    this.blockBackButton();
  }

  private blockBackButton() {
    history.pushState(null, null, window.location.href);
    this.locationStrategy.onPopState(() => {
      history.pushState(null, null, window.location.href);
    });
  }

  ngOnInit() {
    prepareMenuCuenta();
    this.totalSecctions = 23;
    this.usuario = this.sessionService.currentUserValue.datosPersonales;

    this.declaraLoad = sessionStorage.getItem('declaracionLoaded');
    console.log('declaraLoad', JSON.parse(this.declaraLoad));
    if (!this.declaraLoad) {
      this.router.navigate(['/inicio']);
    }
    let d: any = null;
    d = this.globals.declaracionLoaded ? JSON.parse(JSON.stringify(this.globals.declaracionLoaded)) : null;

    this.declaracionLoaded = d || JSON.parse(sessionStorage.getItem('declaracionLoaded'));

    if (this.tipoFormato === 'SIMPLIFICADO' && this.declaracionLoaded.encabezado.tipoFormato === 'COMPLETO') {
      this.mensajeCambioFormate = 'Se ha modificado el formato de tu declaración de acuerdo a tu encargo declarado.';
    }

    this.tipoFormato = this.declaracionLoaded.encabezado.tipoFormato;
    this.tDeclaracion = this.declaracionLoaded.encabezado.tipoDeclaracion;
    this.tDeclaracionOrigen = this.declaracionLoaded.encabezado.declaracionOrigen.encabezado.tipoDeclaracion;
    this.tipoDeclara = this.declaracionLoaded.encabezado.declaracionOrigen.encabezado.tipoDeclaracion;
    this.anio = this.declaracionLoaded.encabezado.declaracionOrigen.encabezado.anio;
    this.fechaEncargo = this.declaracionLoaded.encabezado.declaracionOrigen.encabezado.fechaEncargo;
    this.institucionReceptora = this.declaracionLoaded.institucionReceptora;

    if (this.tDeclaracion === 'AVISO') {
      this.totalSecctions = 3
    } else {
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
    }


    if (sessionStorage.getItem('notaTemp')) {
      this.notaTemp = JSON.parse(sessionStorage.getItem('notaTemp'));
    } else {
      this.notaTemp = {
        datosGenerales: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        domicilioDeclarante: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        datosCurricularesDeclarante: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        datosEmpleoCargoComision: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        experienciasLaborales: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        datosParejas: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        datosDependientesEconomicos: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        ingresos: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        actividadAnualAnterior: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        bienesInmuebles: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        vehiculos: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        bienesMuebles: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        inversionesCuentasValores: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        adeudosPasivos: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        prestamoComodato: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        participaEmpresasSociedadesAsociaciones: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        participacionTomaDecisiones: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        apoyos: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        representaciones: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        clientesPrincipales: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        beneficiosPrivados: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        fideicomisos: {
          aceptar: false,
          aclaracionesObservaciones: ''
        },
        detalleAvisoCambioDependencia: {
          aceptar: false,
          aclaracionesObservaciones: ''
        }
      };
    }

    this.notaValid = false;
    this.secciones = this.notaTemp;

    this.nota = {
      declaracion: {},
      encabezado: this.declaraLoad.encabezado,
      firmada: this.declaraLoad.firmada,
      institucionReceptora: this.declaraLoad.institucionReceptora
    }
  }

  ngAfterViewInit() {

    const total = 24;
    let c;
    $(".menu-nav ul").hover(() => {
      $(".menu-nav").css('width', '300px');

      c = false;
      setTimeout(() => {
        if (!c) {
          $(".menu-nav > ul > li > span").css({ 'display': 'flex' });
        }
      }, 200);
    }, () => {
      $(".menu-nav > ul > li > span").css('display', 'none');
      $(".menu-nav").css('width', '40px');
      c = true;
    });


    $("#item1").on('click', () => {
      $("#sectionNota1").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 1) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });

    $("#item2").on('click', () => {
      $("#sectionNota2").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 2) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });

    $("#item3").on('click', () => {
      $("#sectionNota3").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 3) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });

    $("#item4").on('click', () => {
      $("#sectionNota4").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 4) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });

    $("#item5").on('click', () => {
      $("#sectionNota5").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 5) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });
    $("#item6").on('click', () => {
      $("#sectionNota6").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 6) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });
    $("#item7").on('click', () => {
      $("#sectionNota7").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 7) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });
    $("#item8").on('click', () => {
      $("#sectionNota8").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 8) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });
    $("#item9").on('click', () => {
      $("#sectionNota9").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 9) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });
    $("#item10").on('click', () => {
      $("#sectionNota10").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 10) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });
    $("#item11").on('click', () => {
      $("#sectionNota11").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 11) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });
    $("#item12").on('click', () => {
      $("#sectionNota12").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 12) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });
    $("#item13").on('click', () => {
      $("#sectionNota13").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 13) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });
    $("#item14").on('click', () => {
      $("#sectionNota14").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 14) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });
    $("#item15").on('click', () => {
      $("#sectionNota15").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 15) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });
    $("#item16").on('click', () => {
      $("#sectionNota16").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 16) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });
    $("#item17").on('click', () => {
      $("#sectionNota17").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 17) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });
    $("#item18").on('click', () => {
      $("#sectionNota18").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 18) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });
    $("#item19").on('click', () => {
      $("#sectionNota19").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 19) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });
    $("#item20").on('click', () => {
      $("#sectionNota20").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 20) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });
    $("#item21").on('click', () => {
      $("#sectionNota21").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 21) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });
    $("#item22").on('click', () => {
      $("#sectionNota22").slideDown();
      for (var i = 0; i <= total; i++) {
        if (i !== 22) {
          $(`#sectionNota${i}`).css('display', 'none');
        }
      }
    });
   
  }
  home() {
    this.router.navigate(['/inicio']);
    sessionStorage.removeItem('notaTemp');
  }

  logout() {
    const navOut = sessionStorage.getItem('enteReceptor') ? JSON.parse(sessionStorage.getItem('enteReceptor')).enteContext || '' : '';
    this.sessionService.logout();
    this.router.navigate(['/' + navOut]);
  }

  showToast(_section, status) {
    if (status === 'alta') {
      this.toastService.success(
        'No olvides enviar tu aclaración',
        `${_section} ha sido capturada`,
        {
          timeOut: 2000,
          extendedTimeOut: 800,
          closeButton: true,
          tapToDismiss: true,
          progressBar: true
        }
      );
    }

    if (status === 'baja') {
      this.toastService.error(
        '',
        `${_section} ha sido cancelada`,
        {
          extendedTimeOut: 1000,
          closeButton: true,
          tapToDismiss: true,
          progressBar: true
        }
      );
    }

  }

  validNota() {

    if (this.secciones.datosGenerales.aclaracionesObservaciones !== '' ||
      this.secciones.domicilioDeclarante.aclaracionesObservaciones !== '' ||
      this.secciones.datosCurricularesDeclarante.aclaracionesObservaciones !== '' ||
      this.secciones.datosEmpleoCargoComision.aclaracionesObservaciones !== '' ||
      this.secciones.experienciasLaborales.aclaracionesObservaciones !== '' ||
      this.secciones.datosParejas.aclaracionesObservaciones !== '' ||
      this.secciones.datosDependientesEconomicos.aclaracionesObservaciones !== '' ||
      this.secciones.ingresos.aclaracionesObservaciones !== '' ||
      this.secciones.actividadAnualAnterior.aclaracionesObservaciones !== '' ||
      this.secciones.bienesInmuebles.aclaracionesObservaciones !== '' ||
      this.secciones.vehiculos.aclaracionesObservaciones !== '' ||
      this.secciones.bienesMuebles.aclaracionesObservaciones !== '' ||
      this.secciones.inversionesCuentasValores.aclaracionesObservaciones !== '' ||
      this.secciones.adeudosPasivos.aclaracionesObservaciones !== '' ||
      this.secciones.prestamoComodato.aclaracionesObservaciones !== '' ||
      this.secciones.participaEmpresasSociedadesAsociaciones.aclaracionesObservaciones !== '' ||
      this.secciones.participacionTomaDecisiones.aclaracionesObservaciones !== '' ||
      this.secciones.apoyos.aclaracionesObservaciones !== '' ||
      this.secciones.representaciones.aclaracionesObservaciones !== '' ||
      this.secciones.clientesPrincipales.aclaracionesObservaciones !== '' ||
      this.secciones.beneficiosPrivados.aclaracionesObservaciones !== '' ||
      this.secciones.fideicomisos.aclaracionesObservaciones !== '' ||
      this.secciones.detalleAvisoCambioDependencia.aclaracionesObservaciones !== '') {
      this.notaValid = true;
    } else {
      this.notaValid = false;
    }
  }
  
  firmarNota() {
    this.flagGuardando = true;
    sessionStorage.setItem('notaTemp', JSON.stringify(this.secciones));
    this.seccionesCopia = JSON.parse(JSON.stringify(this.secciones));
    if (this.seccionesCopia.datosGenerales.aceptar === false)
     { delete this.seccionesCopia.datosGenerales } else { delete this.seccionesCopia.datosGenerales.aceptar }
    if (this.seccionesCopia.domicilioDeclarante.aceptar === false)
     { delete this.seccionesCopia.domicilioDeclarante } else { delete this.seccionesCopia.domicilioDeclarante.aceptar }
    if (this.seccionesCopia.datosCurricularesDeclarante.aceptar === false)
     { delete this.seccionesCopia.datosCurricularesDeclarante } else { delete this.seccionesCopia.datosCurricularesDeclarante.aceptar }
    if (this.seccionesCopia.datosEmpleoCargoComision.aceptar === false) 
    { delete this.seccionesCopia.datosEmpleoCargoComision } else { delete this.seccionesCopia.datosEmpleoCargoComision.aceptar }
    if (this.seccionesCopia.experienciasLaborales.aceptar === false)
     { delete this.seccionesCopia.experienciasLaborales } else { delete this.seccionesCopia.experienciasLaborales.aceptar }
    if (this.seccionesCopia.datosParejas.aceptar === false)
     { delete this.seccionesCopia.datosParejas } else { delete this.seccionesCopia.datosParejas.aceptar }
    if (this.seccionesCopia.datosDependientesEconomicos.aceptar === false)
     { delete this.seccionesCopia.datosDependientesEconomicos } else { delete this.seccionesCopia.datosDependientesEconomicos.aceptar }
    if (this.seccionesCopia.ingresos.aceptar === false) 
    { delete this.seccionesCopia.ingresos } else { delete this.seccionesCopia.ingresos.aceptar }
    if (this.seccionesCopia.actividadAnualAnterior.aceptar === false)
     { delete this.seccionesCopia.actividadAnualAnterior } else { delete this.seccionesCopia.actividadAnualAnterior.aceptar }
    if (this.seccionesCopia.bienesInmuebles.aceptar === false)
     { delete this.seccionesCopia.bienesInmuebles } else { delete this.seccionesCopia.bienesInmuebles.aceptar }
    if (this.seccionesCopia.vehiculos.aceptar === false)
     { delete this.seccionesCopia.vehiculos } else { delete this.seccionesCopia.vehiculos.aceptar }
    if (this.seccionesCopia.bienesMuebles.aceptar === false) { delete this.seccionesCopia.bienesMuebles } else { delete this.seccionesCopia.bienesMuebles.aceptar }
    if (this.seccionesCopia.inversionesCuentasValores.aceptar === false)
     { delete this.seccionesCopia.inversionesCuentasValores } else { delete this.seccionesCopia.inversionesCuentasValores.aceptar }
    if (this.seccionesCopia.adeudosPasivos.aceptar === false) 
    { delete this.seccionesCopia.adeudosPasivos } else { delete this.seccionesCopia.adeudosPasivos.aceptar }
    if (this.seccionesCopia.prestamoComodato.aceptar === false) 
    { delete this.seccionesCopia.prestamoComodato } else { delete this.seccionesCopia.prestamoComodato.aceptar }
    if (this.seccionesCopia.participaEmpresasSociedadesAsociaciones.aceptar === false) { delete this.seccionesCopia.participaEmpresasSociedadesAsociaciones } else { delete this.seccionesCopia.participaEmpresasSociedadesAsociaciones.aceptar }
    if (this.seccionesCopia.participacionTomaDecisiones.aceptar === false)
     { delete this.seccionesCopia.participacionTomaDecisiones } else { delete this.seccionesCopia.participacionTomaDecisiones.aceptar }
    if (this.seccionesCopia.apoyos.aceptar === false)
     { delete this.seccionesCopia.apoyos } else { delete this.seccionesCopia.apoyos.aceptar }
    if (this.seccionesCopia.representaciones.aceptar === false)
     { delete this.seccionesCopia.representaciones } else { delete this.seccionesCopia.representaciones.aceptar }
    if (this.seccionesCopia.clientesPrincipales.aceptar === false) 
    { delete this.seccionesCopia.clientesPrincipales } else { delete this.seccionesCopia.clientesPrincipales.aceptar }
    if (this.seccionesCopia.beneficiosPrivados.aceptar === false) 
    { delete this.seccionesCopia.beneficiosPrivados } else { delete this.seccionesCopia.beneficiosPrivados.aceptar }
    if (this.seccionesCopia.fideicomisos.aceptar === false) 
    { delete this.seccionesCopia.fideicomisos } else { delete this.seccionesCopia.fideicomisos.aceptar }
    if (this.seccionesCopia.detalleAvisoCambioDependencia.aceptar === false) 
    { delete this.seccionesCopia.detalleAvisoCambioDependencia } else { delete this.seccionesCopia.detalleAvisoCambioDependencia.aceptar }
    
    this.nota.declaracion = this.seccionesCopia;
    const decla = JSON.parse(sessionStorage.getItem('declaracionLoaded'));
    this.nota.encabezado = decla.encabezado;
    this.nota.firmada = decla.firmada;
    this.nota.institucionReceptora = decla.institucionReceptora;
    this.nota.encabezado.fechaRegistro = "2020-01-20T09:23:21.317";
    try {
      this.sessionService.renovarToken().subscribe(ren => {
        console.log("nota a guardar ", this.nota);
        
        this.graphqlService.guardaNota(this.nota).subscribe(respuesta => {
          console.log("r", respuesta);
          switch (respuesta.data.guardarNotas.estado) {
            case 'CORRECTO':
              const declara = JSON.parse(sessionStorage.getItem('declaracionLoaded'));
              declara.declaracion = respuesta.data.guardarNotas.declaracion.declaracion;
              sessionStorage.setItem('declaracionLoaded', JSON.stringify(declara.declaracion));
              this.globals.declaracionLoaded = declara.declaracion;
              this.flagGuardando = false;
              const declaracionLoaded = this.globals.declaracionLoaded;
              const idUsu = declaracionLoaded.encabezado.usuario.idUsuario;
              const numeroDeclaracion = declaracionLoaded.encabezado.numeroDeclaracion;
              const collName = declaracionLoaded.institucionReceptora.collName;
              $('#cerrarModalFirmar').click();
              this.declaracionService.iniciaFirmaDeclaracion(this.globals.declaracionLoaded, idUsu, numeroDeclaracion, collName)
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
              for (const mod of respuesta.data.guardarNotas.modulos) {
                for (const e of mod.errores) {
                  this.guardarDeclaracionError = 'Error en la captura de módulos:';
                  this.guardarDeclaracionErrorArr.push({ error: `Error en el campo: ${e.nombreCampo} ${e.listErrorMensajes[0].mensaje}` });
                }
              }
              this.modalGuardarButton.nativeElement.click();
              break;
            default:
              break;
          }
          this.flagGuardando = false;
        },
          error => {
            this.guardarDeclaracionError = 'Error de incompatibilidad';
            this.flagGuardando = false;
            console.log(error);
          });
      });
    } catch (e) { }

  }


  estadoNota(seccion, operacion) {
    switch (seccion) {
      case 'datosGenerales':
        if (operacion == 'aceptar') {
          this.secciones.datosGenerales.aceptar = true;
          this.showToast('Datos generales', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.datosGenerales.aceptar = false;
          this.secciones.datosGenerales.aclaracionesObservaciones = '';
          this.showToast('Datos generales', 'baja');
          this.validNota();
        }
        break;

      case 'domicilioDeclarante':
        if (operacion === 'aceptar') {
          this.secciones.domicilioDeclarante.aceptar = true;
          this.showToast('Domicilio declarante', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.domicilioDeclarante.aceptar = false;
          this.secciones.domicilioDeclarante.aclaracionesObservaciones = '';
          this.showToast('Domicilio declarante', 'baja');
          this.validNota();

        }
        break;

      case 'datosCurricularesDeclarante':
        if (operacion === 'aceptar') {
          this.secciones.datosCurricularesDeclarante.aceptar = true;
          this.showToast('Datos curriculares', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.datosCurricularesDeclarante.aceptar = false;
          this.secciones.datosCurricularesDeclarante.aclaracionesObservaciones = '';
          this.showToast('Datos curriculares', 'baja');
          this.validNota();

        }
        break;

      case 'datosEmpleoCargoComision':
        if (operacion === 'aceptar') {
          this.secciones.datosEmpleoCargoComision.aceptar = true;
          this.showToast('Datos empleo', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.datosEmpleoCargoComision.aceptar = false;
          this.secciones.datosEmpleoCargoComision.aclaracionesObservaciones = '';
          this.showToast('Datos empleo', 'baja');
          this.validNota();

        }
        break;

      case 'experienciasLaborales':
        if (operacion === 'aceptar') {
          this.secciones.experienciasLaborales.aceptar = true;
          this.showToast('Experiencia laboral', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.experienciasLaborales.aceptar = false;
          this.secciones.experienciasLaborales.aclaracionesObservaciones = '';
          this.showToast('Experiencia laboral', 'baja');
          this.validNota();

        }
        break;
      case 'datosParejas':
        if (operacion === 'aceptar') {
          this.secciones.datosParejas.aceptar = true;
          this.showToast('Datos de la pareja', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.datosParejas.aceptar = false;
          this.secciones.datosParejas.aclaracionesObservaciones = '';
          this.showToast('Datos de la pareja', 'baja');
          this.validNota();

        }
        break;
      case 'datosDependientesEconomicos':
        if (operacion === 'aceptar') {
          this.secciones.datosDependientesEconomicos.aceptar = true;
          this.showToast('Datos dependiente económico', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.datosDependientesEconomicos.aceptar = false;
          this.secciones.datosDependientesEconomicos.aclaracionesObservaciones = '';
          this.showToast('Datos dependiente económico', 'baja');
          this.validNota();

        }
        break;
      case 'ingresos':
        if (operacion === 'aceptar') {
          this.secciones.ingresos.aceptar = true;
          this.showToast('Ingresos', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.ingresos.aceptar = false;
          this.secciones.ingresos.aclaracionesObservaciones = '';
          this.showToast('Ingresos', 'baja');
          this.validNota();

        }
        break;
      case 'actividadAnualAnterior':
        if (operacion === 'aceptar') {
          this.secciones.actividadAnualAnterior.aceptar = true;
          this.showToast('Actividad anual anterior', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.actividadAnualAnterior.aceptar = false;
          this.secciones.actividadAnualAnterior.aclaracionesObservaciones = '';
          this.showToast('Actividad anual anterior', 'baja');
          this.validNota();

        }
        break;
      case 'bienesInmuebles':
        if (operacion === 'aceptar') {
          this.secciones.bienesInmuebles.aceptar = true;
          this.showToast('Bienes inmuebles', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.bienesInmuebles.aceptar = false;
          this.secciones.bienesInmuebles.aclaracionesObservaciones = '';
          this.showToast('Bienes inmuebles', 'baja');
          this.validNota();

        }
        break;
      case 'vehiculos':
        if (operacion === 'aceptar') {
          this.secciones.vehiculos.aceptar = true;
          this.showToast('Vehiculos', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.vehiculos.aceptar = false;
          this.secciones.vehiculos.aclaracionesObservaciones = '';
          this.showToast('Vehiculos', 'baja');
          this.validNota();

        }
        break;
      case 'bienesMuebles':
        if (operacion === 'aceptar') {
          this.secciones.bienesMuebles.aceptar = true;
          this.showToast('Bienes muebles', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.bienesMuebles.aceptar = false;
          this.secciones.bienesMuebles.aclaracionesObservaciones = '';
          this.showToast('Bienes muebles', 'baja');
          this.validNota();

        }
        break;
      case 'inversionesCuentasValores':
        if (operacion === 'aceptar') {
          this.secciones.inversionesCuentasValores.aceptar = true;
          this.showToast('Inversiones', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.inversionesCuentasValores.aceptar = false;
          this.secciones.inversionesCuentasValores.aclaracionesObservaciones = '';
          this.showToast('Inversiones', 'baja');
          this.validNota();

        }
        break;
      case 'adeudosPasivos':
        if (operacion === 'aceptar') {
          this.secciones.adeudosPasivos.aceptar = true;
          this.showToast('Adeudos pasivos', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.adeudosPasivos.aceptar = false;
          this.secciones.adeudosPasivos.aclaracionesObservaciones = '';
          this.showToast('Adeudos pasivos', 'baja');
          this.validNota();

        }
        break;
      case 'prestamoComodato':
        if (operacion === 'aceptar') {
          this.secciones.prestamoComodato.aceptar = true;
          this.showToast('Prestamos o comodatos', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.prestamoComodato.aceptar = false;
          this.secciones.prestamoComodato.aclaracionesObservaciones = '';
          this.showToast('Prestamos o comodatos', 'baja');
          this.validNota();

        }
        break;
      case 'participaEmpresasSociedadesAsociaciones':
        if (operacion === 'aceptar') {
          this.secciones.participaEmpresasSociedadesAsociaciones.aceptar = true;
          this.showToast('Participación de empresas', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.participaEmpresasSociedadesAsociaciones.aceptar = false;
          this.secciones.participaEmpresasSociedadesAsociaciones.aclaracionesObservaciones = '';
          this.showToast('Participación de empresas', 'baja');
          this.validNota();

        }
        break;
      case 'participacionTomaDecisiones':
        if (operacion === 'aceptar') {
          this.secciones.participacionTomaDecisiones.aceptar = true;
          this.showToast('¿Participa en toma d decisiónes?', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.participacionTomaDecisiones.aceptar = false;
          this.secciones.participacionTomaDecisiones.aclaracionesObservaciones = '';
          this.showToast('¿Participa en toma d decisiónes?', 'baja');
          this.validNota();

        }
        break;
      case 'apoyos':
        if (operacion === 'aceptar') {
          this.secciones.apoyos.aceptar = true;
          this.showToast('Apoyos o beneficios', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.apoyos.aceptar = false;
          this.secciones.apoyos.aclaracionesObservaciones = '';
          this.showToast('Apoyos o beneficios', 'baja');
          this.validNota();

        }
        break;
      case 'representaciones':
        if (operacion === 'aceptar') {
          this.secciones.representaciones.aceptar = true;
          this.showToast('Representación', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.representaciones.aceptar = false;
          this.secciones.representaciones.aclaracionesObservaciones = '';
          this.showToast('Representación', 'baja');
          this.validNota();

        }
        break;
      case 'clientesPrincipales':
        if (operacion === 'aceptar') {
          this.secciones.clientesPrincipales.aceptar = true;
          this.showToast('Clientes principales', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.clientesPrincipales.aceptar = false;
          this.secciones.clientesPrincipales.aclaracionesObservaciones = '';
          this.showToast('Clientes principales', 'baja');
          this.validNota();

        }
        break;
      case 'beneficiosPrivados':
        if (operacion === 'aceptar') {
          this.secciones.beneficiosPrivados.aceptar = true;
          this.showToast('Beneficios privados', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.beneficiosPrivados.aceptar = false;
          this.secciones.beneficiosPrivados.aclaracionesObservaciones = '';
          this.showToast('Beneficios privados', 'baja');
          this.validNota();

        }
        break;
      case 'fideicomisos':
        if (operacion === 'aceptar') {
          this.secciones.fideicomisos.aceptar = true;
          this.showToast('Fideicomisos', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.fideicomisos.aceptar = false;
          this.secciones.fideicomisos.aclaracionesObservaciones = '';
          this.showToast('Fideicomisos', 'baja');
          this.validNota();

        }
        break;
      case 'detalleAvisoCambioDependencia':
        if (operacion === 'aceptar') {
          this.secciones.detalleAvisoCambioDependencia.aceptar = true;
          this.showToast('aviso', 'alta');
          this.validNota();
        }
        if (operacion === 'cancelar') {
          this.secciones.detalleAvisoCambioDependencia.aceptar = false;
          this.secciones.detalleAvisoCambioDependencia.aclaracionesObservaciones = '';
          this.showToast('Aviso', 'baja');
          this.validNota();
        }
        break;
    }
  }

  goToAnchor(_currentSection) {
    this.currentSection = _currentSection
    $(`.item${this.currentSection + 1}`).addClass("menu-nav-active");

    for (var i = 1; i <= this.totalSecctions; i++) {
      if (i != this.currentSection + 1) {
        $(`.item${i}`).removeClass("menu-nav-active");
      }
    }
  }

  revisaTooltip() {
    return !this.flagGuardar ? 'Para poder guardar la declaración, deberá dar clic al botón \naceptar de los módulos en los que realizó cambios' : '';
  }

}


