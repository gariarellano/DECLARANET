import { Component, OnInit, Output, EventEmitter, Input, HostListener, AfterViewInit, ViewChild } from '@angular/core';
import * as $ from 'jquery';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, NgForm } from '@angular/forms';
import { Globals } from 'src/app/globals';

@Component({
  selector: 'app-participa-desiciones-modal',
  templateUrl: './participa-desiciones-modal.component.html',
  styleUrls: ['./participa-desiciones-modal.component.scss']
})
export class ParticipaDesicionesModalComponent implements OnInit, AfterViewInit {

  @Output()
  aceptar = new EventEmitter<JSON>();
  parTomaDecisiones: any;
  form: FormGroup;
  help = false;
  fechaValid: any;
  lista: Array<any>;
  datosDesiciones: any;
  @Input() public objUpdate;
  decLoaded: any;
  ini: any;
  dataDisabled: any;

  initLoad2: boolean;
  participaR: any;
  histFormInval: boolean;
  TIPO_OPERACION: any;
  @ViewChild('participaDeciForm') participaDeciForm: NgForm;

  constructor(private readonly modalService: NgbModal, private readonly activeModal: NgbActiveModal, public cata: Globals) {
    this.dataDisabled = [
      {
        name: 'tipoPersona',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'tipoInstitucion',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'valorInstitucion',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'nombreInstitucion',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'rfcInstitucion',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'rol',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'fecha',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'remuneracion',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'Monto',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'moneda',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'lugarUbica',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'entidad',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
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
    ];

  }

  @HostListener('paste', ['$event']) blockPaste(e: KeyboardEvent) {
    e.preventDefault();
  }

  ngOnInit() {
    this.decLoaded = JSON.parse(sessionStorage.getItem('declaracionLoaded'));
    this.ini = this.decLoaded.encabezado.tipoDeclaracion;

    this.fechaValid = {
      fechaMax: this.cata.fechaReferenciaMax,
      fechaMin: null
    }
    this.fechaValid = JSON.stringify(this.fechaValid);
    this.parTomaDecisiones = {
      id: null,
      idPosicionVista: null,
      verificar: true,
      registroHistorico: false,
      tipoOperacion: null,
      participante: null, //DECLARANETE, PAREJA, DEPENDIENTE ECONOMICO
      tipoInstitucion: { //catalogo de tipo institucion
        id: null,
        valor: null
      },
      tipoInstitucionOtro: null,
      institucion: {
        //personaMoral
        nombre: null,
        rfc: null
      },
      puestoRol: null,
      fechaInicioParticipacion: null,
      recibeRemuneracion: null,
      montoMensual: {
        //@montoMoneda
        moneda: {
          id: null,
          valor: null
        },
        monto: null
      },
      localizacion: {
        //@localizacion
        ubicacion: null,
        localizacionMexico: { //uno u otro
          entidadFederativa: {
            id: null,
            valor: null
          }
        },

        localizacionExtranjero: { //uno u otro
          pais: {
            id: null,
            valor: null
          }
        }
      },
    }

    if (this.objUpdate) {
      if (this.objUpdate.tipoInstitucion === null) { this.objUpdate.tipoInstitucion = this.parTomaDecisiones.tipoInstitucion; }
      if (this.objUpdate.montoMensual === null) { this.objUpdate.montoMensual = this.parTomaDecisiones.montoMensual; }
      if (this.objUpdate.montoMensual.moneda === null) { this.objUpdate.montoMensual.moneda = this.parTomaDecisiones.montoMensual.moneda; }
      if (this.objUpdate.localizacion.localizacionMexico === null) { this.objUpdate.localizacion.localizacionMexico = this.parTomaDecisiones.localizacion.localizacionMexico; }
      if (this.objUpdate.localizacion.localizacionExtranjero === null) { this.objUpdate.localizacion.localizacionExtranjero = this.parTomaDecisiones.localizacion.localizacionExtranjero; }

      this.parTomaDecisiones = this.objUpdate;
      this.initLoad2 = true;
    }
    this.participaR = JSON.parse(JSON.stringify(this.parTomaDecisiones));
  }

  close() {
    this.modalService.dismissAll("participaTomaDecisiones");
    $("body").removeClass("disabled-onepage-scroll");
  }

  guardaLocal() {
    //obtener datos del formulario
    let dato = this.parTomaDecisiones;
    dato.verificar = true;
    if (!this.decLoaded.numeroDeclaracionPrecarga && dato.registroHistorico === true) {
      dato.registroHistorico = false;
    }

    if (dato.tipoInstitucion.id !== 9999) {
      dato.tipoInstitucionOtro = null;
    }
    //obtener array de sessionstorage
    let s = JSON.parse(sessionStorage.getItem('participaDesiciones'));

    //se asigna a lista local
    this.lista = s.participaciones;

    const index = this.lista.findIndex(({ idPosicionVista }) => idPosicionVista == dato.idPosicionVista);

    if (this.parTomaDecisiones.recibeRemuneracion == false) {
      this.parTomaDecisiones.montoMensual = null

    }

    if (this.parTomaDecisiones.localizacion.ubicacion == 'EXTRANJERO') {
      this.parTomaDecisiones.localizacion.localizacionMexico = null
    } else if (this.parTomaDecisiones.localizacion.ubicacion == 'MEXICO') {
      this.parTomaDecisiones.localizacion.localizacionExtranjero = null
    }


    if (this.parTomaDecisiones.tipoInstitucion.id != 9999) {
      this.parTomaDecisiones.tipoInstitucionOtro = null
    }

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
    this.datosDesiciones = { ninguno: "", participaciones: this.lista, aclaracionesObservaciones: s.aclaracionesObservaciones }

    //se agrega al sessionStorage
    sessionStorage.setItem('participaDesiciones', JSON.stringify(this.datosDesiciones));

    this.activeModal.close({ result: 'success', form: 'participaTomaDecisiones' });
  }

  ngAfterViewInit(): void {

    console.log(this.participaDeciForm.valid);

    let formVal: boolean;
    const a = this.participaDeciForm.statusChanges.subscribe(() => {
      if (this.participaDeciForm.valid !== formVal) {
        this.TIPO_OPERACION = this.cata.getTipoOperacion(this.participaDeciForm.valid, this.parTomaDecisiones.registroHistorico, this.parTomaDecisiones.tipoOperacion, 'I');
        this.histFormInval = (this.parTomaDecisiones.registroHistorico && !this.participaDeciForm.valid);
        if (!this.participaDeciForm.valid) {
          a.unsubscribe();
          setTimeout(() => {
            this.initLoad2 = false;
            this.resetBaja();
          }, 500);

        }
        formVal = this.participaDeciForm.valid;
      }
    });

    setTimeout(() => {
      this.initLoad2 = false;
      if (this.participaDeciForm.valid && this.cata.precargaOracle) {
        this.resetBaja();
      }
      a.unsubscribe();
    }, 500);
  }

  resetBaja() {
    this.dataDisabled.forEach((data) => {
      if (this.cata.precargaOracle) {
        data.tipoOperacion.BAJA = false;
        data.tipoOperacion.MODIFICAR = false;
      } else {
        data.tipoOperacion.BAJA = this.participaDeciForm.controls[data.name] ? this.participaDeciForm.controls[data.name].valid : false;
        if (data.tipoOperacion.MODIFICAR === true) {
          data.tipoOperacion.MODIFICAR = this.participaDeciForm.controls[data.name] ? this.participaDeciForm.controls[data.name].valid : false;
        }
      }
    });
  }

  tipoOperacionChange(id) {
    if (id === 'SIN_CAMBIO' || id === 'BAJA') {
      this.parTomaDecisiones = JSON.parse(JSON.stringify(this.participaR));
      this.parTomaDecisiones.tipoOperacion = id;
    }
  }

  getDisabled(obj, tipoOperacion1) {
    if (!obj.name || !tipoOperacion1 || this.initLoad2) {
      return obj.isDisabled;
    }
    if (!this.parTomaDecisiones.registroHistorico) {
      return false;
    } else {
      const newObj = this.dataDisabled.find((value) => value.name === obj.name);
      return newObj.tipoOperacion[tipoOperacion1];
    }
  }
}
