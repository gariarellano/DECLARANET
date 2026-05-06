import { Component, OnInit, Input, HostListener, ViewChild, AfterViewInit } from '@angular/core';
import { FormGroup, NgForm } from '@angular/forms';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import * as $ from 'jquery';
import { Globals } from 'src/app/globals';

@Component({
  selector: 'app-bienes-inmuebles-modal',
  templateUrl: './bienes-inmuebles-modal.component.html',
  styleUrls: ['./bienes-inmuebles-modal.component.scss']
})
export class BienesInmueblesModalComponent implements OnInit, AfterViewInit {

  form: FormGroup;
  help = false;
  lista: any;
  bienInmueble: any;
  bien: any;
  initLoad: boolean;
  decLoaded: any;
  transmisor: any;
  transmisores: Array<any> = [];
  tercero: any;
  terceros: Array<any> = [];
  @Input() public objUpdate;
  filterMuns: any;
  bandera = false;
  dataDisabled: any;
  readOnlyPorcInmueble: boolean;
  fecha: any;
  bienInmR: any;
  histFormInval: boolean;
  TIPO_OPERACION: any;
  @ViewChild('bienInmuebleF') bienInmuebleF: NgForm;

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
        name: 'tipoInmueble',
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
        name: 'porcentajeProp',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'superficieTerr',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'superficieConst',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
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
        name: 'valorAdquisicion',
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
        name: 'datosRegistro',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'valorAdquisicionInmueble',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'ubicaInmueble',
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
        name: 'especificBien',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'tipoPersona',
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
        name: 'pApellidoT',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'seApellidoT',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'rfcFT',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'nombreMT',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'rfcM',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'tipoPersona',
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
        name: 'pApellido',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'sApellidoTer',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'rfcFisicTercero',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'nombreMorTer',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'rfcTercerMoral',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'calleMexi',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: false,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'numExMex',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: false,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'numIntMex',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: false,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'colMex',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: false,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'entidadMex',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: false,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'municiMex',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: false,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'cpMex',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: false,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'calleExtra',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: false,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'numExtExtra',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: false,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'numInterExtra',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: false,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'ciudadExtra',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: false,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'edoExtra',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: false,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'piasExtra',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: false,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'cpExtra',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: false,
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

