import { Component, OnInit, EventEmitter, Output, HostListener } from '@angular/core';
import * as $ from 'jquery';
import { FormGroup } from '@angular/forms';
import { timeTouchEvent } from 'src/app/globals';

@Component({
  selector: 'app-experiencia-laboral',
  templateUrl: './experiencia-laboral.component.html',
  styleUrls: ['./experiencia-laboral.component.scss']
})
export class ExperienciaLaboralComponent implements OnInit {
  help: boolean = false;
  form: FormGroup;
  datosExperiencia = [];
  noEmpleo: boolean;
  declaracionInicio = false;

  @Output() modificar = new EventEmitter();
  @Output() eliminar = new EventEmitter();
  @Output() acla = new EventEmitter();
  @Output() ninguno = new EventEmitter();
  aclara: string;
  e: any;

  endTimeTouch
  startTimeTouch

  countTouches = 0;
  countFingers = 0;

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

  div = document.getElementById('scrollExLab');
  @HostListener('touchend', ['$event'])
  stop(event) {
    this.div = document.getElementById('scrollExLab');
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
          console.log(this.distanceTouch);

          this.countTouches = 0;
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
    this.div = document.getElementById('scrollExLab');
   
    if (this.div['scrollHeight'] !== this.div['clientHeight']) {event.stopPropagation()}
    if (this.div['offsetHeight'] !== this.div['scrollHeight']){
      if (this.div['scrollTop'] + this.div['offsetHeight'] + 1 > this.div['scrollHeight'] && event['wheelDeltaY'] < 0)
      { document.getElementById('goForwardBtn').click()}
      else if (this.div['scrollTop'] === 0 && event['wheelDeltaY'] > 0)
       { document.getElementById('goBackBtn').click()}
      
    }
  }
  constructor() { }

  ngOnInit() {

    let ac = JSON.parse(sessionStorage.getItem('datosCurricularesDeclarante'));
    this.aclara=ac.aclaracionesObservaciones;
    this.getDatos();
  }

  abrirObservaciones() {
    $('body').addClass('disabled-onepage-scroll');
    $("#obs-experiencia").css({ "display": "flex" });
    $(".menu-nav").css({ "display": "none" });
    $("#content-btnSave").css({ "display": "none" });
    this.aclaObs =this.aclara;
  }

  cerrarObservaciones() {
    $('body').removeClass('disabled-onepage-scroll');
    $("#obs-experiencia").css("display", "none");
    $(".menu-nav").css({ "display": "flex" });
    $("#content-btnSave").css({ "display": "flex" });
    this.aclaObs=this.aclara;
  }

  guardarObservaciones(){
    const dataAclara = this.aclaObs;
    const curriculares= JSON.parse(sessionStorage.getItem('experienciaLab'));
    curriculares.aclaracionesObservaciones=dataAclara;
    sessionStorage.setItem('experienciaLab', JSON.stringify(curriculares));
    $('#obs-experiencia').css('display', 'none');
    $('.menu-nav').css({ display: 'flex' });
    $('#content-btnSave').css({ display: 'flex' });
    this.acla.emit(JSON.stringify(curriculares.aclaracionesObservaciones));
  }

  getDatos() {
    this.e = JSON.parse(sessionStorage.getItem('experienciaLab'));
    this.datosExperiencia = this.e.experienciaLaboral || [];
    this.aclara = this.e.aclaracionesObservaciones;
    if (this.e.ninguno == true) {
      $("#abrirExperiencia").css("display", "none");
    }
  }

  empleoStatus() {
    this.noEmpleo = !this.noEmpleo;
    if (this.noEmpleo) {

      let data = JSON.parse(sessionStorage.getItem('participaEmpresas'));
      data.ninguno = true;
      sessionStorage.setItem('participaEmpresas', JSON.stringify(data));

      $("#abrirExperiencia").css("display", "none");
      $(".item5").find("i").removeClass("foco-rojo foco-naranja").addClass("foco-verde");

      setTimeout(() => {
        $("#nextSection").click();
      }, 1000);
    } else {
      let data = JSON.parse(sessionStorage.getItem('participaEmpresas'));
      data.ninguno = null;
      sessionStorage.setItem('participaEmpresas', JSON.stringify(data));
      $("#abrirExperiencia").css("display", "block");
      $(".item5").find("i").removeClass("foco-verde foco-naranja").addClass("foco-rojo");
    }
  }
  modificarDatos(data) {

    if (data.actividadLaboral.sectorPrivadoOtro == null) {

     data.actividadLaboral.sectorPrivadoOtro = {
      nombreEmpresaSociedadAsociacion: '',
      rfc: '',
      area: '',
      empleoCargo: '',
      sectorOtro: '',
      sector: {
        id: 0,
        valor: ''
      }
     };
   }

    if ( data.actividadLaboral.sectorPublico == null) {

    data.actividadLaboral.sectorPublico = {
      nivelOrdenGobierno: '',
      ambitoPublico: '',
      nombreEntePublico: '',
      areaAdscripcion: '',
      empleoCargoComision: '',
      funcionPrincipal: ''
    };
   }

    this.modificar.emit(data);
  }

  eliminarDatos(a, data) {
    console.log(a);
    const id = `#${a.currentTarget.id}`;
    const spanId = `#spanEliminarExLab${data.idPosicionVista}`;
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
    const spanId = `#spanEliminarExLab${data.idPosicionVista}`;
    if ($(id).hasClass('confirm') || $(this).hasClass('done')) {
      setTimeout(() => {
        $(id).removeClass('confirm').removeClass('done');
        $(spanId).text('Eliminar');
      }, 2000);
    }
  }

  pareja() {
    this.noEmpleo = this.e.ninguno;
    if (this.noEmpleo === true) {
      let data = JSON.parse(sessionStorage.getItem('experienciaLab'));
      data.ninguno = true;
      data.experienciaLaboral = null;
      sessionStorage.setItem('experienciaLab', JSON.stringify(data));
      this.ninguno.emit(data);
      setTimeout(() => {
        $('#nextSection').click();
      }, 1000);
      $("#abrirExperiencia").css("display", "none");

    } else {
      let data = JSON.parse(sessionStorage.getItem('experienciaLab'));
      data.ninguno = null;
      data.experienciaLaboral = [];
      sessionStorage.setItem('experienciaLab', JSON.stringify(data));
      this.ninguno.emit(data);
      $("#abrirExperiencia").css("display", "block");
    }
  }
}
