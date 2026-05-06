import { Component, OnInit, EventEmitter, Output, HostListener } from '@angular/core';
import * as $ from 'jquery';
import { FormGroup } from '@angular/forms';
import { timeTouchEvent } from 'src/app/globals';


@Component({
  selector: 'app-clientes-principales',
  templateUrl: './clientes-principales.component.html',
  styleUrls: ['./clientes-principales.component.scss']
})
export class ClientesPrincipalesComponent implements OnInit {

  form: FormGroup;
  clientesPrinc = [];
  noEmpleo: any;

  @Output() realizaActividadLucrativa = new EventEmitter();
  @Output() modificar = new EventEmitter();
  @Output() eliminar = new EventEmitter();
  @Output() acla = new EventEmitter();
  @Output() ninguno = new EventEmitter();
    aclara: string;
c: any;

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

div=document.getElementById('scrollCliPr');
@HostListener('touchend', ['$event'])
stop(event) {
  this.div = document.getElementById('scrollCliPr');
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
  this.div = document.getElementById('scrollCliPr');
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

    this.getDatos();



  }

  getDatos() {
    this.c = JSON.parse(sessionStorage.getItem('clientesPrincipales'));
    this.clientesPrinc = this.c.clientes || [];
    
  }

  abrirObservaciones() {
    $('body').addClass('disabled-onepage-scroll');
    $("#obs-clientes").css({ "display": "flex" });
    $(".menu-nav").css({ "display": "none" });
    $("#content-btnSave").css({ "display": "none" });
    this.c = JSON.parse(sessionStorage.getItem('clientesPrincipales'));
    this.aclara = this.c.aclaracionesObservaciones;
  }

  cerrarObservaciones() {
    $('body').removeClass('disabled-onepage-scroll');
    $("#obs-clientes").css("display", "none");
    $(".menu-nav").css({ "display": "flex" });
    $("#content-btnSave").css({ "display": "flex" });
    this.aclara=this.c.aclaracionesObservaciones;
  }

  guardarObservaciones() {
    const val = $("#aclaracionesObservacionesCli").val();
    console.log("val ",val);
    
    let clientes= JSON.parse(sessionStorage.getItem('clientesPrincipales'));  
    clientes.aclaracionesObservaciones=val;
    sessionStorage.setItem('clientesPrincipales', JSON.stringify(clientes));
    $('#obs-clientes').css('display', 'none');
    $('.menu-nav').css({ display: 'flex' });
    $('#content-btnSave').css({ display: 'flex' });
    this.acla.emit(clientes.aclaracionesObservaciones);
  }
     


  

  clientesStatus() {
    this.noEmpleo = this.c.realizaActividadLucrativa;
    if (this.noEmpleo) {

      const data = JSON.parse(sessionStorage.getItem('clientesPrincipales'));
      data.realizaActividadLucrativa = true;
      data.clientes = [];
      sessionStorage.setItem('clientesPrincipales', JSON.stringify(data));
      this.realizaActividadLucrativa.emit();

    } else {
      const data = JSON.parse(sessionStorage.getItem('clientesPrincipales'));

      data.realizaActividadLucrativa = false;
      data.clientes = null;
      sessionStorage.setItem('clientesPrincipales', JSON.stringify(data));
      this.realizaActividadLucrativa.emit();

      setTimeout(() => {
        $("#nextSection").click();
      }, 1000);
    }
  }

  modificarDatos(data) {
    this.modificar.emit(data);
  }

  eliminarDatos(a, data) {
    console.log(a);
    const id = `#${a.currentTarget.id}`;
    const spanId = `#spanEliminarCliPr${data.idPosicionVista}`;
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
    const spanId = `#spanEliminarCliPr${data.idPosicionVista}`;
    if ($(id).hasClass('confirm') || $(this).hasClass('done')) {
      setTimeout(() => {
        $(id).removeClass('confirm').removeClass('done');
        $(spanId).text('Eliminar');
      }, 2000);
    }
  }

}
