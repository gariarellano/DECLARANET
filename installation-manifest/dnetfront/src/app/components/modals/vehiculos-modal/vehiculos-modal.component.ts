import { Component, OnInit, Input, HostListener, ViewChild, AfterViewInit } from '@angular/core';
import { FormControl, FormGroup, NgForm } from '@angular/forms';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import * as $ from 'jquery';
import { Globals } from 'src/app/globals';

@Component({
  selector: 'app-vehiculos-modal',
  templateUrl: './vehiculos-modal.component.html',
  styleUrls: ['./vehiculos-modal.component.scss']
})
export class VehiculosModalComponent implements OnInit, AfterViewInit {

  form: FormGroup;
  help = false;
  vehiculo: any;
  vehiculoR: any;
  initLoad: boolean;
  transmisor: any;
  transmisores: Array<any> = [];
  tercero: any;
  terceros: Array<any> = [];
  lista: any;
  @Input() public objUpdate;
  dataDisabled: any;
  histFormInval: boolean;
  TIPO_OPERACION: any;
  @ViewChild('vehiculosForm') vehiculosForm: NgForm;
  fecha: any;
  constructor(private modalService: NgbModal, private activeModal: NgbActiveModal, public cata: Globals, public globals: Globals) {
    this.TIPO_OPERACION = null;
    this.dataDisabled = [
      {
        name: "tipoVehiculo",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "titularVehiculo",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "marca",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "modelo",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "anio",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "numSerie",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "ubicacion",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "entidadFederativa",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "pais",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "formaAdquisicion",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "formaPago",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "valorAdquisicion",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "tipoMoneda",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "fechaAdquisicion",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      }
    ];
    this.fecha = {
      fechaMax: cata.fechaReferenciaMax,
      fechaMin: null
    }
    this.fecha = JSON.stringify(this.fecha);
  }



  @HostListener('paste', ['$event']) blockPaste(e: KeyboardEvent) {
    e.preventDefault();
  }

  ngOnInit() {
    this.transmisor = {
      id: null,
      idPosicionVista: null,
      relacionConTitular: {
        id: null,
        valor: null
      },
      relacionConTitularOtro: null,
      tipoPersona: null,
      personaFisica: {
        nombre: null,
        primerApellido: null,
        segundoApellido: null,
        rfc: null
      },
      personaMoral: {
        nombre: null,
        rfc: null
      }
    }

    this.tercero = {
      id: null,
      idPosicionVista: null,
      tipoPersona: null,
      personaFisica: {
        nombre: null,
        primerApellido: null,
        segundoApellido: null,
        rfc: null
      },
      personaMoral: {
        nombre: null,
        rfc: null
      }
    }

    this.vehiculo = {
      id: "",
      verificar: true,
      idPosicionVista: "",
      registroHistorico: false,
      tipoOperacion: null,
      tipoVehiculo: {
        id: null,
        valor: null,
      },
      tipoVehiculoOtro: "",
      titular: {
        id: null,
        valor: null,
        valorUno: null
      },
      transmisores: [],
      marca: "",
      modelo: "",
      anio: "",
      numeroSerieRegistro: "",
      terceros: [],
      lugarRegistro: {
        ubicacion: null,

        localizacionMexico: {
          entidadFederativa: {
            id: null,
            valor: ""
          }
        },

        localizacionExtranjero: {
          pais: {
            id: null,
            valor: ""
          }
        }
      },
      formaAdquisicion: {
        id: null,
        valor: ""
      },
      formaPago: null,
      valorAdquisicion: {
        moneda: {
          id: null,
          valor: ""
        },
        monto: ""
      },
      fechaAdquisicion: "",
      motivoBaja: {
        id: null,
        valor: null
      },
      motivoBajaOtro: null,
      valorVenta: {
        moneda: {
          id: 96,
          valor: 'PESO MEXICANO'
        },
        monto: null
      }

    }

    if (this.objUpdate) {

      if (this.objUpdate.valorAdquisicion == null) { this.objUpdate.valorAdquisicion = this.vehiculo.valorAdquisicion; }
      if (this.objUpdate.valorAdquisicion.moneda == null) { this.objUpdate.valorAdquisicion.moneda = this.vehiculo.valorAdquisicion.moneda; }
      if (this.objUpdate.terceros == null) { this.objUpdate.terceros = this.vehiculo.terceros; }
      if (this.objUpdate.transmisores == null) { this.objUpdate.transmisores = this.vehiculo.transmisores; }
      if (this.objUpdate.motivoBaja == null) { this.objUpdate.motivoBaja = this.vehiculo.motivoBaja; }
      if (this.objUpdate.motivoBajaOtro == null) { this.objUpdate.motivoBajaOtro = this.vehiculo.motivoBajaOtro; }
      if (this.objUpdate.valorVenta == null) { this.objUpdate.valorVenta = this.vehiculo.valorVenta; }
      if (this.objUpdate.lugarRegistro == null) { this.objUpdate.lugarRegistro = this.vehiculo.lugarRegistro; }
      if (this.objUpdate.lugarRegistro.localizacionExtranjero == null) { this.objUpdate.lugarRegistro.localizacionExtranjero = this.vehiculo.lugarRegistro.localizacionExtranjero; }
      if (this.objUpdate.lugarRegistro.localizacionMexico == null) { this.objUpdate.lugarRegistro.localizacionMexico = this.vehiculo.lugarRegistro.localizacionMexico; }
      if (this.objUpdate.tipoVehiculo == null) { this.objUpdate.tipoVehiculo = this.vehiculo.tipoVehiculo; }

      this.objUpdate.titular = this.objUpdate.titular == null ? this.vehiculo.titular : this.objUpdate.titular;
      this.objUpdate.formaAdquisicion = this.objUpdate.formaAdquisicion == null ? this.objUpdate.formaAdquisicion = this.vehiculo.formaAdquisicion : this.objUpdate.formaAdquisicion;

      console.log(this.objUpdate);
      this.vehiculo = this.objUpdate;
      this.transmisores = this.vehiculo.transmisores;
      this.terceros = this.vehiculo.terceros;
      this.initLoad = true;

    }

    this.vehiculoR = JSON.parse(JSON.stringify(this.vehiculo));
  }

