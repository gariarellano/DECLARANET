import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';

import { AuthenticationService } from '../../services/authentication.service';
import { AlertService } from '../../services/alert.service';
import { SimpleGlobal } from 'ng2-simple-global';
import { UsuarioService } from '../../services/usuario.service';
import { environment } from 'src/environments/environment';
declare var $: any;


@Component({
  templateUrl: 'login.component.html',
  styleUrls: ['login.component.css']
})
export class LoginComponent implements OnInit {
  // variables a usar en el componente
  loginForm: FormGroup;
  vincForm: FormGroup;
  validateUsrForm: FormGroup;
  recoveryPasswordForm: FormGroup;
  sincronizaTelegram: FormGroup;
  loading = false;
  submitted = false;
  returnUrl: string;
  token: string;
  authserv: any;
  valserv: any;
  recPassServ: any;
  callback: string;
  codeUsr = 0; // div visible segun situacion
  tokenLogin: string;
  typeWindow: string;
  aplicacion: string;
  renapoEstatus: number;
  msjRenapoEstatus: string;
  refeer: string;
  intentos = 0;
  transaccion: string;
  clienteId: string;
  secretId: string;
  usrImported: string;
  recoverPassEmail = false;
  recoveryText: string;
  resendActivationMailTextButton: string;
  resendCodeTelegramButton: string;
  msjRedirect: string;
  timeOut: any;
  textCustomLogin: string;
  padreURL: string;
  telegramUrl = environment.TELEGRAM_URL;
  secretKeyCaptcha = environment.SECRET_RECAPTCHA;
  @ViewChild('modalButton') myModalButton: ElementRef;
  @ViewChild('modalButtonTelegram') modalButtonTelegram: ElementRef;
  @ViewChild('closeModalButton') closeModalButton: ElementRef;
  @ViewChild('closeModalTelegram') closeModalTelegram: ElementRef;
  idUsuario: any;
  loadingResendCodeTelegramButton: boolean;

  constructor(
    // Instancias de las librerías importadas
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private usuarioService: UsuarioService,
    private alertService: AlertService,
    private activatedRoute: ActivatedRoute,
    private sg: SimpleGlobal
  ) {
    // Validamos si se encuentra con sesión activa
    // if (this.authenticationService.currentUserValue) {
    //   this.router.navigate(['/']);
    // }
  }

  ngOnInit() {
    // leemos variables de get
    this.token = this.route.snapshot.queryParams.k;
    this.typeWindow =
      this.route.snapshot.queryParams.t || sessionStorage.getItem('typeWindow'); // 0:redirect, 1:popup, 2:embebido
    sessionStorage.setItem('typeWindow', this.typeWindow);
    this.clienteId = this.route.snapshot.queryParams.c;
    this.secretId = this.route.snapshot.queryParams.s;
    this.usrImported = this.route.snapshot.queryParams.u ? this.route.snapshot.queryParams.u : '';
    this.textCustomLogin = this.route.snapshot.queryParams.txtC ? this.route.snapshot.queryParams.txtC : '';
    const bgColor = this.route.snapshot.queryParams.bg ? this.route.snapshot.queryParams.bg : null;
    const bgImage = this.route.snapshot.queryParams.bgI ? this.route.snapshot.queryParams.bgI : null;
    this.padreURL = this.route.snapshot.queryParams.pU ? this.route.snapshot.queryParams.pU : null;
    console.log('bg', bgColor);
    if (bgColor) {
      document.body.style.backgroundColor = `#${bgColor}`;
    }
    if (bgImage) {
      // document.body.style.backgroundColor = `#${bgColor}`;
      document.body.style.backgroundImage = `url(${bgImage})`;
    }
    // validamos que exista token


    // asignamos referencia de vuelta en caso de typeWindow redirect
    if ((this.typeWindow === '0' || this.typeWindow === '2') && this.route.snapshot.queryParams.t) {
      const refPath = document.referrer + (this.route.snapshot.queryParams.p || '');
      sessionStorage.setItem('refeer', refPath);
    }
    this.refeer = sessionStorage.getItem('refeer');


    if (!this.token) {
      const er: string = encodeURI('{"error": "Acceso no autorizado"}');
      this.router.navigateByUrl('/error?k=' + er);
    }

    // agregamos usuario importado si existe


    // removemos variables de get
    const uri = window.location.toString();
    if (uri.indexOf('?') > 0) {
      const cleanUri = uri.substring(0, uri.indexOf('?'));
      window.history.replaceState({}, document.title, cleanUri);
    }

    this.loginForm = this.formBuilder.group({
      username: [this.usrImported, [Validators.minLength(15), Validators.required]],
      password: ['', Validators.required]
    });

    this.validateUsrForm = this.formBuilder.group({
      token: ['', Validators.required]
    });

    this.recoveryPasswordForm = this.formBuilder.group({
      usernameRecovery: ['', Validators.required],
      recaptchaReactive: new FormControl(null)

    });
    this.sincronizaTelegram = this.formBuilder.group({
      codigoTelegram: ['', Validators.required]
    });

    if(this.secretKeyCaptcha) {
      this.recoveryPasswordForm.controls.recaptchaReactive.setValidators([Validators.required]);

    }
    this.resendActivationMailTextButton = 'Reenvíar correo de activación';
    this.resendCodeTelegramButton = 'Reenviar código';

    this.authenticationService.logout();
  }

