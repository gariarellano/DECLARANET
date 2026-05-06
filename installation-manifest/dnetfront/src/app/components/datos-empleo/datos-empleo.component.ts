import { Component, OnInit, EventEmitter, Output, HostListener } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import * as $ from 'jquery';
import { timeTouchEvent } from 'src/app/globals';

@Component({
  selector: 'app-datos-empleo',
  templateUrl: './datos-empleo.component.html',
  styleUrls: ['./datos-empleo.component.scss']
})

export class DatosEmpleoComponent implements OnInit {

  form: FormGroup;
  datosEmpleo: any;
  help: boolean = false;
  @Output() modificar = new EventEmitter();
  @Output() eliminar = new EventEmitter();
  @Output() acla = new EventEmitter();
  aclara: string;

  endTimeTouch
  startTimeTouch

  countTouches = 0
  countFingers

  distanceTouch
  startScreenTouch
  endScreenTouch
  aclaObs: any;
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

  div = document.getElementById('scrollEmpleo');
  @HostListener('touchend', ['$event'])
  stop(event) {
    this.div = document.getElementById('scrollEmpleo');
    this.endTimeTouch = new Date().getTime()
    let diff = this.endTimeTouch - this.startTimeTouch
    if (this.div['scrollHeight'] !== this.div['clientHeight']) {
      if (this.countFingers === 2) {
        
        this.countTouches++
        if (this.countTouches === 2) {
          if (this.startTimeTouch){
            if (diff < timeTouchEvent) {
              if (this.distanceTouch > 0) document.getElementById('goForwardBtn').click()
              if (this.distanceTouch < 0) document.getElementById('goBackBtn').click()
            }            
            this.countTouches = 0;
          }
        }
      }
    } else
    if (diff < timeTouchEvent) {
        if (this.distanceTouch > 0) document.getElementById('goForwardBtn').click()
        if (this.distanceTouch < 0) document.getElementById('goBackBtn').click()
      }
      console.log(this.distanceTouch);
  }

  @HostListener('mousewheel', ['$event'])
  _wheel(event: MouseEvent) {
    this.div = document.getElementById('scrollEmpleo');
    
    if (this.div['scrollHeight'] != this.div['clientHeight']) event.stopPropagation()
    if (this.div['offsetHeight'] != this.div['scrollHeight'])
    if (this.div['scrollTop'] + this.div['offsetHeight'] + 1 > this.div['scrollHeight'] && event['wheelDeltaY'] < 0)
      document.getElementById('goForwardBtn').click()
    else if (this.div['scrollTop'] === 0 && event['wheelDeltaY'] > 0)
      document.getElementById('goBackBtn').click()
  }
  
  constructor() { }

  ngOnInit() {
    let ac = JSON.parse(sessionStorage.getItem('datosEmpleo'));

    this.aclara=ac.aclaracionesObservaciones;
    this.getDatos();
  }

  abrirObservaciones() {
    $('body').addClass('disabled-onepage-scroll');
    $("#obs-empleo").css({ "display": "flex" });
    $(".menu-nav").css({ "display": "none" });
    $("#content-btnSave").css({ "display": "none" });
    this.aclaObs =this.aclara;
  }

  cerrarObservaciones() {
    $('body').removeClass('disabled-onepage-scroll');
    $("#obs-empleo").css("display", "none");
    $(".menu-nav").css({ "display": "flex" });
    $("#content-btnSave").css({ "display": "flex" });
    this.aclaObs=this.aclara;
  }

  guardarObservaciones(){
    const dataAclara = this.aclaObs;  
    const curriculares= JSON.parse(sessionStorage.getItem('datosEmpleo'));  
    curriculares.aclaracionesObservaciones=dataAclara;
    sessionStorage.setItem('datosEmpleo', JSON.stringify(curriculares));
    $('#obs-empleo').css('display', 'none');
    $('.menu-nav').css({ display: 'flex' });
    $('#content-btnSave').css({ display: 'flex' });
    this.acla.emit(JSON.stringify(curriculares.aclaracionesObservaciones));
  }

  getDatos() {
    let data = JSON.parse(sessionStorage.getItem('datosEmpleo'));
    this.datosEmpleo = data.empleoCargoComision || [];
    this.aclara = data.aclaracionesObservaciones;

    console.log("lista: ", this.datosEmpleo);
  }

  modificarDatos(data) {
    console.log("dataa: ", data);

    this.modificar.emit(data);
  }

  eliminarDatos(a, data) {
    console.log(a);
    const id = `#${a.currentTarget.id}`;
    const spanId = `#spanEliminarDaEm${data.idPosicionVista}`;
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
    const spanId = `#spanEliminarDaEm${data.idPosicionVista}`;
    if ($(id).hasClass('confirm') || $(this).hasClass('done')) {
      setTimeout(() => {
        $(id).removeClass('confirm').removeClass('done');
        $(spanId).text('Eliminar');
      }, 2000);
    }
  }

}