  ngAfterViewInit(): void {

    let formVal: boolean;
    const a = this.vehiculosForm.statusChanges.subscribe(() => {
      if (this.vehiculosForm.valid !== formVal) {
        this.TIPO_OPERACION = this.globals.getTipoOperacion(this.vehiculosForm.valid, this.vehiculo.registroHistorico, this.vehiculo.tipoOperacion, 'P');
        this.histFormInval = (this.vehiculo.registroHistorico && !this.vehiculosForm.valid);
        if (!this.vehiculosForm.valid) {
          a.unsubscribe();
          setTimeout(() => {
            this.initLoad = false;
            this.resetBaja();
          }, 500);
        }
        formVal = this.vehiculosForm.valid;

      }
    });

    setTimeout(() => {
      this.initLoad = false;
      if (this.vehiculosForm.valid && this.globals.precargaOracle) {
        this.resetBaja();
      }
      a.unsubscribe();
    }, 500);
  }

  resetBaja() {
    this.dataDisabled.forEach((data) => {
      if (this.globals.precargaOracle) {
        data.tipoOperacion.BAJA = false;
        data.tipoOperacion.MODIFICAR = false;
      } else {
        data.tipoOperacion.BAJA = this.vehiculosForm.controls[data.name] ? this.vehiculosForm.controls[data.name].valid && this.vehiculosForm.controls[data.name].value !== '' : false;
        if (data.tipoOperacion.MODIFICAR === true) {
          data.tipoOperacion.MODIFICAR = this.vehiculosForm.controls[data.name] ? this.vehiculosForm.controls[data.name].valid && this.vehiculosForm.controls[data.name].value !== '' : false;
        }
      }
    });
  }

  tipoOperacionChange(id) {
    if (id === 'SIN_CAMBIO' || id === 'BAJA') {
      this.vehiculo = JSON.parse(JSON.stringify(this.vehiculoR));
      this.vehiculo.tipoOperacion = id;
    }
  }
  getDisabled(obj, tipoOperacion1) {
    if (!obj.name || !tipoOperacion1 || this.initLoad) {
      return obj.disabled;
    }
    if (!this.vehiculo.registroHistorico) {
      return false;
    } else {
      const newObj = this.dataDisabled.find((value) => value.name === obj.name);
      const newObj1 = newObj.tipoOperacion[tipoOperacion1];
      return newObj1;
    }


  }


  close() {
    this.modalService.dismissAll('vehiculos');
    $("body").removeClass("disabled-onepage-scroll");
  }

