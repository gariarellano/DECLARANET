import { Component, OnInit, EventEmitter, Output, HostListener } from '@angular/core';
import * as $ from 'jquery';
import { FormControl, FormGroup } from '@angular/forms';
import { timeTouchEvent } from 'src/app/globals';

@Component({
  selector: 'app-datos-dependiente',
  templateUrl: './datos-dependiente.component.html',
  styleUrls: ['./datos-dependiente.component.scss']
})
export class DatosDependienteComponent implements OnInit {

  form: FormGroup;
  help: boolean = false;
  datosDependiente: any;
  noEmpleo: boolean;
  @Output() modificar = new EventEmitter();
  @Output() eliminar = new EventEmitter();
  @Output() acla = new EventEmitter();
  @Output() ninguno = new EventEmitter();

  aclara: string;
  d: any;

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

  div=document.getElementById('scrollDaDe');
  @HostListener('touchend', ['$event'])
  stop(event) {
    this.div = document.getElementById('scrollDaDe');
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
          if (this.startTimeTouch){
            // Valida la velocidad del movimiento
            if (diff < timeTouchEvent) {
              // Si la distancia es > 0 avanza a la siguiente sección
              if (this.distanceTouch > 0) document.getElementById('goForwardBtn').click()
              // Si la distancia es < 0 regresa a la sección anterior
              if (this.distanceTouch < 0) document.getElementById('goBackBtn').click()
            }
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
    this.div = document.getElementById('scrollDaDe');
    // Obtener elemento que tiene el scroll
    // Si tiene scroll detiene la propagación del componente padre
    if (this.div['scrollHeight'] != this.div['clientHeight']) 
    {event.stopPropagation()}
    if (this.div['offsetHeight'] != this.div['scrollHeight']){
      // Valida que este en el top del elemto y la dirección del scroll sea hacia arriba
      if (this.div['scrollTop'] + this.div['offsetHeight'] + 1 > this.div['scrollHeight'] && event['wheelDeltaY'] < 0)
        {document.getElementById('goForwardBtn').click()}
      // Valida que este en el bottom del elemto y la dirección del scroll sea hacia abajo
      else if (this.div['scrollTop'] === 0 && event['wheelDeltaY'] > 0)
        {document.getElementById('goBackBtn').click()}
    }
  }
  constructor() { }

  ngOnInit() {
    this.datosDependiente = JSON.parse(sessionStorage.getItem('datosDependiente'));
    this.form = new FormGroup({
      'habitaDomicilioDeclarante': new FormControl(' '),
      'lugarReside': new FormControl(' '),
      'parentesco': new FormControl(' '),
      'ciudadano': new FormControl(' '),
      'dependiente': new FormControl(' '),
      'habitaDomicilio': new FormControl(' '),
      'proveedor': new FormControl(' '),
      'sector': new FormControl(' ')
    });
    const ac = JSON.parse(sessionStorage.getItem('datosDependiente'));
    this.aclara=ac.aclaracionesObservaciones;
    this.getDatos();
  }

  abrirObservaciones() {
    $('body').addClass('disabled-onepage-scroll')
    $("#obs-dependiente").css({ "display": "flex" });
    $(".menu-nav").css({ "display": "none" });
    $("#content-btnSave").css({ "display": "none" });
    this.aclaObs =this.aclara;
  }

  cerrarObservaciones() {
    $("#obs-dependiente").css("display", "none");
    $(".menu-nav").css({ "display": "flex" });
    $("#content-btnSave").css({ "display": "flex" });
    this.aclaObs =this.aclara;
  }

  guardarObservaciones(){
    const dataAclara = this.aclaObs;  
    const curriculares= JSON.parse(sessionStorage.getItem('datosDependiente'));  
    curriculares.aclaracionesObservaciones=dataAclara;
    sessionStorage.setItem('datosDependiente', JSON.stringify(curriculares));
    $('#obs-dependiente').css('display', 'none');
    $('.menu-nav').css({ display: 'flex' });
    $('#content-btnSave').css({ display: 'flex' });
    this.acla.emit(JSON.stringify(curriculares.aclaracionesObservaciones));
  }

  getDatos() {
    this.d = JSON.parse(sessionStorage.getItem('datosDependiente'));
    this.datosDependiente = this.d.datosDependientesEconomicos || [];
    this.aclara = this.d.aclaracionesObservaciones;
    console.log("datos json dependiente", this.datosDependiente);
    if (this.d.ninguno == true) {
      $("#abrirDependiente").css("display","none");
    }
  }

  modificarDatos(data) {

    this.modificar.emit(data);
  }

  eliminarDatos(a, data) {
    console.log(a);
    const id = `#${a.currentTarget.id}`;
    const spanId = `#spanEliminarDaDe${data.idPosicionVista}`;
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
    const spanId = `#spanEliminarDaDe${data.idPosicionVista}`;
    if ($(id).hasClass('confirm') || $(this).hasClass('done')) {
      setTimeout(() => {
        $(id).removeClass('confirm').removeClass('done');
        $(spanId).text('Eliminar');
      }, 2000);
    }
  }

  pareja() {
    this.noEmpleo = this.d.ninguno;
    if (this.noEmpleo === true) {
      const data = JSON.parse(sessionStorage.getItem('datosDependiente'));
      data.ninguno = true;
      data.datosDependientesEconomicos = null;
      sessionStorage.setItem('datosDependiente', JSON.stringify(data));
      this.ninguno.emit(data);
      setTimeout(() => {
        $('#nextSection').click();
      }, 1000);
      $("#abrirDependiente").css("display", "none");

    } else {
      const data = JSON.parse(sessionStorage.getItem('datosDependiente'));
      data.ninguno = null;
      data.datosDependientesEconomicos = [];
      sessionStorage.setItem('datosDependiente', JSON.stringify(data));
      this.ninguno.emit(data);
      $("#abrirDependiente").css("display", "block");
    }
  }
}
