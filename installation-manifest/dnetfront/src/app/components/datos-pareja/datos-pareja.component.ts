import { Component, OnInit, EventEmitter, Output, ViewChild, HostListener } from '@angular/core';
import { FormGroup, NgForm } from '@angular/forms';
import * as $ from 'jquery';
import { DatosPareja } from './../../interfaces/datosPareja';
import { Globals, timeTouchEvent } from 'src/app/globals';

@Component({
  selector: 'app-datos-pareja',
  templateUrl: './datos-pareja.component.html',
  styleUrls: ['./datos-pareja.component.scss']
})
export class DatosParejaComponent implements OnInit {

  @Output()
  aceptar = new EventEmitter();
  @ViewChild('parejaForm') parejaForm: NgForm;
  @Output() acla = new EventEmitter();
  aclara: string;
  fecha: any;
  endTimeTouch
  startTimeTouch

  countTouches = 0
  countFingers

  distanceTouch
  startScreenTouch
  endScreenTouch

  @HostListener('touchmove', ['$event'])
  moveTouch(event) {
    this.countFingers = event.touches.length
    this.distanceTouch = event.changedTouches[0].clientX - this.startScreenTouch
  }

  @HostListener('touchstart', ['$event'])
  start(event) {
    this.startTimeTouch = new Date().getTime()
    this.startScreenTouch = event.changedTouches[0].clientX
  }

  div = document.getElementById('scrollDaPa');
  @HostListener('touchend', ['$event'])
  stop(event) {
    this.div = document.getElementById('scrollDaPa');
    this.endTimeTouch = new Date().getTime()
    let diff = this.endTimeTouch - this.startTimeTouch
    if (this.div['scrollHeight'] != this.div['clientHeight']) {
      if (this.countFingers === 2) {

        this.countTouches++
        if (this.countTouches === 2) {
          if (this.startTimeTouch)
            if (diff < timeTouchEvent) {
              if (this.distanceTouch > 0) document.getElementById('goForwardBtn').click()
              if (this.distanceTouch < 0) document.getElementById('goBackBtn').click()
            }
          this.countTouches = 0
        }
      }
    } else
      if (diff < timeTouchEvent) {
        if (this.distanceTouch > 0) document.getElementById('goForwardBtn').click()
        if (this.distanceTouch < 0) document.getElementById('goBackBtn').click()
      }
  }

  @HostListener('mousewheel', ['$event'])
  _wheel(event: MouseEvent) {
    this.div = document.getElementById('scrollDaPa');
    if (this.div['scrollHeight'] != this.div['clientHeight']) event.stopPropagation()
    if (this.div['offsetHeight'] != this.div['scrollHeight'])
      if (this.div['scrollTop'] + this.div['offsetHeight'] + 1 > this.div['scrollHeight'] && event['wheelDeltaY'] < 0)
        document.getElementById('goForwardBtn').click()
      else if (this.div['scrollTop'] === 0 && event['wheelDeltaY'] > 0)
        document.getElementById('goBackBtn').click()
  }

  form: FormGroup;
  form2: FormGroup;
  habitaDomicilioDeclarante: boolean;
  lugarResi: number;
  noPareja: boolean;
  label = '';
  info = '';
  help = false;
  dataInfo: any;
  datosDePareja: DatosPareja;
  datoPareja: any;
  filterMuns: any;
  datoParejaTmp; any;
  constructor(public cata: Globals) {
    this.fecha = {
      fechaMax: cata.fechaReferenciaMax,
      fechaMin: null
    }
    this.fecha = JSON.stringify(this.fecha);
  }

  @HostListener('paste', ['$event']) blockPaste(e: KeyboardEvent) {
    e.preventDefault();
  }

