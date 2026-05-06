import { Component, OnInit, EventEmitter, Output, HostListener } from '@angular/core';
import * as $ from 'jquery';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormControl, FormGroup } from '@angular/forms';
import { timeTouchEvent } from 'src/app/globals';

@Component({
  selector: 'app-inversiones',
  templateUrl: './inversiones.component.html',
  styleUrls: ['./inversiones.component.scss']
})

export class InversionesComponent implements OnInit {

  help = false;
  form: FormGroup;
  declaracionInicio = false;
  inversiones = [];
  noEmpleo: any;
  @Output() modificar = new EventEmitter();
  @Output() eliminar = new EventEmitter();
  @Output() acla = new EventEmitter();
  @Output() ninguno = new EventEmitter();
  aclara: string;
  i: any;
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

  div = document.getElementById('scrollInversiones');
  @HostListener('touchend', ['$event'])
  stop(event) {
    this.div = document.getElementById('scrollInversiones');
    this.endTimeTouch = new Date().getTime()
    let diff = this.endTimeTouch - this.startTimeTouch
    if (this.div['scrollHeight'] !== this.div['clientHeight']) {
      if (this.countFingers === 2) {
      
        this.countTouches++
        if (this.countTouches === 2) {
          if (this.startTimeTouch){
            if (diff < timeTouchEvent) {
              if (this.distanceTouch > 0) {document.getElementById('goForwardBtn').click()}
            }
          this.countTouches = 0;
          }
        }
      }
    } else
      if (diff < timeTouchEvent) {
        if (this.distanceTouch > 0) document.getElementById('goForwardBtn').click()
      }
  }

  @HostListener('mousewheel', ['$event'])
  _wheel(event: MouseEvent) {
    this.div = document.getElementById('scrollInversiones');
    if (this.div['scrollHeight'] != this.div['clientHeight']) event.stopPropagation()
    if (this.div['offsetHeight'] != this.div['scrollHeight'])
      if (this.div['scrollTop'] + this.div['offsetHeight'] + 1 > this.div['scrollHeight'] && event['wheelDeltaY'] < 0)
        document.getElementById('goForwardBtn').click()
      else if (this.div['scrollTop'] === 0 && event['wheelDeltaY'] > 0)
        document.getElementById('goBackBtn').click()
  }
  constructor(private modalService: NgbModal, private activeModal: NgbActiveModal) { }

  ngOnInit() {
    let ac = JSON.parse(sessionStorage.getItem('inversiones'));
    this.aclara=ac.aclaracionesObservaciones;
    this.getDatos();
    this.form = new FormGroup({
      'inversionLocaliza': new FormControl(' '),
      'tipoInversion': new FormControl(' '),
      'titularInversion': new FormControl(' '),
      'bancaria': new FormControl(' '),
      'tercero': new FormControl(' '),
      'fondosInversion': new FormControl(' '),
      'organizaciones': new FormControl(' '),
      'seguros': new FormControl(' '),
      'valoresBursatiles': new FormControl(' '),
      'afores': new FormControl(' ')
    });
  }

  abrirObservaciones() {
    $('body').addClass('disabled-onepage-scroll');
    $("#obs-inversiones").css({ "display": "flex" });
    $(".menu-nav").css({ "display": "none" });
    $("#content-btnSave").css({ "display": "none" });
    this.aclaObs=this.aclara;
  }

  cerrarObservaciones() {
    $('body').removeClass('disabled-onepage-scroll');
    $("#obs-inversiones").css("display", "none");
    $(".menu-nav").css({ "display": "flex" });
    $("#content-btnSave").css({ "display": "flex" });
    this.aclaObs=this.aclara;
  }

  guardarObservaciones(){
    const dataAclara = this.aclaObs;  
    const curriculares= JSON.parse(sessionStorage.getItem('inversiones'));  
    curriculares.aclaracionesObservaciones=dataAclara;
    sessionStorage.setItem('inversiones', JSON.stringify(curriculares));
    $('#obs-inversiones').css('display', 'none');
    $('.menu-nav').css({ display: 'flex' });
    $('#content-btnSave').css({ display: 'flex' });
    this.acla.emit(curriculares.aclaracionesObservaciones);
  }

  getDatos() {
    this.i = JSON.parse(sessionStorage.getItem('inversiones'));

    this.inversiones = this.i.inversion || [];
    this.aclara = this.i.aclaracionesObservaciones;
    if (this.i.ninguno == true) {
      $("#abrirInversion").css("display", "none");
    }
    console.log("llego inversiones: ", this.inversiones);
  }

  modificarDatos(data) {
    this.modificar.emit(data);
  }

  eliminarDatos(a, data) {
    console.log(a);
    const id = `#${a.currentTarget.id}`;
    const spanId = `#spanEliminarInver${data.idPosicionVista}`;
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
    const spanId = `#spanEliminarInver${data.idPosicionVista}`;
    if ($(id).hasClass('confirm') || $(this).hasClass('done')) {
      setTimeout(() => {
        $(id).removeClass('confirm').removeClass('done');
        $(spanId).text('Eliminar');
      }, 2000);
    }
  }

  pareja() {

    this.noEmpleo = this.i.ninguno;
    if (this.noEmpleo === true) {
      let data = JSON.parse(sessionStorage.getItem('inversiones'));
      data.ninguno = true;
      data.inversion = null;
      sessionStorage.setItem('inversiones', JSON.stringify(data));
      this.ninguno.emit(data);
      setTimeout(() => {
        $('#nextSection').click();
      }, 1000);
      $("#abrirInversion").css("display", "none");
    } else {
      let data = JSON.parse(sessionStorage.getItem('inversiones'));
      data.ninguno = null;
      data.inversion = [];
      sessionStorage.setItem('inversiones', JSON.stringify(data));
      this.ninguno.emit(data);
      $("#abrirInversion").css("display", "block");
    }
  }

}
