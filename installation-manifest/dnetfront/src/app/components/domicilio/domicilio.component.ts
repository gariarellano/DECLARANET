import { Component, OnInit, EventEmitter, Output, HostListener } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import * as $ from 'jquery';
import { Globals, timeTouchEvent } from '../../globals';

@Component({
  selector: 'app-domicilio',
  templateUrl: './domicilio.component.html',
  styleUrls: ['./domicilio.component.scss']
})
export class DomicilioComponent implements OnInit {
  @Output()
  aceptar = new EventEmitter<JSON>();
  help = false;
  form: FormGroup;
  formValid: boolean;
  filterMuns: any;
  @Output() acla = new EventEmitter();
  aclara: string;
  dom: any;

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

  div = document.getElementById('scrollDomicilio');
  @HostListener('touchend', ['$event'])
  stop(event) {
    this.div = document.getElementById('scrollDomicilio');
    this.endTimeTouch = new Date().getTime()
    let diff = this.endTimeTouch - this.startTimeTouch
    if (this.div['scrollHeight'] !== this.div['clientHeight']) {
      if (this.countFingers === 2) {
        this.countTouches++
        if (this.countTouches === 2) {
          if (this.startTimeTouch){
            if (diff < timeTouchEvent) {
              if (this.distanceTouch > 0) {document.getElementById('goForwardBtn').click()}
              if (this.distanceTouch < 0) {document.getElementById('goBackBtn').click()}
            }
          console.log(this.distanceTouch);

          this.countTouches = 0;
          }
        }
      }
    } else
      if (diff < timeTouchEvent) {
        if (this.distanceTouch > 0) {document.getElementById('goForwardBtn').click()}
        if (this.distanceTouch < 0) {document.getElementById('goBackBtn').click()}
      }
    console.log(this.distanceTouch);
  }

  @HostListener('mousewheel', ['$event'])
  _wheel(event: MouseEvent) {
    this.div = document.getElementById('scrollDomicilio');
    
    if (this.div['scrollHeight'] !== this.div['clientHeight']) event.stopPropagation()
    if (this.div['offsetHeight'] !== this.div['scrollHeight'])
      if (this.div['scrollTop'] + this.div['offsetHeight'] + 1 > this.div['scrollHeight'] && event['wheelDeltaY'] < 0)
        document.getElementById('goForwardBtn').click()
      else if (this.div['scrollTop'] === 0 && event['wheelDeltaY'] > 0)
        document.getElementById('goBackBtn').click()
  }
  constructor(public globals: Globals) { }

  @HostListener('paste', ['$event']) blockPaste(e: KeyboardEvent) {
    e.preventDefault();
  }

