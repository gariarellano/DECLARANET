import { Component, OnInit, Input, HostListener, ViewChild, AfterViewInit } from '@angular/core';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormControl, FormGroup, Validators, NgForm } from '@angular/forms';
import * as $ from 'jquery';
import { Globals } from 'src/app/globals';
import { validateCaracteres } from './../../../validators/validadorFormControl';
import { validaFecha } from './../../../validators/validadorFormControl';
import { validaFechaLimit } from './../../../validators/validadorFormControl';
import { validateCurp } from './../../../validators/validateCurp';
@Component({
  selector: 'app-datos-curriculares-modal',
  templateUrl: './datos-curriculares-modal.component.html',
  styleUrls: ['./datos-curriculares-modal.component.scss']
})
export class DatosCurricularesModalComponent implements OnInit, AfterViewInit {

  form: FormGroup;
  help = false;
  lista: any;
  datoCurricular: any;
  @Input() public objUpdate;
  dataDisabled: any;
  histFormInval: boolean;
  curriR: any;
  TIPO_OPERACION: any;
  initLoad: boolean;
  @ViewChild('curricularesForm') curricularesForm: NgForm;
  formOperacion: FormGroup;

  constructor(private modalService: NgbModal,
    private activeModal: NgbActiveModal,
    public globals: Globals) {
    this.dataDisabled = [
      {
        name: "escolaridad.nivel.id",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "escolaridad.institucionEducativa",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "escolaridad.carreraAreaConocimiento",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "escolaridad.estatus",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "escolaridad.documentoObtenido.tipo",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "escolaridad.documentoObtenido.fechaObtencion",
        tipoOperacion: {
          AGREGAR: false,
          MODIFICAR: false,
          BAJA: true,
          SIN_CAMBIO: true
        }
      },
      {
        name: "escolaridad.ubicacion",
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
    this.formOperacion = new FormGroup({
      tipoOperacion: new FormControl(null, [Validators.required])
    });

    this.form = new FormGroup({
      escolaridad: new FormGroup({
        id: new FormControl(null),
        tipoOperacion: new FormControl(null),
        nivel: new FormGroup({
          id: new FormControl(null, [Validators.required]),
          valor: new FormControl('')
        }),
        institucionEducativa: new FormControl('', [Validators.required]),
        carreraAreaConocimiento: new FormControl('', [Validators.required, validateCaracteres]),
        estatus: new FormControl(null, [Validators.required]),
        documentoObtenido: new FormGroup({
          tipo: new FormControl(null, [Validators.required]),
          fechaObtencion: new FormControl('', [Validators.required, validaFechaLimit(this.globals.fechaReferenciaMax)])
        }),
        ubicacion: new FormControl(null, [Validators.required]),
        verificar: new FormControl(true),
        registroHistorico: new FormControl(false),
        idPosicionVista: new FormControl(null)
      }),
    });

    if (this.objUpdate) {

      if (this.objUpdate.nivel == null) {
        this.objUpdate.nivel = this.form.get('escolaridad.nivel').value;
      }
      this.formOperacion.get('tipoOperacion').setValue(this.objUpdate.tipoOperacion);

      this.form.get('escolaridad').setValue(this.objUpdate);
      this.initLoad = true;



    }
    this.curriR = JSON.parse(JSON.stringify(this.form.get('escolaridad').value));
  }

  ngAfterViewInit(): void {

    console.log(this.curricularesForm.valid);

    let formVal: boolean;

    setTimeout(() => {

      if (this.curricularesForm.valid !== formVal) {
        this.TIPO_OPERACION = this.globals.getTipoOperacion(this.curricularesForm.valid, this.form.get('escolaridad.registroHistorico').value,
          this.form.get('escolaridad.tipoOperacion').value, 'G');
        this.histFormInval = (this.form.get('escolaridad.registroHistorico').value && !this.curricularesForm.valid);
        if (!this.curricularesForm.valid) {
          setTimeout(() => {
            this.initLoad = false;
            this.resetBaja();
          }, 500);

        }
        formVal = this.curricularesForm.valid;

      }
    }, 100);

    setTimeout(() => {
      this.initLoad = false;
    }, 500);
    setTimeout(() => {
      this.form.get('escolaridad.documentoObtenido.tipo').setValue(this.form.get('escolaridad.documentoObtenido.tipo').value);

    }, 100);

  }

  resetBaja() {
    console.log(this.curricularesForm.control);
    this.dataDisabled.forEach((data) => {
      if (this.globals.precargaOracle) {
        data.tipoOperacion.BAJA = false;
      } else {
        data.tipoOperacion.BAJA = this.form.get(data.name).valid;
      }
    });
    console.log(this.dataDisabled);
  }

  tipoOperacionChange(id) {
    if (id === 'SIN_CAMBIO' || id === 'BAJA') {
      this.form.get('escolaridad').setValue(JSON.parse(JSON.stringify(this.curriR)));
    }
    this.form.get('escolaridad.tipoOperacion').setValue(id);
  }

  getDisabled(obj, tipoOperacion1) {

    let newObj1;
    if (!obj || !tipoOperacion1 || this.initLoad) {
      return null;
    }
    if (!this.form.get('escolaridad.registroHistorico').value) {
      return null;
    } else {
      const newObj = this.dataDisabled.find((value) => value.name === obj);
      newObj1 = newObj.tipoOperacion[tipoOperacion1];
    }
    if (newObj1) {
      this.form.get(obj).disable();
    } else {
      this.form.get(obj).enable();
    }

  }

  guardaLocal(form: FormGroup) {

    this.form.get('escolaridad').enable();
    const escolaridad = this.form.get('escolaridad').value;
    escolaridad.nivel = this.globals.catalogos.CAT_NIVEL_ACADEMICO.find(item => item.id === Number(escolaridad.nivel.id));
    escolaridad.verificar = true;
    escolaridad.tipoOperacion = this.formOperacion.get('tipoOperacion').value;

    console.log("datos es:", escolaridad);

    let s = JSON.parse(sessionStorage.getItem('datosCurricularesDeclarante'));

    this.lista = s.escolaridad;

    const index = this.lista.findIndex(({ idPosicionVista }) => idPosicionVista == escolaridad.idPosicionVista);

    if (index === -1) {
      this.lista.push(escolaridad);
    } else {
      this.lista[index] = escolaridad;
    }

    this.lista.forEach(
      (element, index) => {
        this.lista[index].idPosicionVista = index + 1;
      });
    this.datoCurricular = { ninguno: false, escolaridad: this.lista, aclaracionesObservaciones: s.aclaracionesObservaciones };

    sessionStorage.setItem('datosCurricularesDeclarante', JSON.stringify(this.datoCurricular));

    this.activeModal.close({ result: 'success', form: 'curriculares' });
  }

  close() {
    this.modalService.dismissAll("curriculares");
    $("body").removeClass("disabled-onepage-scroll");
  }

}
