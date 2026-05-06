import { Component, OnInit, Input, Output, EventEmitter, HostListener } from '@angular/core';
import { DetalleAvisoCambioDependencia } from '../../interfaces/avisoCambioDependencia';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Globals, timeTouchEvent } from 'src/app/globals';
import * as $ from 'jquery';

@Component({
  selector: 'app-aviso-cambio-dependencia',
  templateUrl: './aviso-cambio-dependencia.component.html',
  styleUrls: ['./aviso-cambio-dependencia.component.scss']
})

export class AvisoCambioDependenciaComponent implements OnInit {

  @Output() acla = new EventEmitter();
  @Output() aceptar = new EventEmitter();

  avisos : DetalleAvisoCambioDependencia;
  avisosM: any;
  textTitulo: string;
  today: any;

  avisoTmp: any;
  filterMuns: any;
  aclara: string;
  fechaC: any;
  fechaI: any;
  help= false;
  MS_PER_DAY = 1000 * 60 * 60 * 24;

  /* Datos duración del evento touch */
endTimeTouch
startTimeTouch

/* Contar touchEnd events */
countTouches = 0
countFingers

/* Datos de la distacia del touch Event */
distanceTouch
startScreenTouch
endScreenTouch
/* Escuchar cuando el touch se mueva */
@HostListener('touchmove', ['$event'])
moveTouch(event) {
  // Obtener el No. dedos del evento touch
  this.countFingers = event.touches.length
  // Calcular distancia recorrida desde el punto inicial
  this.distanceTouch = event.changedTouches[0].clientX - this.startScreenTouch
}

/* Escuchar cuando inicie el evento touch */
@HostListener('touchstart', ['$event'])
start(event) {
  // Settear el momento en que inicia el evento touch
  this.startTimeTouch = new Date().getTime()
  // Settear posición en que inicia el evento touch
  this.startScreenTouch = event.changedTouches[0].clientX
}

div = document.getElementById('scroll1');
/* Escuchar cuando termine el evento touch */
@HostListener('touchend', ['$event'])
stop(event) {
  this.div = document.getElementById('scroll1');
  // Setter el momento en que termina el touch event
  this.endTimeTouch = new Date().getTime()
  // Calcular diferencia de tiempo de inicio al fin del evento touch
  let diff = this.endTimeTouch - this.startTimeTouch
  // Valida si el elemento tiene scrollBars
  if (this.div['scrollHeight'] != this.div['clientHeight']) {
    // Valida que el movimiento sea con dos dedos
    if (this.countFingers === 2) {
      /* Contar cuando termina el evento
         (1 dedo desencadena 1 touchEnd event,
          2 dedos desencadenan 2 touchEnd events,
          3 dedos desencadenan 3 touchEnd events, etc)
      */
      this.countTouches++
      // Cuando sea 2 es porque quitó los dos dedos de la pantalla
      if (this.countTouches === 2) {
        // Valida que exista el momento de inicio del evento
        if (this.startTimeTouch)
          // Valida la velocidad del movimiento
          if (diff < timeTouchEvent) {
            // Si la distancia es > 0 avanza a la siguiente sección
            if (this.distanceTouch > 0) document.getElementById('goForwardBtn').click()
            // Si la distancia es < 0 regresa a la sección anterior
            // if (this.distanceTouch < 0) document.getElementById('goBackBtn').click()
          }
        this.countTouches = 0 // Settear a 0 el némero de de touchEnd events
      }
    }
  } else
    // Valida la velocidad del movimiento
    if (diff < timeTouchEvent) {
      // Si la distancia es > 0 avanza a la siguiente sección
      if (this.distanceTouch > 0) document.getElementById('goForwardBtn').click()
      // Si la distancia es < 0 regresa a la sección anterior
      // if (this.distanceTouch < 0) document.getElementById('goBackBtn').click()
    }
}

@HostListener('mousewheel', ['$event'])
_wheel(event: MouseEvent) {
  this.div = document.getElementById('scroll1');
  // Obtener elemento que tiene el scroll
  // Si tiene scroll detiene la propagación del componente padre
  if (this.div['scrollHeight'] != this.div['clientHeight']) event.stopPropagation()
  if (this.div['offsetHeight'] != this.div['scrollHeight'])
  // Valida que este en el top del elemto y la dirección del scroll sea hacia arriba
  if (this.div['scrollTop'] + this.div['offsetHeight'] + 1 > this.div['scrollHeight'] && event['wheelDeltaY'] < 0)
    document.getElementById('goForwardBtn').click()
  // Valida que este en el bottom del elemto y la dirección del scroll sea hacia abajo
  else if (this.div['scrollTop'] === 0 && event['wheelDeltaY'] > 0)
    document.getElementById('goBackBtn').click()
}


