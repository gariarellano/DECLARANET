import { Component, OnInit,EventEmitter,Output } from '@angular/core';
import * as $ from 'jquery';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Globals } from 'src/app/globals';

@Component({
  selector: 'app-actividad-laboral-pareja',
  templateUrl: './actividad-laboral-pareja.component.html',
  styleUrls: ['./actividad-laboral-pareja.component.scss']
})
export class ActividadLaboralParejaComponent implements OnInit {

  @Output()
  aceptar = new EventEmitter<JSON>();

  form: FormGroup;
  help = false;
  actividadLaboral:any;
  constructor(public cata: Globals) { }

  ngOnInit() {

    this.actividadLaboral={
      ambitoSector: {
        id: "",
        valor: ""
      },
      sectorPublico: {
        nivelOrdenGobierno: "", // FEDERAL, ESTATAL, MUNICIPAL / ALCALDÍA
        ambitoPublico: "", //EJECUTIVO, LEGISLATIVO, JUDICIAL, ÓRGANO AUTÓNOMO ANTES PODER
        nombreEntePublico: "",
        areaAdscripcion: "",
        empleoCargoComision: "",
        funcionPrincipal: ""
      },
      sectorPrivadoOtro: {
        nombreEmpresaSociedadAsociacion: "",
        rfc: "",
        area: "",
        empleoCargo: "",
        sector: {
          id: "",
          valor: ""
        }
      },
      fechaIngreso: "",
      salarioMensualNeto:""
    }


    this.form = new FormGroup({
      'actividad': new FormControl(''),
      'lugarReside': new FormControl('')
    });
  }

  guardaLocal(){
    this.aceptar.emit(this.actividadLaboral);
  }

  abrirObservaciones() {
    $("#obs-act-lab").css({ "display": "flex" });
    $(".menu-nav").css({ "display": "none" });
    $("#content-btnSave").css({ "display": "none" });
  }

  cerrarObservaciones() {
    $("#obs-act-lab").css("display", "none");
    $(".menu-nav").css({ "display": "flex" });
    $("#content-btnSave").css({ "display": "flex" });
  }

}
