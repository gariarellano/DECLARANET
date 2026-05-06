import { Component, OnInit, Input, AfterViewInit, HostListener, ViewChild } from '@angular/core';
import * as $ from 'jquery';
import { NgbModal, NgbModalConfig, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, FormControl, Validators, NgForm } from '@angular/forms';


import { Directive, forwardRef } from '@angular/core';
import { NG_VALIDATORS, AbstractControl, ValidatorFn, Validator } from '@angular/forms';
import { Globals } from 'src/app/globals';
import { Fideicomisos } from 'src/app/interfaces/Fedeicomisos';

@Component({
  selector: 'app-fideicomisos-modal',
  templateUrl: './fideicomisos-modal.component.html',
  styleUrls: ['./fideicomisos-modal.component.scss']
})
export class FideicomisosModalComponent implements OnInit, AfterViewInit{


  form: FormGroup;
  help = false;

  fideicomisos: Fideicomisos;
  listaFide: any;
  datosFide: any;
  @Input() public objUpdate;
  dataDisabled:any;

  initLoad: boolean;
  decLoaded: any;
  fideicomisosR: any;
  histFormInval: boolean;
  TIPO_OPERACION: any;
  @ViewChild('fidei')fidei: NgForm;

  constructor(private modalService: NgbModal, private activeModal: NgbActiveModal, public cat: Globals, public globals: Globals) {
    this.dataDisabled = [
      {
        name: 'participante',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'tipoFidei',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'participacion',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },      
      {
        name: 'rfcFideicomiso',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },      {
        name: 'fideicomitente',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },      {
        name: 'nombrefideicomitente',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },      {
        name: 'rfcFideicomitenteMoral',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },      {
        name: 'nombrefideicomitenteF',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },      {
        name: 'apePatfideicomitente',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },      {
        name: 'apeMatfideicomitente',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },      {
        name: 'rfcFideicomitenteFisica',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },      {
        name: 'nombreFiduciario',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },      {
        name: 'rfcfidu',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },      {
        name: 'fideicomisario',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },      {
        name: 'nombreFideicomisario',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },      {
        name: 'rfcFideicomisarioMoral',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },      {
        name: 'nombreFideicomisarioF',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },      {
        name: 'apePatFideicomisario',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },      {
        name: 'apeMatFideicomisario',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },      {
        name: 'rfcFideicomisarioFisica',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },      {
        name: 'sector',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },      {
        name: 'otroEspecifique',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },      {
        name: 'lugarFideicomiso',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
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

    this.fideicomisos = {
      // ninguno: false,
      // aclaracionesObservaciones: '',
        id: null,
        idPosicionVista: null,
        verificar: true,
        registroHistorico: false,
        tipoOperacion: null,
        participante: null, //DECLARANETE, PAREJA, DEPENDIENTE ECONOMICO
        tipoFideicomiso: null, //PUBLICO, PROVADO, MIXTO
        tipoParticipacion: null, //FIDEICOMITENTE, FIDUCIARIO, FIDEICOMISARIO, COMITE TECNICO
        rfcFideicomiso: null,
        fideicomitente: {
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
        fiduciario: {
          //@personaMoral
          nombre: '',
          rfc: ''
        },
        fideicomisario: {
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
        sector: {
          id: null,

          valor: null
        },
        sectorOtro:null,
        localizacion: null //MÉXICO, EXTRANJERO
      }


      console.log("entro ");



      // tslint:disable-next-line:align
      if (this.objUpdate) {
        if (this.objUpdate.fideicomitente.personaMoral === null ) {this.objUpdate.fideicomitente.personaMoral = this.fideicomisos.fideicomitente.personaMoral;}
        if (this.objUpdate.fideicomitente.personaFisica === null ) {this.objUpdate.fideicomitente.personaFisica = this.fideicomisos.fideicomitente.personaFisica;}
        if (this.objUpdate.fideicomisario.personaMoral === null ) {this.objUpdate.fideicomisario.personaMoral = this.fideicomisos.fideicomisario.personaMoral;}
        if (this.objUpdate.fideicomisario.personaFisica === null ) {this.objUpdate.fideicomisario.personaFisica = this.fideicomisos.fideicomisario.personaFisica;}
        if (this.objUpdate.sector === null ) {this.objUpdate.sector = this.fideicomisos.sector;}
        this.fideicomisos = this.objUpdate;
        this.initLoad = true;
      }

      this.fideicomisosR = JSON.parse(JSON.stringify(this.fideicomisos));

  }

  close() {
    this.modalService.dismissAll('fideicomisos');
    $("body").removeClass("disabled-onepage-scroll");
  }

  guardaLocal() {

    let dato = this.fideicomisos;
    dato.verificar=true;
    if (!this.decLoaded.numeroDeclaracionPrecarga && dato.registroHistorico === true) {
      dato.registroHistorico = false;
    }
    // //obtener array de sessionstorage
    let s = JSON.parse(sessionStorage.getItem('fideicomisos'));

    // //se asigna a lista local
    this.listaFide = s.fideicomisos;

    const index = this.listaFide.findIndex(({ idPosicionVista }) => idPosicionVista == dato.idPosicionVista);

    if(this.fideicomisos.fideicomitente.tipoPersona == 'PERSONA_FISICA' ){
      this.fideicomisos.fideicomitente.personaMoral = null
    } else if (this.fideicomisos.fideicomitente.tipoPersona == 'PERSONA_MORAL' ){
      this.fideicomisos.fideicomitente.personaFisica = null
    }

    if(this.fideicomisos.fideicomisario.tipoPersona == 'PERSONA_FISICA' ){
      this.fideicomisos.fideicomisario.personaMoral = null
    } else if (this.fideicomisos.fideicomisario.tipoPersona == 'PERSONA_MORAL' ){
      this.fideicomisos.fideicomisario.personaFisica = null
    }

    if(this.fideicomisos.sector.id != 9999 ){
      this.fideicomisos.sectorOtro = null
    }


    if (index === -1) {
      //se agrega el nuevo registro al array local
      this.listaFide.push(dato);
    } else {
      this.listaFide[index] = dato;
    }

    this.listaFide.forEach(
      (element, index) => {
        this.listaFide[index].idPosicionVista = index + 1;
      });


    // // //se crea el json que se volvera a guardar en el sessionStorage
    this.datosFide = { ninguno: false, fideicomisos: this.listaFide, aclaracionesObservaciones: s.aclaracionesObservaciones }

    // // //se agrega al sessionStorage
    sessionStorage.setItem('fideicomisos', JSON.stringify(this.datosFide));

    // this.close();
    this.activeModal.close({result: 'success', form: 'fideicomisos'});
  }

 ngAfterViewInit(): void {
    
    console.log(this.fidei.valid);

    let formVal: boolean;
    const a = this.fidei.statusChanges.subscribe(() => {
      if (this.fidei.valid !== formVal) {
        this.TIPO_OPERACION = this.cat.getTipoOperacion(this.fidei.valid, this.fideicomisos.registroHistorico, this.fideicomisos.tipoOperacion, 'I');
        this.histFormInval = (this.fideicomisos.registroHistorico && !this.fidei.valid);
        if (!this.fidei.valid) {
          a.unsubscribe();
          setTimeout(() => {
            this.initLoad = false;
            this.resetBaja();
          }, 500);
          
        }
        formVal = this.fidei.valid;

      }
    });

    setTimeout(() => {
      this.initLoad = false;
      if (this.fidei.valid && this.cat.precargaOracle) {
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
      /* data.tipoOperacion.BAJA = this.fidei.controls[data.name].valid; */
      data.tipoOperacion.BAJA = this.fidei.controls[data.name] ? this.fidei.controls[data.name].valid : false;
      if (data.tipoOperacion.MODIFICAR === true) {
        data.tipoOperacion.MODIFICAR = this.fidei.controls[data.name] ? this.fidei.controls[data.name].valid : false;
      } 
    // console.log(this.vehiculosForm.controls[data.name]);
      }
      });
  }

  tipoOperacionChange(id) {
    if (id === 'SIN_CAMBIO' || id === 'BAJA') {
      this.fideicomisos = JSON.parse(JSON.stringify(this.fideicomisosR));
      this.fideicomisos.tipoOperacion = id;
    }
  }

  getDisabled(obj, tipoOperacion1) {
    // console.log( 'vehiculosGetDisabled' , {valid: obj.valid, name : obj.name });
    if (!obj.name || !tipoOperacion1  || this.initLoad) {
      return obj.isDisabled;
    }
    if (!this.fideicomisos.registroHistorico) {
      return false;
    } else {
      const newObj = this.dataDisabled.find  ((value) => value.name === obj.name);
      const newObj1 = newObj.tipoOperacion[tipoOperacion1];
      // console.log('filteradeudos', newObj1);
      return newObj1;
    }
  }

}
