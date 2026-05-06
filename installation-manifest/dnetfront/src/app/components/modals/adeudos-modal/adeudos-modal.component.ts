import { Component, OnInit, Input, HostListener, ViewChild, AfterViewInit } from '@angular/core';
import * as $ from 'jquery';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, NgForm } from '@angular/forms';
import { Globals } from 'src/app/globals';

@Component({
  selector: 'app-adeudos-modal',
  templateUrl: './adeudos-modal.component.html',
  styleUrls: ['./adeudos-modal.component.scss']
})
export class AdeudosModalComponent implements OnInit, AfterViewInit {

  form: FormGroup;
  help = false;
  adeudo: any;
  tercero: any;
  terceros: Array<any> = [];
  lista: any;
  @Input() public objUpdate;
  dataDisabled: any;
  fecha: any;
  initLoad: boolean;
  adeudosR: any;
  histFormInval: boolean;
  TIPO_OPERACION: any;
  montoOriginalValido: boolean;
  @ViewChild('adeudosValida') adeudosValida: NgForm;

  constructor(private readonly modalService: NgbModal, private readonly activeModal: NgbActiveModal, public cata: Globals, public globals: Globals) {
    this.dataDisabled = [{
      name: 'titularAdeudo',
      tipoOperacion: {
        AGREGAR: false,
        MODIFICAR: true,
        BAJA: true,
        SIN_CAMBIO: true
      }
    },
    {
      name: 'tipoAdeudo',
      tipoOperacion: {
        AGREGAR: false,
        MODIFICAR: true,
        BAJA: true,
        SIN_CAMBIO: true
      }
    }, {
      name: 'especificAdeudo',
      tipoOperacion: {
        AGREGAR: false,
        MODIFICAR: true,
        BAJA: true,
        SIN_CAMBIO: true
      }
    },
    {
      name: 'numeroCuenta',
      tipoOperacion: {
        AGREGAR: false,
        MODIFICAR: true,
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
      name: 'tipoMoneda',
      tipoOperacion: {
        AGREGAR: false,
        MODIFICAR: false,
        BAJA: false,
        SIN_CAMBIO: true
      }
    },
    {
      name: 'montoOriginal',
      tipoOperacion: {
        AGREGAR: false,
        MODIFICAR: false,
        BAJA: false,
        SIN_CAMBIO: true
      }
    },
    {
      name: 'saldoInsoluto',
      tipoOperacion: {
        AGREGAR: false,
        MODIFICAR: false,
        BAJA: false,
        SIN_CAMBIO: true
      }
    },
    {
      name: 'otorganteCredito',
      tipoOperacion: {
        AGREGAR: false,
        MODIFICAR: true,
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
      name: 'rfcMoral',
      tipoOperacion: {
        AGREGAR: false,
        MODIFICAR: false,
        BAJA: true,
        SIN_CAMBIO: true
      }
    },
    {
      name: 'nombreFisica',
      tipoOperacion: {
        AGREGAR: false,
        MODIFICAR: false,
        BAJA: true,
        SIN_CAMBIO: true
      }
    },
    {
      name: 'pApellidoFisica',
      tipoOperacion: {
        AGREGAR: false,
        MODIFICAR: false,
        BAJA: true,
        SIN_CAMBIO: true
      }
    },
    {
      name: 'sApellidoFisica',
      tipoOperacion: {
        AGREGAR: false,
        MODIFICAR: false,
        BAJA: true,
        SIN_CAMBIO: true
      }
    },
    {
      name: 'rfcFisica',
      tipoOperacion: {
        AGREGAR: false,
        MODIFICAR: false,
        BAJA: true,
        SIN_CAMBIO: true
      }
    },
    {
      name: 'pais',
      tipoOperacion: {
        AGREGAR: false,
        MODIFICAR: true,
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
      name: 'nombreFi',
      tipoOperacion: {
        AGREGAR: false,
        MODIFICAR: false,
        BAJA: true,
        SIN_CAMBIO: true
      }
    },
    {
      name: 'pApelidoFi',
      tipoOperacion: {
        AGREGAR: false,
        MODIFICAR: false,
        BAJA: true,
        SIN_CAMBIO: true
      }
    },
    {
      name: 'sApellidoFi',
      tipoOperacion: {
        AGREGAR: false,
        MODIFICAR: false,
        BAJA: true,
        SIN_CAMBIO: true
      }
    },
    {
      name: 'rfcFi',
      tipoOperacion: {
        AGREGAR: false,
        MODIFICAR: false,
        BAJA: true,
        SIN_CAMBIO: true
      }
    },
    {
      name: 'nombreMo',
      tipoOperacion: {
        AGREGAR: false,
        MODIFICAR: false,
        BAJA: true,
        SIN_CAMBIO: true
      }
    },
    {
      name: 'rfcMo',
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
    this.tercero = {
      id: null,
      idPosicionVista: null,
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
    };

    this.adeudo = {
      id: null,
      idPosicionVista: null,
      registroHistorico: false,
      tipoOperacion: null,
      titular: {
        id: null,
        valor: null,
        valorUno: null
      },
      tipoAdeudo: {
        id: null,
        valor: null
      },
      tipoAdeudoOtro: null,
      numeroCuentaContrato: null,
      fechaAdquisicion: '',
      montoOriginal: {
        //@montoMoneda
        moneda: {
          id: null,
          valor: null
        },
        monto: null
      },
      saldoInsoluto: null,
      terceros: [],
      otorganteCredito: {
        id: null,
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
      },
      paisAdeudo: {
        id: null,
        valor: null
      }
    }

    if (this.objUpdate) {
      if (this.objUpdate.titular == null) { this.objUpdate.titular = this.adeudo.titular; }
      if (this.objUpdate.tipoAdeudo == null) { this.objUpdate.tipoAdeudo = this.adeudo.tipoAdeudo; }
      if (this.objUpdate.montoOriginal == null) { this.objUpdate.montoOriginal = this.adeudo.montoOriginal; }
      if (this.objUpdate.montoOriginal.moneda == null) { this.objUpdate.montoOriginal.moneda = this.adeudo.montoOriginal.moneda; }
      if (this.objUpdate.otorganteCredito == null) { this.objUpdate.otorganteCredito = this.adeudo.otorganteCredito; }
      if (this.objUpdate.terceros == null) { this.objUpdate.terceros = this.adeudo.terceros; }
      if (this.objUpdate.otorganteCredito.personaMoral === null) { this.objUpdate.otorganteCredito.personaMoral = this.adeudo.otorganteCredito.personaMoral; }
      if (this.objUpdate.otorganteCredito.personaFisica === null) { this.objUpdate.otorganteCredito.personaFisica = this.adeudo.otorganteCredito.personaFisica; }
      if (this.objUpdate.terceros.personaMoral == null) { this.objUpdate.terceros.personaMoral = this.adeudo.terceros.personaMoral; }
      if (this.objUpdate.terceros.personaFisica == null) { this.objUpdate.terceros.personaFisica = this.adeudo.terceros.personaFisica; }
      if (this.objUpdate.tipoAdeudoOtro === null) { this.objUpdate.tipoAdeudoOtro = this.adeudo.tipoAdeudoOtro; }
      if (this.objUpdate.numeroCuentaContrato === null) { this.objUpdate.numeroCuentaContrato = this.adeudo.numeroCuentaContrato; }

      this.adeudo = this.objUpdate;
      this.terceros = this.adeudo.terceros;
      this.initLoad = true;
    }

    this.adeudosR = JSON.parse(JSON.stringify(this.adeudo));
  }

  aceptarTercero() {
    this.terceros.push(this.tercero);
    console.log(this.terceros);

    this.tercero = {
      id: null,
      idPosicionVista: 0,
      tipoPersona: "", //Fisica, Moral
      personaFisica: {
        //@persona
        nombre: "",
        primerApellido: "",
        segundoApellido: "",
        rfc: ""
      },
      personaMoral: {
        //@personaMoral
        nombre: "",
        rfc: ""
      }
    }
  }

  guardaLocal() {
    this.adeudo.terceros = this.terceros;


    let dato = this.adeudo;
    dato.verificar = true;
    dato.terceros = dato.titular.valorUno != 'DC' ? [] : dato.terceros;

    this.terceros.forEach(t => {
      t.personaFisica = t.tipoPersona == 'PERSONA_MORAL' ? null : t.personaFisica;
      t.personaMoral = t.tipoPersona == 'PERSONA_FISICA' ? null : t.personaMoral;
      t.idPosicionVista = null;
      t.id = null;
    });

    // //obtener array de sessionstorage
    let s = JSON.parse(sessionStorage.getItem('adeudos'));

    if (this.tercero.tipoPersona == 'PERSONA_FISICA') {
      this.tercero.personaMoral = null
    } else if (this.tercero.tipoPersona == 'PERSONA_MORAL') {
      this.tercero.personaFisica = null
    }

    if (this.adeudo.otorganteCredito.tipoPersona == 'PERSONA_FISICA') {
      this.adeudo.otorganteCredito.personaMoral = null
    } else if (this.adeudo.otorganteCredito.tipoPersona == 'PERSONA_MORAL') {
      this.adeudo.otorganteCredito.personaFisica = null
    }

    if (this.adeudo.tipoAdeudo.id != 9999) {
      this.adeudo.tipoAdeudoOtro = null
    }

    // //se asigna a lista local
    this.lista = s.adeudos;
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
    sessionStorage.setItem('adeudos', JSON.stringify({ ninguno: false, adeudos: this.lista, aclaracionesObservaciones: s.aclaracionesObservaciones }));

    // this.close();
    this.activeModal.close({ result: 'success', form: 'adeudos' });
  }

  deleteTercero(index) {
    this.terceros.splice(index, 1);
  }

  close() {
    this.modalService.dismissAll('adeudos');
    $("body").removeClass("disabled-onepage-scroll");
  }

  ngAfterViewInit(): void {

    console.log(this.adeudosValida.valid);

    let formVal: boolean;
    const a = this.adeudosValida.statusChanges.subscribe(() => {
      if (this.adeudosValida.valid !== formVal) {
        this.TIPO_OPERACION = this.cata.getTipoOperacion(this.adeudosValida.valid, this.adeudo.registroHistorico, this.adeudo.tipoOperacion, 'P');
        this.histFormInval = (this.adeudo.registroHistorico && !this.adeudosValida.valid);
        if (!this.adeudosValida.valid) {
          a.unsubscribe();
          setTimeout(() => {
            this.initLoad = false;
            this.resetBaja();
          }, 500);
        }
        formVal = this.adeudosValida.valid;
      }
    });

    setTimeout(() => {
      this.initLoad = false;
      if (this.adeudosValida.valid && this.cata.precargaOracle) {
        this.resetBaja();
      }
      a.unsubscribe();
    }, 500);

  }

  resetBaja() {
    this.montoOriginalValido = this.adeudosValida.controls.montoOriginal.value !== null && this.adeudosValida.controls.montoOriginal.value !== '';
    this.dataDisabled.forEach((data) => {
      if (this.globals.precargaOracle) {
        data.tipoOperacion.BAJA = false;
        data.tipoOperacion.MODIFICAR = false;
      } else {
        data.tipoOperacion.BAJA = this.adeudosValida.controls[data.name] ? this.adeudosValida.controls[data.name].valid && this.adeudosValida.controls[data.name].value !== '' : false;
        if (data.tipoOperacion.MODIFICAR === true) {
          data.tipoOperacion.MODIFICAR = this.adeudosValida.controls[data.name] ? this.adeudosValida.controls[data.name].valid && this.adeudosValida.controls[data.name].value !== '' : false;
        }
      }
    });
  }

  tipoOperacionChange(id) {
    if (id === 'SIN_CAMBIO' || id === 'BAJA') {
      this.adeudo = JSON.parse(JSON.stringify(this.adeudosR));
      this.adeudo.tipoOperacion = id;
    }
  }

  getDisabled(obj, tipoOperacion1) {
    if (!obj.name || !tipoOperacion1 || this.initLoad) {
      return obj.isDisabled;
    }
    if (!this.adeudo.registroHistorico) {
      return false;
    } else {
      const newObj = this.dataDisabled.find((value) => value.name === obj.name);
      return newObj.tipoOperacion[tipoOperacion1];
    }
  }
}
