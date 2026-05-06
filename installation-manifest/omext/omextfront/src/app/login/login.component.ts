import { Component, OnInit, AfterViewInit, ElementRef } from '@angular/core';

import { NgbModal, NgbModalConfig } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { first } from 'rxjs/operators';
import { EnteReceptorService } from '../services/ente-receptor.service';
import { SessionService } from '../services/session.service';
import { environment } from '../../environments/environment';
import { Globals } from '../globals';
declare var $: any;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  providers: [NgbModalConfig, NgbModal]
})
export class LoginComponent implements OnInit {

  form: FormGroup;
  path: any;
  enteImage: string;
  nombreEnte: string;
  cl: string;
  sk: string;
  curpLogin: string;
  flagEnte: boolean;
  constructor(private router: Router,
    private elementRef: ElementRef,
    private enteReceptorService: EnteReceptorService,
    private activatedRoute: ActivatedRoute,
    private sessionService: SessionService,
    private globals: Globals

  ) {
    this.cl = `${environment.ID_CLIENTE}`;
    this.sk = `${environment.SECRET_KEY}`;
    this.curpLogin = '';
    this.flagEnte = false;
    this.globals.component = '280419';
    console.log('login');
  }

  ngOnInit() {
    if (this.sessionService.currentUserValue) {
      this.router.navigate(['/inicio']);
    } else {
      let ente: any = null;
      if (this.activatedRoute.snapshot.url.length > 0) {
        ente = this.activatedRoute.snapshot.url[0].path === 'login' ? null : this.activatedRoute.snapshot.url[0].path;
      }
      this.enteReceptorService
        .access(ente)
        .pipe(first())
        .subscribe(
          data => {
            if (data.status === 200) {
              this.enteImage = data.body.frontInfo.imagenB64;

              this.nombreEnte = data.body.frontInfo.nombre;
              if (!this.nombreEnte) {
                // this.router.navigate(['/error']);
                this.router.navigate(['/error'], { queryParams: { error: 'badEnte' } });
              }
              sessionStorage.setItem('institucionReceptora', JSON.stringify(data.body.institucionReceptora));
              const enteReceptor = {
                enteImage: data.body.frontInfo.imagenB64,
                enteContext: ente,
                prefijo: data.body.frontInfo.prefijo
              }
              sessionStorage.setItem('enteReceptor', JSON.stringify(enteReceptor));
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
    s.src = environment.API_IDP;
    s.id = 'DnetScript';
    this.elementRef.nativeElement.appendChild(s);
  }

}