  // obtenemos por una variable, valores de formulario
  get f() {
    return this.loginForm.controls;
  }
  get valForm() {
    return this.validateUsrForm.controls;
  }
  get recForm() {
    return this.recoveryPasswordForm.controls;
  }
  get sincTelForm() {
    return this.sincronizaTelegram.controls;
  }

  // submit
  onSubmit() {
    this.submitted = true;
    // validacion de formulario
    if (this.loginForm.invalid) {
      console.log(this.f.username.errors);

      return;
    }

    this.loading = true;
    this.authenticationService
      .login(this.f.username.value, this.f.password.value, this.token, this.clienteId, this.secretId)
      .pipe(first())
      .subscribe(
        data => {
          if (data.status === 200) {
            this.authserv = data;
            // this.loading = false;
            switch (this.authserv.body.codigo) {
              // login correcto
              case 1: {
                this.transaccion = this.authserv.body.transaccion;
                this.tokenLogin = this.authserv.headers.get('Authorization');
                this.callback =
                  this.getReviewUrl(this.authserv.body.url) +
                  '?access_token=' +
                  this.tokenLogin +
                  '&transaction=' +
                  this.transaccion;
                this.alertService.success(
                  'Acceso permitido. Redireccionando...'
                );
                this.renapoEstatus = this.authserv.body.estatus;
                let timeWait = 1500;

                if (this.renapoEstatus === 100) {
                  this.codeUsr = 100.1
                  this.loading = false;
                  this.alertService.clear();


                } else {
                  let counter = 7;
                  if (this.renapoEstatus === 3) {
                    timeWait = ((counter * 1000) + 1500);
                    setInterval(() => {
                      counter--;
                      if (counter >= 0) {
                        this.msjRedirect = 'Redireccionando en ' + counter + ' segundos';
                      }
                      if (counter === 0) {
                        this.msjRedirect = 'Redireccionando...';
                      }
                    }, 1000);

                    // this.renapoEstatus === 3 ? this.myModalButton.nativeElement.click() : this.modalButtonTelegram.nativeElement.click();
                    this.myModalButton.nativeElement.click();

                  }

                  // if (this.typeWindow !== '0') {
                  //   window.opener.location.href = this.callback;
                  //   this.timeOut = setTimeout(() => {
                  //     window.close();
                  //   }, timeWait);
                  // } else {
                  //   this.timeOut = setTimeout(() => {
                  //     window.location.replace(this.callback);
                  //   }, timeWait);
                  // }
                  this.redirectCorrect(timeWait);
                }

                break;
              }
              // usuario sin vincular
              case 2: {
                this.renapoEstatus = this.authserv.body.estatus;
                this.aplicacion = this.authserv.body.aplicacion;
                this.codeUsr = 2; // Usuario sin vincular al sistema cliente
                this.tokenLogin = this.authserv.headers.get('Authorization');
                this.transaccion = this.authserv.body.transaccion;
                this.loading = false;
                break;
              }
              // credenciales invalidas
              case 3: {
                this.loading = false;
                this.alertService.error('CURP y/o contraseña incorrectos');
                this.intentos += 1;
                if (this.intentos >= 3) {
                  console.log('Salir');
                  // this.salir('Intentos excedidos');
                  this.recoveryText = 'Se han superado el número de intentos permitidos, si deseas recuperar tu contraseña, favor de ingresar tu CURP nuevamente:';
                  this.codeUsr = 3; // recuperar contraseña
                }
                break;
              }
              case 4: {
                // this.codeUsr = 4.1; // Usuario sin activar en IP y activación por código
                this.codeUsr = 4; // Usuario sin activar en IP y activación por liga
                this.submitted = false;
                this.loading = false;
                break;
              }
              case 5: {
                this.loading = false;
                this.codeUsr = 5; // Problemas de validación con RENAPO
                this.renapoEstatus = this.authserv.body.estatus;
                switch (this.renapoEstatus) {
                  case 0:
                    this.msjRenapoEstatus = 'No se ha podido validar la existencia de su CURP en RENAPO, favor de intentarlo mas tarde.';
                    break;
                  case 2:
                    this.msjRenapoEstatus = 'La CURP registrada no se ha encontrado en los datos de RENAPO favor de corregir en la siguiente liga:';
                    break;
                  // case 3:
                  //   this.msjRenapoEstatus = 'Los datos registrados tienen inconsistencias de acuerdo al CURP registrado en RENAPO, favor de modificar para no recibir esta notificación, o puede simplemente "Omitir y continuar"';
                  //   break;
                }
                break;
              }


            }
          } else if (data.status === 204) {
            // usuario inexistente
            this.loading = false;
            this.codeUsr = 2; // Usuario inexistente
            // this.alertService.error('Usuario inexistente');
          }
        },
        error => {
          this.loading = false;
          if (error.status === 401) {
            this.salir('Token expirado, vuelve a intentarlo');
            // this.alertService.error('Token expirado, vuelve a intentarlo');
          } else {
            this.alertService.error(
              'Ha ocurrido un error, por favor comunicarse con el administrador'
            );
          }
        }
      );
  }