  ngOnInit() {
    this.form = new FormGroup({
      verificar: new FormControl(null),
      domicilio: new FormGroup({
        ubicacion: new FormControl( null, [Validators.required]),
        domicilioMexico: new FormGroup({
          calle: new FormControl('', [Validators.required]),
          codigoPostal: new FormControl('', [Validators.required]),
          coloniaLocalidad: new FormControl('', [Validators.required]),
          entidadFederativa: new FormGroup({
            id: new FormControl('', [Validators.required]),
            valor: new FormControl(''),
          }),
          municipioAlcaldia: new FormGroup({
            id: new FormControl('', [Validators.required]),
            valor: new FormControl(''),
            fk: new FormControl(''),
          }),
          numeroExterior: new FormControl('', [Validators.required]),
          numeroInterior: new FormControl(''),
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
            valor: new FormControl(''),
          })
        })
      }),
      aclaracionesObservaciones: new FormControl('')
    });
    this.aclara = JSON.parse(sessionStorage.getItem('domicilioDeclarante')).aclaracionesObservaciones;
    this.form.setValue(JSON.parse(sessionStorage.getItem('domicilioDeclarante')));
    const ent = JSON.parse(sessionStorage.getItem('domicilioDeclarante')).domicilio.domicilioMexico.entidadFederativa.id;
    if (ent) {
      this.filterMunicipios(ent);
    }
  }

  guardaLocal(form: FormGroup) {
    console.log(this.form);
    const domicilio = this.form.value;
    domicilio.verificar = true;
    if (domicilio.domicilio.ubicacion === 'MEXICO') {
      domicilio.domicilio.domicilioMexico.entidadFederativa = this.globals.catalogos.CAT_ENTIDAD_FEDERATIVA
        .find(item => item.id == this.form.get('domicilio.domicilioMexico.entidadFederativa.id').value);
      domicilio.domicilio.domicilioMexico.municipioAlcaldia = this.globals.catalogoMun
        .find(item => item.id == this.form.get('domicilio.domicilioMexico.municipioAlcaldia.id').value);
    } else if (domicilio.domicilio.ubicacion === 'EXTRANJERO') {
      domicilio.domicilio.domicilioExtranjero.pais = this.globals.catalogos.CAT_PAIS
        .find(item => item.id == this.form.get('domicilio.domicilioExtranjero.pais.id').value);
    }

    this.aceptar.emit(domicilio);

    setTimeout(() => {
      $("#nextSection").click();
    }, 1000);
  }

  selectUbicacion(ubicacion) {
    console.log(ubicacion);
    switch (ubicacion) {

      case "MEXICO":

        this.form.get('domicilio.domicilioExtranjero.calle').setValue('');
        this.form.get('domicilio.domicilioExtranjero.ciudadLocalidad').setValue('');
        this.form.get('domicilio.domicilioExtranjero.codigoPostal').setValue('');
        this.form.get('domicilio.domicilioExtranjero.estadoProvincia').setValue('');
        this.form.get('domicilio.domicilioExtranjero.numeroExterior').setValue('');
        this.form.get('domicilio.domicilioExtranjero.numeroInterior').setValue('');
        this.form.get('domicilio.domicilioExtranjero.pais.id').setValue(null);

        break;

      case "EXTRANJERO":

        this.form.get('domicilio.domicilioMexico.calle').setValue('');
        this.form.get('domicilio.domicilioMexico.codigoPostal').setValue('');
        this.form.get('domicilio.domicilioMexico.coloniaLocalidad').setValue('');
        this.form.get('domicilio.domicilioMexico.entidadFederativa.id').setValue(null);
        this.form.get('domicilio.domicilioMexico.municipioAlcaldia.id').setValue(null);
        this.form.get('domicilio.domicilioMexico.numeroExterior').setValue('');
        this.form.get('domicilio.domicilioMexico.numeroInterior').setValue('');

        break;
    }
  }

  abrirObservaciones() {
    $('body').addClass('disabled-onepage-scroll')
    $("#obs-domicilio").css({ "display": "flex" });
    $(".menu-nav").css({ "display": "none" });
    $("#content-btnSave").css({ "display": "none" });
    this.aclaraObs = this.form.get('aclaracionesObservaciones').value;
  }

  cerrarObservaciones() {
    this.aclara = this.form.get('aclaracionesObservaciones').value;
    $("#obs-domicilio").css("display", "none");
    $(".menu-nav").css({ "display": "flex" });
    $("#content-btnSave").css({ "display": "flex" });
    this.form.get('aclaracionesObservaciones').setValue(this.aclaraObs);
    let dataAclara = JSON.parse(sessionStorage.getItem('domicilioDeclarante'));
    dataAclara.aclaracionesObservaciones = this.aclara;
    sessionStorage.setItem('domicilioDeclarante', JSON.stringify(dataAclara));
    this.acla.emit(dataAclara.aclaracionesObservaciones);
  }
  guardarObservaciones() {
    const val = $("#aclaracionesObservacionesDomicilio").val();
    console.log("valor obs ",val);
    this.form.get('aclaracionesObservaciones').setValue(val);
    
    $('#obs-domicilio').css('display', 'none');
    $('.menu-nav').css({ display: 'flex' });
    $('#content-btnSave').css({ display: 'flex' });

  }

  validateStatusForm() {
    if (!this.form.get('domicilio.domicilioMexico').valid || !this.form.get('domicilio.domicilioExtranjero').valid) {
      $(".item2").find("i").removeClass("foco-verde foco-naranja").addClass("foco-rojo");
    }
  }

  filterMunicipios(id, clear = true) {
    console.log(this.form.get('domicilio.domicilioMexico.municipioAlcaldia.id').value);
    this.filterMuns = this.globals.catalogoMun.filter(item => item.fk === Number(id));
    const mun = this.filterMuns.find(item => item.id == this.form.get('domicilio.domicilioMexico.municipioAlcaldia.id').value);
    const idMun = mun ? mun.id : null;
    this.form.get('domicilio.domicilioMexico.municipioAlcaldia.id').setValue(idMun);
  }

}
