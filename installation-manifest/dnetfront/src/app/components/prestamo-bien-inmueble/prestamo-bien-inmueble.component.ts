import { Component, OnInit, EventEmitter, Output, HostListener } from '@angular/core';
import * as $ from 'jquery';
import { FormGroup } from '@angular/forms';
import { timeTouchEvent } from 'src/app/globals';
@Component({
  selector: 'app-prestamo-bien-inmueble',
  templateUrl: './prestamo-bien-inmueble.component.html',
  styleUrls: ['./prestamo-bien-inmueble.component.scss']
})
export class PrestamoBienInmuebleComponent implements OnInit {

  help = false;
  form: FormGroup;
  declaracionInicio = false;
  bienInmuebles: any = [];
  noEmpleo: any;
  @Output() modificarI = new EventEmitter();
  @Output() modificarV = new EventEmitter();
  @Output() eliminar = new EventEmitter();
  @Output() acla = new EventEmitter();
  @Output() ninguno = new EventEmitter();
  b: any;
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

  div = document.getElementById('scrollComodatos');
  @HostListener('touchend', ['$event'])
  stop(event) {
    this.div = document.getElementById('scrollComodatos');
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
    } else
      if (diff < timeTouchEvent) {
        if (this.distanceTouch > 0) {document.getElementById('goForwardBtn').click()}
        if (this.distanceTouch < 0) {document.getElementById('goBackBtn').click()}
      }
    console.log(this.distanceTouch);
  }

  @HostListener('mousewheel', ['$event'])
  _wheel(event: MouseEvent) {
    this.div = document.getElementById('scrollComodatos');
    if (this.div['scrollHeight'] !== this.div['clientHeight']) event.stopPropagation()
    if (this.div['offsetHeight'] !== this.div['scrollHeight'])
      if (this.div['scrollTop'] + this.div['offsetHeight'] + 1 > this.div['scrollHeight'] && event['wheelDeltaY'] < 0)
        document.getElementById('goForwardBtn').click()
      else if (this.div['scrollTop'] === 0 && event['wheelDeltaY'] > 0)
        document.getElementById('goBackBtn').click()
  }
  constructor() { }

  ngOnInit() {
    let ac = JSON.parse(sessionStorage.getItem('prestamoComodato'));
    this.aclara=ac.aclaracionesObservaciones;
    this.getDatos();
  }

  abrirObservaciones() {
    $('body').addClass('disabled-onepage-scroll');
    $("#obs-prestamo-inmueble").css({ "display": "flex" });
    $(".menu-nav").css({ "display": "none" });
    $("#content-btnSave").css({ "display": "none" });
    this.aclaObs=this.aclara;
  }

  cerrarObservaciones() {
    $('body').removeClass('disabled-onepage-scroll');
    $("#obs-prestamo-inmueble").css("display", "none");
    $(".menu-nav").css({ "display": "flex" });
    $("#content-btnSave").css({ "display": "flex" });
    this.aclaObs=this.aclara;
  }

  guardarObservaciones(){
    let dataAclara = this.aclaObs;
    let prestamoInmu= JSON.parse(sessionStorage.getItem('prestamoComodato'));  
    prestamoInmu.aclaracionesObservaciones=dataAclara;
    sessionStorage.setItem('prestamoComodato', JSON.stringify(prestamoInmu));
    $('#obs-prestamo-inmueble').css('display', 'none');
    $('.menu-nav').css({ display: 'flex' });
    $('#content-btnSave').css({ display: 'flex' });
    this.acla.emit(prestamoInmu.aclaracionesObservaciones);
  }

  getDatos() {
    this.b = JSON.parse(sessionStorage.getItem('prestamoComodato'));
    this.bienInmuebles = this.b || [];
    this.aclara = this.b.aclaracionesObservaciones;
    if (this.b.ninguno == true) {
      $("#botonesComodatos").css("display", "none");
    }
  }

  comodatoStatus() {
    this.noEmpleo = !this.noEmpleo;
    if (this.noEmpleo) {

      let data = JSON.parse(sessionStorage.getItem('prestamoComodato'));
      data.ninguno = true;
      sessionStorage.setItem('prestamoComodato', JSON.stringify(data));

      $("#abrirPrestaInmueble").css("display", "none");
      $("#abrirPrestaVehiculo").css("display", "none");
      $(".item5").find("i").removeClass("foco-rojo foco-naranja").addClass("foco-verde");

      setTimeout(() => {
        $("#nextSection").click();
      }, 1000);
    } else {
      let data = JSON.parse(sessionStorage.getItem('prestamoComodato'));
      data.ninguno = null;
      sessionStorage.setItem('prestamoComodato', JSON.stringify(data));
      $("#abrirPrestaInmueble").css("display", "inline-block");
      $("#abrirPrestaVehiculo").css("display", "inline-block");
      $(".item5").find("i").removeClass("foco-verde foco-naranja").addClass("foco-rojo");
    }
  }
  modificarDatosV(data) {

    this.modificarV.emit(data);
  }
  modificarDatosI(data) {


    if (data.tipoBien == "INMUEBLE") {
      if (data.inmueble.domicilio.domicilioMexico == null) {
        data.inmueble.domicilio.domicilioMexico = {
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

      if (data.inmueble.domicilio.domicilioExtranjero == null) {
        data.inmueble.domicilio.domicilioExtranjero = {
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
    }

    this.modificarI.emit(data);

  }

  eliminarDatos(a, data) {
    console.log(a);
    const id = `#${a.currentTarget.id}`;
    const spanId = `#spanEliminarPrestaBiIn${data.idPosicionVista}`;
    if ($(id).hasClass('confirm')) {
      $(id).addClass('done');
      $(spanId).text('Eliminando');
      this.eliminar.emit(data);
    } else {
      $(id).addClass('confirm');
      $(spanId).text('Confirmar eliminar');
    }
  }

  prestamoStatus() {
    this.noEmpleo = !this.noEmpleo;
    if (this.noEmpleo) {

      let data = JSON.parse(sessionStorage.getItem('prestamoComodato'));
      data.ninguno = true;
      data.prestamo = null;
      sessionStorage.setItem('prestamoComodato', JSON.stringify(data));

      $("#abrirPrestaVehiculo").css("display", "none");
      $("#abrirPrestaInmueble").css("display", "none");
      $(".item5").find("i").removeClass("foco-rojo foco-naranja").addClass("foco-verde");

      setTimeout(() => {
        $("#nextSection").click();
      }, 1000);
    } else {
      let data = JSON.parse(sessionStorage.getItem('prestamoComodato'));
      data.ninguno = null;
      data.prestamo = [];
      sessionStorage.setItem('prestamoComodato', JSON.stringify(data));
      $("#abrirPrestaVehiculo").css("display", "block");
      $("#abrirPrestaInmueble").css("display", "block");
      $(".item5").find("i").removeClass("foco-verde foco-naranja").addClass("foco-rojo");
    }
  }

  eliminarMouseout(a, data) {
    const id = `#${a.currentTarget.id}`;
    const spanId = `#spanEliminarPrestaBiIn${data.idPosicionVista}`;
    if ($(id).hasClass('confirm') || $(this).hasClass('done')) {
      setTimeout(() => {
        $(id).removeClass('confirm').removeClass('done');
        $(spanId).text('Eliminar');
      }, 2000);
    }
  }

  pareja() {

    this.noEmpleo = this.b.ninguno;
    console.log("No empleo", this.noEmpleo);

    if (this.noEmpleo === true) {
      let data = JSON.parse(sessionStorage.getItem('prestamoComodato'));
      data.ninguno = true;
      data.prestamo = null;
      sessionStorage.setItem('prestamoComodato', JSON.stringify(data));
      this.ninguno.emit(data);
      setTimeout(() => {
        $('#nextSection').click();
      }, 1000);
      $("#botonesComodatos").css("display", "none");

    } else {
      console.log("entro no empleo", this.noEmpleo);

      let data = JSON.parse(sessionStorage.getItem('prestamoComodato'));
      data.ninguno = null;
      data.prestamo = [];
      sessionStorage.setItem('prestamoComodato', JSON.stringify(data));
      this.ninguno.emit(data);
      $("#botonesComodatos").css("display", "block");
    }
  }
}
