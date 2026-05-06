import { Component, OnInit, Input, HostListener, ViewChild } from '@angular/core';
import * as $ from 'jquery';
import { FormControl, FormGroup, Validators, NgForm } from '@angular/forms';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Globals } from 'src/app/globals';
import { validateRfcCompletoControl } from './../../../validators/validatorRfcCompletoControl'
import { validaFechaLimit } from 'src/app/validators/validadorFormControl';
import { ToastrService } from 'ngx-toastr';
import * as moment from 'moment';

@Component({
  selector: 'app-experiencia-laboral-modal',
  templateUrl: './experiencia-laboral-modal.component.html',
  styleUrls: ['./experiencia-laboral-modal.component.scss']
})
export class ExperienciaLaboralModalComponent implements OnInit {

  form: FormGroup;
  form2: FormGroup;
  help = false;
  lista: any;
  datoExperiencia: any;
  tiOp = false;
  @Input() public objUpdate;
  regisHisto: any;
  dataDisabled: any;
  histFormInval: boolean;
  curriR: any;
  TIPO_OPERACION: any;
  initLoad: boolean;
  tipoDeclaracion: String;
  @ViewChild('formExperiencia') formExperiencia: NgForm;
  formOperaEx: FormGroup;

  constructor(private modalService: NgbModal, private activeModal: NgbActiveModal, public cata: Globals, public globals: Globals, private readonly toastService: ToastrService) {
    this.dataDisabled = [
      {
        name: "experienciaLaboral.actividadLaboral.ambitoSector.id",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "experienciaLaboral.actividadLaboral.sectorPublico.nivelOrdenGobierno",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "experienciaLaboral.actividadLaboral.sectorPublico.ambitoPublico",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "experienciaLaboral.actividadLaboral.ambitoSectorOtro",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "experienciaLaboral.actividadLaboral.sectorPublico.nombreEntePublico",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "experienciaLaboral.actividadLaboral.sectorPublico.areaAdscripcion",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "experienciaLaboral.actividadLaboral.sectorPublico.empleoCargoComision",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "experienciaLaboral.actividadLaboral.sectorPublico.funcionPrincipal",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "experienciaLaboral.actividadLaboral.sectorPrivadoOtro.nombreEmpresaSociedadAsociacion",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "experienciaLaboral.actividadLaboral.sectorPrivadoOtro.rfc",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "experienciaLaboral.actividadLaboral.sectorPrivadoOtro.area",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "experienciaLaboral.actividadLaboral.sectorPrivadoOtro.empleoCargo",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "experienciaLaboral.actividadLaboral.sectorPrivadoOtro.sector.id",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "experienciaLaboral.actividadLaboral.fechaIngreso",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "experienciaLaboral.actividadLaboral.fechaEgreso",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "experienciaLaboral.actividadLaboral.ubicacion",
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

    this.lista = [];
    this.formOperaEx = new FormGroup({
      tipoOperacion: new FormControl(null, [Validators.required])
    });

    this.form2 = new FormGroup({
      aclaracionesObservaciones: new FormControl(''),
      ambito: new FormControl(''),
      experienciaLaboral: new FormGroup({
        id: new FormControl(null),
        verificar: new FormControl(true),
        idPosicionVista: new FormControl(null),
        registroHistorico: new FormControl(false),
        tipoOperacion: new FormControl(null),
        actividadLaboral: new FormGroup({
          ambitoSector: new FormGroup({
            id: new FormControl(null, [Validators.required]),
            valor: new FormControl(''),
          }),
          ambitoSectorOtro: new FormControl(null),

          sectorPublico: new FormGroup({
            nivelOrdenGobierno: new FormControl(null, [Validators.required]),
            ambitoPublico: new FormControl(null, [Validators.required]),
            nombreEntePublico: new FormControl(null, [Validators.required]),
            areaAdscripcion: new FormControl(null, [Validators.required]),
            empleoCargoComision: new FormControl(null, [Validators.required]),
            funcionPrincipal: new FormControl(null, [Validators.required]),
          }),
          sectorPrivadoOtro: new FormGroup({
            nombreEmpresaSociedadAsociacion: new FormControl('', [Validators.required]),
            rfc: new FormControl('', validateRfcCompletoControl),
            area: new FormControl('', [Validators.required]),
            empleoCargo: new FormControl('', [Validators.required]),
            sectorOtro: new FormControl(''),
            sector: new FormGroup({
              id: new FormControl(null, [Validators.required]),
              valor: new FormControl('')
            })
          }),
          fechaIngreso: new FormControl('', [Validators.required]),
          fechaEgreso: new FormControl('', [Validators.required, validaFechaLimit(this.globals.fechaReferenciaMax)]),
          ubicacion: new FormControl(null, [Validators.required])
        })
      })

    });

    if (this.objUpdate) {

      this.tiOp = this.form2.get('experienciaLaboral.registroHistorico').value;
      console.log("ex ", this.form2);

      if (this.objUpdate.actividadLaboral.sectorPrivadoOtro.rfc == null) {
        this.objUpdate.actividadLaboral.sectorPrivadoOtro.rfc = this.form2.get('experienciaLaboral.actividadLaboral.sectorPrivadoOtro.rfc').value;
      }

      this.formOperaEx.get('tipoOperacion').setValue(this.objUpdate.tipoOperacion);
      if (this.objUpdate.actividadLaboral.ambitoSector == null) { this.objUpdate.actividadLaboral.ambitoSector = this.form2.get('experienciaLaboral.actividadLaboral.ambitoSector').value; }
      if (this.objUpdate.actividadLaboral.sectorPublico == null) { this.objUpdate.actividadLaboral.sectorPublico = this.form2.get('experienciaLaboral.actividadLaboral.sectorPublico').value; }
      if (this.objUpdate.actividadLaboral.sectorPrivadoOtro == null) { this.objUpdate.actividadLaboral.sectorPrivadoOtro = this.form2.get('experienciaLaboral.actividadLaboral.sectorPrivadoOtro').value }
      if (this.objUpdate.actividadLaboral.sectorPrivadoOtro.sector == null) { this.objUpdate.actividadLaboral.sectorPrivadoOtro.sector = this.form2.get('experienciaLaboral.actividadLaboral.sectorPrivadoOtro.sector').value }
      this.form2.get('experienciaLaboral').setValue(this.objUpdate);
      this.initLoad = true;
    }

    this.curriR = JSON.parse(JSON.stringify(this.form2.get('experienciaLaboral').value));

    if (this.form2.get('experienciaLaboral.actividadLaboral.ambitoSector.id').value == 1) {
      this.form2.get('experienciaLaboral.actividadLaboral.sectorPrivadoOtro').disable();
    }

    if (this.form2.get('experienciaLaboral.actividadLaboral.ambitoSector.id').value == 2) {
      this.form2.get('experienciaLaboral.actividadLaboral.sectorPublico').disable();
    }

    if (this.form2.get('experienciaLaboral.actividadLaboral.ambitoSector.id').value == 9999) {
      this.form2.get('experienciaLaboral.actividadLaboral.sectorPublico').disable();
    }


  }

  ngAfterViewInit(): void {
    const declaracion = JSON.parse(sessionStorage.getItem('declaracionLoaded'));
    this.tipoDeclaracion = declaracion.encabezado.tipoDeclaracion;
    this.tipoDeclaracion = String(this.tipoDeclaracion);

    this.form2.get('experienciaLaboral.actividadLaboral.ambitoSector.id').valueChanges.subscribe(valor => {
      console.log(valor);
      if (!this.form2.get('experienciaLaboral.actividadLaboral.ambitoSector.id').disabled) {
        if (valor == 1) {
          console.log("form2 ", this.form2);
          this.form2.get('experienciaLaboral.actividadLaboral.sectorPublico').enable();
        } else {
          this.form2.get('experienciaLaboral.actividadLaboral.sectorPublico').disable();
        }


        if (valor == 2 || valor == 9999) {

          this.form2.get('experienciaLaboral.actividadLaboral.sectorPrivadoOtro').enable();
        } else {
          this.form2.get('experienciaLaboral.actividadLaboral.sectorPrivadoOtro').disable();
        }
      }
    });


    let formVal: boolean;
    setTimeout(() => {
      if (this.formExperiencia.valid !== formVal) {
        this.TIPO_OPERACION = this.globals.getTipoOperacion(this.formExperiencia.valid, this.form2.get('experienciaLaboral.registroHistorico').value, this.form2.get('experienciaLaboral.tipoOperacion').value, 'G');
        console.log("tipo operacion ", this.TIPO_OPERACION);

        this.histFormInval = (this.form2.get('experienciaLaboral.registroHistorico').value && !this.formExperiencia.valid);
        if (!this.formExperiencia.valid) {
          setTimeout(() => {
            this.initLoad = false;
            this.resetBaja();
          }, 500);

        }
        formVal = this.formExperiencia.valid;

      }
    }, 100);
    setTimeout(() => {
      this.initLoad = false;
    }, 500);
  }

  resetBaja() {
    this.dataDisabled.forEach((data) => {
      if (this.globals.precargaOracle) {
        data.tipoOperacion.BAJA = false;
      } else {
        data.tipoOperacion.BAJA = this.form2.get(data.name) ? this.form2.get(data.name).valid : false;
      }
    });
  }

  tipoOperacionChange(id) {
    if (id === 'SIN_CAMBIO' || id === 'BAJA') {
      this.form2.get('experienciaLaboral').setValue(JSON.parse(JSON.stringify(this.curriR)));
    }
    this.form2.get('experienciaLaboral.tipoOperacion').setValue(id);
  }

  getDisabled(obj, tipoOperacion1) {
    let newObj1;
    if (!obj || !tipoOperacion1 || this.initLoad) {
      return this.form2.get(obj).disabled;
    }
    if (!this.form2.get('experienciaLaboral.registroHistorico').value) {
      return false;
    } else {
      const newObj = this.dataDisabled.find((value) => value.name === obj);
      newObj1 = newObj.tipoOperacion[tipoOperacion1];
    }
    if (newObj1) {
      this.form2.get(obj).disable();
    } else {
      this.form2.get(obj).enable();
    }

  }

  guardaLocal(form: FormGroup) {

    // let formDisabled = this.form2.get('experienciaLaboral').disabled ? true : false;
    let fechas;
    let formDisabled;
    this.form2.get('experienciaLaboral.actividadLaboral.fechaIngreso').enable();
    this.form2.get('experienciaLaboral.actividadLaboral.fechaEgreso').enable();


    if (this.form2.get('experienciaLaboral.actividadLaboral.fechaIngreso').disabled &&
      this.form2.get('experienciaLaboral.actividadLaboral.fechaEgreso').disabled) {
      fechas = true;
    } else {
      fechas = false;
    }

    const fechaIngreso = this.form2.get('experienciaLaboral.actividadLaboral.fechaIngreso').value;
    const fechaEgreso = this.form2.get('experienciaLaboral.actividadLaboral.fechaEgreso').value;

    if (fechaIngreso > fechaEgreso || fechaIngreso == fechaEgreso) {
      this.toastService.error(
        `Mensaje: la fecha de ingreso debe ser menor ala fecha de egreso`,
        `Fecha invalida`,
        {
          timeOut: 5000,
          extendedTimeOut: 5000,
          closeButton: true,
          tapToDismiss: true,
          positionClass: 'toast-top-center',
          progressBar: true
        }
      );

      if (fechas) {
        this.form2.get('experienciaLaboral.actividadLaboral.fechaIngreso').disable();
        this.form2.get('experienciaLaboral.actividadLaboral.fechaEgreso').disable();
      } else {
        this.form2.get('experienciaLaboral.actividadLaboral.fechaIngreso').enable();
        this.form2.get('experienciaLaboral.actividadLaboral.fechaEgreso').enable();
      }

    } else {
      this.form2.get('experienciaLaboral').enable();
      let experienciaLaboral = this.form2.get('experienciaLaboral').value;
      experienciaLaboral.verificar = true;
      experienciaLaboral.tipoOperacion = this.formOperaEx.get('tipoOperacion').value;
      experienciaLaboral.actividadLaboral.ambitoSector = this.globals.catalogos.CAT_AMBITO_SECTOR
        .find(item => item.id == experienciaLaboral.actividadLaboral.ambitoSector.id);

      if (experienciaLaboral.actividadLaboral.ambitoSector.id != 1) {
        experienciaLaboral.actividadLaboral.sectorPrivadoOtro.sector = this.globals.catalogos.CAT_SECTOR_PRIVADO
          .find(item => item.id == experienciaLaboral.actividadLaboral.sectorPrivadoOtro.sector.id);
      }

      experienciaLaboral.actividadLaboral.sectorPublico = experienciaLaboral.actividadLaboral.ambitoSector.id === 2 || experienciaLaboral.actividadLaboral.ambitoSector.id === 9999 ? null : experienciaLaboral.actividadLaboral.sectorPublico;
      experienciaLaboral.actividadLaboral.sectorPrivadoOtro = experienciaLaboral.actividadLaboral.ambitoSector.id === 1 ? null : experienciaLaboral.actividadLaboral.sectorPrivadoOtro;

      if (experienciaLaboral.actividadLaboral.ambitoSector.id !== 9999) {
        experienciaLaboral.actividadLaboral.ambitoSectorOtro = null;
      }
      if (experienciaLaboral.actividadLaboral.sectorPrivadoOtro != null) {
        if (experienciaLaboral.actividadLaboral.sectorPrivadoOtro.sector.id !== 9999) {
          experienciaLaboral.actividadLaboral.sectorPrivadoOtro.sectorOtro = null;
        }
      }

      let s = JSON.parse(sessionStorage.getItem('experienciaLab'));

      this.lista = s.experienciaLaboral;

      const index = this.lista.findIndex(({ idPosicionVista }) => idPosicionVista == experienciaLaboral.idPosicionVista);

      if (index === -1) {
        this.lista.push(experienciaLaboral);
      } else {
        this.lista[index] = experienciaLaboral;
      }


      this.lista.forEach(
        (element, index) => {
          this.lista[index].idPosicionVista = index + 1;

        });

      this.datoExperiencia = { ninguno: false, experienciaLaboral: this.lista, aclaracionesObservaciones: s.aclaracionesObservaciones };

      sessionStorage.setItem('experienciaLab', JSON.stringify(this.datoExperiencia));

      this.activeModal.close({ result: 'success', form: 'experiencia' });

    }


  }

  close() {
    this.modalService.dismissAll('experiencia');
    $("body").removeClass("disabled-onepage-scroll");
  }

  cambioAmbito(form: FormGroup) {
    let ambito = this.form2.get('experienciaLaboral.actividadLaboral.ambitoSector.id').value;
    switch (ambito) {
      case 'publico':
        this.form2.get('experienciaLaboral.actividadLaboral.sectorPrivadoOtro.nombreEmpresaSociedadAsociacion').setValue("");
        this.form2.get('experienciaLaboral.actividadLaboral.sectorPrivadoOtro.rfc').setValue("");
        this.form2.get('experienciaLaboral.actividadLaboral.sectorPrivadoOtro.area').setValue("");
        this.form2.get('experienciaLaboral.actividadLaboral.sectorPrivadoOtro.empleoCargo').setValue("");
        this.form2.get('experienciaLaboral.actividadLaboral.sectorPrivadoOtro.sector.valor').setValue("");

        break;

      case 'privado':

        this.form2.get('experienciaLaboral.actividadLaboral.sectorPublico.nivelOrdenGobierno.valor').setValue("");
        this.form2.get('experienciaLaboral.actividadLaboral.sectorPublico.ambitoPublico.valor').setValue("");
        this.form2.get('experienciaLaboral.actividadLaboral.sectorPublico.nombreEntePublico').setValue("");
        this.form2.get('experienciaLaboral.actividadLaboral.sectorPublico.areaAdscripcion').setValue("");
        this.form2.get('experienciaLaboral.actividadLaboral.sectorPublico.empleoCargoComision').setValue("");
        this.form2.get('experienciaLaboral.actividadLaboral.sectorPublico.funcionPrincipal').setValue("");

        break;
    }

  }

}
