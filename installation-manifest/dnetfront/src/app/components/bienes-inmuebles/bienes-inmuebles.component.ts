import { Component, OnInit, EventEmitter, Output, HostListener } from '@angular/core';
import { FormGroup } from '@angular/forms';
import * as $ from 'jquery';
import { timeTouchEvent } from 'src/app/globals';

@Component({
  selector: 'app-bienes-inmuebles',
  templateUrl: './bienes-inmuebles.component.html',
  styleUrls: ['./bienes-inmuebles.component.scss']
})
export class BienesInmueblesComponent implements OnInit {

  form: FormGroup;
  bienesInmuebles = [];
  noEmpleo: any;
  @Output() modificar = new EventEmitter();
  @Output() eliminar = new EventEmitter();
  @Output() acla = new EventEmitter();
  @Output() ninguno = new EventEmitter();

  aclara: string;
  b: any;

  endTimeTouch
  startTimeTouch
  
  /* Contar touchEnd events */
  countTouches = 0
  countFingers
  
  /* Datos de la distacia del touch Event */
  distanceTouch
  startScreenTouch
  endScreenTouch
  aclaObs: any;
  
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
  
  div=document.getElementById('scrollBiIn');
  @HostListener('touchend', ['$event'])
  stop(event) {

    this.div = document.getElementById('scrollBiIn');
    // Setter el momento en que termina el touch event
    this.endTimeTouch = new Date().getTime()
    // Calcular diferencia de tiempo de inicio al fin del evento touch
    let diff = this.endTimeTouch - this.startTimeTouch
    // Valida si el elemento tiene scrollBars
    if (this.div['scrollHeight'] != this.div['clientHeight']) {
      // Valida que el movimiento sea con dos dedos
      if (this.countFingers === 2) {
    
        this.countTouches++
        // Cuando sea 2 es porque quitó los dos dedos de la pantalla
        if (this.countTouches === 2) {
          // Valida que exista el momento de inicio del evento
          if (this.startTimeTouch)
            // Valida la velocidad del movimiento
            if (diff < timeTouchEvent) {
              // Si la distancia es > 0 avanza a la siguiente sección
              if (this.distanceTouch > 0) 
              {document.getElementById('goForwardBtn').click()}
              // Si la distancia es < 0 regresa a la sección anterior
              if (this.distanceTouch < 0) 
              {document.getElementById('goBackBtn').click()}
            }
          this.countTouches = 0 // Settear a 0 el némero de de touchEnd events
        }
      }
    } else
      // Valida la velocidad del movimiento
      if (diff < timeTouchEvent) {
        // Si la distancia es > 0 avanza a la siguiente sección
        if (this.distanceTouch > 0) 
        {document.getElementById('goForwardBtn').click()}
        // Si la distancia es < 0 regresa a la sección anterior
        if (this.distanceTouch < 0) 
        {document.getElementById('goBackBtn').click()}
      }
  }
  
  @HostListener('mousewheel', ['$event'])
  _wheel(event: MouseEvent) {
    this.div=document.getElementById('scrollBiIn');
    // Obtener elemento que tiene el scroll
    // Si tiene scroll detiene la propagación del componente padre
    if (this.div['scrollHeight'] != this.div['clientHeight']) 
    {event.stopPropagation()}
    if (this.div['offsetHeight'] != this.div['scrollHeight']){
      // Valida que este en el top del elemto y la dirección del scroll sea hacia arriba
      if (this.div['scrollTop'] + this.div['offsetHeight'] + 1 > this.div['scrollHeight'] && event['wheelDeltaY'] < 0)
       { document.getElementById('goForwardBtn').click()}
      // Valida que este en el bottom del elemto y la dirección del scroll sea hacia abajo
      else if (this.div['scrollTop'] === 0 && event['wheelDeltaY'] > 0)
       { document.getElementById('goBackBtn').click()}
    }
  }


  constructor() { }

  ngOnInit() {
    const ac = JSON.parse(sessionStorage.getItem('bienesInmuebles'));
    this.aclara=ac.aclaracionesObservaciones;
    this.getDatos();
  }