  ngOnInit() {

    this.dataInfo = {
      nombre: {
        label: 'Nombre',
        info: 'Es el nombre de la persona'
      },
      primerApellido: {
        label: 'Primer Apellido',
        info: 'Es el apellido paterno de la persona'
      },
      segundoApellido: {
        label: 'Segundo Apellido',
        info: 'Es el apellido materno de la persona'
      },
      curp: {
        label: 'Curp',
        info: 'clave única de registro (si no se cuenta con ella, consultar en el siguiente link) https://consultas.curp.gob.mx/'
      },
      pareja: {
        label: '¿No tienes pareja?',
        info: ''
      },
      rfc: {
        label: 'RFC',
        info: 'Registro Federal de Contribuyentes'
      }
    };

    $('.help').click(() => {
      this.help = !this.help;
      if (this.help) {
        $('label').addClass('openHelp');
        $('label').next('input,select').css('visibility', 'hidden');
      } else {
        $('label').removeClass('openHelp');
        $('label').next('input,select').css('visibility', 'visible');
      }
    });

    this.habitaDomicilioDeclarante = true;
    this.lugarResi = 0;

    this.datosDePareja = JSON.parse(sessionStorage.getItem('datosPareja'));
    this.datoPareja = this.datosDePareja.datosParejas[0];
    this.checkvisibles();
    console.log('imprime', this.datoPareja);

    this.datoParejaTmp = {
      domicilioDependienteEconomico: {
        domicilio: {
          domicilioMexico: JSON.parse(JSON.stringify(this.datoPareja.domicilioDependienteEconomico.domicilio.domicilioMexico)),
          domicilioExtranjero: JSON.parse(JSON.stringify(this.datoPareja.domicilioDependienteEconomico.domicilio.domicilioExtranjero))

        }
      },
      actividadLaboral: JSON.parse(JSON.stringify(this.datoPareja.actividadLaboral))
    };


  }

  limpiaDatosActividad() {
    this.datoParejaTmp.actividadLaboral = {
      ambitoSector: this.datoParejaTmp.actividadLaboral.ambitoSector,
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
    };
  }
  guardaLocal() {
    if (this.datosDePareja.ninguno === true) {
      const data = JSON.parse(sessionStorage.getItem('datosPareja'));
      data.ninguno = true;
      data.aclaracionesObservaciones = this.datosDePareja.aclaracionesObservaciones;
      data.datosParejas = null;
      sessionStorage.setItem('datosPareja', JSON.stringify(data));
      this.aceptar.emit(data);
    } else {

      this.datosDePareja.datosParejas[0] = this.datoPareja;
      this.datosDePareja.datosParejas[0].verificar = true;
      if (this.parejaForm.valid && this.datosDePareja.ninguno === null) {
        this.datosDePareja.ninguno = false;

      }

      if (this.datosDePareja.datosParejas[0].actividadLaboral.ambitoSector.id !== 9999) {
        this.datosDePareja.datosParejas[0].actividadLaboral.ambitoSectorOtro = null;
      }
      if (this.datosDePareja.datosParejas[0].actividadLaboral.sectorPrivadoOtro.sector.id !== 9999) {
        this.datosDePareja.datosParejas[0].actividadLaboral.sectorPrivadoOtro.sectorOtro = null;
      }
      this.aceptar.emit(this.datosDePareja);
    }
  }


  limpiaSectorPrivadoOtro() {
    if (this.datoPareja.actividadLaboral.sectorPrivadoOtro.sector.id != '9999') {
      this.datoPareja.actividadLaboral.sectorPrivadoOtro.sectorOtro = null;
    }
  }

  abrirModalLugar(lugar: string) {
    switch (lugar) {
      case 'mexico':

        this.datoParejaTmp.domicilioDependienteEconomico.domicilio.domicilioMexico = JSON.parse(JSON.stringify(this.datoPareja.domicilioDependienteEconomico.domicilio.domicilioMexico));

        if (this.datoParejaTmp.domicilioDependienteEconomico.domicilio.domicilioMexico.entidadFederativa == null) {
          this.datoParejaTmp.domicilioDependienteEconomico.domicilio.domicilioMexico.entidadFederativa =
            this.datoPareja.domicilioDependienteEconomico.domicilio.domicilioMexico.entidadFederativa
        }
        if (this.datoParejaTmp.domicilioDependienteEconomico.domicilio.domicilioMexico.municipioAlcaldia == null) {
          this.datoParejaTmp.domicilioDependienteEconomico.domicilio.domicilioMexico.municipioAlcaldia =
            this.datoPareja.domicilioDependienteEconomico.domicilio.domicilioMexico.municipioAlcaldia
        }
        this.filterMunicipios(this.datoParejaTmp.domicilioDependienteEconomico.domicilio.domicilioMexico.entidadFederativa.id, false);
        break;
      case 'extranjero':
        console.log("mexi", this.datoParejaTmp);
        this.datoParejaTmp.domicilioDependienteEconomico.domicilio.domicilioExtranjero = JSON.parse(JSON.stringify(this.datoPareja.domicilioDependienteEconomico.domicilio.domicilioExtranjero));
        break;
      case 'actividad':
        this.datoParejaTmp.actividadLaboral = JSON.parse(JSON.stringify(this.datoPareja.actividadLaboral));
        break;
    }

    $(`#modal-lugar-${lugar}`).css({ 'display': 'flex' });
    $('.menu-nav').css({ 'display': 'none' });
    $('#content-btnSave').css({ 'display': 'none' });
    $('body').addClass('disabled-onepage-scroll');


  }

