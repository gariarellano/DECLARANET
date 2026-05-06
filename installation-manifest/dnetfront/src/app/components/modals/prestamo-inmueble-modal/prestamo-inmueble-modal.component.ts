import { Component, OnInit, Input, HostListener, ViewChild, AfterViewInit } from '@angular/core';
import * as $ from 'jquery';
import { NgbModal, NgbModalConfig, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormControl, FormGroup, Validators,NgForm } from '@angular/forms';
import { Globals } from 'src/app/globals';

@Component({
  selector: 'app-prestamo-inmueble-modal',
  templateUrl: './prestamo-inmueble-modal.component.html',
  styleUrls: ['./prestamo-inmueble-modal.component.scss']
})
export class PrestamoInmuebleModalComponent implements OnInit,AfterViewInit {

  form: FormGroup;
  help = false;
  inversion: any;
  inmueble: any;
  inmuebles: Array<any> = [];
  lista: any;
  @Input() public objUpdate;
  filterMuns: any;
  bienTmp: any;
  inmuebleR:any;
  dataDisabled: any;
  histFormInval: boolean;
  TIPO_OPERACION: any;
  @ViewChild('pInmuebleForm') pInmuebleForm: NgForm;
  initLoad: boolean;
  
  constructor(private modalService: NgbModal, private activeModal: NgbActiveModal, public cata: Globals, public globals: Globals) {
    this.dataDisabled = [
      {
        name: "inmuebleD",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "especificInmueble",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "ubicacionInmueble",
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
        name: "calleMexi",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "calleMexi",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "numExMex",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "numIntMex",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "numIntMex",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "colMex",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "colMex",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "entidadMex",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "municiMex",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "cpMex",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "calleExt",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "numExExt",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "numIntExt",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "ciudadExt",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "estadoExt",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "paisExtPinmueble",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "cpExt",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
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
        name: "rfcMoralInmu",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "relacionDuenoInmu",
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
        name: "rfcFisicaInm",
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

    this.inmueble = {
      id: null,
      idPosicionVista: null,
      verificar: null,
      registroHistorico: false,
      tipoOperacion: null,
      tipoBien: null,
      relacionConTitular: null,
      inmueble: {
        tipoInmueble: {
          id: null,
          valor: null
        },
        tipoInmuebleOtro: null,
        
        domicilio: {
          ubicacion: null, 
          domicilioMexico: { 
            calle: null,
            codigoPostal: null,
            coloniaLocalidad: null,
            entidadFederativa: {
              id: null,
              valor: null
            },
            municipioAlcaldia: {
              fk: null,
              id: null,
              valor: null
            },
            numeroExterior: null,
            numeroInterior: null
          },

          domicilioExtranjero: { 
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
        }
      },
      vehiculo: {
        tipoVehiculo: {
          id: null,
          valor: null
        },
        tipoVehiculoOtro: null,
        marca: null,
        modelo: null,
        anio: null,
        numeroSerieRegistro: null,
        lugarRegistro: {
          ubicacion: null,
          localizacionMexico: {
            entidadFederativa: {
              id: null,
              valor: null
            }
          },
          localizacionExtranjero: { 
            pais: {
              id: null,
              valor: null
            }
          }
        }
      },
      duenoTitular: {
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
        },
        id: null,
        idPosicionVista: null,
        verificar: false
      }

    }


    if (this.objUpdate) {
      console.log("data obkj", this.objUpdate);
      if(this.objUpdate.inmueble.domicilio.domicilioExtranjero === null){this.objUpdate.inmueble.domicilio.domicilioExtranjero = this.inmueble.inmueble.domicilio.domicilioExtranjero}
      if(this.objUpdate.inmueble.domicilio.domicilioMexico === null){this.objUpdate.inmueble.domicilio.domicilioMexico = this.inmueble.inmueble.domicilio.domicilioMexico}
      if (this.objUpdate.inmueble.domicilio.domicilioMexico.entidadFederativa) 
        {this.filterMunicipios(this.objUpdate.inmueble.domicilio.domicilioMexico.entidadFederativa.id, false);}
      this.inmueble = this.objUpdate;
      if(this.inmueble.duenoTitular.personaMoral === null){this.inmueble.duenoTitular.personaMoral =  { nombre: '', rfc: '' }}
      if(this.inmueble.duenoTitular.personaFisica === null){this.inmueble.duenoTitular.personaFisica = { nombre: '', primerApellido: '', segundoApellido: '', rfc: '' } }
      this.initLoad = true;
    }

    this.bienTmp = {
      domicilio: {
        domicilioMexico: JSON.parse(JSON.stringify(this.inmueble.inmueble.domicilio.domicilioMexico)),
        domicilioExtranjero: JSON.parse(JSON.stringify(this.inmueble.inmueble.domicilio.domicilioExtranjero))
      }
    }
    this.inmuebleR = JSON.parse(JSON.stringify(this.inmueble));

  }
  
  ngAfterViewInit(): void {
    
    console.log(this.pInmuebleForm.valid);

    let formVal: boolean;
    const a = this.pInmuebleForm.statusChanges.subscribe(() => {
      if (this.pInmuebleForm.valid !== formVal) {
        this.TIPO_OPERACION = this.globals.getTipoOperacion(this.pInmuebleForm.valid, this.inmueble.registroHistorico, this.inmueble.tipoOperacion, 'P');
        this.histFormInval = (this.inmueble.registroHistorico && !this.pInmuebleForm.valid);
        if (!this.pInmuebleForm.valid) {
          a.unsubscribe();
          setTimeout(() => {
            this.initLoad = false;
            this.resetBaja();
          }, 500);
          
        }
        formVal = this.pInmuebleForm.valid;

      }
    });

    setTimeout(() => {
      this.initLoad = false;
      a.unsubscribe();
    }, 500);
   
  }

  resetBaja() {
    this.dataDisabled.forEach((data) => {
      if (this.globals.precargaOracle) {
        data.tipoOperacion.BAJA = false;
        data.tipoOperacion.MODIFICAR = false;
      } else {
     data.tipoOperacion.BAJA = this.pInmuebleForm.controls[data.name] ? this.pInmuebleForm.controls[data.name].valid : false;

      if (data.tipoOperacion.MODIFICAR === true) {
        data.tipoOperacion.MODIFICAR = this.pInmuebleForm.controls[data.name] ? this.pInmuebleForm.controls[data.name].valid : false;
      }
     }
     });
  }

  tipoOperacionChange(id) {
    if (id === 'SIN_CAMBIO' || id === 'BAJA') {
      this.inmueble = JSON.parse(JSON.stringify(this.inmuebleR));
      this.inmueble.tipoOperacion = id;
    }
  }

  getDisabled(obj, tipoOperacion1) {
    if (!obj.name || !tipoOperacion1 || this.initLoad) {
      return obj.isDisabled;
    }
    if (!this.inmueble.registroHistorico) {
      return false;
    } else {
      const newObj = this.dataDisabled.find  ((value) => value.name === obj.name);
      const newObj1 = newObj.tipoOperacion[tipoOperacion1];
      return newObj1;
    }
  }

  guardaLocal() {

    let dato = this.inmueble;
    dato.verificar = true;
    dato.tipoBien = "INMUEBLE";
    dato.vehiculo = null;
    if(dato.inmueble.domicilio.ubicacion === "MEXICO"){dato.inmueble.domicilio.domicilioExtranjero = null}
    if(dato.inmueble.domicilio.ubicacion === "EXTRANJERO"){dato.inmueble.domicilio.domicilioMexico = null}
    if(dato.duenoTitular.tipoPersona === "PERSONA_MORAL"){dato.duenoTitular.personaFisica = null}
    if(dato.duenoTitular.tipoPersona === "PERSONA_FISICA"){dato.duenoTitular.personaMoral = null}
    if(this.inmueble.inmueble.tipoInmueble.id != 9999){this.inmueble.inmueble.tipoInmuebleOtro = ''}
    if (dato.inmueble.tipoInmueble.id !== 9999) {
      dato.inmueble.tipoInmuebleOtro = null;
    }
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

  abreMexico() {
    this.bienTmp.domicilio.domicilioMexico = JSON.parse(JSON.stringify(this.inmueble.inmueble.domicilio.domicilioMexico));
    if (this.bienTmp.domicilio.ubicacion == 'MEXICO') {
      this.filterMunicipios(this.bienTmp.domicilio.domicilioMexico.entidadFederativa.id, false);
    }
  }

  abreExtranjero() {
    this.bienTmp.domicilio.domicilioExtranjero = JSON.parse(JSON.stringify(this.inmueble.inmueble.domicilio.domicilioExtranjero));
    this.bienTmp.domicilio.ubicacion = 'EXTRANJERO';
  }

  closeUbicacion() {
    this.bienTmp.domicilio.domicilioMexico = JSON.parse(JSON.stringify(this.inmueble.inmueble.domicilio.domicilioMexico));
    this.bienTmp.domicilio.domicilioExtranjero = JSON.parse(JSON.stringify(this.inmueble.inmueble.domicilio.domicilioExtranjero));
  }

  guardaSub(v: string) {

    switch (v) {
      case 'domicilioMexico':
        this.inmueble.inmueble.domicilio.domicilioMexico = JSON.parse(JSON.stringify(this.bienTmp.domicilio.domicilioMexico));
        break;
      case 'domicilioExtranjero':
        this.inmueble.inmueble.domicilio.domicilioExtranjero = JSON.parse(JSON.stringify(this.bienTmp.domicilio.domicilioExtranjero));
        break;
    }
  }
  close() {
    this.modalService.dismissAll('bienInmueble');
    $("body").removeClass("disabled-onepage-scroll");
  }

  filterMunicipios(id, clear = true) {
    console.log("el id es: ", id);
    this.filterMuns = this.globals.catalogoMun.filter(item => item.fk === Number(id));
    if (clear) {
      this.bienTmp.domicilio.domicilioMexico.municipioAlcaldia = null;
    }


  }


}

