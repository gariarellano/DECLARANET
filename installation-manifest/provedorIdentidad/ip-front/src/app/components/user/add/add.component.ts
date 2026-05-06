import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators, NgForm } from '@angular/forms';
// import { ValidateRfc } from '../../validators/rfc.validator';
// import { ValidateCurp } from '../../validators/curp.validator';
// import { matchOtherValidator } from '../../validators/match.validator';
import { SimpleGlobal } from 'ng2-simple-global';
import { AppComponent } from '../../../app.component';
import { UsuarioService } from '../../../services/usuario.service';
import { first } from 'rxjs/operators';
import { AlertService } from '../../../services/alert.service';
import { Usuario } from 'src/app/interfaces/usuario';
import { Button } from 'protractor';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component ({
  selector: 'app-user-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.css'],
})
export class UserAddComponent implements OnInit {
  // add ViewChild
  @ViewChild('modalButton') myModalButton: ElementRef;
  @ViewChild('modalErrorButton') myModalErrorButton: ElementRef;
  @ViewChild('registroForm') registroForm: any;
  // @ViewChild('rfcCurpForm') rfcCurpForm: any;
  // rfcCurpForm: FormGroup;
  // registroForm: FormGroup;
  usuarioNuevo = false;
  submitted = false;
  loading = false;
  primerVal = false;
  btnRegisterText = 'Registrar';
  clienteId: string;
  secretId: string;
  activateMailSentMsg: string;
  usuario: Usuario;
  padreURLTypeWindow2: string;
  confirmCelVar: string;
  confirmEmailLaboralVar: string;
  confirmEmailPersonalVar: string;
  confirmPwVar: string;

  constructor(
    private formBuilder: FormBuilder,
    private sg: SimpleGlobal,
    private appcomponent: AppComponent,
    private usuarioService: UsuarioService,
    private alertService: AlertService,
    private authenticationService: AuthenticationService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.usuario = {
      pwdEnc: '',
      nombre: '',
      primerApellido: '',
      segundoApellido: '',
      curp: '',
      rfc: '',
      homoclave: '',
      numCelular: '',
      email: '',
      emailAlt: ''
    };

  }

  ngOnInit() {
    // tslint:disable-next-line:no-string-literal
    this.sg['innerWidth'] = window.innerWidth;
    // tslint:disable-next-line:no-string-literal
    this.sg['innerHeight'] = window.innerHeight;
    this.appcomponent.checkResponsive();
    // leemos variables de get
    this.clienteId = this.route.snapshot.queryParams.c || sessionStorage.getItem('clienteId');
    this.secretId = this.route.snapshot.queryParams.s || sessionStorage.getItem('secretId');
    this.padreURLTypeWindow2 = this.route.snapshot.queryParams.pU || null;
    if (!this.clienteId || !this.secretId) {
      const er: string = encodeURI('{"error": "Acceso no Autorizado a agregar usuario", "redirect": "0"}');
      this.router.navigateByUrl('/error?k=' + er);
    }
    sessionStorage.setItem('clienteId', this.clienteId);
    sessionStorage.setItem('secretId', this.secretId);

    const uri = window.location.toString();
    if (uri.indexOf('?') > 0) {
          const cleanUri = uri.substring(0, uri.indexOf('?'));
          window.history.replaceState({}, document.title, cleanUri);
        }
  }



  get r() {
    return this.registroForm.controls;
  }

  validateRfcCurp(f: NgForm) {
    // this.submitted = true;
    this.primerVal = true;
    if (f.invalid) {
      console.log(f.controls.rfc.errors);
      return;
    }
    this.loading = true;
    this.usuarioService
      .validateDuplicate(this.r.curp.value , this.r.rfc.value + this.r.homo_cve.value)
      .pipe(first())
      .subscribe(
        data => {
          if (data.status === 200) {
            if (data.body.codigo === 1) {
              this.alertService.error('El CURP y/o RFC ya se encuentra registrado en este servicio, si no recuerda sus credenciales intente recuperar contraseña en el login.');
              this.usuarioNuevo = false;
              this.submitted = false;
            } else if (data.body.codigo === 0) {
            this.usuarioNuevo = true;
            this.submitted = false;
            this.alertService.clear();
            }
          }
          this.loading = false;
        },
        error => {
          this.loading = false;
          this.alertService.error(
              'Ha ocurrido un error en la validación de RFC y CURP.'
            );
          }
      );
  }
  guarda(f: NgForm) {
    this.submitted = true;
    this.loading = true;
    if (!this.clienteId || !this.secretId) {
      this.loading = false;
      this.alertService.error('No existen parámetros de solicitud de retorno');
      return;
    }
    if (f.invalid) {
      this.loading = false;
      // console.log(f.controls.confirmEmailLaboral.errors);
      return;
    }
    if (this.usuario.email.trim() === this.usuario.emailAlt.trim()) {
      this.loading = false;
      this.myModalErrorButton.nativeElement.click();
      return;
    }
    this.btnRegisterText = 'Registrando';
    this.usuarioService
      .register(this.usuario)
      .pipe(first())
      .subscribe(
        data => {
          if (data.status === 200) {
            console.log('Ok');
            this.myModalButton.nativeElement.click();
            this.sendActivationMail();
            this.btnRegisterText = 'Redireccionando';
            this.goToLogin(7000);
          }
        },
        error => {
          console.log('Error');
        }
      );
  }

  goToLogin(tiempo: number) {
    this.loading = true;
    if (this.padreURLTypeWindow2) {
      setTimeout(() => {
        window.location.assign(this.padreURLTypeWindow2);
      }, tiempo);
      return;
    }
    let urlNva: string;
    this.authenticationService
              .getAuth(this.clienteId, this.secretId)
              .pipe(first())
              .subscribe(
                auth => {
                  if (auth.status === 200) {
                    if (auth.body.url) {
                      const Authorization = auth.headers.get('Authorization');
                      console.log(Authorization);
                      urlNva = auth.body.url + '?k=' + Authorization + '&c=' + this.clienteId + '&s=' + this.secretId;
                    } else {
                      urlNva =
                        auth.body + '?k=' + encodeURIComponent(JSON.stringify(''));
                    }
                    setTimeout(() => {
                      window.location.assign(urlNva);
                    }, tiempo);
                  }
                },
                error => {
                  console.log('Error');
                }
              );
  }

  sendActivationMail() {
    // const usr = this.usuario.rfc + this.usuario.homoclave;
    const usr = this.usuario.curp;
    console.log('envio de correo a ' + usr);

    // servicio reenvió de activación
    this.authenticationService
      .sendActivationMail(usr)
      .pipe(first())
      .subscribe(
        data => {
          if (data.status === 200) {

            let activateServ: any;
            activateServ = data;
          }
        },
        error => {
          this.loading = false;
          this.activateMailSentMsg = 'Error en envío, reintente iniciando sesión';
            }
      );
  }
}

