import { Component, OnInit, Input, HostListener, ViewChild, AfterViewInit } from '@angular/core';
import { FormGroup, NgForm } from '@angular/forms';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import * as $ from 'jquery';
import { Globals } from 'src/app/globals';

@Component({
  selector: 'app-bienes-muebles-modal',
  templateUrl: './bienes-muebles-modal.component.html',
  styleUrls: ['./bienes-muebles-modal.component.scss']
})
export class BienesMueblesModalComponent implements OnInit, AfterViewInit {

  form: FormGroup;
  help = false;
  lista: any;
  bienMueble: any;
  bien: any;
  initLoad: boolean;
  transmisor: any;
  transmisores: Array<any> = [];
  tercero: any;
  terceros: Array<any> = [];
  @Input() public objUpdate;
  dataDisabled: any;
  fecha: any;
  bienR: any;
  histFormInval: boolean;
  TIPO_OPERACION: any;
  @ViewChild('bMuebles') bMuebles: NgForm;

  constructor(private readonly modalService: NgbModal, private readonly activeModal: NgbActiveModal, public cata: Globals, public globals: Globals) {
    this.dataDisabled = [
      {
        name: 'titular',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'tipoBien',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'especificBien',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'descripcionBien',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'formaAdquisicion',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'formaPago',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'formaPagos',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'valorAdquisicionMueble',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'moneda',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'tipoMonedaVenta',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'fechaAdquisicion',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'relacionTitular',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'especificTrans',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'tipoPersonaTrans',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'nombreTrans',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'primerApellido',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'segundoApellido',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'rfcTransFisic',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'nombreMoral',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'rfcMoraltra',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'tipoPersonaT',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'nombreF',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'primerApellidoF',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'segundoApellidoF',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'rfcFtra',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'nombreM',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'rfcMter',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'motivoBaja',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'motivoBajaOtro',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'valorBajaVenta',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
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
      id: "",
      idPosicionVista: "",
      relacionConTitular: {
        id: null,
        valor: null

      },
      relacionConTitularOtro: null,
      tipoPersona: null, //Fisica, Moral
      personaFisica: {
        //@persona
        nombre: null,
        primerApellido: null,
        segundoApellido: null,
        rfc: null
      },
      personaMoral: {
        //@personaMoral
        nombre: null,
        rfc: null
      }
    }

    this.tercero = {
      id: "",
      idPosicionVista: "",
      tipoPersona: null, //Fisica, Moral
      personaFisica: {
        //@persona
        nombre: null,
        primerApellido: null,
        segundoApellido: null,
        rfc: null
      },
      personaMoral: {
        //@personaMoral
        nombre: null,
        rfc: null
      }
    }

    this.bienMueble = {

      id: null,
      verificar: null,
      idPosicionVista: 0,
      registroHistorico: false,
      tipoOperacion: null,
      tipoBien: {
        id: null,
        valor: ""
      },
      tipoBienOtro: '',
      titular: {
        id: null,
        valor: null,
        valorUno: null
      },
      descripcionGeneralBien: "",
      formaAdquisicion: {
        id: null,
        valor: ""
      }, //CATALOGO
      formaPago: null, //CRÉDITO,CONTADO,NO APLICA
      valorAdquisicion: {
        //@montoMoneda
        moneda: {
          id: null,
          valor: ""
        },
        monto: 0
      },
      fechaAdquisicion: "",
      terceros: [],
      transmisores: [],
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

      if (this.objUpdate.valorAdquisicion.moneda == null) { this.objUpdate.valorAdquisicion.moneda = this.bienMueble.valorAdquisicion.moneda; }
      if (this.objUpdate.terceros == null) { this.objUpdate.terceros = this.bienMueble.terceros; }
      if (this.objUpdate.transmisores == null) { this.objUpdate.transmisores = this.bienMueble.transmisores; }
      if (this.objUpdate.tipoBien == null) { this.objUpdate.tipoBien = this.bienMueble.tipoBien; }
      if (this.objUpdate.terceros == null) { this.objUpdate.terceros = this.bienMueble.terceros; }
      if (this.objUpdate.terceros.personaMoral == null) { this.objUpdate.terceros.personaMoral = this.bienMueble.terceros.personaMoral; }
      if (this.objUpdate.terceros.personaFisica == null) { this.objUpdate.terceros.personaFisica = this.bienMueble.terceros.personaFisica; }
      if (this.objUpdate.transmisores.personaMoral == null) { this.objUpdate.transmisores.personaMoral = this.bienMueble.transmisores.personaMoral; }
      if (this.objUpdate.transmisores.personaFisica == null) { this.objUpdate.transmisores.personaFisica = this.bienMueble.transmisores.personaFisica; }
      if (this.objUpdate.titular == null) { this.objUpdate.titular = this.bienMueble.titular; }
      if (this.objUpdate.formaAdquisicion == null) { this.objUpdate.formaAdquisicion = this.bienMueble.formaAdquisicion }
      if (this.objUpdate.motivoBaja == null) { this.objUpdate.motivoBaja = this.bienMueble.motivoBaja; }
      if (this.objUpdate.motivoBajaOtro == null) { this.objUpdate.motivoBajaOtro = this.bienMueble.motivoBajaOtro; }
      if (this.objUpdate.valorVenta == null) { this.objUpdate.valorVenta = this.bienMueble.valorVenta; }

      this.bienMueble = this.objUpdate;
      this.transmisores = this.bienMueble.transmisores;
      this.terceros = this.bienMueble.terceros;

      this.initLoad = true;
    }
    this.bienR = JSON.parse(JSON.stringify(this.bienMueble));
  }


  guardaLocal() {
    this.bienMueble.terceros = this.terceros;
    this.bienMueble.transmisores = this.transmisores;

    const dato = this.bienMueble;
    dato.verificar = true;
    dato.valorAdquisicion.monto = Number(dato.valorAdquisicion.monto);
    dato.terceros = dato.titular.valorUno != 'DC' ? [] : dato.terceros;
    dato.formaPago = dato.formaAdquisicion.id != 1 ? 'NO_APLICA' : dato.formaPago;
    if (dato.tipoBien.id !== 9999) {
      dato.tipoBienOtro = null;
    }


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

    // //obtener array de sessionstorage
    let s = JSON.parse(sessionStorage.getItem('bienesMuebles'));

    // //se asigna a lista local
    this.lista = s.bienesMuebles;
    const index = this.lista.findIndex(({ idPosicionVista }) => idPosicionVista == dato.idPosicionVista);




    if (index === -1) {
      //se agrega el nuevo registro al array local
      this.lista.push(dato);
    } else {
      this.lista[index] = dato;
    }

    this.lista.forEach(
      (element, index) => {
        this.lista[index].idPosicionVista = index + 1;
      });

    // //se agrega al sessionStorage
    sessionStorage.setItem('bienesMuebles', JSON.stringify({ ninguno: false, bienesMuebles: this.lista, aclaracionesObservaciones: "" }));

    this.activeModal.close({ result: 'success', form: 'muebles' });
  }


  aceptarTercero() {
    this.terceros = this.terceros == null ? [] : this.terceros;
    this.terceros.push(this.tercero);
    console.log(this.terceros);

    this.tercero = {
      id: "",
      idPosicionVista: "",
      tipoPersona: null, //Fisica, Moral
      personaFisica: {
        //@persona
        nombre: null,
        primerApellido: null,
        segundoApellido: null,
        rfc: null
      },
      personaMoral: {
        //@personaMoral
        nombre: null,
        rfc: null
      }
    }
  }

  aceptarTrans() {

    this.transmisores.push(this.transmisor);

    console.log(this.transmisores);

    this.transmisor = {
      id: "",
      idPosicionVista: "",
      relacionConTitular: {
        id: null,
        valor: null

      },
      relacionConTitularOtro: null,
      tipoPersona: null, //Fisica, Moral
      personaFisica: {
        //@persona
        nombre: null,
        primerApellido: null,
        segundoApellido: null,
        rfc: null
      },
      personaMoral: {
        //@personaMoral
        nombre: null,
        rfc: null
      }
    }

  }

  deleteTrans(index) {
    this.transmisores.splice(index, 1);
  }
  deleteTercero(index) {
    this.terceros.splice(index, 1);
  }

  close() {
    this.modalService.dismissAll('muebles');
    $("body").removeClass("disabled-onepage-scroll");
  }

  limpiaTrans() {
    this.tercero = {
      id: "",
      idPosicionVista: "",
      tipoPersona: null, //Fisica, Moral
      personaFisica: {
        //@persona
        nombre: null,
        primerApellido: null,
        segundoApellido: null,
        rfc: null
      },
      personaMoral: {
        //@personaMoral
        nombre: null,
        rfc: null
      }
    }

    this.transmisor.personaFisica.nombre = null;
    this.transmisor.personaFisica.primerApellido = null;
    this.transmisor.personaFisica.segundoApellido = null;
    this.transmisor.personaFisica.rfc = null;
    this.transmisor.personaMoral.nombre = null;
    this.transmisor.personaMoral.rfc = null;

  }

  changeTipoOperacion() {
    if (this.bienMueble.tipoOperacion === 'SIN_CAMBIO' || this.bienMueble.tipoOperacion === 'BAJA') {
      $(".block").css('display', 'flex');
    } else {
      $(".block").css('display', 'none');
    }
  }

  ngAfterViewInit(): void {

    console.log(this.bMuebles.valid);

    let formVal: boolean;
    const a = this.bMuebles.statusChanges.subscribe(() => {
      if (this.bMuebles.valid !== formVal) {
        this.TIPO_OPERACION = this.cata.getTipoOperacion(this.bMuebles.valid, this.bienMueble.registroHistorico, this.bienMueble.tipoOperacion, 'P');
        this.histFormInval = (this.bienMueble.registroHistorico && !this.bMuebles.valid);
        if (!this.bMuebles.valid) {
          a.unsubscribe();
          setTimeout(() => {
            this.initLoad = false;
            this.resetBaja();
          }, 500);

        }
        formVal = this.bMuebles.valid;

      }
    });

    setTimeout(() => {
      this.initLoad = false;
      if (this.bMuebles.valid && this.globals.precargaOracle) {
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

        data.tipoOperacion.BAJA = this.bMuebles.controls[data.name] ? this.bMuebles.controls[data.name].valid && this.bMuebles.controls[data.name].value !== '' : false;
        if (data.tipoOperacion.MODIFICAR === true) {
          data.tipoOperacion.MODIFICAR = this.bMuebles.controls[data.name] ? this.bMuebles.controls[data.name].valid && this.bMuebles.controls[data.name].value !== '' : false;
        }
      }
    });
    console.log(this.dataDisabled);
  }

  tipoOperacionChange(id) {
    if (id === 'SIN_CAMBIO' || id === 'BAJA') {
      this.bienMueble = JSON.parse(JSON.stringify(this.bienR));
      this.bienMueble.tipoOperacion = id;
    }
  }

  getDisabled(obj, tipoOperacion1) {
    if (!obj.name || !tipoOperacion1 || this.initLoad) {
      return obj.disabled;
    }
    if (!this.bienMueble.registroHistorico) {
      return false;
    } else {
      const newObj = this.dataDisabled.find((value) => value.name === obj.name);
      return newObj.tipoOperacion[tipoOperacion1];
    }
  }

}
