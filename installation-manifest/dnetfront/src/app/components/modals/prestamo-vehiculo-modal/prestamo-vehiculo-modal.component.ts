import { Component, OnInit, Input, HostListener, ViewChild, AfterViewInit } from '@angular/core';
import * as $ from 'jquery';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup,NgForm } from '@angular/forms';
import { Globals } from 'src/app/globals';

@Component({
  selector: 'app-prestamo-vehiculo-modal',
  templateUrl: './prestamo-vehiculo-modal.component.html',
  styleUrls: ['./prestamo-vehiculo-modal.component.scss']
})
export class PrestamoVehiculoModalComponent implements OnInit,AfterViewInit {

  form: FormGroup;
  help = false;
  vehiculo: any;
  inmuebles: Array<any> = [];
  lista: any;
  vehiculoR:any;
  initLoad: boolean;
  @Input() public objUpdate;
  filterMuns: any;
  dataDisabled: any;
  histFormInval: boolean;
  TIPO_OPERACION: any;
  @ViewChild('prestaVehiForm') prestaVehiForm: NgForm;

  constructor(private modalService: NgbModal, private activeModal: NgbActiveModal, public cata: Globals) {
    this.dataDisabled = [
      {
        name: "tipoVehiculo",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "especificTipoVehiculo",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "marca",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "modelo",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "anio",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "numSerie",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "registrado",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "entidadFederativa",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "pais",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "duenoTitular",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "nombreMoral",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "rfcMoralVehi",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "relacionDuenoVehi",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "especificRelacionVehiculo",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "nombreFisica",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "pApellidoFisica",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "sApellidoFisica",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "rfcFisicaVehi",
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
    this.vehiculo = {
      id: null,
      idPosicionVista: "",
      verificar: null,
      registroHistorico: false,
      tipoOperacion: null,
      tipoBien: "",
      relacionConTitular: '',
      inmueble: {
        tipoInmueble: {
          id: null,
          valor: ""
        },
        tipoInmuebleOtro: '',
        domicilio: {
          ubicacion: null, 
          domicilioMexico: { 
            calle: "",
            codigoPostal: "",
            coloniaLocalidad: "",
            entidadFederativa: {
              id: null,
              valor: ""
            },
            municipioAlcaldia: {
              fk: null,
              id: null,
              valor: ""
            },
            numeroExterior: "",
            numeroInterior: ""
          },

          domicilioExtranjero: { 
            calle: "",
            ciudadLocalidad: "",
            codigoPostal: "",
            estadoProvincia: "",
            numeroExterior: "",
            numeroInterior: "",
            pais: {
              id: null,
              valor: ""
            }
          }
        }
      },
      vehiculo: {
        tipoVehiculo: {
          id: null,
          valor: ""
        },
        tipoVehiculoOtro: '',
        marca: "",
        modelo: "",
        anio: "",
        numeroSerieRegistro: "",
        lugarRegistro: {
          ubicacion: null,
          localizacionMexico: { 
            entidadFederativa: {
              id: null,
              valor: ""
            }
          },

          localizacionExtranjero: { 
            pais: {
              id: null,
              valor: ""
            }
          }
        }
      },
      duenoTitular: {
        tipoPersona: null, 
        personaFisica: {
          nombre: "",
          primerApellido: "",
          segundoApellido: "",
          rfc: null
        },
        personaMoral: {
          nombre: "",
          rfc: null
        },
        id: null,
        idPosicionVista: "",
        verificar: false
      }

    }

    if (this.objUpdate) {
      this.objUpdate.inmueble = this.vehiculo.inmueble;
      this.vehiculo = this.objUpdate;
      if(this.vehiculo.vehiculo.lugarRegistro.localizacionMexico === null){this.vehiculo.vehiculo.lugarRegistro.localizacionMexico = { entidadFederativa: { id: null, valor: '' } }}
      if(this.vehiculo.vehiculo.lugarRegistro.localizacionExtranjero === null){this.vehiculo.vehiculo.lugarRegistro.localizacionExtranjero = { pais: { id: null, valor: '' } }}
      if(this.vehiculo.duenoTitular.personaFisica === null){this.vehiculo.duenoTitular.personaFisica = { nombre: '', primerApellido: '', segundoApellido: '', rfc: '' }}
      if(this.vehiculo.duenoTitular.personaMoral === null){this.vehiculo.duenoTitular.personaMoral = { nombre: '', rfc: '' }}
      this.initLoad = true;

    }
    this.vehiculoR = JSON.parse(JSON.stringify(this.vehiculo));

  }

  ngAfterViewInit(): void {
    
    console.log(this.prestaVehiForm.valid);

    let formVal: boolean;
    const a = this.prestaVehiForm.statusChanges.subscribe(() => {
      if (this.prestaVehiForm.valid !== formVal) {
        this.TIPO_OPERACION = this.cata.getTipoOperacion(this.prestaVehiForm.valid, this.vehiculo.registroHistorico, this.vehiculo.tipoOperacion, 'P');
        this.histFormInval = (this.vehiculo.registroHistorico && !this.prestaVehiForm.valid);
        if (!this.prestaVehiForm.valid) {
          a.unsubscribe();
          setTimeout(() => {
            this.initLoad = false;
            this.resetBaja();
          }, 500);
          
        }
        formVal = this.prestaVehiForm.valid;

      }
    });

    setTimeout(() => {
      this.initLoad = false;
      a.unsubscribe();
    }, 500);
   
  }

  resetBaja() {
    this.dataDisabled.forEach((data) => {
      if (this.cata.precargaOracle) {
        data.tipoOperacion.BAJA = false;
        data.tipoOperacion.MODIFICAR = false;
      } else {
      data.tipoOperacion.BAJA = this.prestaVehiForm.controls[data.name] ? this.prestaVehiForm.controls[data.name].valid : false;

    if (data.tipoOperacion.MODIFICAR === true) {
      data.tipoOperacion.MODIFICAR = this.prestaVehiForm.controls[data.name] ? this.prestaVehiForm.controls[data.name].valid : false;
    }
  }
  });
  }

  tipoOperacionChange(id) {
    if (id === 'SIN_CAMBIO' || id === 'BAJA') {
      this.vehiculo = JSON.parse(JSON.stringify(this.vehiculoR));
      this.vehiculo.tipoOperacion = id;
    }
  }

  getDisabled(obj, tipoOperacion1) {
    if (!obj.name || !tipoOperacion1 || this.initLoad) {
      return obj.isDisabled;
    }
    if (!this.vehiculo.registroHistorico) {
      return false;
    } else {
      const newObj = this.dataDisabled.find  ((value) => value.name === obj.name);
      const newObj1 = newObj.tipoOperacion[tipoOperacion1];
      return newObj1;
    }
  }

  guardaLocal() {
    const dato = this.vehiculo;
    dato.verificar = true;
    dato.inmueble = null;
    dato.tipoBien = "VEHICULO";
    dato.inmueble = null;
    if (dato.vehiculo.tipoVehiculo.id !== 9999) {
      dato.vehiculo.tipoVehiculoOtro = null;
    }

    if(dato.vehiculo.lugarRegistro.ubicacion === "MEXICO"){dato.vehiculo.lugarRegistro.localizacionExtranjero = null}
    if(dato.vehiculo.lugarRegistro.ubicacion === "EXTRANJERO"){dato.vehiculo.lugarRegistro.localizacionMexico = null}
    if( dato.duenoTitular.tipoPersona === "PERSONA_MORAL"){dato.duenoTitular.personaFisica = null}
    if(dato.duenoTitular.tipoPersona === "PERSONA_FISICA"){dato.duenoTitular.personaMoral = null}
    if(this.vehiculo.vehiculo.tipoVehiculo.id != 9999){ this.vehiculo.vehiculo.tipoVehiculoOtro = ''}
    let s = JSON.parse(sessionStorage.getItem('prestamoComodato'));

    this.lista = s.prestamo;
    const index = this.lista.findIndex(({ idPosicionVista }) => idPosicionVista == dato.idPosicionVista);
    if (index === -1) {
      this.lista.push(dato);
    } else {
      this.lista[index] = dato;
    }

    this.lista.forEach(
      (element, index) => {
        this.lista[index].idPosicionVista = index + 1;
      });

    let bien = { ninguno: false, prestamo: this.lista, aclaracionesObservaciones: s.aclaracionesObservaciones }
    sessionStorage.setItem('prestamoComodato', JSON.stringify(bien));
    this.activeModal.close({ result: 'success', form: 'bienInmueble' });

  }

  close() {
    this.modalService.dismissAll('bienInmueble');
    $("body").removeClass("disabled-onepage-scroll");
  }
  
}

