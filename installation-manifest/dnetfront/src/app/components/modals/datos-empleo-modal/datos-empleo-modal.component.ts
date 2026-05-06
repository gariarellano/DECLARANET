import { Component, OnInit, Input, HostListener, AfterViewInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators, NgForm } from '@angular/forms';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import * as $ from 'jquery';
import { Globals } from 'src/app/globals';
import { CatalogoService } from './../../../services/catalogo.service';
import { Session } from 'inspector';
import { validaFechaLimit } from 'src/app/validators/validadorFormControl';

@Component({
  selector: 'app-datos-empleo-modal',
  templateUrl: './datos-empleo-modal.component.html',
  styleUrls: ['./datos-empleo-modal.component.scss']
})
export class DatosEmpleoModalComponent implements OnInit, AfterViewInit {

  form: FormGroup;
  help = false;
  lista: any;
  datoEmpleo: any;
  @Input() public objUpdate;
  filterMuns: any;
  initLoad: any;
  ini: any;
  labelRemu: string = '';
  textSubTitleEmpleo: string;
  domicilio: any;
  empleoR;
  dataDisabled: any;
  histFormInval: boolean;
  TIPO_OPERACION: any;
  @ViewChild('datosEmpleoForm') datosEmpleoForm: NgForm;

  constructor(private readonly modalService: NgbModal, private readonly activeModal: NgbActiveModal,
    public globals: Globals, public cata: Globals, private readonly cataMuni: CatalogoService) {
    this.dataDisabled = [
      {
        name: "nombreEnte",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "nivelJerarquico",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "areaAdscripcion",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "empleoCargoComision",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "contratado",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "nivelEmpleoCargoComision",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "fechaEncargo",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "telefonoOficina",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "extension",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "ubicacion",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "remuneracionNeta",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "moneda",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "remuneracionNeta",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "remuneracionNeta",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "remuneracionNeta",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "remuneracionNeta",
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

    this.initLoad = JSON.parse(sessionStorage.getItem('declaracionLoaded'));
    this.ini = this.initLoad.encabezado.tipoDeclaracion;
    if (this.ini === 'CONCLUSION') {
      this.textSubTitleEmpleo = 'Fecha de conclusión del empleo, cargo o comisión';
    } else {
      this.textSubTitleEmpleo = 'Fecha de toma de posesión del empleo, cargo o comisión'
    }
    this.lista = [];

    this.form = new FormGroup({
      aclaracionesObservaciones: new FormControl(''),
      empleoCargoComision: new FormGroup({
        tipoOperacion: new FormControl(null),
        idMovimiento: new FormControl(null),
        id: new FormControl(null),
        enteTemp: new FormControl(null, [Validators.required]),
        areaAdscripcion: new FormControl(null, [Validators.required]),
        empleoCargoComision: new FormControl('', [Validators.required]),
        nivelJerarquico: new FormGroup({
          id: new FormControl(null, [Validators.required]),
          valor: new FormControl(''),
          valorUno: new FormControl(''),
          fk: new FormControl(0),
        }),
        nivelEmpleoCargoComision: new FormControl('', [Validators.required]),
        contratadoPorHonorarios: new FormControl('', [Validators.required]),
        remuneracionNeta: new FormGroup({
          moneda: new FormGroup({
            id: new FormControl(null, [Validators.required]),
            valor: new FormControl('')
          }),
          monto: new FormControl('', [Validators.required])
        }),
        tipoRemuneracion: new FormControl(''),
        funcionPrincipal: new FormControl('', [Validators.required]),
        fechaEncargo: new FormControl(this.initLoad.encabezado.tipoDeclaracion != 'MODIFICACION' ? this.initLoad.encabezado.fechaEncargo : '', [Validators.required, validaFechaLimit(this.globals.fechaReferenciaMax)]),
        telefonoOficina: new FormGroup({
          numero: new FormControl('', [Validators.required]),
          extension: new FormControl('', [Validators.required])
        }),
        domicilioTemp: new FormGroup({
          ubicacion: new FormControl(null),
          domicilioMexico: new FormGroup({
            calle: new FormControl('', [Validators.required]),
            codigoPostal: new FormControl('', [Validators.required]),
            coloniaLocalidad: new FormControl('', [Validators.required]),
            entidadFederativa: new FormGroup({
              id: new FormControl(null, [Validators.required]),
              valor: new FormControl('')
            }),
            municipioAlcaldia: new FormGroup({
              id: new FormControl(null, [Validators.required]),
              valor: new FormControl(''),
              fk: new FormControl(0)
            }),
            numeroExterior: new FormControl(null, [Validators.required]),
            numeroInterior: new FormControl(null)
          }),
          domicilioExtranjero: new FormGroup({
            calle: new FormControl('', [Validators.required]),
            ciudadLocalidad: new FormControl('', [Validators.required]),
            codigoPostal: new FormControl('', [Validators.required]),
            estadoProvincia: new FormControl('', [Validators.required]),
            numeroExterior: new FormControl('', [Validators.required]),
            numeroInterior: new FormControl(''),
            pais: new FormGroup({
              id: new FormControl(null, [Validators.required]),
              valor: new FormControl('')
            })
          })
        }),
        domicilio: new FormGroup({
          ubicacion: new FormControl('', [Validators.required]),
          domicilioMexico: new FormGroup({
            calle: new FormControl(''),
            codigoPostal: new FormControl(''),
            coloniaLocalidad: new FormControl(''),
            entidadFederativa: new FormGroup({
              id: new FormControl(null),
              valor: new FormControl('')
            }),
            municipioAlcaldia: new FormGroup({
              id: new FormControl(null),
              valor: new FormControl(''),
              fk: new FormControl(null)
            }),
            numeroExterior: new FormControl(''),
            numeroInterior: new FormControl('')
          }),
          domicilioExtranjero: new FormGroup({
            calle: new FormControl(''),
            ciudadLocalidad: new FormControl(''),
            codigoPostal: new FormControl(''),
            estadoProvincia: new FormControl(''),
            numeroExterior: new FormControl(''),
            numeroInterior: new FormControl(''),
            pais: new FormGroup({
              id: new FormControl(null),
              valor: new FormControl('')
            })
          })
        }),
        verificar: new FormControl(true),
        registroHistorico: new FormControl(false),
        idPosicionVista: new FormControl('')

      })

    });

    this.form.get('empleoCargoComision.domicilio.ubicacion').valueChanges.subscribe(valor => {

      if (valor === 'MEXICO') {
        this.form.get('empleoCargoComision.domicilioTemp.domicilioMexico').enable();
        this.form.get('empleoCargoComision.domicilio.domicilioMexico').enable();
        this.form.get('empleoCargoComision.domicilioTemp.domicilioExtranjero').disable();
        this.form.get('empleoCargoComision.domicilio.domicilioExtranjero').disable();
      }

      if (valor === 'EXTRANJERO') {
        this.form.get('empleoCargoComision.domicilioTemp.domicilioExtranjero').enable();
        this.form.get('empleoCargoComision.domicilio.domicilioExtranjero').enable();
        this.form.get('empleoCargoComision.domicilioTemp.domicilioMexico').disable();
        this.form.get('empleoCargoComision.domicilio.domicilioMexico').disable();
      }
    }

    );


    if (this.objUpdate) {
      console.log(this.objUpdate);
      const objJson = JSON.stringify(this.objUpdate);
      const obj = JSON.parse(objJson);

      obj.enteTemp = obj.ente.id;
      delete obj.ente;
      if (obj.domicilio == null) { obj.domicilio = this.form.get('empleoCargoComision.domicilio').value; }
      if (obj.domicilio.domicilioMexico == null) { obj.domicilio.domicilioMexico = this.form.get('empleoCargoComision.domicilio.domicilioMexico').value; }
      if (obj.domicilio.domicilioExtranjero == null) { obj.domicilio.domicilioExtranjero = this.form.get('empleoCargoComision.domicilio.domicilioExtranjero').value; }
      obj.domicilioTemp = obj.domicilio;


      this.filterMunicipios(this.form.get('empleoCargoComision.domicilioTemp.domicilioMexico.entidadFederativa.id').value);
      if (obj.nivelJerarquico === null) { obj.nivelJerarquico = this.form.get('empleoCargoComision.nivelJerarquico').value; }
      if (obj.remuneracionNeta === null) { obj.remuneracionNeta = this.form.get('empleoCargoComision.remuneracionNeta').value; }
      if (obj.telefonoOficina === null) { obj.telefonoOficina = this.form.get('empleoCargoComision.telefonoOficina').value; }
      if (obj.nivelJerarquico === null) { obj.nivelJerarquico = this.form.get('empleoCargoComision.nivelJerarquico').value; }
      obj.idMovimiento = obj.idMovimiento || null; // Validar que si exista la propiedad

      this.form.get('empleoCargoComision').setValue(obj);
    }

    switch (this.ini) {
      case 'INICIO':
        this.form.get('empleoCargoComision.tipoRemuneracion').setValue('MENSUAL');
        this.labelRemu = 'mensual neta';
        break;

      case 'MODIFICACION':
        this.form.get('empleoCargoComision.tipoRemuneracion').setValue('ANUAL_ANTERIOR');
        this.labelRemu = 'anual neta';
        break;

      case 'CONCLUSION':
        this.form.get('empleoCargoComision.tipoRemuneracion').setValue('ANUAL_ACTUAL');
        this.labelRemu = 'neta del año en curso a la fecha de conclusión';
        break;
    }


    let mex = this.form.get('empleoCargoComision.domicilioTemp.domicilioMexico').value;
    let ext = this.form.get('empleoCargoComision.domicilioTemp.domicilioExtranjero').value;

    if (mex != null) {
      this.form.get('empleoCargoComision.domicilioTemp.domicilioMexico').setValue(this.form.get('empleoCargoComision.domicilio.domicilioMexico').value);
    }
    if (ext != null) {
      this.form.get('empleoCargoComision.domicilioTemp.domicilioExtranjero').setValue(this.form.get('empleoCargoComision.domicilio.domicilioExtranjero').value);
    }


  }

  ngAfterViewInit(): void {

    console.log(this.datosEmpleoForm.valid);

    let formVal: boolean;
    setTimeout(() => {


      if (this.datosEmpleoForm.valid !== formVal) {
        this.TIPO_OPERACION = this.globals.getTipoOperacion(this.datosEmpleoForm.valid,
          this.form.get('empleoCargoComision.registroHistorico').value, this.form.get('empleoCargoComision.tipoOperacion').value, 'G');
        this.histFormInval = (this.form.get('empleoCargoComision.registroHistorico').value && !this.datosEmpleoForm.valid);
        if (!this.datosEmpleoForm.valid) {
          setTimeout(() => {
            this.resetBaja();
          }, 500);

        }
        formVal = this.datosEmpleoForm.valid;

      }
    }, 200);

  }

  resetBaja() {
    this.dataDisabled.forEach((data) => {

      data.tipoOperacion.BAJA = this.form.get(data.name) ? this.form.get(data.name).valid : false;

    });
  }

  tipoOperacionChange(id) {
    if (id === 'SIN_CAMBIO' || id === 'BAJA') {
      this.form.get('empleoCargoComision').setValue(JSON.parse(JSON.stringify(this.empleoR)));
      this.form.get('empleoCargoComision.tipoOperacion').setValue(id);
    }
  }

  getDisabled(obj, tipoOperacion1) {
    if (!obj.name || !tipoOperacion1) {
      return obj.isDisabled;
    }
    if (!this.form.get('empleoCargoComision.registroHistorico').value) {
      return false;
    } else {
      const newObj = this.dataDisabled.find((value) => value.name === obj.name);
      const newObj1 = newObj.tipoOperacion[tipoOperacion1];
      return newObj1;
    }
  }
  guardaLocal(form: FormGroup) {
    const domicilio = this.form.value;
    const cata = this.form.value;


    if (this.form.get('empleoCargoComision.domicilio.ubicacion').value === 'MEXICO') {
      domicilio.empleoCargoComision.domicilio.domicilioMexico.municipioAlcaldia = this.globals.catalogoMun
        .find(item => item.id == this.form.get('empleoCargoComision.domicilio.domicilioMexico.municipioAlcaldia.id').value);
    }

    cata.empleoCargoComision.nivelJerarquico = this.globals.catalogos.CAT_NIVEL_JERARQUICO
      .find(item => item.id == this.form.get('empleoCargoComision.nivelJerarquico.id').value);

    cata.empleoCargoComision.remuneracionNeta.moneda = this.globals.catalogos.CAT_MONEDA
      .find(item => item.id == this.form.get('empleoCargoComision.remuneracionNeta.moneda.id').value);

    if (this.form.get('empleoCargoComision.domicilio.ubicacion').value === 'MEXICO') {
      cata.empleoCargoComision.domicilio.domicilioMexico.entidadFederativa = this.globals.catalogos.CAT_ENTIDAD_FEDERATIVA
        .find(item => item.id == cata.empleoCargoComision.domicilio.domicilioMexico.entidadFederativa.id);
    }


    if (this.form.get('empleoCargoComision.domicilio.ubicacion').value === 'EXTRANJERO') {
      cata.empleoCargoComision.domicilio.domicilioExtranjero.pais = this.globals.catalogos.CAT_PAIS
        .find(item => item.id == cata.empleoCargoComision.domicilio.domicilioExtranjero.pais.id);
    }


    let empleoCargo = this.form.get('empleoCargoComision').value;
    empleoCargo.ente = this.cata.catalogoEntes
      .find(item => item.id == this.form.get('empleoCargoComision.enteTemp').value);
    delete empleoCargo.enteTemp;
    delete empleoCargo.domicilioTemp;
    empleoCargo.verificar = true;

    empleoCargo.remuneracionNeta.monto = Number(empleoCargo.remuneracionNeta.monto);
    empleoCargo.domicilio.domicilioExtranjero = empleoCargo.domicilio.ubicacion === "MEXICO" ? null : empleoCargo.domicilio.domicilioExtranjero;
    empleoCargo.domicilio.domicilioMexico = empleoCargo.domicilio.ubicacion === "EXTRANJERO" ? null : empleoCargo.domicilio.domicilioMexico;


    if (empleoCargo.contratadoPorHonorarios == "true") {
      empleoCargo.contratadoPorHonorarios = true;
    }

    if (empleoCargo.contratadoPorHonorarios == "false") {
      empleoCargo.contratadoPorHonorarios = false;
    }


    let s = JSON.parse(sessionStorage.getItem('datosEmpleo'));

    this.lista = s.empleoCargoComision;


    const index = this.lista.findIndex(({ idPosicionVista }) => idPosicionVista == empleoCargo.idPosicionVista);

    if (index === -1) {
      this.lista.push(empleoCargo);
    } else {
      this.lista[index] = empleoCargo;
    }

    this.lista.forEach(
      (element, index) => {
        this.lista[index].idPosicionVista = index + 1;
      });

    this.datoEmpleo = { aclaracionesObservaciones: s.aclaracionesObservaciones, empleoCargoComision: this.lista }

    sessionStorage.setItem('datosEmpleo', JSON.stringify(this.datoEmpleo));

    this.activeModal.close({ result: 'success', form: 'empleo' });
  }

  closeUbicacion() {
    this.form.get('empleoCargoComision.domicilioTemp.domicilioMexico').setValue(this.form.get('empleoCargoComision.domicilio.domicilioMexico').value);
    this.form.get('empleoCargoComision.domicilioTemp.domicilioExtranjero').setValue(this.form.get('empleoCargoComision.domicilio.domicilioExtranjero').value);
  }

  close() {
    console.log(this.form);

    this.modalService.dismissAll("empleo");
    $("body").removeClass("disabled-onepage-scroll");


  }

  abreMexico() {

    this.form.get('empleoCargoComision.domicilioTemp.domicilioMexico').setValue(JSON.parse(JSON.stringify(this.form.get('empleoCargoComision.domicilio.domicilioMexico').value)));
    if (this.form.get('empleoCargoComision.domicilio.ubicacion').value === 'MEXICO') {

      this.filterMunicipios(this.form.get('empleoCargoComision.domicilioTemp.domicilioMexico.entidadFederativa.id').value, false);
    }
  }

  abreExtranjero() {

    this.form.get('empleoCargoComision.domicilioTemp.domicilioExtranjero').setValue(JSON.parse(JSON.stringify(this.form.get('empleoCargoComision.domicilio.domicilioExtranjero').value)));

  }


  selectUbicacion(ubicacion) {
    console.log(ubicacion);
    switch (ubicacion) {

      case "mexico":
        this.form.get('empleoCargoComision.domicilio.domicilioMexico').setValue(JSON.parse(JSON.stringify(this.form.get('empleoCargoComision.domicilioTemp.domicilioMexico').value)));
        this.form.get('empleoCargoComision.domicilioTemp.domicilioExtranjero').disable();
        this.form.get('empleoCargoComision.domicilio.domicilioExtranjero').disable();
        break;

      case "extranjero":
        this.form.get('empleoCargoComision.domicilio.domicilioExtranjero').setValue(JSON.parse(JSON.stringify(this.form.get('empleoCargoComision.domicilioTemp.domicilioExtranjero').value)));
        this.form.get('empleoCargoComision.domicilioTemp.domicilioMexico').disable();
        this.form.get('empleoCargoComision.domicilio.domicilioMexico').disable();
        break;
    }

  }


  filterMunicipios(id, clear = true) {

    this.filterMuns = this.globals.catalogoMun.filter(item => item.fk === Number(id));
    const mun = this.filterMuns.find(item => item.id == this.form.get('empleoCargoComision.domicilio.domicilioMexico.municipioAlcaldia.id').value);
    const idMun = mun ? mun.id : null;
    this.form.get('empleoCargoComision.domicilio.domicilioMexico.municipioAlcaldia.id').setValue(idMun);
    if (clear) {
      this.form.get('empleoCargoComision.domicilioTemp.domicilioMexico.municipioAlcaldia.id').setValue(null);
    }
  }

  logForm() {
    console.log("form vlaue", this.form.value);

  }

}
