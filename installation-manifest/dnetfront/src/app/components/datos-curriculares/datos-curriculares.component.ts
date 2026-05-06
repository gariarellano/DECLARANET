import { Component, OnInit, EventEmitter, Output, HostListener } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import * as $ from 'jquery';
import { timeTouchEvent } from 'src/app/globals';

@Component({
  selector: 'app-datos-curriculares',
  templateUrl: './datos-curriculares.component.html',
  styleUrls: ['./datos-curriculares.component.scss']
})
export class DatosCurricularesComponent implements OnInit {

  help = false;
  form: FormGroup;
  datosCurricularesDeclarante: Array<any>;
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

  div = document.getElementById('scrollDatosCurri');
  @HostListener('touchend', ['$event'])
  stop(event) {
    this.div = document.getElementById('scrollDatosCurri');
    this.endTimeTouch = new Date().getTime()
    let diff = this.endTimeTouch - this.startTimeTouch
    if (this.div['scrollHeight'] !== this.div['clientHeight']) {
      if (this.countFingers === 2) {
        this.countTouches++
        if (this.countTouches === 2) {
          if (this.startTimeTouch){
            if (diff < timeTouchEvent) {
              if (this.distanceTouch > 0) {document.getElementById('goForwardBtn').click()}
              if (this.distanceTouch < 0) {document.getElementById('goBackBtn').click()}
            }

            this.countTouches = 0;
          }
        }
      }
    } else if (diff < timeTouchEvent) {
        if (this.distanceTouch > 0) {document.getElementById('goForwardBtn').click()}
        if (this.distanceTouch < 0) {document.getElementById('goBackBtn').click()}
      }
  }

  @HostListener('mousewheel', ['$event'])
  _wheel(event: MouseEvent) {
    this.div = document.getElementById('scrollDatosCurri');

    if (this.div['scrollHeight'] !== this.div['clientHeight']){event.stopPropagation()}
    if (this.div['offsetHeight'] !== this.div['scrollHeight']){
      if (this.div['scrollTop'] + this.div['offsetHeight'] + 1 > this.div['scrollHeight'] && event['wheelDeltaY'] < 0)
        {document.getElementById('goForwardBtn').click()}
      else if (this.div['scrollTop'] === 0 && event['wheelDeltaY'] > 0)
        {document.getElementById('goBackBtn').click()}
    }
  }
  constructor() { }


  procesaAceptar(mensaje) {
    console.log('llego ', mensaje);
  }

  ngOnInit() {

    this.form = new FormGroup({
      'nivel': new FormControl(' '),
      'estatus': new FormControl(' '),
      'documento': new FormControl(' '),
      'lugarInstitucion': new FormControl(' ')
    });
    
    let ac = JSON.parse(sessionStorage.getItem('datosCurricularesDeclarante'));
    this.aclara=ac.aclaracionesObservaciones;
    
    this.getDatos();

  }

  abrirObservaciones() {
    $('body').addClass('disabled-onepage-scroll')
    $('#obs-curriculares').css({ 'display': 'flex' });
    $('.menu-nav').css({ 'display': 'none' });
    $('#content-btnSave').css({ 'display': 'none' });
    this.aclaObs =this.aclara;
  }

  cerrarObservaciones() {
    $('#obs-curriculares').css('display', 'none');
    $('.menu-nav').css({ 'display': 'flex' });
    $('#content-btnSave').css({ 'display': 'flex' });
    this.aclaObs=this.aclara;
  } 
  
  guardarObservaciones(){
    const dataAclara = this.aclaObs;  
    const curriculares= JSON.parse(sessionStorage.getItem('datosCurricularesDeclarante'));  
    curriculares.aclaracionesObservaciones=dataAclara;
    sessionStorage.setItem('datosCurricularesDeclarante', JSON.stringify(curriculares));
    $('#obs-curriculares').css('display', 'none');
    $('.menu-nav').css({ display: 'flex' });
    $('#content-btnSave').css({ display: 'flex' });
    this.acla.emit(curriculares.aclaracionesObservaciones);
  }

  getDatos() {

    const data = JSON.parse(sessionStorage.getItem('datosCurricularesDeclarante'));
    console.log('esta es la revision ' , data);
    
    this.datosCurricularesDeclarante = data.escolaridad || [];
    this.aclara = data.aclaracionesObservaciones;

  }

  modificarDatos(data) {
    this.modificar.emit(data);
  }

  eliminarDatos(a, data) {
    
    const id = `#${a.currentTarget.id}`;
    const spanId = `#spanEliminar${data.idPosicionVista}`;
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
    const spanId = `#spanEliminar${data.idPosicionVista}`;
    if ($(id).hasClass('confirm') || $(this).hasClass('done')) {
      setTimeout(() => {
        $(id).removeClass('confirm').removeClass('done');
        $(spanId).text('Eliminar');
      }, 2000);
    }
  }

getNivel(data){
  const nivel = data
  if (nivel == null){
    return 'POR DEFINIR';
  }else{
    return nivel.valor;
  }
}




}
