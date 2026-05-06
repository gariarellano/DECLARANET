import { Component, OnInit, EventEmitter, Output, AfterViewInit, ViewChildren, ElementRef, QueryList, HostListener } from '@angular/core';
import * as $ from 'jquery';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { validateCurp } from './../../validators/validateCurp';
import { validateRfcControl } from './../../validators/validatorRfcControl';
import { validateHomoControl } from './../../validators/validatorHomoControl';
import { validateEmail } from './../../validators/emailValidator';
import { Globals,timeTouchEvent } from '../../globals';

@Component({
  selector: 'app-datos-generales',
  templateUrl: './datos-generales.component.html',
  styleUrls: ['./datos-generales.component.scss']
})
export class DatosGeneralesComponent implements OnInit, AfterViewInit {
  @Output()
  aceptar = new EventEmitter<JSON>();
  help = false;
  @ViewChildren('form1', { read: ElementRef}) elements: QueryList<ElementRef>;
endTimeTouch
startTimeTouch

countTouches = 0
countFingers

distanceTouch
startScreenTouch
endScreenTouch
  aclaraObs: any;
@HostListener('touchmove', ['$event'])
moveTouch(event) {
  this.countFingers = event.touches.length
  this.distanceTouch = event.changedTouches[0].clientX - this.startScreenTouch
}

@HostListener('touchstart', ['$event'])
start(event) {
  this.startTimeTouch = new Date().getTime()
  this.startScreenTouch = event.changedTouches[0].clientX
}

div = document.getElementById('scroll1');
@HostListener('touchend', ['$event'])
stop(event) {
  this.div = document.getElementById('scroll1');
  this.endTimeTouch = new Date().getTime()
  let diff = this.endTimeTouch - this.startTimeTouch
  if (this.div['scrollHeight'] !== this.div['clientHeight']) {
    if (this.countFingers === 2) {
      
      this.countTouches++
      if (this.countTouches === 2) {
        if (this.startTimeTouch){
          if (diff < timeTouchEvent) {
            if (this.distanceTouch > 0) {document.getElementById('goForwardBtn').click()}
          }
        this.countTouches = 0;
        }
      }
    }
  } else
    if (diff < timeTouchEvent) {
      if (this.distanceTouch > 0) document.getElementById('goForwardBtn').click()
    }
}

@HostListener('mousewheel', ['$event'])
_wheel(event: MouseEvent) {
  this.div = document.getElementById('scroll1');
  if (this.div['scrollHeight'] !== this.div['clientHeight']) event.stopPropagation()
  if (this.div['offsetHeight'] !== this.div['scrollHeight'])
  if (this.div['scrollTop'] + this.div['offsetHeight'] + 1 > this.div['scrollHeight'] && event['wheelDeltaY'] < 0)
    document.getElementById('goForwardBtn').click()
  else if (this.div['scrollTop'] === 0 && event['wheelDeltaY'] > 0)
    document.getElementById('goBackBtn').click()
}
  label: any = '';
  info: any = '';
  link: any = '';
  leyenda: any = '';
  dataInfo: any;
  dataInfo2: any;
  form: FormGroup;
  obj: any;
  datosGenerales: any;
  @Output() acla = new EventEmitter();
  aclara: string;

