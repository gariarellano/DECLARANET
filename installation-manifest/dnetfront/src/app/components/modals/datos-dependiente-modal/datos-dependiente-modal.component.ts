import { Component, OnInit, Input, HostListener, ViewChild, AfterViewInit } from '@angular/core';
import * as $ from 'jquery';
import { FormGroup, NgForm } from '@angular/forms';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Globals } from 'src/app/globals';

@Component({
  selector: 'app-datos-dependiente-modal',
  templateUrl: './datos-dependiente-modal.component.html',
  styleUrls: ['./datos-dependiente-modal.component.scss']
})
export class DatosDependienteModalComponent implements OnInit, AfterViewInit {

  form: FormGroup;
  public help = false;
  datosDependiente: any;
  lista: any;
  @Input() public objUpdate;
  filterMuns: any;
  datosDependienteTmp: any;
  dataDisabled: any;
  fecha: any;
  initLoad: boolean;
  dependienteR: any;
  histFormInval: boolean;
  TIPO_OPERACION: any;
  @ViewChild('dependienteForm') dependienteForm: NgForm;

  constructor(private readonly modalService: NgbModal, private readonly activeModal: NgbActiveModal, public cata: Globals, public globals: Globals) {
    this.dataDisabled = [
      {
        name: 'nombre',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'primerApellido',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'segundoApellido',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'fechaNacimiento',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'rfcDependiente',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'parentescoRelacion',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'especificParent',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: true,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'esExtranjero',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'curp',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'habitaDomicilioDeclarante',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'lugarDondeReside',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'ningunoActividad',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'calleMex',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'numExMex',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'numInMex',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'colMex',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'entidadFederativa',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'municipioMex',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'cp',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'calleExt',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'numExExt',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'numIntExt',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'ciudadLocalidad',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'estadoProvincia',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'pais',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'cpExt',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'actividadLaboral',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'especificParent',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'nivelOrdenGobierno',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'ambitoPublico',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'nombreEntePublico',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'areaAdscripcion',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'empleoCargoComision',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'funcionPrincipal',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'salarioMensualNeto',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'fechaIngreso',
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
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'rfcEmpresa',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'empleoCargo',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'area',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'esProveedor',
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
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: 'especificSector',
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
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
    this.datosDependiente = {
      "id": null,
      "verificar": null,
      "idPosicionVista": 0,
      "registroHistorico": false,
      "ninguno": false,
      "tipoOperacion": null, //AGREGAR MODIFICAR SIN CAMBIO (BAJA="BORRADO LOGICO")
      "datosPersonales": {
        //@persona
        "nombre": "",
        "primerApellido": "",
        "segundoApellido": "",
        "rfc": "", //solo obligatorio para el declaranete
        "fechaNacimiento": ""
      },
      "parentescoRelacion": {
        "id": null,
        "valor": ""
      },
      "parentescoRelacionOtro": "",
      "ciudadanoExtranjero": {
        "esExtranjero": null, //SI =true, NO= false
        "curp": "" //vendra solo si es false
      },
      // "esDependienteEconomico": null,
      "habitaDomicilioDeclarante": null,
      "domicilioDependienteEconomico": {
        "lugarDondeReside": "",
        "domicilio": { //solo si es false el habitaMismoDomDeclarante
          //@ domicilio
          "ubicacion": "", //México, Extranjero
          "domicilioMexico": { //uno u otro
            "calle": null,
            "codigoPostal": null,
            "coloniaLocalidad": null,
            "entidadFederativa": {
              "id": null,
              "valor": "",
            },
            "municipioAlcaldia": {
              "fk": 0,
              "id": null,
              "valor": ""
            },
            "numeroExterior": null,
            "numeroInterior": null
          },
          "domicilioExtranjero": { //uno u otro
            "calle": null,
            "ciudadLocalidad": null,
            "codigoPostal": null,
            "estadoProvincia": null,
            "numeroExterior": null,
            "numeroInterior": null,
            "pais": {
              "id": null,
              "valor": null
            }
          }
        }
      },
      "actividadLaboral": {

        //@actividadLaboral
        "ambitoSector": {
          "id": null,
          "valor": ""
        },
        "ambitoSectorOtro": "",
        "sectorPublico": {
          "nivelOrdenGobierno": null,
          "ambitoPublico": null,
          "nombreEntePublico": "",
          "areaAdscripcion": "",
          "empleoCargoComision": "",
          "funcionPrincipal": ""
        },
        "sectorPrivadoOtro": {
          "nombreEmpresaSociedadAsociacion": "",
          "rfc": "",
          "area": "",
          "empleoCargo": "",
          "sectorOtro": "",
          "sector": {
            "id": null,
            "valor": ""
          },
          "proveedorContratistaGobierno": null
        },
        "fechaIngreso": "",
        "salarioMensualNeto": null
      }


    }

    if (this.objUpdate) {
      this.objUpdate.actividadLaboral = this.objUpdate.actividadLaboral === null ? this.datosDependiente.actividadLaboral : this.objUpdate.actividadLaboral;
      if (this.objUpdate.actividadLaboral.ambitoSector === null) { this.objUpdate.actividadLaboral.ambitoSector = this.datosDependiente.actividadLaboral.ambitoSector; }
      if (this.objUpdate.actividadLaboral.sectorPrivadoOtro === null) { this.objUpdate.actividadLaboral.sectorPrivadoOtro = this.datosDependiente.actividadLaboral.sectorPrivadoOtro; }
      if (this.objUpdate.domicilioDependienteEconomico === null) { this.objUpdate.domicilioDependienteEconomico = this.datosDependiente.domicilioDependienteEconomico }
      if (this.objUpdate.domicilioDependienteEconomico.domicilio === null) { this.objUpdate.domicilioDependienteEconomico.domicilio = this.datosDependiente.domicilioDependienteEconomico.domicilio }
      if (this.objUpdate.domicilioDependienteEconomico.domicilio.domicilioMexico === null) { this.objUpdate.domicilioDependienteEconomico.domicilio.domicilioMexico = this.datosDependiente.domicilioDependienteEconomico.domicilio.domicilioMexico }
      if (this.objUpdate.domicilioDependienteEconomico.domicilio.domicilioExtranjero === null) { this.objUpdate.domicilioDependienteEconomico.domicilio.domicilioExtranjero = this.datosDependiente.domicilioDependienteEconomico.domicilio.domicilioExtranjero }
      if (this.objUpdate.domicilioDependienteEconomico.domicilio.domicilioMexico.entidadFederativa === null) { this.objUpdate.domicilioDependienteEconomico.domicilio.domicilioMexico.entidadFederativa = this.datosDependiente.domicilioDependienteEconomico.domicilio.domicilioMexico.entidadFederativa; }
      if (this.objUpdate.domicilioDependienteEconomico.domicilio.domicilioMexico.municipioAlcaldia === null) { this.objUpdate.domicilioDependienteEconomico.domicilio.domicilioMexico.municipioAlcaldia = this.datosDependiente.domicilioDependienteEconomico.domicilio.domicilioMexico.municipioAlcaldia; }
      if (this.objUpdate.domicilioDependienteEconomico.domicilio.domicilioExtranjero.pais === null) { this.objUpdate.domicilioDependienteEconomico.domicilio.domicilioExtranjero.pais = this.datosDependiente.domicilioDependienteEconomico.domicilio.domicilioExtranjero.pais; }
      if (this.objUpdate.actividadLaboral.sectorPrivadoOtro.sector == null) { this.objUpdate.actividadLaboral.sectorPrivadoOtro.sector = this.datosDependiente.actividadLaboral.sectorPrivadoOtro.sector; }
      if (this.objUpdate.parentescoRelacion === null) { this.objUpdate.parentescoRelacion = this.datosDependiente.parentescoRelacion; }
      if (this.objUpdate.actividadLaboral === null) { this.objUpdate.actividadLaboral = this.datosDependiente.actividadLaboral; }


      this.objUpdate.actividadLaboral.sectorPublico = this.objUpdate.actividadLaboral.sectorPublico === null ? this.datosDependiente.actividadLaboral.sectorPublico : this.objUpdate.actividadLaboral.sectorPublico;
      this.objUpdate.actividadLaboral.sectorPrivadoOtro = this.objUpdate.actividadLaboral.sectorPrivadoOtro === null ? this.datosDependiente.actividadLaboral.sectorPrivadoOtro : this.objUpdate.actividadLaboral.sectorPrivadoOtro;

      this.datosDependiente = this.objUpdate;
      this.initLoad = true;

    }

    this.datosDependienteTmp = {
      domicilioDependienteEconomico: {
        domicilio: {
          domicilioMexico: JSON.parse(JSON.stringify(this.datosDependiente.domicilioDependienteEconomico.domicilio.domicilioMexico)),
          domicilioExtranjero: JSON.parse(JSON.stringify(this.datosDependiente.domicilioDependienteEconomico.domicilio.domicilioExtranjero))

        }
      },
      actividadLaboral: JSON.parse(JSON.stringify(this.datosDependiente.actividadLaboral))
    };

    this.dependienteR = JSON.parse(JSON.stringify(this.datosDependiente));


  }


  limpiaParentesco() {
    this.datosDependiente.parentescoRelacionOtro = null;
  }

  limpiaSector() {
    this.datosDependienteTmp.actividadLaboral.sectorPrivadoOtro.sectorOtro = null;
  }

  limpiaDatosActividad() {
    this.datosDependienteTmp.actividadLaboral = {
      ambitoSector: this.datosDependienteTmp.actividadLaboral.ambitoSector,
      ambitoSectorOtro: '',
      sectorPublico: {
        nivelOrdenGobierno: null,
        ambitoPublico: null,
        nombreEntePublico: '',
        areaAdscripcion: '',
        empleoCargoComision: '',
        funcionPrincipal: ''
      },
      sectorPrivadoOtro: {
        nombreEmpresaSociedadAsociacion: '',
        rfc: '',
        area: '',
        empleoCargo: '',
        sector: {
          id: null,
          valor: ''
        },
        sectorOtro: '',
        proveedorContratistaGobierno: null


      },
      fechaIngreso: '',
      salarioMensualNeto: 0
    };
  }
  close() {
    this.modalService.dismissAll("dependiente");
    $("body").removeClass("disabled-onepage-scroll");
  }

  guardaLocal() {

    console.log("datos dependiente ", this.datosDependiente);


    let dato = this.datosDependiente;
    dato.verificar = true;

    dato.actividadLaboral.sectorPublico = dato.actividadLaboral.ambitoSector.id > 1 ? null : dato.actividadLaboral.sectorPublico;
    dato.actividadLaboral.sectorPrivadoOtro = dato.actividadLaboral.ambitoSector.id == 1 ? null : dato.actividadLaboral.sectorPrivadoOtro;
    dato.actividadLaboral = dato.ninguno == true ? null : dato.actividadLaboral;
    dato.domicilioDependienteEconomico.domicilio.ubicacion = dato.domicilioDependienteEconomico.lugarDondeReside;
    dato.domicilioDependienteEconomico.domicilio.domicilioMexico = dato.domicilioDependienteEconomico.domicilio.ubicacion == "EXTRANJERO" ? null : dato.domicilioDependienteEconomico.domicilio.domicilioMexico;
    dato.domicilioDependienteEconomico.domicilio.domicilioExtranjero = dato.domicilioDependienteEconomico.domicilio.ubicacion == "MEXICO" ? null : dato.domicilioDependienteEconomico.domicilio.domicilioExtranjero;
    dato.domicilioDependienteEconomico.domicilio = dato.domicilioDependienteEconomico.domicilio.ubicacion == "SE_DESCONOCE" ? null : dato.domicilioDependienteEconomico.domicilio;
    dato.domicilioDependienteEconomico = dato.habitaDomicilioDeclarante == true ? null : dato.domicilioDependienteEconomico;

    if (dato.ciudadanoExtranjero.esExtranjero == true) { dato.ciudadanoExtranjero.curp = null; }
    // obtener array de sessionstorage
    let s = JSON.parse(sessionStorage.getItem('datosDependiente'));

    // se asigna a lista local
    this.lista = s.datosDependientesEconomicos;
    const index = this.lista.findIndex(({ idPosicionVista }) => idPosicionVista == dato.idPosicionVista);

    if (index === -1) {
      //se agrega el nuevo registro al array local
      this.lista.push(dato);
    } else {
      this.lista[index] = dato;
    }

    // this.lista.push(empleoCargo);

    this.lista.forEach(
      (element, index) => {
        this.lista[index].idPosicionVista = index + 1;
      });

    // se crea el json que se volvera a guardar en el sessionStorage
    let bien = { ninguno: false, datosDependientesEconomicos: this.lista, aclaracionesObservaciones: "" }

    // se agrega al sessionStorage
    sessionStorage.setItem('datosDependiente', JSON.stringify(bien));

    this.activeModal.close({ result: 'success', form: 'dependiente' });

  }

  filterMunicipios(id, clear = true) {
    this.filterMuns = this.globals.catalogoMun.filter(item => item.fk === Number(id));
    if (clear) {
      this.datosDependienteTmp.domicilioDependienteEconomico.domicilio.domicilioMexico.municipioAlcaldia.id = null;
    }
  }

  abrirModalLugar(lugar: string) {
    switch (lugar) {
      case 'mexico':
        this.datosDependienteTmp.domicilioDependienteEconomico.domicilio.domicilioMexico = JSON.parse(JSON.stringify(this.datosDependiente.domicilioDependienteEconomico.domicilio.domicilioMexico));
        this.filterMunicipios(this.datosDependienteTmp.domicilioDependienteEconomico.domicilio.domicilioMexico.entidadFederativa.id, false);
        break;
      case 'extranjero':
        this.datosDependienteTmp.domicilioDependienteEconomico.domicilio.domicilioExtranjero = JSON.parse(JSON.stringify(this.datosDependiente.domicilioDependienteEconomico.domicilio.domicilioExtranjero));
        break;
      case 'actividad':
        this.datosDependienteTmp.actividadLaboral = JSON.parse(JSON.stringify(this.datosDependiente.actividadLaboral));
        break;
    }
  }

  cerrarModalLugar(lugar: string) {

    $(`#modal-lugar-${lugar}`).fadeOut('slow', () => {
      $(this).css('display', 'none');
    });

    $('.menu-nav').css({ 'display': 'flex' });
    $('#content-btnSave').css({ 'display': 'flex' });
    $('body').removeClass('disabled-onepage-scroll');
  }

  guardarModalLugar(lugar: string) {
    switch (lugar) {
      case 'mexico':
        this.datosDependiente.domicilioDependienteEconomico.domicilio.domicilioMexico = JSON.parse(JSON.stringify(this.datosDependienteTmp.domicilioDependienteEconomico.domicilio.domicilioMexico));
        break;
      case 'extranjero':
        this.datosDependiente.domicilioDependienteEconomico.domicilio.domicilioExtranjero = JSON.parse(JSON.stringify(this.datosDependienteTmp.domicilioDependienteEconomico.domicilio.domicilioExtranjero));
        break;
      case 'actividad':
        this.datosDependiente.actividadLaboral = JSON.parse(JSON.stringify(this.datosDependienteTmp.actividadLaboral));
        break;
    }
    this.cerrarModalLugar(lugar);
  }

  ngAfterViewInit(): void {

    console.log(this.dependienteForm.valid);

    let formVal: boolean;
    const a = this.dependienteForm.statusChanges.subscribe(() => {
      if (this.dependienteForm.valid !== formVal) {
        this.TIPO_OPERACION = this.cata.getTipoOperacion(this.dependienteForm.valid, this.datosDependiente.registroHistorico, this.datosDependiente.tipoOperacion, 'G');
        this.histFormInval = (this.datosDependiente.registroHistorico && !this.dependienteForm.valid);
        if (!this.dependienteForm.valid) {
          a.unsubscribe();
          setTimeout(() => {
            this.initLoad = false;
            this.resetBaja();
          }, 500);

        }
        formVal = this.dependienteForm.valid;

      }
    });

    setTimeout(() => {
      this.initLoad = false;
      if (this.dependienteForm.valid && this.cata.precargaOracle) {
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
        data.tipoOperacion.BAJA = this.dependienteForm.controls[data.name] ? this.dependienteForm.controls[data.name].valid : false;
        if (data.tipoOperacion.MODIFICAR === true) {
          data.tipoOperacion.MODIFICAR = this.dependienteForm.controls[data.name] ? this.dependienteForm.controls[data.name].valid : false;
        }
      }
    });
  }

  tipoOperacionChange(id) {
    if (id === 'SIN_CAMBIO' || id === 'BAJA') {
      this.datosDependiente = JSON.parse(JSON.stringify(this.dependienteR));
      this.datosDependiente.tipoOperacion = id;
    }
  }

  getDisabled(obj, tipoOperacion1) {
    if (!obj.name || !tipoOperacion1 || this.initLoad) {
      return obj.isDisabled;
    }
    if (!this.datosDependiente.registroHistorico) {
      return false;
    } else {
      const newObj = this.dataDisabled.find((value) => value.name === obj.name);
      return newObj.tipoOperacion[tipoOperacion1];
    }
  }

}
