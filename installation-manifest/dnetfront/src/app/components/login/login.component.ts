import { Component, OnInit, AfterViewInit, ElementRef } from '@angular/core';

import { NgbModal, NgbModalConfig, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { first } from 'rxjs/operators';
import { EnteReceptorService } from '../../services/ente-receptor.service';
import { SessionService } from '../../services/session.service';
import { environment } from '../../../environments/environment';
import { Globals } from '../../globals';
declare var $: any;


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  providers: [NgbModalConfig, NgbModal]
})

export class LoginComponent implements OnInit, AfterViewInit {
  form: FormGroup;
  path: any;
  enteImage: string;
  nombreEnte: string;
  cl: string;
  sk: string;
  curpLogin: string;
  flagEnte: boolean;
  e: boolean;
  msjLoginInicio: string;
  constructor(private router: Router,
              private elementRef: ElementRef,
              private enteReceptorService: EnteReceptorService,
              private activatedRoute: ActivatedRoute,
              private sessionService: SessionService,
              private globals: Globals
    ) {
      console.log('constructor');
      this.cl = `${environment.ID_CLIENTE}`;
      this.sk = `${environment.SECRET_KEY}`;
      this.curpLogin = '';
      this.flagEnte = false;
  }

  ngOnInit() {
    this.globals.component = '030612';
    console.log('routes', this.activatedRoute);
    if (this.sessionService.currentUserValue) {
      this.router.navigate(['/inicio']);
    } else {
    let ente: any = null;
    if (this.activatedRoute.snapshot.url.length > 0) {
      ente = this.activatedRoute.snapshot.url[0].path === 'login' ? null : this.activatedRoute.snapshot.url[0].path;
    }
    // if (ente === null) {
    //  $('#modalProrrogaMsj'). modal('show');
    // }
    console.log('servicio ente');
    this.enteReceptorService
      .access(ente)
      .pipe(first())
      .subscribe(
        data => {
          if (data.status === 200) {
            console.log(data.body);
            this.enteImage = data.body.frontInfo.imagenB64;
            this.nombreEnte = data.body.frontInfo.nombre;
            if (!this.nombreEnte) {
              // this.router.navigate(['/error']);
              this.router.navigate(['/error'], { queryParams: { error: 'badEnte' }});
            }
            sessionStorage.setItem('institucionReceptora', JSON.stringify(data.body.institucionReceptora));
            const enteReceptor = {
              enteImage : data.body.frontInfo.imagenB64,
              enteContext: ente,
              prefijo: data.body.frontInfo.prefijo,
              parametrosEspecificos: data.body.frontInfo.parametrosEspecificos || null
            }
            sessionStorage.setItem('enteReceptor', JSON.stringify(enteReceptor));
            this.mensajeLoginInicio();
            this.flagEnte = true;
          }
        },
        error => {
          alert("Error de comunicación... Favor de intentarlo mas tarde.");
        }
        );
 }
}

  ngAfterViewInit() {
    const s = document.createElement('script');
    s.type = 'text/javascript';
    // s.src = 'https://dgti-ejz-ip-front-staging.200.34.175.120.nip.io/assets/plugin/DnetTool.js';
    s.src = environment.API_IDP;
    // s.src = 'https://70-review-testing-7qeaae.200.34.175.120.nip.io/assets/plugin/DnetTool.js';
    s.id = 'DnetScript';
    this.elementRef.nativeElement.appendChild(s);
  }

  mensajeLoginInicio() {
    const ente = sessionStorage.getItem('enteReceptor') ? JSON.parse(sessionStorage.getItem('enteReceptor')) : null;
    if (ente.parametrosEspecificos && ente.parametrosEspecificos.hasOwnProperty('avisoLogin') && ente.parametrosEspecificos.avisoLogin !== null) {
      if (ente.parametrosEspecificos.avisoLogin !== '') {
        this.msjLoginInicio = this.globals.htmlDecode(ente.parametrosEspecificos.avisoLogin);
        $('#modalLoginInicio').modal('show');
      }
    } else {
      this.msjLoginInicio = `
  <span class="alerticonModal">
      <h5><b>Teléfono de atención para la presentación de la declaración de modificación patrimonial a través de Declara<span class="DnetRojo font-weight-bold">Net</span>:</b>
  </h5>
  </span>


<p class="text-black">
      A fin de poder asesorarte sobre la presentación de tu declaración patrimonial y de intereses, la Secretaría Contraloría pone a tu servicio* el número telefónico <b>435 556 0292</b>
  </p>
  
  <p></p>
  <p class="text-black" style="font-size: 11px;">
      * De lunes a viernes de 09:00a 17:00 horas.
  </p>`;
      $('#modalLoginInicio').modal('show');
    }
  }
}