  forgotPass() {
    this.loginForm.reset();
    this.recoveryText = 'Favor de ingresar tu CURP para solicitar recuperación de contraseña:';
    this.codeUsr = 3; // recuperar contraseña
  }

  // Aceptar vinculacion
  onVincSubmit() {
    this.submitted = true;
    this.loading = true;
    // servicio vinculacion, sync o el otro nombre que quieran ponerle
    this.authenticationService
      .sync(this.tokenLogin)
      .pipe(first())
      .subscribe(
        data => {
          if (data.status === 200) {
            this.authserv = data;
            console.log(this.authserv);
            if (!this.authserv.body.error) {
              // const token: string = this.authserv.headers.get('Authorization');
              this.callback =
                this.getReviewUrl(this.authserv.body.url) +
                '?access_token=' +
                // token +
                this.tokenLogin +
                '&transaction=' +
                this.transaccion;
              this.alertService.success('Acceso permitido. Redireccionando...');
              // if (this.typeWindow !== '0') {
              //   window.opener.location.href = this.callback;
              //   setTimeout(() => {
              //     window.close();
              //   }, 1500);
              // } else {
              //   setTimeout(() => {
              //     window.location.replace(this.callback);
              //   }, 1500);
              // }
              this.redirectCorrect(1500);
            } else {
              this.alertService.error(this.authserv.body.error);
              this.loading = false;

            }
          }
        },
        error => {
          this.loading = false;
          if (error.status === 0) {
            this.salir('Token expirado, vuelve a intentarlo');
          } else {
            this.alertService.error(
              'Ha ocurrido un error, por favor comunicarse con el administrador'
            );
          }
        }
      );
  }

  aceptoSincroTelegram(acepto: boolean, intentos: number = null) {
    this.loading = true;
    if (acepto) {
      if (this.timeOut) {
        clearTimeout(this.timeOut);
      }
      this.sincronizaTelegram.reset();
      this.authenticationService
        .generateOtp(this.tokenLogin)
        .pipe(first())
        .subscribe(
          data => {
            this.idUsuario = data.body.idUsuario;
            this.codeUsr = 100
            // this.intentos = intentos === null ? this.intentos : intentos;
            this.intentos = 0;
            this.submitted = false;
            this.loading = false;
            this.alertService.success(
              'Código enviado por telegram'
            );
            let counter = 90;
            this.loadingResendCodeTelegramButton = true;
            setInterval(() => {
              counter--;
              if (counter >= 0) {
                this.resendCodeTelegramButton = counter + ' segundos';
              }
              if (counter === 0) {
                this.submitted = false;
                this.loadingResendCodeTelegramButton = false;
                this.resendCodeTelegramButton = 'Reenviar código';
              }
            }, 1000);
          }, error => {
            if (error.status === 401) {
              this.salir('Token expirado, vuelve a intentarlo');
            } else if (error.status === 403) {
              this.loading = false;
              this.codeUsr = 100.3;
            } else {
            this.alertService.error(
              'Error al validar código.'
            );
            this.loading = false;
          }
          });

    } else {
      let counter = 3;
      setInterval(() => {
        counter--;
        if (counter >= 0) {
          this.msjRedirect = 'Redireccionando en ' + counter + ' segundos';
        }
        if (counter === 0) {
          this.msjRedirect = 'Redireccionando...';
          this.hurryRedirect();
        }
      }, 1000);
    }
  }

