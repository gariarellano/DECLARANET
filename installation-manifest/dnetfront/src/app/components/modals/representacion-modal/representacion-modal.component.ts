import { Component, OnInit, Input, HostListener, ViewChild, AfterViewInit } from '@angular/core';
import * as $ from 'jquery';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, NgForm } from '@angular/forms';
import { Globals } from 'src/app/globals';

@Component({
  selector: 'app-representacion-modal',
  templateUrl: './representacion-modal.component.html',
  styleUrls: ['./representacion-modal.component.scss']
})
export class RepresentacionModalComponent implements OnInit, AfterViewInit {

  jsonRepresentacion: any;
  form: FormGroup;
  help = false;
  fecha: any;
  listaRepresentacion: any;
  @Input() public objUpdate;
  dataDisabled: any;

  initLoad: boolean;
  decLoaded: any;
  representacionR: any;
  histFormInval: boolean;
  TIPO_OPERACION: any;
  @ViewChild('representacionForm') representacionForm: NgForm;

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
        name: 'tipoRepresentacion',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'representante',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'nomOtor',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'apePatOtor',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'apeMatOtor',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'rfcOtor',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'nombreRazon',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'rfc',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'fechaInicio',
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
        name: 'montoMensual',
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
        name: 'ubicacion',
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
      {
        name: 'sector',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'sectorOtro',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
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
    this.decLoaded = JSON.parse(sessionStorage.getItem('declaracionLoaded'));

    this.jsonRepresentacion = {
      id: null,
      idPosicionVista: null,
      registroHistorico: false,
      verificar: true,
      tipoOperacion: null,
      participante: null, //Declarante, Pareja, Dependiente Economico
      tipoRepresentacion: null, //Representante, Representado
      fechaInicioRepresentacion: null,
      representanteRepresentado: {
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
      sector: {
        id: null,

        valor: null
      },
      sectorOtro: null,
    }

    if (this.objUpdate) {
      if (this.objUpdate.montoMensual === null) { this.objUpdate.montoMensual = this.jsonRepresentacion.montoMensual; }
      if (this.objUpdate.montoMensual.moneda === null) { this.objUpdate.montoMensual.moneda = this.jsonRepresentacion.montoMensual.moneda; }
      if (this.objUpdate.representanteRepresentado.personaMoral === null) { this.objUpdate.representanteRepresentado.personaMoral = this.jsonRepresentacion.representanteRepresentado.personaMoral; }
      if (this.objUpdate.representanteRepresentado.personaFisica === null) { this.objUpdate.representanteRepresentado.personaFisica = this.jsonRepresentacion.representanteRepresentado.personaFisica; }
      if (this.objUpdate.montoMensual === null) { this.objUpdate.montoMensual = this.jsonRepresentacion.montoMensual; }
      if (this.objUpdate.localizacion === null) { this.objUpdate.localizacion = this.jsonRepresentacion.localizacion; }
      if (this.objUpdate.localizacion.localizacionMexico === null) { this.objUpdate.localizacion.localizacionMexico = this.jsonRepresentacion.localizacion.localizacionMexico; }
      if (this.objUpdate.localizacion.localizacionExtranjero === null) { this.objUpdate.localizacion.localizacionExtranjero = this.jsonRepresentacion.localizacion.localizacionExtranjero; }
      if (this.objUpdate.localizacion.localizacionMexico.entidadFederativa === null) { this.objUpdate.localizacion.localizacionMexico.entidadFederativa = this.jsonRepresentacion.localizacion.localizacionMexico.entidadFederativa; }
      if (this.objUpdate.localizacion.localizacionExtranjero.pais === null) { this.objUpdate.localizacion.localizacionExtranjero.pais = this.jsonRepresentacion.localizacion.localizacionExtranjero.pais; }
      if (this.objUpdate.sector === null) { this.objUpdate.sector = this.jsonRepresentacion.sector; }
      this.jsonRepresentacion = this.objUpdate;
      this.initLoad = true;
    }
    this.representacionR = JSON.parse(JSON.stringify(this.jsonRepresentacion));
  }

  close() {
    this.modalService.dismissAll("representacion");
    $("body").removeClass("disabled-onepage-scroll");
  }

  guardaLocal() {
    //obtener datos del formulario
    const dato = this.jsonRepresentacion;
    dato.verificar = true;
    if (!this.decLoaded.numeroDeclaracionPrecarga && dato.registroHistorico === true) {
      dato.registroHistorico = false;
    }
    //obtener array de sessionstorage
    const s = JSON.parse(sessionStorage.getItem('representaciones'));

    //se asigna a lista local
    this.listaRepresentacion = s.representaciones;

    const index = this.listaRepresentacion.findIndex(({ idPosicionVista }) => idPosicionVista == dato.idPosicionVista);

    if (this.jsonRepresentacion.representanteRepresentado.tipoPersona == 'PERSONA_FISICA') {
      this.jsonRepresentacion.representanteRepresentado.personaMoral = null
    } else if (this.jsonRepresentacion.representanteRepresentado.tipoPersona == 'PERSONA_MORAL') {
      this.jsonRepresentacion.representanteRepresentado.personaFisica = null
    }

    if (this.jsonRepresentacion.localizacion.ubicacion == 'EXTRANJERO') {
      this.jsonRepresentacion.localizacion.localizacionMexico = null
    } else if (this.jsonRepresentacion.localizacion.ubicacion == 'MEXICO') {
      this.jsonRepresentacion.localizacion.localizacionExtranjero = null
    }

    if (this.jsonRepresentacion.recibeRemuneracion == false) {
      this.jsonRepresentacion.montoMensual = null
    }

    if (this.jsonRepresentacion.sector.id != 9999) {
      this.jsonRepresentacion.sectorOtro = null
    }


    if (index === -1) {
      //se agrega el nuevo registro al array local
      this.listaRepresentacion.push(dato);
    } else {
      this.listaRepresentacion[index] = dato;
    }

    this.listaRepresentacion.forEach(
      (element, index) => {
        this.listaRepresentacion[index].idPosicionVista = index + 1;
      });

    //se crea el json que se volvera a guardar en el sessionStorage
    this.jsonRepresentacion = { ninguno: false, representaciones: this.listaRepresentacion, aclaracionesObservaciones: s.aclaracionesObservaciones }

    //se agrega al sessionStorage
    sessionStorage.setItem('representaciones', JSON.stringify(this.jsonRepresentacion));

    this.activeModal.close({ result: 'success', form: 'representacion' });

  }

  ngAfterViewInit(): void {

    let formVal: boolean;
    const a = this.representacionForm.statusChanges.subscribe(() => {
      if (this.representacionForm.valid !== formVal) {
        this.TIPO_OPERACION = this.cata.getTipoOperacion(this.representacionForm.valid, this.jsonRepresentacion.registroHistorico, this.jsonRepresentacion.tipoOperacion, 'I');
        this.histFormInval = (this.jsonRepresentacion.registroHistorico && !this.representacionForm.valid);
        if (!this.representacionForm.valid) {
          a.unsubscribe();
          setTimeout(() => {
            this.initLoad = false;
            this.resetBaja();
          }, 500);

        }
        formVal = this.representacionForm.valid;

      }
    });

    setTimeout(() => {
      this.initLoad = false;
      if (this.representacionForm.valid && this.cata.precargaOracle) {
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
        data.tipoOperacion.BAJA = this.representacionForm.controls[data.name] ? this.representacionForm.controls[data.name].valid : false;
        if (data.tipoOperacion.MODIFICAR === true) {
          data.tipoOperacion.MODIFICAR = this.representacionForm.controls[data.name] ? this.representacionForm.controls[data.name].valid : false;
        }
      }
    });
  }

  tipoOperacionChange(id) {
    if (id === 'SIN_CAMBIO' || id === 'BAJA') {
      this.jsonRepresentacion = JSON.parse(JSON.stringify(this.representacionR));
      this.jsonRepresentacion.tipoOperacion = id;
    }
  }

  getDisabled(obj, tipoOperacion1) {
    if (!obj.name || !tipoOperacion1 || this.initLoad) {
      return obj.isDisabled;
    }
    if (!this.jsonRepresentacion.registroHistorico) {
      return false;
    } else {
      const newObj = this.dataDisabled.find((value) => value.name === obj.name);
      return newObj.tipoOperacion[tipoOperacion1];
    }
  }
}
