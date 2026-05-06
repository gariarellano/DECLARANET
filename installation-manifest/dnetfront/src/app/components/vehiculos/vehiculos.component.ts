import { Component, OnInit, EventEmitter, Output, HostListener } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import * as $ from 'jquery';
import { timeTouchEvent } from 'src/app/globals';

@Component({
  selector: 'app-vehiculos',
  templateUrl: './vehiculos.component.html',
  styleUrls: ['./vehiculos.component.scss']
})
export class VehiculosComponent implements OnInit {
  help = false;
  form: FormGroup;
  vehiculos: any = [];
  declaracionInicio = false;
  noEmpleo: any;
  @Output() modificar = new EventEmitter();
  @Output() eliminar = new EventEmitter();
  @Output() acla = new EventEmitter();
  @Output() ninguno = new EventEmitter();

  aclara: string;
  v: any;
  endTimeTouch
  startTimeTouch

  /* Contar touchEnd events */
  countTouches = 0
  countFingers

  /* Datos de la distacia del touch Event */
  distanceTouch
  startScreenTouch
  endScreenTouch
  aclaObs: string;
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

  div = document.getElementById('scrollVehiculos');
  /* Escuchar cuando termine el evento touch */
  @HostListener('touchend', ['$event'])
  stop(event) {
    this.div = document.getElementById('scrollVehiculos');
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
              if (this.distanceTouch < 0) document.getElementById('goBackBtn').click()
            }
          console.log(this.distanceTouch);

          this.countTouches = 0 // Settear a 0 el némero de de touchEnd events
        }
      }
    } else
      // Valida la velocidad del movimiento
      if (diff < timeTouchEvent) {
        // Si la distancia es > 0 avanza a la siguiente sección
        if (this.distanceTouch > 0) document.getElementById('goForwardBtn').click()
        // Si la distancia es < 0 regresa a la sección anterior
        if (this.distanceTouch < 0) document.getElementById('goBackBtn').click()
      }
    console.log(this.distanceTouch);
  }

  @HostListener('mousewheel', ['$event'])
  _wheel(event: MouseEvent) {
    this.div = document.getElementById('scrollVehiculos');
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
  constructor() { }

  ngOnInit() {

    this.form = new FormGroup({
      'tipoVehiculo': new FormControl(' '),
      'tipoVehiculoEspecific': new FormControl(' '),
      'titularVehiculo': new FormControl(' '),
      'transmisor': new FormControl(' '),
      'relacionTransmisor': new FormControl(''),
      'relacionTransmisorEspecific': new FormControl(''),
      'marca': new FormControl(''),
      'modelo': new FormControl(''),
      'anio': new FormControl(''),
      'tercero': new FormControl(''),
      'numSerie': new FormControl(''),
      'lugarRegistrado': new FormControl(''),
      'nombreTercero': new FormControl(''),
      'rfc': new FormControl(''),
      'formaAdquisicion': new FormControl(''),
      'formaPago': new FormControl(''),
      'valorAdquisicion': new FormControl(''),
      'tipoMoneda': new FormControl(''),
      'fechaAdquisicion': new FormControl(''),
      'nombreTransmisor': new FormControl('')
    });
    let ac = JSON.parse(sessionStorage.getItem('vehiculos'));
    this.aclara=ac.aclaracionesObservaciones;
    this.getDatos();
  }

  abrirObservaciones() {
    $('body').addClass('disabled-onepage-scroll');
    $("#obs-vehiculos").css({ "display": "flex" });
    $(".menu-nav").css({ "display": "none" });
    $("#content-btnSave").css({ "display": "none" });
    this.aclaObs =this.aclara;
  }

  cerrarObservaciones() {
    $('body').removeClass('disabled-onepage-scroll');
    $("#obs-vehiculos").css("display", "none");
    $(".menu-nav").css({ "display": "flex" });
    $("#content-btnSave").css({ "display": "flex" });
    this.aclaObs=this.aclara;
  }

    
  guardarObservaciones(){
    let dataAclara = this.aclaObs;  
    let vehi= JSON.parse(sessionStorage.getItem('vehiculos'));  
    vehi.aclaracionesObservaciones=dataAclara;
    sessionStorage.setItem('vehiculos', JSON.stringify(vehi));
    $('#obs-vehiculos').css('display', 'none');
    $('.menu-nav').css({ display: 'flex' });
    $('#content-btnSave').css({ display: 'flex' });
    this.acla.emit(vehi.aclaracionesObservaciones);
  }

  getDatos() {
    this.v = JSON.parse(sessionStorage.getItem('vehiculos'));
    this.vehiculos = this.v.vehiculos || [];
    console.log("vehiculos",this.vehiculos);
    
    this.aclara = this.v.aclaracionesObservaciones;
    if (this.v.ninguno == true) {
      $("#abrirVehiculo").css("display", "none");
    }
  }

  modificarDatos(data) {
    this.modificar.emit(data);
  }

  eliminarDatos(a, data) {
    console.log(a);
    const id = `#${a.currentTarget.id}`;
    const spanId = `#spanEliminarVehiculos${data.idPosicionVista}`;
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
    const spanId = `#spanEliminarVehiculos${data.idPosicionVista}`;
    if ($(id).hasClass('confirm') || $(this).hasClass('done')) {
      setTimeout(() => {
        $(id).removeClass('confirm').removeClass('done');
        $(spanId).text('Eliminar');
      }, 2000);
    }
  }

  pareja() {

    this.noEmpleo = this.v.ninguno;
    if (this.noEmpleo === true) {
      let data = JSON.parse(sessionStorage.getItem('vehiculos'));
      data.ninguno = true;
      data.vehiculos = null;
      sessionStorage.setItem('vehiculos', JSON.stringify(data));
      this.ninguno.emit(data);
      $("#abrirVehiculo").css("display", "none");
      setTimeout(() => {
        $('#nextSection').click();
      }, 1000);

    } else {
      let data = JSON.parse(sessionStorage.getItem('vehiculos'));
      data.ninguno = null;
      data.vehiculos = [];
      sessionStorage.setItem('vehiculos', JSON.stringify(data));
      this.ninguno.emit(data);
      $("#abrirVehiculo").css("display", "block");
    }
  }
}