  cerrarModalLugar(lugar: string) {
    console.log("kygar ", this.datoParejaTmp.domicilioDependienteEconomico);

    switch (lugar) {
      case 'mexico':
        this.datoParejaTmp.domicilioDependienteEconomico.domicilio.domicilioMexico = JSON.parse(JSON.stringify(this.datoPareja.domicilioDependienteEconomico.domicilio.domicilioMexico));
        if (this.datoParejaTmp.domicilioDependienteEconomico.domicilio.domicilioMexico.entidadFederativa != null) {
          this.filterMunicipios(this.datoParejaTmp.domicilioDependienteEconomico.domicilio.domicilioMexico.entidadFederativa.id, false);
        }
        break;
      case 'extranjero':
        this.datoParejaTmp.domicilioDependienteEconomico.domicilio.domicilioExtranjero = JSON.parse(JSON.stringify(this.datoPareja.domicilioDependienteEconomico.domicilio.domicilioExtranjero));
        break;
      case 'actividad':
        this.datoParejaTmp.actividadLaboral = JSON.parse(JSON.stringify(this.datoPareja.actividadLaboral));
        break;
    }

    $(`#modal-lugar-${lugar}`).fadeOut('slow', () => {
      $(this).css('display', 'none');
    });

    $('.menu-nav').css({ 'display': 'flex' });
    $('#content-btnSave').css({ 'display': 'flex' });
    $('body').removeClass('disabled-onepage-scroll');
  }

  guardarModalLugar(lugar: string) {
    switch (lugar) {
      case 'mexico':
        this.datoPareja.domicilioDependienteEconomico.domicilio.domicilioMexico = JSON.parse(JSON.stringify(this.datoParejaTmp.domicilioDependienteEconomico.domicilio.domicilioMexico));
        break;
      case 'extranjero':
        this.datoPareja.domicilioDependienteEconomico.domicilio.domicilioExtranjero = JSON.parse(JSON.stringify(this.datoParejaTmp.domicilioDependienteEconomico.domicilio.domicilioExtranjero));
        break;
      case 'actividad':
        this.datoPareja.actividadLaboral = JSON.parse(JSON.stringify(this.datoParejaTmp.actividadLaboral));
        break;
    }
    this.cerrarModalLugar(lugar);
  }

  abriInfo(label) {
    console.log('llego');

    this.label = this.dataInfo[label].label;
    this.info = this.dataInfo[label].info;

    $('.content-info').css({ 'display': 'flex' });
    $('.menu-nav').css({ 'display': 'none' });
    $('#content-btnSave').css({ 'display': 'none' });
  }

  cerrarInfo() {
    this.label = '';
    this.info = '';
    $('.content-info').css('display', 'none');
    $('.menu-nav').css({ 'display': 'flex' });
    $('#content-btnSave').css({ 'display': 'flex' });
  }

  pareja() {
    this.noPareja = this.datosDePareja.ninguno;
    if (this.noPareja === true) {
      this.datosDePareja.datosParejas = [{
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

      }];

    }
  }

  checkvisibles() {

    if (this.datosDePareja.ninguno) {
      $('#content-pareja').css('display', 'none');
      $('#content-act-pareja').css('display', 'none');
    } else {
      $('#content-pareja').css('display', 'block');
      $('#content-act-pareja').css('display', 'block');
    }

  }

  asignaDomicilio(form: FormGroup) {
  }

  abrirObservaciones() {
    $('body').addClass('disabled-onepage-scroll')
    $('#obs-pareja').css({ 'display': 'flex' });
    $('.menu-nav').css({ 'display': 'none' });
    $('#content-btnSave').css({ 'display': 'none' });
    this.aclara = this.datosDePareja.aclaracionesObservaciones;
  }
  cerrarObservaciones() {
    $('#obs-pareja').css('display', 'none');
    $('.menu-nav').css({ display: 'flex' });
    $('#content-btnSave').css({ display: 'flex' });
    this.aclara = this.datosDePareja.aclaracionesObservaciones;

  }
  guardarObservaciones() {
    $('#obs-pareja').css('display', 'none');
    $('.menu-nav').css({ display: 'flex' });
    $('#content-btnSave').css({ display: 'flex' });
    this.datosDePareja.aclaracionesObservaciones = this.aclara;
  }

  filterMunicipios(id, clear = true) {

    this.filterMuns = this.cata.catalogoMun.filter(item => item.fk === Number(id));
    if (clear) {
      this.datoParejaTmp.domicilioDependienteEconomico.domicilio.domicilioMexico.municipioAlcaldia.id = null;
    }
  }
}