  sendTokenTelegram(valor) {

    this.submitted = true;
    if (this.sincronizaTelegram.invalid) {
      return;
    }
    this.loading = true;
    this.alertService.clear();
    this.intentos += 1;
    this.authenticationService
      .validateOtp(this.idUsuario, valor)
      .pipe(first())
      .subscribe(
        data => {
          console.log(data);
          if (data.status === 200 && data.body.localizado) {
            this.alertService.success(
              'Sincronización completa... Redireccionando'
            );
            this.redirectCorrect(1500);
          }
          if (data.status === 204) {
            const x = 3 - this.intentos;
            if (x <= 0) {
              this.alertService.error(
                'Se han agotado los intentos permitidos para este código, para continuar deberás reenviar un nuevo código o presionar Cancelar para ingresar al sistema'
              );
            } else {
              this.alertService.error(
                'Código erróneo, intente de nuevo \n Intentos restantes: ' + x
              );
            }
            this.loading = false;

          }
        }, error => {
          this.alertService.error(
            'Error al validar código.'
          );
          this.loading = false;

        });

  }

  cancelTelegram() {
    this.modalButtonTelegram.nativeElement.click();
  };

  eliminaSincroTelegram() {
    this.authenticationService
      .eliminasincronizacion(this.tokenLogin)
      .pipe(first())
      .subscribe(
        data => {
          if (data.status === 200) {
            this.alertService.success(
              'Sincronización cancelada... Redireccionando'
            );
            this.redirectCorrect(1500);
          }
        }, error => {



        });
  }



  // validateUsr() {
  //   this.submitted = true;
  //   this.loading = true;
  //   const validateCode = this.valForm.token.value;
  //   const usr = this.f.username.value;
  //   // servicio vinculacion, sync o el otro nombre que quieran ponerle
  //   this.usuarioService
  //     .activateCode(validateCode, usr)
  //     .pipe(first())
  //     .subscribe(
  //       data => {
  //         if (data.status === 200) {
  //           this.valserv = data;
  //           console.log(this.valserv);
  //           this.loading = false;
  //           this.authenticationService
  //           .getAuth(this.clienteId, this.secretId)
  //           .pipe(first())
  //           .subscribe(
  //             auth => {
  //               if (auth.status === 200) {
  //                 if (auth.body.url) {
  //                   this.token = auth.headers.get('Authorization');
  //                   this.onSubmit();
  //                 } else {
  //                   this.alertService.error('El usuario fue activado pero el intento de acceso fue erróneo, favor de reintentar');
  //                 }
  //               }
  //             },
  //             error => {
  //               this.alertService.error('El usuario fue activado pero el intento de acceso fue erróneo, favor de reintentar');
  //             }
  //           );
  //         } else {
  //           this.alertService.error(this.authserv.body.error);
  //         }
  //       },
  //       error => {
  //         this.loading = false;
  //         if (error.status === 0) {
  //           this.salir('Token expirado, vuelve a intentarlo');
  //         } else {
  //           if (error.error.text === 'No existe') {
  //             this.alertService.error('Código incorrecto, favor de reintentar');
  //           } else {
  //             this.alertService.error(
  //               'Ha ocurrido un error, por favor comunicarse con el administrador'
  //             );
  //           }
  //         }
  //       }
  //     );
  // }
  // status 5, recuperar contraseña
  recoveryPassword() {
    this.submitted = true;
    if (this.recoveryPasswordForm.invalid) {
      return;
    }
    this.loading = true;

    const usernameRecover = this.recForm.usernameRecovery.value;
    const usr = this.f.username.value;
    let emailrecover: string;
    if (usernameRecover !== usr && usr) {
      this.alertService.error('El CURP ingresado no corresponde al CURP que intentó iniciar sesión, favor de capturarlo nuevamente o cancele el proceso e intente de nuevo.');
      this.loading = false;
      // this.submitted = true;
      return;
    }
    // servicio vinculacion, sync o el otro nombre que quieran ponerle
    this.authenticationService
      .recoveryPassMail(usernameRecover)
      .pipe(first())
      .subscribe(
        data => {
          if (data.status === 200) {
            this.recPassServ = data;
            emailrecover = this.recPassServ.body.email;
            this.recoverPassEmail = true;
            this.loading = false;
            this.alertService.success('Solicitud enviada');
          } else if (data.status === 204) {
            this.alertService.error('No se encontró usuario para recuperación');
            this.loading = false;
          }
        },
        error => {
          this.loading = false;
          this.alertService.error(
            'Ha ocurrido un error, por favor comunicarse con el administrador'
          );
        }
      );
  }
  resendActivationMail() {
    this.submitted = true;

    this.loading = true;
    const usr = this.f.username.value;

    // servicio reenvió de activación
    this.authenticationService
      .sendActivationMail(usr)
      .pipe(first())
      .subscribe(
        data => {
          if (data.status === 200) {
            this.recPassServ = data;
            let counter = 90;
            setInterval(() => {
              counter--;
              if (counter >= 0) {
                this.resendActivationMailTextButton = 'Reintentar en ' + counter + ' segundos';
              }
              if (counter === 0) {
                this.submitted = false;
                this.loading = false;
                this.resendActivationMailTextButton = 'Reenvíar correo de activación';
              }
            }, 1000);
          }
        },
        error => {
          this.loading = false;
          this.alertService.error(
            'Ha ocurrido un error, por favor comunicarse con el administrador'
          );
        }
      );
  }
  // salir al sistema
  cancel(msj: string = 'Proceso cancelado') {
    this.salir(msj);
  }