  constructor(public globals: Globals) {
    this.dataInfo = {
      datosGenerales: {
        nombre: {
          label: 'Nombre(s)',
          info: 'Es el nombre de la persona que queremos capturar',
          validator: {
            message: 'Campo obligatorio',
            valid: 'El nombre solo contener letras'
          },
          publico: true
        },
        primerApellido: {
          label: 'Primer Apellido',
          info: 'Es el apellido paterno de la persona',
          validator: {
            message: 'Campo obligatorio',
            valid: 'Curp invalida'
          }
        },
        segundoApellido: {
          label: 'Segundo Apellido',
          info: 'Es el apellido materno de la persona',
          validator: {
            message: 'Campo obligatorio',
            valid: 'Curp invalida'
          }
        },
        curp: {
          label: 'Curp',
          info: 'clave única de registro de población',
          link: 'https://www.gob.mx/curp/',
          leyend: 'Consultar aquí',
          validator: {
            message: 'Campo obligatorio',
            valid: 'Curp invalida'
          }
        },
        rfc: {
          label: 'RFC',
          info: 'Clave única de registro',
          validator: {
            message: 'Campo obligatorio',
            valid: 'RFC invalido'
          }
        },
        correoIns: {
          label: 'Correo Institucional',
          info: 'Correo de trabajo o institución',
          validator: {
            message: 'Campo obligatorio',
            valid: 'Correo invalido'
          }
        },
        correoPer: {
          label: 'Correo Personal',
          info: 'Correo de uso personal',
          validator: {
            message: 'Campo obligatorio',
            valid: 'Correo invalido'
          }
        }
      }
    };
  }
  @HostListener('paste', ['$event']) blockPaste(e: KeyboardEvent) {
    e.preventDefault();
  }
  ngOnInit() {


    this.datosGenerales = JSON.parse(sessionStorage.getItem('datosGenerales'));

    delete this.datosGenerales.telefonoCasa.paisCelularPersonal;



    this.datosGenerales.datosPersonales.homoclave = this.datosGenerales.datosPersonales.rfc.substr(
      10,
      12
    );
    this.datosGenerales.datosPersonales.rfc = this.datosGenerales.datosPersonales.rfc.substr(
      0,
      10
    );

    this.form = new FormGroup({
      datosPersonales: new FormGroup({
        nombre: new FormControl('', [Validators.required]),
        primerApellido: new FormControl('', [Validators.required]),
        segundoApellido: new FormControl(''),
        rfc: new FormControl('', [Validators.required,validateRfcControl]),
        curp: new FormControl('', [Validators.required, validateCurp]),
        homoclave: new FormControl('',[Validators.required,validateHomoControl])
      }),
      aclaracionesObservaciones: new FormControl(''),
      verificar: new FormControl(''),
      correoElectronico: new FormGroup({
        institucional: new FormControl('', [validateEmail]),
        personalAlterno: new FormControl('', [validateEmail])
      }),
      telefonoCasa: new FormGroup({
        numero: new FormControl('')
      }),
      telefonoCelular: new FormGroup({
        numero: new FormControl(''),
        paisCelularPersonal: new FormGroup({
          id: new FormControl(''),
          valor: new FormControl('')
        })
      }),
      situacionPersonalEstadoCivil: new FormGroup({
        id: new FormControl('', [Validators.required]),
        valor: new FormControl('')
      }),
      regimenMatrimonial: new FormGroup({
        id: new FormControl('',[Validators.required]),
        valor: new FormControl('')
      }),
      regimenMatrimonialOtro: new FormControl('', [Validators.required]),
      paisNacimiento: new FormGroup({
        id: new FormControl('', [Validators.required]),
        valor: new FormControl('')
      }),
      nacionalidad: new FormGroup({
        id: new FormControl('', [Validators.required]),
        valor: new FormControl('')
      })
    });


    this.form.get('situacionPersonalEstadoCivil').valueChanges.subscribe(valor => {
      if (valor.id == '2') {
        this.form.get('regimenMatrimonial.id').enable();
      } else {
        this.form.get('regimenMatrimonial.id').disable();
        this.form.get('regimenMatrimonial.id').setValue(null);
        this.form.get('regimenMatrimonial.id').setValue(null);
        this.form.get('regimenMatrimonialOtro').setValue('');
      }
    });

    this.form.get('regimenMatrimonial.id').valueChanges.subscribe(valor => {
      if (valor == '9999') {
        this.form.get('regimenMatrimonialOtro').enable();
      } else {
        this.form.get('regimenMatrimonialOtro').disable();
        this.form.get('regimenMatrimonialOtro').setValue('');
      }
    });

    this.datosGenerales.regimenMatrimonial = this.datosGenerales.regimenMatrimonial === null ? { id: '', valor: '' } : this.datosGenerales.regimenMatrimonial;
    this.datosGenerales.telefonoCelular.paisCelularPersonal = this.datosGenerales.telefonoCelular.paisCelularPersonal === null ? { id: '', valor: '' } : this.datosGenerales.telefonoCelular.paisCelularPersonal;
    this.form.setValue(this.datosGenerales);
    this.aclara = this.datosGenerales.aclaracionesObservaciones;
    $('.help').click(() => {
      this.help = !this.help;
      if (this.help) {
        $('label').addClass('openHelp');
        $('label')
          .next('input,select')
          .css('visibility', 'hidden');
      } else {
        $('label').removeClass('openHelp');
        $('label')
          .next('input,select')
          .css('visibility', 'visible');
      }
    });
  }



  ngAfterViewInit() {
  }

