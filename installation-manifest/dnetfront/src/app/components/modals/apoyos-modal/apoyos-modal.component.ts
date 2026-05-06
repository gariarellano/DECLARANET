import { Component, OnInit, Input, HostListener, ViewChild, AfterViewInit } from '@angular/core';
import * as $ from 'jquery';
import { NgbModal, NgbModalConfig, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormControl, FormGroup, Validators, NgForm } from '@angular/forms';
import { Globals } from 'src/app/globals';

@Component({
  selector: 'app-apoyos-modal',
  templateUrl: './apoyos-modal.component.html',
  styleUrls: ['./apoyos-modal.component.scss']
})
export class ApoyosModalComponent implements OnInit, AfterViewInit {

  form:FormGroup;
  help = false;

  apoyoBeneficio: any;

  listaApoyos: Array<any>;
  datosApoyos: any;
  @Input() public objUpdate;
  dataDisabled: any;

  initLoad : boolean;
  decLoaded: any;
  apoyosR: any;
  histFormInval: boolean;
  TIPO_OPERACION: any;
  @ViewChild('apoyosForm')apoyosForm: NgForm;

  constructor(private readonly modalService: NgbModal, private readonly activeModal: NgbActiveModal, public cata: Globals) {
    this.dataDisabled = [
      {
        name: 'beneficiario',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'nombrePrograma',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'institucionOtorgante',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'nivelGobierno',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'tipoApoyoM',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'apoyoValor',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'formaRecepcion',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'espFormaRec',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'montoMone',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'tipoMoneda',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
    ]
   }

  @HostListener('paste', ['$event']) blockPaste(e: KeyboardEvent) {
    e.preventDefault();
  }

  ngOnInit() {
    this.decLoaded = JSON.parse(sessionStorage.getItem('declaracionLoaded'));

    this.apoyoBeneficio = {

          id: null,
          idPosicionVista:0,
          verificar: true,
          registroHistorico: false,
          tipoOperacion: null,
          beneficiarioPrograma:
          {
              id: null,

              valor: null
              },
          nombrePrograma: '',
          institucionOtorgante: '',
          nivelOrdenGobierno: null, // FEDERAL, ESTATAL, MUNICIPAL / ALCALDÍA
          tipoApoyo:
          {
              id: null,

              valor: null
              },
          tipoApoyoOtro: '',
          formaRecepcion: null, //Monetario, Especie
          montoApoyoMensual: {

              moneda: {
                  id: null,

                  valor: null
                  },

              monto: null,

              },
          especifiqueApoyo: '',

      };
      console.log("entro ");



        if (this.objUpdate) {
          if (this.objUpdate.beneficiarioPrograma === null ) 
          {this.objUpdate.beneficiarioPrograma = this.apoyoBeneficio.beneficiarioPrograma;}  
          if (this.objUpdate.tipoApoyo === null ) 
          {this.objUpdate.tipoApoyo = this.apoyoBeneficio.tipoApoyo;}
          if (this.objUpdate.nivelOrdenGobierno === null ) 
          {this.objUpdate.nivelOrdenGobierno = this.apoyoBeneficio.nivelOrdenGobierno;}
          if (this.objUpdate.montoApoyoMensual === null ) 
          {this.objUpdate.montoApoyoMensual = this.apoyoBeneficio.montoApoyoMensual;}
          this.apoyoBeneficio=this.objUpdate;
          this.initLoad = true;
        }

        this.apoyosR = JSON.parse(JSON.stringify(this.apoyoBeneficio));

  }



  close() {
    this.modalService.dismissAll('apoyosBen');
    $("body").removeClass("disabled-onepage-scroll");
  }

  guardaLocal() {
    //obtener datos del formulario
    let dato= this.apoyoBeneficio;
    dato.verificar = true;
    if (!this.decLoaded.numeroDeclaracionPrecarga && dato.registroHistorico === true) {
      dato.registroHistorico = false;
    }
    //obtener array de sessionstorage
    let s = JSON.parse(sessionStorage.getItem('apoyosBeneficios'));

    //se asigna a lista local
    this.listaApoyos = s.apoyos;

    const index = this.listaApoyos.findIndex(({ idPosicionVista }) => idPosicionVista == dato.idPosicionVista);

    if(this.apoyoBeneficio.formaRecepcion == 'MONETARIO' ){
      this.apoyoBeneficio.especifiqueApoyo= null
    }

    if(this.apoyoBeneficio.tipoApoyo.id !== 9999 ){
      this.apoyoBeneficio.tipoApoyoOtro = null
    }


    if (index === -1) {
      //se agrega el nuevo registro al array local
      this.listaApoyos.push(dato);
    } else {
      this.listaApoyos[index] = dato;
    }

    this.listaApoyos.forEach(
      (element, index) => {
        this.listaApoyos[index].idPosicionVista = index + 1;
      });

    //se crea el json que se volvera a guardar en el sessionStorage
    this.datosApoyos = { ninguno: false, apoyos: this.listaApoyos, aclaracionesObservaciones: s.aclaracionesObservaciones }

    sessionStorage.setItem('apoyosBeneficios', JSON.stringify(this.datosApoyos));

    this.activeModal.close({result: 'success', form: 'apoyosBen'});
  }

  ngAfterViewInit(): void {
    
    console.log(this.apoyosForm.valid);

    let formVal: boolean;
    const a = this.apoyosForm.statusChanges.subscribe(() => {
      if (this.apoyosForm.valid !== formVal) {
        this.TIPO_OPERACION = this.cata.getTipoOperacion(this.apoyosForm.valid, this.apoyoBeneficio.registroHistorico, this.apoyoBeneficio.tipoOperacion, 'I');
        this.histFormInval = (this.apoyoBeneficio.registroHistorico && !this.apoyosForm.valid);
        if (!this.apoyosForm.valid) {
          a.unsubscribe();
          setTimeout(() => {
            this.initLoad = false;
            this.resetBaja();
          }, 500);
          
        }
        formVal = this.apoyosForm.valid;

      }
    });

    setTimeout(() => {
      this.initLoad = false;
      if (this.apoyosForm.valid && this.cata.precargaOracle) {
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
      data.tipoOperacion.BAJA = this.apoyosForm.controls[data.name] ? this.apoyosForm.controls[data.name].valid : false;
      if (data.tipoOperacion.MODIFICAR === true) {
        data.tipoOperacion.MODIFICAR = this.apoyosForm.controls[data.name] ? this.apoyosForm.controls[data.name].valid : false;
      } 
    
    }
  });
  }

  tipoOperacionChange(id) {
    if (id === 'SIN_CAMBIO' || id === 'BAJA') {
      this.apoyoBeneficio = JSON.parse(JSON.stringify(this.apoyosR));
      this.apoyoBeneficio.tipoOperacion = id;
    }
  }

  getDisabled(obj, tipoOperacion1) {
    if (!obj.name || !tipoOperacion1 || this.initLoad) {
      return obj.isDisabled;
    }
    if (!this.apoyoBeneficio.registroHistorico) {
      return false;
    } else {
      const newObj = this.dataDisabled.find  ((value) => value.name === obj.name);
      return newObj.tipoOperacion[tipoOperacion1];
    }
  }

}
