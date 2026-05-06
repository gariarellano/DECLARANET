import { Component, OnInit, Output, EventEmitter, Input, HostListener, ViewChild, AfterViewInit } from '@angular/core';
import * as $ from 'jquery';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, FormControl, NgForm } from '@angular/forms';
import { Globals } from 'src/app/globals';


@Component({
  selector: 'app-participacion-empresas-modal',
  templateUrl: './participacion-empresas-modal.component.html',
  styleUrls: ['./participacion-empresas-modal.component.scss']
})

export class ParticipacionEmpresasModalComponent implements OnInit, AfterViewInit {

  participaEmpresas: any;
  @Input() public objUpdate;

  @Output()
  aceptar = new EventEmitter<JSON>();

  form: FormGroup;
  help = false;
  lista: Array<any>;
  datosParticipacion: any;

  dataDisabled : any;
  initLoad : boolean;
  decLoaded: any;
  parEmpresasR: any;
  histFormInval: boolean;
  TIPO_OPERACION: any;
  @ViewChild('participaEmpresaForm')participaEmpresaForm: NgForm;

  constructor(private readonly modalService: NgbModal, private readonly activeModal: NgbActiveModal, public cata: Globals) {
    this.dataDisabled = [
      {
        name: 'participante',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'nombreEmpresa',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
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
        name: 'porcentajeParticipacion',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'tipo',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'especificaSector',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'otroParticipa',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'recibeRemu',
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
        name: 'ubicacion',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'entidadFederativa',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'paisLocaliza',
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
        name: 'especificaSector',
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

    this.participaEmpresas = {
      id: null,
      idPosicionVista: 0,
      verificar: true,
      registroHistorico: false,
      tipoOperacion: null,
      participante: null, //DECLARANETE, PAREJA, DEPENDIENTE ECONOMICO
      nombreEmpresaSociedadAsociacion: {
        //@personaMoral
        nombre: '',
        rfc: '',
      },
      porcentajeParticipacion: null,
      tipoParticipacion: { //catalogo tipo participacion
        id: null,
        valor: null
      },
      tipoParticipacionOtro:'',
      recibeRemuneracion: null,
      montoMensual: {
        //@montoMoneda
        moneda: {
          id: null,
          valor: null
        },
        monto: 0
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
        },


      },
      sector: {
        id: null,
        valor: null
      },
      sectorOtro: '',

    }


    if (this.objUpdate) {
      if(this.objUpdate.montoMensual == null)
        {this.objUpdate.montoMensual = this.participaEmpresas.montoMensual}
      if (this.objUpdate.localizacion.localizacionMexico === null ) 
        {this.objUpdate.localizacion.localizacionMexico = this.participaEmpresas.localizacion.localizacionMexico;}
      if (this.objUpdate.localizacion.localizacionExtranjero === null )
        {this.objUpdate.localizacion.localizacionExtranjero = this.participaEmpresas.localizacion.localizacionExtranjero;}
      this.participaEmpresas=this.objUpdate;
      this.initLoad=true;
    }

    this.parEmpresasR = JSON.parse(JSON.stringify(this.participaEmpresas));
  }



  close() {
    this.modalService.dismissAll('participaEmpresa');
    $("body").removeClass("disabled-onepage-scroll");
  }


  guardaLocal() {
    //obtener datos del formulario
    const dato= this.participaEmpresas;
    dato.verificar=true;
    if (!this.decLoaded.numeroDeclaracionPrecarga && dato.registroHistorico === true) {
      dato.registroHistorico = false;
    }
    //obtener array de sessionstorage
    const s = JSON.parse(sessionStorage.getItem('participaEmpresas'));

    //se asigna a lista local
    this.lista = s.participaciones;

    const index = this.lista.findIndex(({ idPosicionVista }) => idPosicionVista == dato.idPosicionVista);
   
    this.participaEmpresas.porcentajeParticipacion = Math.round(this.participaEmpresas.porcentajeParticipacion);
    if(this.participaEmpresas.montoMensual != null && this.participaEmpresas.montoMensual.monto != null){
      this.participaEmpresas.montoMensual.monto= Math.round(this.participaEmpresas.montoMensual.monto);
    }
   

    if(this.participaEmpresas.localizacion.ubicacion == 'EXTRANJERO'){
      this.participaEmpresas.localizacion.localizacionMexico = null
    } else if (this.participaEmpresas.localizacion.ubicacion == 'MEXICO'){
      this.participaEmpresas.localizacion.localizacionExtranjero = null
    }

    if(this.participaEmpresas.recibeRemuneracion == false){
      this.participaEmpresas.montoMensual = null
    }

    if(this.participaEmpresas.sector.id != 9999 ){
      this.participaEmpresas.sectorOtro = null
    }

    if(this.participaEmpresas.tipoParticipacion.id != 9999 ){
      this.participaEmpresas.tipoParticipacionOtro = null
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
    this.datosParticipacion = { ninguno: false, participaciones: this.lista, aclaracionesObservaciones: s.aclaracionesObservaciones }

    //se agrega al sessionStorage
    sessionStorage.setItem('participaEmpresas', JSON.stringify(this.datosParticipacion));

    this.activeModal.close({result: 'success', form: 'participaEmpresa'});
  }

  ngAfterViewInit(): void {
    
    console.log(this.participaEmpresaForm.valid);

    let formVal: boolean;
    const a = this.participaEmpresaForm.statusChanges.subscribe(() => {
      if (this.participaEmpresaForm.valid !== formVal) {
        this.TIPO_OPERACION = this.cata.getTipoOperacion(this.participaEmpresaForm.valid, this.participaEmpresas.registroHistorico, this.participaEmpresas.tipoOperacion, 'I');
        this.histFormInval = (this.participaEmpresas.registroHistorico && !this.participaEmpresaForm.valid);
        if (!this.participaEmpresaForm.valid) {
          a.unsubscribe();
          setTimeout(() => {
            this.initLoad = false;
            this.resetBaja();
          }, 500);          
        }
        formVal = this.participaEmpresaForm.valid;
      }
    });

    setTimeout(() => {
      this.initLoad = false;
      if (this.participaEmpresaForm.valid && this.cata.precargaOracle) {
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
        data.tipoOperacion.BAJA = this.participaEmpresaForm.controls[data.name] ? this.participaEmpresaForm.controls[data.name].valid : false;
        if (data.tipoOperacion.MODIFICAR === true) {
        data.tipoOperacion.MODIFICAR = this.participaEmpresaForm.controls[data.name] ? this.participaEmpresaForm.controls[data.name].valid : false;
}
}
  });
  }

  tipoOperacionChange(id) {
    if (id === 'SIN_CAMBIO' || id === 'BAJA') {
      this.participaEmpresas = JSON.parse(JSON.stringify(this.parEmpresasR));
      this.participaEmpresas.tipoOperacion = id;
    }
  }

  getDisabled(obj, tipoOperacion1) {
    if (!obj.name || !tipoOperacion1 || this.initLoad) {
      return obj.isDisabled;
    }
    if (!this.participaEmpresas.registroHistorico) {
      return false;
    } else {
      const newObj = this.dataDisabled.find  ((value) => value.name === obj.name); 
      return newObj.tipoOperacion[tipoOperacion1];;
    }
  }

}
