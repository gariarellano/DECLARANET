import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import * as $ from 'jquery';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-prestamo-bien-vehiculo',
  templateUrl: './prestamo-bien-vehiculo.component.html',
  styleUrls: ['./prestamo-bien-vehiculo.component.scss']
})
export class PrestamoBienVehiculoComponent implements OnInit {

  help = false;
  form: FormGroup;
  declaracionInicio = false;
  bienVehiculos: any;
  @Output() modificarV = new EventEmitter();
  @Output() eliminar = new EventEmitter();
  @Output() acla = new EventEmitter();
  aclara: string;
  constructor() { }

  ngOnInit() {

    this.getDatos();

    this.form = new FormGroup({
      'ubicacionVehiculo': new FormControl(' '),
      'dueno': new FormControl(' '),
      'vehiculo': new FormControl(' ')
    });

    console.log("vehiculo ", JSON.parse(sessionStorage.getItem('datosGenerales')));

  }

  abrirObservaciones() {
    $('body').addClass('disabled-onepage-scroll')
    $("#obs-prestamo-vehiculo").css({ "display": "flex" });
    $(".menu-nav").css({ "display": "none" });
    $("#content-btnSave").css({ "display": "none" });
  }

  cerrarObservaciones() {
    $("#obs-prestamo-vehiculo").css("display", "none");
    $(".menu-nav").css({ "display": "flex" });
    $("#content-btnSave").css({ "display": "flex" });
    let dataAclara = JSON.parse(sessionStorage.getItem('prestamoComodato'));
    dataAclara.aclaracionesObservaciones = this.aclara;
    sessionStorage.setItem('prestamoComodato', JSON.stringify(dataAclara));
    this.acla.emit(dataAclara.aclaracionesObservaciones);
  }
  getDatos() {
    let data = JSON.parse(sessionStorage.getItem('prestamoComodato'));
    this.bienVehiculos = data.prestamo || [];

    console.log("llego vehiculo", data);
  }

  // NO LO OCUPO

}