  constructor(private modalService: NgbModal, private activeModal: NgbActiveModal, public cata: Globals, public globals: Globals) {

  }

  @HostListener('paste', ['$event']) blockPaste(e: KeyboardEvent) {
    e.preventDefault();
  }

  ngOnInit() {
    this.today = new Date().toJSON().split('T')[0];

    this.textTitulo = "Aviso por cambio de dependencia";
    // this.fecha = this.globals.declaracionLoaded.encabezado.fechaEncargo;

    // this.avisos = {
    //     empleoCargoComisionConcluye: {
    //       ente: {
    //         ambitoPublico: null,
    //         id: null,
    //         nivelOrdenGobierno: {
    //           entidadFederativa: null,
    //           nivelOrdenGobierno: null
    //         },
    //         nombre: null
    //       },
    //       nivelJerarquico: {
    //         id: null,
    //         valor: null,
    //         fk: null,
    //         valorUno: null
    //       },
    //       fechaConclusionEncargo: this.fecha

    //     },

    //     empleoCargoComisionInicia: {
    //       id: null,
    //       ente: {
    //         ambitoPublico: null,
    //         id: null,
    //         nivelOrdenGobierno: {
    //           entidadFederativa: {
    //             id: null,
    //             valor: null
    //           },
    //           nivelOrdenGobierno: null,
    //         },
    //         nombre: null
    //       },
    //       fechaInicioEncargo: null,
    //       empleoCargoComision: null,
    //       nivelJerarquico: {
    //         id: null,
    //         valor: null,
    //         fk: null,
    //         valorUno: null
    //       },
    //       contratadoPorHonorarios: null,
    //       nivelEmpleoCargoComision: null,
    //       areaAdscripcion: null,
    //       domicilio: {
    //         //@domicilionull
    //         ubicacion: null, //México, Extranjero
    //         domicilioMexico: { //uno u otro
    //           calle: null,
    //           codigoPostal: null,
    //           coloniaLocalidad: null,
    //           entidadFederativa: {
    //             id: null,
    //             valor: null
    //           },
    //           municipioAlcaldia: {
    //             id: null,
    //             valor: null,
    //             fk: 0
    //           },
    //           numeroExterior: null,
    //           numeroInterior: null
    //         },
    //         domicilioExtranjero: { //uno u otro
    //           calle: null,
    //           ciudadLocalidad: null,
    //           codigoPostal: null,
    //           estadoProvincia: null,
    //           numeroExterior: null,
    //           numeroInterior: null,
    //           pais: {
    //             id: null,
    //             valor: null
    //           }
    //         }
    //       },

    //     },
    //   aclaracionesObservaciones: null,
    // }

    this.avisos = JSON.parse(sessionStorage.getItem('avisoCambio'));


    this.avisoTmp = {
        domicilio: {
          domicilioMexico: JSON.parse(JSON.stringify(this.avisos.empleoCargoComisionInicia.domicilio.domicilioMexico)),
          domicilioExtranjero: JSON.parse(JSON.stringify(this.avisos.empleoCargoComisionInicia.domicilio.domicilioExtranjero))
        },
      }



      $('.help').click(() => {
        this.help = !this.help;
        if (this.help) {
          $('label').addClass('openHelp');
          $('label')
            .next('input,select')
            .css('visibility', 'hidden');
        } else {
          $('label').removeClass('openHelp');
          $('label')
            .next('input,select')
            .css('visibility', 'visible');
        }
      });



  }


  abrirModalLugar(lugar: string) {
    switch (lugar) {
      case 'mexico':
          this.avisoTmp.domicilio.domicilioMexico = JSON.parse(JSON.stringify(this.avisos.empleoCargoComisionInicia.domicilio.domicilioMexico));
          this.filterMunicipios(this.avisoTmp.domicilio.domicilioMexico.entidadFederativa.id, false);
          break;
      case 'extranjero':
          this.avisoTmp.domicilio.domicilioExtranjero = JSON.parse(JSON.stringify(this.avisos.empleoCargoComisionInicia.domicilio.domicilioExtranjero));
          break;

    }

    $(`#modal-lugar-${lugar}`).css({ 'display': 'flex' });
    $('.menu-nav').css({ 'display': 'none' });
    $('#content-btnSave').css({ 'display': 'none' });
    $('body').addClass('disabled-onepage-scroll');
  }