  guardaLocal() {
    this.vehiculo.terceros = this.terceros;
    this.vehiculo.transmisores = this.transmisores;

    let dato = this.vehiculo;
    dato.id = null;
    dato.verificar = true;
    const idP = dato.idPosicionVista.toString();
    dato.idPosicionVista = idP;
    dato.anio = Number(dato.anio);
    dato.valorAdquisicion.monto = Number(dato.valorAdquisicion.monto);
    if (dato.tipoOperacion !== 'BAJA') {
      dato.motivoBaja = null;
      dato.motivoBajaOtro = null;
      dato.valorVenta = null;
    } else {
      if (dato.motivoBaja.id !== 9999) {
        dato.motivoBajaOtro = null;
      }
      if (dato.motivoBaja.id !== 1) {
        dato.valorVenta = null;
      }

    }

    if (dato.tipoVehiculo.id !== 9999) {
      dato.tipoVehiculoOtro = null;
    }


    dato.formaPago = dato.formaAdquisicion.id !== 1 ? null : dato.formaPago;
    dato.formaPago = dato.formaAdquisicion.id !== 1 ? null : dato.formaPago;
    dato.terceros = dato.titular.valorUno !== 'DC' ? [] : dato.terceros;
    dato.formaPago = dato.formaAdquisicion.id !== 1 ? 'NO_APLICA' : dato.formaPago;



    this.transmisores.forEach((t, index) => {
      t.personaFisica = t.tipoPersona == 'PERSONA_MORAL' ? null : t.personaFisica;
      t.personaMoral = t.tipoPersona == 'PERSONA_FISICA' ? null : t.personaMoral;
      if (t.relacionConTitular.id !== 9999) {
        t.relacionConTitularOtro = null;
      }
      t.idPosicionVista = index;
      t.id = null;
    });


    this.terceros.forEach((t, index) => {
      t.personaFisica = t.tipoPersona == 'PERSONA_MORAL' ? null : t.personaFisica;
      t.personaMoral = t.tipoPersona == 'PERSONA_FISICA' ? null : t.personaMoral;
      t.idPosicionVista = index;
      t.id = null;
    });


    dato.lugarRegistro.localizacionMexico = dato.lugarRegistro.ubicacion === "EXTRANJERO" ? null : dato.lugarRegistro.localizacionMexico;
    dato.lugarRegistro.localizacionExtranjero = dato.lugarRegistro.ubicacion === "MEXICO" ? null : dato.lugarRegistro.localizacionExtranjero;

    console.log("data vehiculo: ", this.vehiculo);

    let s = JSON.parse(sessionStorage.getItem('vehiculos'));

    this.lista = s.vehiculos;

    const index = this.lista.findIndex(({ idPosicionVista }) => idPosicionVista == dato.idPosicionVista);

    if (index === -1) {
      this.lista.push(dato);
    } else {
      this.lista[index] = dato;
    }

    this.lista.forEach(
      (element, index) => {
        this.lista[index].idPosicionVista = index + 1;
      });
    let bien = { ninguno: false, vehiculos: this.lista, aclaracionesObservaciones: "" }

    sessionStorage.setItem('vehiculos', JSON.stringify(bien));

    this.activeModal.close({ result: 'success', form: 'vehiculos' });
  }

  aceptarTrans() {

    this.transmisores.push(this.transmisor);
    console.log(this.transmisores);


    this.transmisor = {
      id: "",
      idPosicionVista: "",
      relacionConTitular: {
        id: "",
        valor: "",
        clavePdn: ""
      },
      tipoPersona: "",
      personaFisica: {
        nombre: "",
        primerApellido: "",
        segundoApellido: "",
        rfc: ""
      },
      personaMoral: {
        nombre: "",
        rfc: ""
      }
    }
  }

  aceptarTercero() {
    this.terceros.push(this.tercero);
    console.log(this.terceros);

    this.tercero = {
      id: "",
      idPosicionVista: "",
      tipoPersona: "",
      personaFisica: {
        nombre: "",
        primerApellido: "",
        segundoApellido: "",
        rfc: ""
      },
      personaMoral: {
        nombre: "",
        rfc: ""
      }
    }
  }

  deleteTrans(index) {
    this.transmisores.splice(index, 1);
  }
  deleteTercero(index) {
    this.terceros.splice(index, 1);
  }

  limpiaHijoUbica() {
    this.vehiculo.lugarRegistro.localizacionMexico.entidadFederativa.id = null;
    this.vehiculo.lugarRegistro.localizacionExtranjero.pais.id = null;
  }
}


