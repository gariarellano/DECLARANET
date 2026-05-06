import { Component, OnInit, Input, HostListener, ViewChild, AfterViewInit } from '@angular/core';
import * as $ from 'jquery';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, NgForm } from '@angular/forms';
import { Globals } from 'src/app/globals';

@Component({
  selector: 'app-beneficios-modal',
  templateUrl: './beneficios-modal.component.html',
  styleUrls: ['./beneficios-modal.component.scss']
})
export class BeneficiosModalComponent implements OnInit, AfterViewInit {

  form: FormGroup;
  help = false;

  beneficiosPrivados: any;
  listaBeneficiosPrivados: Array<any>;
  datosBeneficiosPrivados: any;
  @Input() public objUpdate;
  dataDisabled: any;

  initLoad: boolean;
  decLoaded: any;
  beneficiosR: any;
  histFormInval: boolean;
  TIPO_OPERACION: any;
  @ViewChild('beneficiosForm') beneficiosForm: NgForm;

  constructor(private readonly modalService: NgbModal, private readonly activeModal: NgbActiveModal, public cata: Globals) {
    this.dataDisabled = [
      {
        name: 'beneficioA',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'otrosTipo',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'beneficiarioD',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'otorgante',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'razon',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
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
        name: 'rfcBene',
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
        name: 'formaRecepcion',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'especifique',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'monto',
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
        name: 'sector',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'otroEspecifique',
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
    this.beneficiosPrivados = {
      id: null,
      idPosicionVista: null,
      verificar: true,
      registroHistorico: false,
      tipoOperacion: null, //AGREGAR, MODIFICAR, SIN CAMBIO ó BAJA
      tipoBeneficio: { //catalogo de tipo de sorteo SORTEO CONCLUSIÓN DONACIÓN OTRO Especifique
        id: null,

        valor: null
      },
      tipoBeneficioOtro: null,
      beneficiario: { //catalogo beneficiario (familiar)
        id: null,

        valor: null
      },
      otorgante: {
        tipoPersona: null, //Fisica, Moral
        personaFisica: {
          //@persona
          nombre: '',
          primerApellido: '',
          segundoApellido: '',
          rfc: ''
        },
        personaMoral: {
          //@personaMoral
          nombre: '',
          rfc: ''
        }
      },
      formaRecepcion: null, //Monetario, Especie
      especifiqueBeneficio: null,
      montoMensualAproximado: {
        //@montoMoneda
        moneda: {
          id: null,

          valor: null
        },
        monto: null
      },
      sector: { //catalogo sector productivo.
        id: null,

        valor: null
      },
      sectorOtro: null
    }

    if (this.objUpdate) {
      if (this.objUpdate.sector === null) 
      { this.objUpdate.sector = this.beneficiosPrivados.sector; }
      if (this.objUpdate.montoMensualAproximado === null) 
      { this.objUpdate.montoMensualAproximado = this.beneficiosPrivados.montoMensualAproximado; }
      if (this.objUpdate.montoMensualAproximado.moneda === null) 
      { this.objUpdate.montoMensualAproximado.moneda = this.beneficiosPrivados.montoMensualAproximado.moneda; }
      if (this.objUpdate.tipoBeneficio === null) 
      { this.objUpdate.tipoBeneficio = this.beneficiosPrivados.tipoBeneficio; }
      if (this.objUpdate.beneficiario === null) 
      { this.objUpdate.beneficiario = this.beneficiosPrivados.beneficiario; }
      if (this.objUpdate.otorgante.personaMoral === null) 
      { this.objUpdate.otorgante.personaMoral = this.beneficiosPrivados.otorgante.personaMoral; }
      if (this.objUpdate.otorgante.personaFisica === null) 
      { this.objUpdate.otorgante.personaFisica = this.beneficiosPrivados.otorgante.personaFisica; }
      if (this.objUpdate.tipoBeneficio === null) 
      { this.objUpdate.tipoBeneficio = this.beneficiosPrivados.tipoBeneficio; }
      if (this.objUpdate.montoMensualAproximado === null) 
      { this.objUpdate.montoMensualAproximado = this.beneficiosPrivados.montoMensualAproximado; }
      if (this.objUpdate.sector === null)
       { this.objUpdate.sector = this.beneficiosPrivados.sector; }
      this.beneficiosPrivados = this.objUpdate;
      this.initLoad = true;
    }
    this.beneficiosR = JSON.parse(JSON.stringify(this.beneficiosPrivados));
  }



  close() {
    this.modalService.dismissAll('beneficios');
    $("body").removeClass("disabled-onepage-scroll");
  }

  guardaLocal() {
    //obtener datos del formulario
    let dato = this.beneficiosPrivados;
    dato.verificar = true;
    if (!this.decLoaded.numeroDeclaracionPrecarga && dato.registroHistorico === true) {
      dato.registroHistorico = false;
    }
    //obtener array de sessionstorage
    let s = JSON.parse(sessionStorage.getItem('beneficiosPrivados'));

    //se asigna a lista local
    this.listaBeneficiosPrivados = s.beneficios;

    const index = this.listaBeneficiosPrivados.findIndex(({ idPosicionVista }) => idPosicionVista == dato.idPosicionVista);

    if (this.beneficiosPrivados.otorgante.tipoPersona == 'PERSONA_FISICA') {
      this.beneficiosPrivados.otorgante.personaMoral = null
    } else if (this.beneficiosPrivados.otorgante.tipoPersona == 'PERSONA_MORAL') {
      this.beneficiosPrivados.otorgante.personaFisica = null
    }


    if (this.beneficiosPrivados.formaRecepcion == 'MONETARIO') {
      this.beneficiosPrivados.especifiqueBeneficio = null;
    }

    if (this.beneficiosPrivados.tipoBeneficio.id != 9999) {
      this.beneficiosPrivados.tipoBeneficioOtro = null
    }

    if (this.beneficiosPrivados.sector.id != 9999) {
      this.beneficiosPrivados.sectorOtro = null
    }

    if (index === -1) {
      //se agrega el nuevo registro al array local
      this.listaBeneficiosPrivados.push(dato);
    } else {
      this.listaBeneficiosPrivados[index] = dato;
    }

    this.listaBeneficiosPrivados.forEach(
      (element, index) => {
        this.listaBeneficiosPrivados[index].idPosicionVista = index + 1;
      });

    //se crea el json que se volvera a guardar en el sessionStorage    
    this.datosBeneficiosPrivados = { ninguno: false, beneficios: this.listaBeneficiosPrivados, aclaracionesObservaciones: s.aclaracionesObservaciones }

    //se agrega al sessionStorage
    sessionStorage.setItem('beneficiosPrivados', JSON.stringify(this.datosBeneficiosPrivados));

    this.activeModal.close({ result: 'success', form: 'beneficios' });
  }

  compareFn(c1: any, c2: any): boolean {
    return c1 && c2 ? c1.id === c2.id : c1 === c2;
  }

  ngAfterViewInit(): void {

    console.log(this.beneficiosForm.valid);

    let formVal: boolean;
    const a = this.beneficiosForm.statusChanges.subscribe(() => {
      if (this.beneficiosForm.valid !== formVal) {
        this.TIPO_OPERACION = this.cata.getTipoOperacion(this.beneficiosForm.valid, this.beneficiosPrivados.registroHistorico, this.beneficiosPrivados.tipoOperacion, 'I');
        this.histFormInval = (this.beneficiosPrivados.registroHistorico && !this.beneficiosForm.valid);
        if (!this.beneficiosForm.valid) {
          a.unsubscribe();
          setTimeout(() => {
            this.initLoad = false;
            this.resetBaja();
          }, 500);

        }
        formVal = this.beneficiosForm.valid;

      }
    });

    setTimeout(() => {
      this.initLoad = false;
      if (this.beneficiosForm.valid && this.cata.precargaOracle) {
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

        data.tipoOperacion.BAJA = this.beneficiosForm.controls[data.name] ? this.beneficiosForm.controls[data.name].valid : false;
        if (data.tipoOperacion.MODIFICAR === true) {
          data.tipoOperacion.MODIFICAR = this.beneficiosForm.controls[data.name] ? this.beneficiosForm.controls[data.name].valid : false;
        }
      }
    });
  }

  tipoOperacionChange(id) {
    if (id === 'SIN_CAMBIO' || id === 'BAJA') {
      this.beneficiosPrivados = JSON.parse(JSON.stringify(this.beneficiosR));
      this.beneficiosPrivados.tipoOperacion = id;
    }
  }

  getDisabled(obj, tipoOperacion1) {
    if (!obj.name || !tipoOperacion1 || this.initLoad) {
      return obj.isDisabled;
    }
    if (!this.beneficiosPrivados.registroHistorico) {
      return false;
    } else {
      const newObj = this.dataDisabled.find((value) => value.name === obj.name);
      return newObj.tipoOperacion[tipoOperacion1];
    }
  };
}