  guardarModalLugar(lugar: string) {
    switch (lugar) {
      case 'mexico':
          this.avisos.empleoCargoComisionInicia.domicilio.domicilioMexico = JSON.parse(JSON.stringify(this.avisoTmp.domicilio.domicilioMexico));
          break;
      case 'extranjero':
          this.avisos.empleoCargoComisionInicia.domicilio.domicilioExtranjero = JSON.parse(JSON.stringify(this.avisoTmp.domicilio.domicilioExtranjero));
          break;
    }
    console.log("este es", this.avisoTmp.domicilio.domicilioMexico);
    this.cerrarModalLugar(lugar);
  }

  guardaLocal() {
    const a = new Date(this.avisos.empleoCargoComisionConcluye.fechaConclusionEncargo),
    b = new Date(this.avisos.empleoCargoComisionInicia.fechaInicioEncargo),
    difference = this.dateDiffInDays(a, b);
    if ( difference > 59 ){
      alert("La diferencia entre la fecha de conclusión de encargo anterior y la fecha de inicio de encargo actual debe ser menor a 60 días");
      return;
    }


    if(this.avisos.empleoCargoComisionInicia.domicilio.ubicacion === 'MEXICO'){
      this.avisos.empleoCargoComisionInicia.domicilio.domicilioExtranjero = null;
    }
    if(this.avisos.empleoCargoComisionInicia.domicilio.ubicacion === 'EXTRANJERO'){
      this.avisos.empleoCargoComisionInicia.domicilio.domicilioMexico = null;
    }

   this.aceptar.emit(this.avisos);
   console.log("que trae", this.avisos);
  }

  cerrarModalLugar(lugar: string) {

    $(`#modal-lugar-${lugar}`).fadeOut('slow', () => {
      $(this).css('display', 'none');
    });

    $('.menu-nav').css({ 'display': 'flex' });
    $('#content-btnSave').css({ 'display': 'flex' });
    $('body').removeClass('disabled-onepage-scroll');
  }


  abrirObservaciones() {
    this.aclara = this.avisos.aclaracionesObservaciones;
    $('body').addClass('disabled-onepage-scroll')
    $('#obs-aviso').css({ 'display': 'flex' });
    $('.menu-nav').css({ 'display': 'none' });
    $('#content-btnSave').css({ 'display': 'none' });
  }

  // cerrarObservaciones() {
  //   $('#obs-pareja').css('display', 'none');
  //   $('.menu-nav').css({ 'display': 'flex' });
  //   $('#content-btnSave').css({ 'display': 'flex' });
  //   let dataAclara = JSON.parse(sessionStorage.getItem('avisoCambio'));
  //   dataAclara.aclaracionesObservaciones = this.aclara;
  //   sessionStorage.setItem('avisoCambio', JSON.stringify(dataAclara));
  //   this.acla.emit(dataAclara.aclaracionesObservaciones);
  // }

  cerrarObservaciones() {
    $('#obs-aviso').css('display', 'none');
    $('.menu-nav').css({ display: 'flex' });
    $('#content-btnSave').css({ display: 'flex' });
    this.aclara = this.avisos.aclaracionesObservaciones;

  }
  guardarObservaciones() {
    $('#obs-aviso').css('display', 'none');
    $('.menu-nav').css({ display: 'flex' });
    $('#content-btnSave').css({ display: 'flex' });
    this.avisos.aclaracionesObservaciones = this.aclara;
  }

  filterMunicipios(id, clear = true) {


    // if (!this.globals.catalogoMun) {

    //   setTimeout(() => {
    //     this.filterMuns = this.globals.catalogoMun.filter(item => item.fk === Number(id));

    //   }, 2000);
    // } else {
    //   this.filterMuns = this.globals.catalogoMun.filter(item => item.fk === Number(id));
    // }
    this.filterMuns = this.globals.catalogoMun.filter(item => item.fk === Number(id));
    if (clear) {
      this.avisoTmp.domicilio.domicilioMexico.municipioAlcaldia = null;
    }
  }



// a and b are javascript Date objects
dateDiffInDays(a, b) {
  // Discard the time and time-zone information.
  const utc1 = Date.UTC(a.getFullYear(), a.getMonth(), a.getDate());
  const utc2 = Date.UTC(b.getFullYear(), b.getMonth(), b.getDate());

  return Math.floor((utc2 - utc1) / this.MS_PER_DAY);
}


}
