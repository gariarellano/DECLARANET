import { Component, OnInit, Input, HostListener, AfterViewInit, ViewChild } from '@angular/core';
import * as $ from 'jquery';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup,NgForm } from '@angular/forms';
import { Globals } from 'src/app/globals';

@Component({
  selector: 'app-inversiones-modal',
  templateUrl: './inversiones-modal.component.html',
  styleUrls: ['./inversiones-modal.component.scss']
})

export class InversionesModalComponent implements OnInit,AfterViewInit {

  form: FormGroup;
  help = false;
  inversion: any;
  tercero: any;
  terceros: Array<any> = [];
  lista: any;
  CATSUBTIPOINVERSION: any;
  @Input() public objUpdate;
  filterInv: any;
  vehiculoR: any;
  bienTmp: any;
  dataDisabled: any;
  histFormInval: boolean;
  TIPO_OPERACION: any;
  @ViewChild('inversionM') inversionM: NgForm;
  initLoad: boolean;

  constructor(private modalService: NgbModal, private activeModal: NgbActiveModal, public cata: Globals, public globals: Globals) {
    this.CATSUBTIPOINVERSION = [{ "id": 9, "valor": "ACCIONES", "fk": 3 },
    { "id": 19, "valor": "ACCIONES Y DERIVADOS", "fk": 6 },
    { "id": 20, "valor": "ACEPTACIONES BANCARIAS", "fk": 6 },
    { "id": 23, "valor": "AFORES", "fk": 7 },
    { "id": 21, "valor": "BONOS GUBERNAMENTALES", "fk": 6 },
    { "id": 10, "valor": "CAJAS DE AHORRO", "fk": 3 },
    { "id": 11, "valor": "CENTENARIOS", "fk": 4 },
    { "id": 25, "valor": "CERTIFICADOS DE LA TESORERÍA", "fk": 7 },
    { "id": 15, "valor": "CRIPTOMONEDAS", "fk": 4 },
    { "id": 2, "valor": "CUENTA DE AHORRO", "fk": 1 },
    { "id": 3, "valor": "CUENTA DE CHEQUES", "fk": 1 },
    { "id": 1, "valor": "CUENTA DE NÓMINA", "fk": 1 },
    { "id": 5, "valor": "CUENTA EJE", "fk": 1 },
    { "id": 4, "valor": "CUENTA MAESTRA", "fk": 1 },
    { "id": 6, "valor": "DEPÓSITO A PLAZOS", "fk": 1 },
    { "id": 12, "valor": "DIVISAS", "fk": 4 },
    { "id": 24, "valor": "FIDEICOMISOS", "fk": 7 },
    { "id": 8, "valor": "INVERSIONES FINANCIERAS EN EL EXTRANJERO", "fk": 2 },
    { "id": 13, "valor": "MONEDA NACIONAL", "fk": 4 },
    { "id": 14, "valor": "ONZAS TROY", "fk": 4 },
    { "id": 22, "valor": "PAPEL COMERCIAL", "fk": 6 },
    { "id": 26, "valor": "PRÉSTAMOS A FAVOR DE UN TERCERO", "fk": 7 },
    { "id": 17, "valor": "SEGURO DE INVERSIÓN", "fk": 5 },
    { "id": 16, "valor": "SEGURO DE SEPARACIÓN INDIVIDUALIZADO", "fk": 5 },
    { "id": 18, "valor": "SEGURO DE VIDA", "fk": 5 },
    { "id": 7, "valor": "SOCIEDADES DE INVERSIÓN", "fk": 2 }];

    this.dataDisabled=[
  {
    name: "tipoInversion",
    tipoOperacion: {
      AGREGAR: false,
      MODIFICAR: true,
      BAJA: true,
      SIN_CAMBIO: true
    }
  },
  {
    name: "bancaria",
    tipoOperacion: {
      AGREGAR: false,
      MODIFICAR: true,
      BAJA: true,
      SIN_CAMBIO: true
    }
  },
  {
    name: "titularInversion",
    tipoOperacion: {
      AGREGAR: false,
      MODIFICAR: true,
      BAJA: true,
      SIN_CAMBIO: true
    }
  },
  {
    name: "numCuentas",
    tipoOperacion: {
      AGREGAR: false,
      MODIFICAR: true,
      BAJA: true,
      SIN_CAMBIO: true
    }
  },
  {
    name: "localizaInversion",
    tipoOperacion: {
      AGREGAR: false,
      MODIFICAR: true,
      BAJA: true,
      SIN_CAMBIO: true
    }
  },
  {
    name: "institucionRazonSocial",
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
      MODIFICAR: false,
      BAJA: true,
      SIN_CAMBIO: true
    }
  },
  {
    name: "saldoAfecha",
    tipoOperacion: {
      AGREGAR: false,
      MODIFICAR: false,
      BAJA: true,
      SIN_CAMBIO: true
    }
  }];
  }

  @HostListener('paste', ['$event']) blockPaste(e: KeyboardEvent) {
    e.preventDefault();
  }
  
  ngOnInit() {

    this.tercero = {
      id: null,
      idPosicionVista: 0,
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

    this.inversion = {

      id: null,
      verificar: null,
      idPosicionVista: 0,
      registroHistorico: false,
      tipoOperacion: null,
      tipoInversion: {
        id: null,
        valor: null,
      },
      subTipoInversion: {
        id: null,
        valor: null,
        fk: 0
      },
      titular: {
        id: null,
        valor: '',
        valorUno: ''
      },
      terceros: [],
      numeroCuentaContrato: null,
      ubicacion: null,
      localizacionInversion: {
        pais: {
          id: null,
          valor: null
        },
        institucionRazonSocial: {
          nombre: null,
          rfc: null
        }
      },
      saldo: {
        moneda: {
          id: null,
          valor: null
        },
        monto: null
      }
    }
    if (this.objUpdate) {
      console.log("inver ",this.objUpdate);
      
      if (this.objUpdate.saldo.moneda === null){ this.objUpdate.saldo.moneda = this.inversion.saldo.moneda;}
      if (this.objUpdate.terceros === null) {this.objUpdate.terceros = this.inversion.terceros;}
      
      if (this.objUpdate.terceros.personaMoral === null) {this.objUpdate.terceros.personaMoral = this.inversion.terceros.personaMoral;}
      if (this.objUpdate.terceros.personaFisica === null) {this.objUpdate.terceros.personaFisica = this.inversion.terceros.personaFisica;}
      
      this.inversion = this.objUpdate;
      this.terceros = this.inversion.terceros;
      if(this.objUpdate.localizacionInversion === null){this.objUpdate.localizacionInversion = this.inversion.localizacionInversion}
      if(this.objUpdate.saldo.moneda === null){this.objUpdate.saldo.moneda = this.inversion.saldo.moneda}
      
      if(this.objUpdate.localizacionInversion.pais === null){this.objUpdate.localizacionInversion.pais = this.inversion.localizacionInversion.pais}
      if(this.objUpdate.localizacionInversion.institucionRazonSocial === null)
      {this.objUpdate.localizacionInversion.institucionRazonSocial = this.inversion.localizacionInversion.institucionRazonSocial}
      
      this.objUpdate.titular = this.objUpdate.titular === null?{id:null,valor:'',valorUno:''}: this.objUpdate.titular;
      if(this.objUpdate.tipoInversion === null){ this.objUpdate.tipoInversion = {id:null,valor:''}}
      console.log("inversiones ",this.objUpdate);
      if(this.objUpdate.tipoInversion != null){
        this.filterInversion(this.inversion.tipoInversion.id);
      }
      this.initLoad = true;
    }
   
    this.bienTmp = {
      localizacionInversion: {
        pais: JSON.parse(JSON.stringify(this.inversion.localizacionInversion.pais)),
        institucionRazonSocial: JSON.parse(JSON.stringify(this.inversion.localizacionInversion.institucionRazonSocial))
      }
    }
    this.vehiculoR = JSON.parse(JSON.stringify(this.inversion));
  }
  
  ngAfterViewInit(): void {
    
    console.log(this.inversionM.valid);

    let formVal: boolean;
    const a =  this.inversionM.statusChanges.subscribe(()=>{
      if (this.inversionM.valid !== formVal) {
        this.TIPO_OPERACION = this.globals.getTipoOperacion(this.inversionM.valid, this.inversion.registroHistorico, this.inversion.tipoOperacion, 'P');
        this.histFormInval = (this.inversion.registroHistorico && !this.inversionM.valid);
        if (!this.inversionM.valid) {
          a.unsubscribe();
          setTimeout(() => {
            this.initLoad = false;
            this.resetBaja();
          }, 500);
          
        }
        formVal = this.inversionM.valid;

      }
    });
    setTimeout(() => {
      this.initLoad = false;
      if (this.inversionM.valid && this.globals.precargaOracle) {
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
      data.tipoOperacion.BAJA = this.inversionM.controls[data.name] ? this.inversionM.controls[data.name].valid : false;

      if (data.tipoOperacion.MODIFICAR === true) {
        data.tipoOperacion.MODIFICAR = this.inversionM.controls[data.name] ? this.inversionM.controls[data.name].valid : false;
      }
     }
     });
  }

  tipoOperacionChange(id) {
    if (id === 'SIN_CAMBIO' || id === 'BAJA') {
      this.inversion = JSON.parse(JSON.stringify(this.vehiculoR));
      this.inversion.tipoOperacion = id;
    }
  }

  getDisabled(obj, tipoOperacion1) {
    if (!obj.name || !tipoOperacion1 || this.initLoad) {
      return obj.isDisabled;
    }
    if (!this.inversion.registroHistorico) {
      return false;
    } else {
      const newObj = this.dataDisabled.find  ((value) => value.name === obj.name);
      const newObj1 = newObj.tipoOperacion[tipoOperacion1];
      return newObj1;
    }
  }

  close() {
    this.modalService.dismissAll('inversiones');
    $("body").removeClass("disabled-onepage-scroll");
  }

  guardaLocal() {
    this.inversion.terceros = this.terceros;


    let dato = this.inversion;
    dato.verificar = true;
    dato.terceros = dato.titular.valorUno != 'DC' ? [] : dato.terceros;

    let s = JSON.parse(sessionStorage.getItem('inversiones'));

    this.lista = s.inversion;
    const index = this.lista.findIndex(({ idPosicionVista }) => idPosicionVista == dato.idPosicionVista);

    this.terceros.forEach(t => {
      t.personaFisica = t.tipoPersona == 'PERSONA_MORAL' ? null : t.personaFisica;
      t.personaMoral = t.tipoPersona == 'PERSONA_FISICA' ? null : t.personaMoral;
      t.idPosicionVista = null;
      t.id = null;
    });

    if (this.inversion.ubicacion == 'EXTRANJERO') {
      this.inversion.localizacionInversion.institucionRazonSocial.rfc = null
    } else if (this.inversion.ubicacion == 'MEXICO') {
      this.inversion.localizacionInversion.pais = null
    }

    if (index === -1) {
      this.lista.push(dato);
    } else {
      this.lista[index] = dato;
    }

    this.lista.forEach(
      (element, index) => {
        this.lista[index].idPosicionVista = index + 1;
      });


    sessionStorage.setItem('inversiones', JSON.stringify({ ninguno: false, inversion: this.lista, aclaracionesObservaciones: "" }));
    this.activeModal.close({ result: 'success', form: 'inversiones' });
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

  deleteTercero(index) {
    this.terceros.splice(index, 1);
  }

  filterInversion(id) {
    this.filterInv = this.CATSUBTIPOINVERSION.filter(item => item.fk === Number(id));
  }

  abreInstitucion() {
    this.bienTmp.localizacionInversion.institucionRazonSocial = JSON.parse(JSON.stringify(this.inversion.localizacionInversion.institucionRazonSocial));
  }

  abrePais() {
    this.bienTmp.localizacionInversion.pais = JSON.parse(JSON.stringify(this.inversion.localizacionInversion.pais));
    this.bienTmp.localizacionInversion.institucionRazonSocial.nombre = JSON.parse(JSON.stringify(this.inversion.localizacionInversion.institucionRazonSocial.nombre));
  }

  guardaSub(v: string) {

    switch (v) {
      case 'domicilioMexico':
        this.inversion.localizacionInversion.institucionRazonSocial = JSON.parse(JSON.stringify(this.bienTmp.localizacionInversion.institucionRazonSocial));
        break;
        case 'domicilioExtranjero':
          this.inversion.localizacionInversion.pais = JSON.parse(JSON.stringify(this.bienTmp.localizacionInversion.pais));
          this.inversion.localizacionInversion.institucionRazonSocial.nombre = JSON.parse(JSON.stringify(this.bienTmp.localizacionInversion.institucionRazonSocial.nombre));
        break;
    }
  }

  closeUbicacion(){
    this.bienTmp.localizacionInversion.institucionRazonSocial = JSON.parse(JSON.stringify(this.inversion.localizacionInversion.institucionRazonSocial));
    this.bienTmp.localizacionInversion.pais = JSON.parse(JSON.stringify(this.inversion.localizacionInversion.pais));
  }

 
}
