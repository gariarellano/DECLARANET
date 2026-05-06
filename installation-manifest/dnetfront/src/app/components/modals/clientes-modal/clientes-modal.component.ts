import { Component, OnInit, Input, HostListener, ViewChild, AfterViewInit } from '@angular/core';
import * as $ from 'jquery';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, FormControl, NgForm } from '@angular/forms';
import { Globals } from 'src/app/globals';

@Component({
  selector: 'app-clientes-modal',
  templateUrl: './clientes-modal.component.html',
  styleUrls: ['./clientes-modal.component.scss']
})
export class ClientesModalComponent implements OnInit, AfterViewInit {

  form: FormGroup;
  help = false;

  clientesPrincipal: any;

  listaClientes: Array<any>;
  datosClientes: any;
  @Input() public objUpdate;
  dataDisabled: any;

  initLoad: boolean;
  decLoaded: any;
  clientesR: any;
  histFormInval: boolean;
  TIPO_OPERACION: any;
  @ViewChild('clientesForm')clientesForm: NgForm;

  constructor(private readonly modalService: NgbModal, private readonly activeModal: NgbActiveModal, public cata: Globals) {
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
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'nomOtor',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'apePatOtor',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'razonSocial',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'rfcCliente',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
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
      {
        name: 'tipoPersona',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
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
    this.form = new FormGroup({
      'lugarUbica': new FormControl(' '),
      'actividadLucrativa': new FormControl(' '),
      'tipoPersona': new FormControl(' '),
      'clientePrincipal': new FormControl(' '),
      'sectorProductivo': new FormControl(' ')
    });

    this.clientesPrincipal= {

        id: null,
        idPosicionVista: null,
        verificar: true,
        registroHistorico:false,
        tipoOperacion: null,
        participante: null, //DECLARANETE, PAREJA, DEPENDIENTE ECONOMICO
        nombreEmpresaServicio: '',
        rfcEmpresa: '',
        clientePrincipal: {
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
        montoAproximadoGanancia: {
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
      if (this.objUpdate.sector === null ) 
        {this.objUpdate.sector = this.clientesPrincipal.sector;}
      if (this.objUpdate.montoAproximadoGanancia === null ) 
        {this.objUpdate.montoAproximadoGanancia = this.clientesPrincipal.montoAproximadoGanancia;}
      if (this.objUpdate.montoAproximadoGanancia.moneda === null ) 
        {this.objUpdate.montoAproximadoGanancia.moneda = this.clientesPrincipal.montoAproximadoGanancia.moneda;}
      if (this.objUpdate.localizacion === null ) 
        {this.objUpdate.localizacion = this.clientesPrincipal.localizacion;}
      if (this.objUpdate.localizacion.localizacionMexico === null ) 
        {this.objUpdate.localizacion.localizacionMexico = this.clientesPrincipal.localizacion.localizacionMexico;}
      if (this.objUpdate.localizacion.localizacionExtranjero === null ) 
        {this.objUpdate.localizacion.localizacionExtranjero = this.clientesPrincipal.localizacion.localizacionExtranjero;}

      if (this.objUpdate.clientePrincipal.personaMoral === null ) 
        {this.objUpdate.clientePrincipal.personaMoral = this.clientesPrincipal.clientePrincipal.personaMoral;}
      if (this.objUpdate.clientePrincipal.personaFisica === null ) 
        {this.objUpdate.clientePrincipal.personaFisica = this.clientesPrincipal.clientePrincipal.personaFisica;}
      
      
      this.clientesPrincipal=this.objUpdate;
      this.initLoad = true;

    }

    this.clientesR = JSON.parse(JSON.stringify(this.clientesPrincipal));

  }

  close() {
    this.modalService.dismissAll('clientesPrinc');
    $("body").removeClass("disabled-onepage-scroll");
  }

  guardaLocal() {
    //obtener datos del formulario
    let dato= this.clientesPrincipal;
    dato.verificar = true;
    if (!this.decLoaded.numeroDeclaracionPrecarga && dato.registroHistorico === true) {
      dato.registroHistorico = false;
    }
    //obtener array de sessionstorage
    let s = JSON.parse(sessionStorage.getItem('clientesPrincipales'));

    //se asigna a lista local
    this.listaClientes = s.clientes;

    const index = this.listaClientes.findIndex(({ idPosicionVista }) => idPosicionVista == dato.idPosicionVista);

    if(this.clientesPrincipal.clientePrincipal.tipoPersona == 'PERSONA_FISICA' ){
      this.clientesPrincipal.clientePrincipal.personaMoral = null
    } else if (this.clientesPrincipal.clientePrincipal.tipoPersona == 'PERSONA_MORAL' ){
      this.clientesPrincipal.clientePrincipal.personaFisica = null
    }

    if(this.clientesPrincipal.localizacion.ubicacion == 'EXTRANJERO'){
      this.clientesPrincipal.localizacion.localizacionMexico = null
    } else if (this.clientesPrincipal.localizacion.ubicacion == 'MEXICO'){
      this.clientesPrincipal.localizacion.localizacionExtranjero = null
    }

    if(this.clientesPrincipal.sector.id != 9999 ){
      this.clientesPrincipal.sectorOtro = null
    }


    if (index === -1) {
      //se agrega el nuevo registro al array local
      this.listaClientes.push(dato);
    } else {
      this.listaClientes[index] = dato;
    }

    this.listaClientes.forEach(
      (element, index) => {
        this.listaClientes[index].idPosicionVista = index + 1;
      });

    // // //se crea el json que se volvera a guardar en el sessionStorage
    let data = JSON.parse(sessionStorage.getItem('clientesPrincipales'));
    this.datosClientes = { realizaActividadLucrativa: data.realizaActividadLucrativa, clientes: this.listaClientes, aclaracionesObservaciones: s.aclaracionesObservaciones }

    // // //se agrega al sessionStorage
    sessionStorage.setItem('clientesPrincipales', JSON.stringify(this.datosClientes));

    this.activeModal.close({result: 'success', form: 'clientesPrinc'});
  }


  ngAfterViewInit(): void {
    
    console.log(this.clientesForm.valid);

    let formVal: boolean;
    const a = this.clientesForm.statusChanges.subscribe(() => {
      if (this.clientesForm.valid !== formVal) {
        this.TIPO_OPERACION = this.cata.getTipoOperacion(this.clientesForm.valid, this.clientesPrincipal.registroHistorico, this.clientesPrincipal.tipoOperacion, 'I');
        this.histFormInval = (this.clientesPrincipal.registroHistorico && !this.clientesForm.valid);
        if (!this.clientesForm.valid) {
          a.unsubscribe();
          setTimeout(() => {
            this.initLoad = false;
            this.resetBaja();
          }, 500);
          
        }
        formVal = this.clientesForm.valid;

      }
    });

    setTimeout(() => {
      this.initLoad = false;
      if (this.clientesForm.valid && this.cata.precargaOracle) {
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
      data.tipoOperacion.BAJA = this.clientesForm.controls[data.name] ? this.clientesForm.controls[data.name].valid : false;
      if (data.tipoOperacion.MODIFICAR === true) {
        data.tipoOperacion.MODIFICAR = this.clientesForm.controls[data.name] ? this.clientesForm.controls[data.name].valid : false;
      } 
     } 
    });
  }

  tipoOperacionChange(id) {
    if (id === 'SIN_CAMBIO' || id === 'BAJA') {
      this.clientesPrincipal = JSON.parse(JSON.stringify(this.clientesR));
      this.clientesPrincipal.tipoOperacion = id;
    }
  }

  getDisabled(obj, tipoOperacion1) {
    if (!obj.name || !tipoOperacion1 || this.initLoad) {
      return obj.isDisabled;
    }
    if (!this.clientesPrincipal.registroHistorico) {
      return false;
    } else {
      const newObj = this.dataDisabled.find  ((value) => value.name === obj.name); 
      return newObj.tipoOperacion[tipoOperacion1];
    }
  }
}
