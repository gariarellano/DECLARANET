import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { User, UserLogin } from '../../../interfaces/user';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { first } from 'rxjs/operators';
import { NgForm } from '@angular/forms';
import { AlertService } from '../../../services/alert.service';
import { UsuarioService } from 'src/app/services/usuario.service';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class UserEditComponent implements OnInit, AfterViewInit {
  @ViewChild('modalButton') myModalButton: ElementRef;
  // @ViewChild('registroForm', {static: false}) registroForm: any;
  // @ViewChild(NgForm) registroForm: any;
  @ViewChild('registroForm', { read: NgForm }) registroForm: { statusChanges: { subscribe: (arg0: (change: any) => void) => void; }; dirty: any; value: any; valueChanges: { subscribe: (arg0: (changedValue: any) => void) => void; }; };
  @ViewChild('registroForm', { read: NgForm }) form: any;
  clienteId: string;
  secretId: string;
  usuario: User;
  usuarioLogin: UserLogin;
  originalValue: any;
  hasChanged: boolean;
  currentUserSubscription: Subscription;
  submitted = false;
  loading = false;
  idAnt: string;
  status: string;
  padreURLTypeWindow2: string;

  constructor(
    private authenticationService: AuthenticationService,
    private route: ActivatedRoute,
    private alertService: AlertService,
    private usuarioService: UsuarioService
    ) {
    this.currentUserSubscription = this.authenticationService.currentUser.subscribe(user => {
        this.usuarioLogin = user;
    });
    this.idAnt = this.usuarioLogin.curp;

}

  ngOnInit() {
    this.clienteId = this.route.snapshot.queryParams.c;
    this.secretId = this.route.snapshot.queryParams.s;
    this.status = this.route.snapshot.queryParams.stat;
    this.status = this.route.snapshot.queryParams.stat;
    this.padreURLTypeWindow2 = this.route.snapshot.queryParams.pU || null;
    // console.log(this.clienteId);
    // console.log(this.secretId);
    // console.log(this.form);
    // console.log(this.myModalButton);
    // this.myModalButton.nativeElement.click();
    this.authenticationService.getUser(this.idAnt, this.clienteId, this.secretId) .pipe(first())
      .subscribe(
      usr => {
        this.usuario = usr.body;
      },
      error => {
        console.log(error);
        console.log('Error en la asignación de sesión');
      });

    const uri = window.location.toString();
    if (uri.indexOf('?') > 0) {
          const cleanUri = uri.substring(0, uri.indexOf('?'));
          window.history.replaceState({}, document.title, cleanUri);
        }
      }

    ngAfterViewInit() {

      const interv = setInterval(() => {
        if (this.usuario) {
        // this returns null
        this.registroForm.statusChanges.subscribe( change => {
          if (!this.registroForm.dirty) {
              this.originalValue = JSON.stringify(this.registroForm.value);
              this.hasChanged = false;
          }
        });

        this.registroForm.valueChanges.subscribe(changedValue => {
          if (this.registroForm.dirty) {
              const currenValue = JSON.stringify(this.registroForm.value);
              if (this.originalValue !== currenValue) {
                this.hasChanged = true;
              }
            }
        });
        clearInterval(interv);
      }
    }, 100);


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
                  console.log('Error obteniendo valor para ir a login');
                }
              );
  }
  guarda(f: NgForm) {
    this.submitted = true;
    this.loading = true;
    if (!this.hasChanged) {
      this.loading = false;
      this.alertService.error('Sin cambios');
      return;
    }
    if (!this.clienteId || !this.secretId) {
      this.loading = false;
      this.alertService.error('No existen parámetros de solicitud de retorno');
      return;
    }
    if (f.invalid) {
      this.loading = false;
      return;
    }
    if (this.idAnt !== this.usuario.curp) {

      const ctrls = f.controls;
      this.usuarioService
        .validateDuplicateCurp(ctrls.curp.value)
        .pipe(first())
        .subscribe(
          data => {
            if (data.status === 200) {
              if (data.body.codigo === 1) {
                this.alertService.error('El CURP ya se encuentra registrado en este servicio, si no recuerda sus credenciales intente recuperar contraseña en el login.');
                this.loading = false;
              } else if (data.body.codigo === 0) {
                console.log('Curp válido');
                this.updateUser();
              }
            }
          },
          error => {
            this.loading = false;
            this.alertService.error(
                'Ha ocurrido un error en la validación de RFC y CURP.'
              );
            }
        );

  } else {
    this.updateUser();
  }

  }

  updateUser() {
    this.usuarioService
    .edit(this.usuario, this.status, this.idAnt)
    .pipe(first())
    .subscribe(
      dataEdit => {
        if (dataEdit.status === 200) {
          console.log('Ok');
          this.myModalButton.nativeElement.click();
          this.goToLogin(7000);
        }
      },
      error => {
        console.log('Error');
      }
    );

  }


}