  abrirObservaciones() {
    $('body').addClass('disabled-onepage-scroll');
    $("#obs-inmuebles").css({ "display": "flex" });
    $(".menu-nav").css({ "display": "none" });
    $("#content-btnSave").css({ "display": "none" });
    const ac = JSON.parse(sessionStorage.getItem('bienesInmuebles'));
    this.aclara=ac.aclaracionesObservaciones;
    this.aclaObs=this.aclara;
  }

  cerrarObservaciones() {
    $('body').removeClass('disabled-onepage-scroll');
    $("#obs-inmuebles").css("display", "none");
    $(".menu-nav").css({ "display": "flex" });
    $("#content-btnSave").css({ "display": "flex" });
    this.aclaObs=this.aclara;
  }

  guardarObservaciones(){
    const dataAclara = this.aclaObs;  
    const curriculares= JSON.parse(sessionStorage.getItem('bienesInmuebles'));  
    curriculares.aclaracionesObservaciones=dataAclara;
    sessionStorage.setItem('bienesInmuebles', JSON.stringify(curriculares));
    $('#obs-inmuebles').css('display', 'none');
    $('.menu-nav').css({ display: 'flex' });
    $('#content-btnSave').css({ display: 'flex' });
    this.acla.emit(curriculares.aclaracionesObservaciones);
  }

  getDatos() {
    this.b = JSON.parse(sessionStorage.getItem('bienesInmuebles'));
    this.bienesInmuebles = this.b.bienesInmuebles || [];
    
    if (this.b.ninguno == true) {
      $("#abrirInmueble").css("display","none");
    }
  }



  modificarDatos(data) {
    console.log("data es", data);
    if (data.domicilio.domicilioExtranjero == null) {
      data.domicilio.domicilioExtranjero = {
        calle: "",
        ciudadLocalidad: "",
        codigoPostal: "",
        estadoProvincia: "",
        numeroExterior: "",
        numeroInterior: "",
        pais: {
          id: 0,
          valor: ""
        }
      }
    }

    if (data.domicilio.domicilioMexico == null) {
      data.domicilio.domicilioMexico = {
        calle: "",
        codigoPostal: "",
        coloniaLocalidad: "",
        entidadFederativa: {
          id: 0,
          valor: "",
        },
        municipioAlcaldia: {
          fk: 0,
          id: 0,
          valor: ""
        },
        numeroExterior: "",
        numeroInterior: ""
      }
    }


    this.modificar.emit(data);
  }

  eliminarDatos(a, data) {
    console.log(a);
    const id = `#${a.currentTarget.id}`;
    const spanId = `#spanEliminarBiIn${data.idPosicionVista}`;
    if ($(id).hasClass('confirm')) {
      $(id).addClass('done');
      $(spanId).text('Eliminando');
      this.eliminar.emit(data);
    } else {
      $(id).addClass('confirm');
      $(spanId).text('Confirmar eliminar');
    }
  }

  eliminarMouseout(a, data) {
    const id = `#${a.currentTarget.id}`;
    const spanId = `#spanEliminarBiIn${data.idPosicionVista}`;
    if ($(id).hasClass('confirm') || $(this).hasClass('done')) {
      setTimeout(() => {
        $(id).removeClass('confirm').removeClass('done');
        $(spanId).text('Eliminar');
      }, 2000);
    }
  }

  pareja() {
    
    this.noEmpleo = this.b.ninguno;
    if (this.noEmpleo === true) {
      const data = JSON.parse(sessionStorage.getItem('bienesInmuebles'));
      data.ninguno = true;
      data.bienesInmuebles = null;
      sessionStorage.setItem('bienesInmuebles', JSON.stringify(data));
      this.ninguno.emit(data);
      $("#abrirInmueble").css("display","none");
      setTimeout(() => {
        $('#nextSection').click();
      }, 1000);
    }else{
      const data =JSON.parse(sessionStorage.getItem('bienesInmuebles'));
      data.ninguno = null;
      data.bienesInmuebles = [];
      sessionStorage.setItem('bienesInmuebles', JSON.stringify(data));
      this.ninguno.emit(data);
      $("#abrirInmueble").css("display","block");
    }
  }
}