  guardaLocal(form: FormGroup) {

    const emailPers = this.form.get('correoElectronico.personalAlterno').value ? true : false;
    const emailInst = this.form.get('correoElectronico.institucional').value ? true : false;
    const numCel = this.form.get('telefonoCelular.numero').value ? true : false;
    const paisNumCel = this.form.get('telefonoCelular.paisCelularPersonal.id').value ? true : false;


    if (!emailPers && !emailInst) {
      alert('Debe tener mínimo un correo');
      return;
    }

    if ((paisNumCel && !numCel )
    || (!paisNumCel && numCel)) {
      alert('Teléfono celular debe llenarse completo (país y número).');
      return;
    }

    if (this.form.get('telefonoCelular.paisCelularPersonal.id').value != null) {
    let valorCel = this.globals.catalogos.CAT_PAIS.find(
      item =>
        item.id == this.form.get('telefonoCelular.paisCelularPersonal.id').value
    );
    if (valorCel) {
    this.form
      .get('telefonoCelular.paisCelularPersonal.valor')
      .setValue(valorCel.valor);
    }
  }
    let valorEdo = this.globals.catalogos.CAT_ESTADO_CIVIL.find(
      item => item.id == this.form.get('situacionPersonalEstadoCivil.id').value
    ).valor;
    this.form.get('situacionPersonalEstadoCivil.valor').setValue(valorEdo);
    console.log(this.form.get('regimenMatrimonial.id').value);
    const valorRegimenObj = this.globals.catalogos.CAT_REGIMEN_MATRIMONIAL.find(
      item => item.id == this.form.get('regimenMatrimonial.id').value);

    if (valorRegimenObj) {
      this.form.get('regimenMatrimonial').setValue(valorRegimenObj);
    }

    let listaPaises = this.globals.catalogos.CAT_PAIS.find(
      item => item.id == this.form.get('paisNacimiento.id').value
    ).valor;
    this.form.get('paisNacimiento.valor').setValue(listaPaises);

    let listaNacion = this.globals.catalogos.CAT_NACIONALIDAD.find(
      item => item.id == this.form.get('nacionalidad.id').value
    ).valor;
    this.form.get('nacionalidad.valor').setValue(listaNacion);

    this.datosGenerales = this.form.getRawValue();
    this.datosGenerales.datosPersonales.rfc =
      this.form.get('datosPersonales.rfc').value +
      this.form.get('datosPersonales.homoclave').value;

    delete this.datosGenerales.datosPersonales.homoclave;
    this.datosGenerales.verificar = true;

    if (this.datosGenerales.situacionPersonalEstadoCivil.id != '2') {
      this.datosGenerales.regimenMatrimonial = null;
    }
    if (this.datosGenerales.telefonoCelular.paisCelularPersonal.id === null || this.datosGenerales.telefonoCelular.paisCelularPersonal.id === '') {
      this.datosGenerales.telefonoCelular.paisCelularPersonal = null;
    }
    this.aceptar.emit(this.datosGenerales);


    setTimeout(() => {
      $('#nextSection').click();
    }, 1000);
  }
  abriInfo(param1, param2) {
    this.label = this.dataInfo[param1][param2].label;
    this.info = this.dataInfo[param1][param2].info;
    this.link = this.dataInfo[param1][param2].link;
    this.leyenda = this.dataInfo[param1][param2].leyend;

    $('.content-info').css({ display: 'flex' });
    $('.menu-nav').css({ display: 'none' });
    $('#content-btnSave').css({ display: 'none' });
    console.log(this.form);
  }
  cerrarInfo() {
    this.label = '';
    this.info = '';
    $('.content-info').css('display', 'none');
    $('.menu-nav').css({ display: 'flex' });
    $('#content-btnSave').css({ display: 'flex' });
  }
  abrirObservaciones() {
    $('body').addClass('disabled-onepage-scroll');
    $('#obs-generales').css({ display: 'flex' });
    $('.menu-nav').css({ display: 'none' });
    $('#content-btnSave').css({ display: 'none' });
    this.aclaraObs = this.form.get('aclaracionesObservaciones').value;
  }

  cerrarObservaciones() {
    $('body').removeClass('disabled-onepage-scroll');
    $('#obs-generales').css('display', 'none');
    $('.menu-nav').css({ display: 'flex' });
    $('#content-btnSave').css({ display: 'flex' });
    this.form.get('aclaracionesObservaciones').setValue(this.aclaraObs);

  }
  guardarObservaciones() {
    const val = $("#aclaracionesObservaciones").val();
    console.log("valor obs ",val);
    this.form.get('aclaracionesObservaciones').setValue(val);
    
    $('#obs-generales').css('display', 'none');
    $('.menu-nav').css({ display: 'flex' });
    $('#content-btnSave').css({ display: 'flex' });

  }

  validateStatusForm() {
    if (!this.form.valid) {
      $('.item1')
        .find('i')
        .removeClass('foco-verde foco-naranja')
        .addClass('foco-rojo');
    }
  }
}