  bienTmp: any;
  ngOnInit() {
    this.decLoaded = JSON.parse(sessionStorage.getItem('declaracionLoaded'));

    this.transmisor = {
      tipoPersona: null,
      relacionConTitular: {
        id: null,
        valor: null
      },
      relacionConTitularOtro: null,
      personaFisica: {
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
      id: 0,
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

    this.bien = {

      id: null,
      verificar: null,
      idPosicionVista: 0,
      registroHistorico: false,
      tipoOperacion: null,
      tipoInmueble: {
        id: null,
        valor: null
      },
      tipoInmuebleOtro: "",
      titular: {
        id: null,
        valor: null,
        valorUno: null
      },
      porcentajePropiedad: null,
      superficieTerrenoM2: 0,
      superficieConstruccionM2: 0,
      terceros: //al menos uno
        []
      ,
      transmisores: //al menos uno
        []
      ,
      formaAdquisicion: {
        id: null,
        valor: null
      },
      formaPago: null,
      valorAdquisicion: {
        //@montoMoneda
        moneda: {
          id: null,
          valor: null
        },
        monto: 0
      },
      fechaAdquisicion: "",
      datoIdentificacion: "",
      valorConformeA: null, //Escritura publica,sentencia,contrato
      domicilio: {
        //@domicilio
        ubicacion: null, //Mexico, Extranjero

        domicilioMexico: { //uno u otro
          calle: null,
          codigoPostal: null,
          coloniaLocalidad: null,
          entidadFederativa: {
            id: null,
            valor: null
            //municipio afuera / ala altura de entidadFederavia
          },
          municipioAlcaldia: {
            fk: 0,
            id: 0,
            valor: ""
          },
          numeroExterior: null,
          numeroInterior: null
        },

        domicilioExtranjero: { //uno u otro
          calle: null,
          ciudadLocalidad: null,
          codigoPostal: null,
          estadoProvincia: null,
          numeroExterior: null,
          numeroInterior: null,
          pais: {
            id: null,
            valor: null
          }
        }
      },
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
      if (this.objUpdate.tipoInmueble == null) { this.objUpdate.tipoInmueble = this.bien.tipoInmueble; }
      if (this.objUpdate.titular == null) { this.objUpdate.titular = this.bien.titular; }
      if (this.objUpdate.valorAdquisicion.moneda == null) { this.objUpdate.valorAdquisicion.moneda = this.bien.valorAdquisicion.moneda; }
      if (this.objUpdate.domicilio.domicilioMexico == null) { this.objUpdate.domicilio.domicilioMexico = this.bien.domicilio.domicilioMexico; }
      if (this.objUpdate.domicilio.domicilioExtranjero == null) { this.objUpdate.domicilio.domicilioExtranjero = this.bien.domicilio.domicilioExtranjero; }
      if (this.objUpdate.formaAdquisicion == null) { this.objUpdate.formaAdquisicion = this.bien.formaAdquisicion; }
      if (this.objUpdate.terceros == null) { this.objUpdate.terceros = this.bien.terceros; }
      if (this.objUpdate.transmisores == null) { this.objUpdate.transmisores = this.bien.transmisores; }
      if (this.objUpdate.domicilio.domicilioMexico == null) { this.objUpdate.domicilio.domicilioMexico = this.bien.domicilio.domicilioMexico; }
      if (this.objUpdate.domicilio.domicilioMexico.entidadFederativa) { this.filterMunicipios(this.objUpdate.domicilio.domicilioMexico.entidadFederativa.id, false); }
      if (this.objUpdate.domicilio.domicilioExtranjero == null) { this.objUpdate.domicilio.domicilioExtranjero = this.bien.domicilio.domicilioExtranjero; }
      if (this.objUpdate.terceros.personaMoral == null) { this.objUpdate.terceros.personaMoral = this.bien.terceros.personaMoral; }
      if (this.objUpdate.terceros.personaFisica == null) { this.objUpdate.terceros.personaFisica = this.bien.terceros.personaFisica; }
      if (this.objUpdate.motivoBaja == null) { this.objUpdate.motivoBaja = this.bien.motivoBaja; }
      if (this.objUpdate.motivoBajaOtro == null) { this.objUpdate.motivoBajaOtro = this.bien.motivoBajaOtro; }
      if (this.objUpdate.valorVenta == null) { this.objUpdate.valorVenta = this.bien.valorVenta; }

      this.bien = this.objUpdate;
      this.transmisores = this.bien.transmisores;
      this.terceros = this.bien.terceros;
      this.initLoad = true;
      this.titularChange(this.bien.titular);
    }
    this.bienInmR = JSON.parse(JSON.stringify(this.bien));

    this.bienTmp = {
      domicilio: {
        domicilioMexico: JSON.parse(JSON.stringify(this.bien.domicilio.domicilioMexico)),
        domicilioExtranjero: JSON.parse(JSON.stringify(this.bien.domicilio.domicilioExtranjero))
      }
    };
  }

  abreMexico() {
    this.bienTmp.domicilio.domicilioMexico = JSON.parse(JSON.stringify(this.bien.domicilio.domicilioMexico));
    if (this.bienTmp.domicilio.ubicacion === 'MEXICO') {
      this.filterMunicipios(this.bienTmp.domicilio.domicilioMexico.entidadFederativa.id, false);
    }
  }

  abreExtranjero() {
    this.bienTmp.domicilio.domicilioExtranjero = JSON.parse(JSON.stringify(this.bien.domicilio.domicilioExtranjero));
    this.bienTmp.domicilio.ubicacion = 'EXTRANJERO';
  }

  guardaSub(v: string) {
    switch (v) {
      case 'domicilioMexico':
        this.bien.domicilio.domicilioMexico = JSON.parse(JSON.stringify(this.bienTmp.domicilio.domicilioMexico));
        // this.bienTmp.domicilio.domicilioMexico = null;
        break;

      case 'domicilioExtranjero':
        this.bien.domicilio.domicilioExtranjero = JSON.parse(JSON.stringify(this.bienTmp.domicilio.domicilioExtranjero));
        // this.bienTmp.domicilio.domicilioExtranjero = null;
        break;
    }
  }

  close() {
    this.modalService.dismissAll('inmuebles');
    $("body").removeClass("disabled-onepage-scroll");
  }

  closeUbicacion() {
    this.bienTmp.domicilio.domicilioMexico = JSON.parse(JSON.stringify(this.bien.domicilio.domicilioMexico));
    this.bienTmp.domicilio.domicilioExtranjero = JSON.parse(JSON.stringify(this.bien.domicilio.domicilioExtranjero));
  }

  guardaLocal() {
    //obtener datos del formulario
    if (this.bien.titular.id === 'declarante') {
      this.bien.porcentajePropiedad = 100;
    }

    this.bien.terceros = this.terceros;
    this.bien.transmisores = this.transmisores;

    let dato = this.bien;
    dato.verificar = true;
    if (!this.decLoaded.numeroDeclaracionPrecarga && dato.registroHistorico === true) {
      dato.registroHistorico = false;
    }
    dato.domicilio.domicilioMexico = dato.domicilio.ubicacion == "EXTRANJERO" ? null : dato.domicilio.domicilioMexico;
    dato.domicilio.domicilioExtranjero = dato.domicilio.ubicacion == "MEXICO" ? null : dato.domicilio.domicilioExtranjero;
    dato.valorAdquisicion.moneda.id = Number(dato.valorAdquisicion.moneda.id);
    dato.valorAdquisicion.monto = Number(dato.valorAdquisicion.monto);
    dato.porcentajePropiedad = Number(dato.porcentajePropiedad);
    dato.superficieTerrenoM2 = Number(dato.superficieTerrenoM2);
    dato.superficieConstruccionM2 = Number(dato.superficieConstruccionM2);
    dato.formaPago = dato.formaAdquisicion.id != '1' ? null : dato.formaPago;
    dato.terceros = dato.titular.valorUno != 'DC' ? [] : dato.terceros;
    if (this.bien.tipoInmueble.id !== 9999) {
      this.bien.tipoInmuebleOtro = null;
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
      t.idPosicionVista = index;
      if (t.relacionConTitular.id !== 9999) {
        t.relacionConTitularOtro = null;
      }
      t.id = null;
    });

    this.terceros.forEach((t, index) => {
      t.personaFisica = t.tipoPersona == 'PERSONA_MORAL' ? null : t.personaFisica;
      t.personaMoral = t.tipoPersona == 'PERSONA_FISICA' ? null : t.personaMoral;
      t.idPosicionVista = index;
      t.id = null;
    });

    dato.formaPago = dato.formaAdquisicion.id != 1 ? 'NO_APLICA' : dato.formaPago;

    //obtener array de sessionstorage
    let s = JSON.parse(sessionStorage.getItem('bienesInmuebles'));

    //se asigna a lista local
    this.lista = s.bienesInmuebles;
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

    //se crea el json que se volvera a guardar en el sessionStorage
    this.bienInmueble = { ninguno: false, bienesInmuebles: this.lista, aclaracionesObservaciones: "" }

    //se agrega al sessionStorage
    sessionStorage.setItem('bienesInmuebles', JSON.stringify(this.bienInmueble));

    // this.close();
    this.activeModal.close({ result: 'success', form: 'inmuebles' });
  }

  aceptarTercero() {
    console.log("viene este tercero", this.tercero);

    this.terceros.push(this.tercero);
    console.log(this.terceros);

    this.tercero = {
      id: "",
      idPosicionVista: "",
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

  deleteTrans(index) {
    this.transmisores.splice(index, 1);
  }

  deleteTercero(index) {
    this.terceros.splice(index, 1);
  }

  checkmuni() {
    this.bandera = this.bien.domicilio.domicilioMexico.entidadFederativa.id == this.bien.domicilio.domicilioMexico.municipioAlcaldia.fk ? true : false;
  }

  filterMunicipios(id, clear = true) {

    this.filterMuns = this.cata.catalogoMun.filter(item => item.fk === Number(id));
    if (clear) {
      this.bienTmp.domicilio.domicilioMexico.municipioAlcaldia = null;
    }
  }

  getTipoInmueble() {
    const tipoInmueble = JSON.parse(sessionStorage.getItem('datosCurricularesDeclarante'));
    if (tipoInmueble.tipoInmueble.id === !null) {
      return "Este es el mensaje"
    } else {
      return tipoInmueble.tipoInmueble.id;
    }
  }

  ngAfterViewInit(): void {

    console.log(this.bienInmuebleF.valid);

    let formVal: boolean;
    const a = this.bienInmuebleF.statusChanges.subscribe(() => {
      if (this.bienInmuebleF.valid !== formVal) {
        this.TIPO_OPERACION = this.cata.getTipoOperacion(this.bienInmuebleF.valid, this.bien.registroHistorico, this.bien.tipoOperacion, 'P');
        this.histFormInval = (this.bien.registroHistorico && !this.bienInmuebleF.valid);
        if (!this.bienInmuebleF.valid) {
          a.unsubscribe();
          setTimeout(() => {
            this.initLoad = false;
            this.resetBaja();
          }, 500);

        }
        formVal = this.bienInmuebleF.valid;

      }
    });

    setTimeout(() => {
      this.initLoad = false;
      if (this.bienInmuebleF.valid && this.globals.precargaOracle) {
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
        data.tipoOperacion.BAJA = this.bienInmuebleF.controls[data.name] ?
          this.bienInmuebleF.controls[data.name].valid && this.bienInmuebleF.controls[data.name].value !== '' : false;
        if (data.tipoOperacion.MODIFICAR === true) {
          data.tipoOperacion.MODIFICAR = this.bienInmuebleF.controls[data.name] ?
            this.bienInmuebleF.controls[data.name].valid && this.bienInmuebleF.controls[data.name].value !== '' : false;
        }
      }
    });
  }

  tipoOperacionChange(id) {
    if (id === 'SIN_CAMBIO' || id === 'BAJA') {
      this.bien = JSON.parse(JSON.stringify(this.bienInmR));
      this.bien.tipoOperacion = id;
    }
  }

  getDisabled(obj, tipoOperacion1) {
    if (!obj.name || !tipoOperacion1 || this.initLoad) {
      return obj.disabled;
    }
    if (!this.bien.registroHistorico) {
      return false;
    } else {
      const newObj = this.dataDisabled.find((value) => value.name === obj.name);
      return newObj.tipoOperacion[tipoOperacion1];
    }
  }

  titularChange(nuevoVal) {
    const declaranteSolo = [1];
    const declaranteCoprop = [2, 3, 4, 5, 6, 12, 13, 17, 25, 19, 20];
    console.log(nuevoVal);
    if (declaranteSolo.includes(nuevoVal.id)) {
      this.readOnlyPorcInmueble = true;
      this.bien.porcentajePropiedad = '100';
      return;
    }
    if (declaranteCoprop.includes(nuevoVal.id)) {
      this.readOnlyPorcInmueble = false;
      this.bien.porcentajePropiedad = ['0', '100'].includes(this.bien.porcentajePropiedad) ? null : this.bien.porcentajePropiedad;
      return;
    }
    this.readOnlyPorcInmueble = true;
    this.bien.porcentajePropiedad = '0';
    return;

  }
}