  backToHome() {
    this.recoveryPasswordForm.reset();
    this.codeUsr = 0;
  }
  backTotelegramSendToken() {
    this.codeUsr = 100.1;
  }

  edit(status: number) {
    if (this.timeOut) {
      clearTimeout(this.timeOut);
    }
    this.closeModalButton.nativeElement.click();


    if (this.typeWindow === '2') {
      const parsedUrl = new URL(window.location.href);
      const baseUrl = parsedUrl.origin;
      this.padreURL = encodeURIComponent(this.padreURL);
      window.top.location.href = `${baseUrl}/user/edit?c=${this.clienteId}&s=${this.secretId}&stat=${status}&pU=${this.padreURL}`;
      // console.log(baseUrl);
    } else {
      this.router.navigate(['/user/edit'], {
        queryParams: { c: this.clienteId, s: this.secretId, stat: status }
      });
    }
  }
  hurryRedirect() {
    if (this.timeOut) {
      clearTimeout(this.timeOut);
    }
    this.closeModalButton.nativeElement.click();
    this.closeModalTelegram.nativeElement.click();
    this.redirectCorrect(500);
  }

  redirectCorrect(timeWait: number) {
    switch (this.typeWindow) {
      case '0':
        this.timeOut = setTimeout(() => {
          window.location.replace(this.callback);
        }, timeWait);
        break;
      case '1':
        window.opener.location.href = this.callback;
        this.timeOut = setTimeout(() => {
          window.close();
        }, timeWait);
        break;
      case '2':
        this.timeOut = setTimeout(() => {
          window.top.location.href = this.callback;
        }, timeWait);
    }

  }

  // funcion para usar en caso de salir del login sin éxito
  salir(msj: string) {
    this.loading = true;

    switch (this.typeWindow) {
      case '0':
        this.alertService.error(msj + '. Redireccionando....');
        setTimeout(() => {
          window.location.replace(this.refeer);
        }, 1500);
        break;
      case '1':
        this.alertService.error(msj + '. Esta pagina se cerrará.');
        setTimeout(() => {
          window.close();
        }, 1500);
        break;
      case '2':
        this.alertService.error(msj + '. Refrescando....');
        this.timeOut = setTimeout(() => {
          window.top.location.href = this.refeer;
        }, 1500);
    }
  }

  // crear usuario
  createUser() {
    //  this.router.navigate(['/registro', { order: 'popular', 'price-range': 'not-cheap' }]);

    if (this.typeWindow === '2') {
      const parsedUrl = new URL(window.location.href);
      const baseUrl = parsedUrl.origin;
      this.padreURL = encodeURIComponent(this.padreURL);
      window.top.location.href = `${baseUrl}/user/add?c=${this.clienteId}&s=${this.secretId}&pU=${this.padreURL}`;
      // console.log(baseUrl);
    } else {
      this.router.navigate(['/user/add'], {
        queryParams: { c: this.clienteId, s: this.secretId }
      });
    }
  }

  getReviewUrl(url) {
    let ambiente: string;
    ambiente = window.location.hostname;
    const newUrl = new URL(url);
    if (ambiente === 'identidad-test.k8s.sfp.gob.mx' || ambiente === 'localhost') {
      const a: any = new URL(this.refeer);
      newUrl.protocol = a.protocol;
      newUrl.hostname = a.hostname;
      newUrl.port = a.port;
    }
    return newUrl.href;

  }
}
