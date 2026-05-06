import { Component, OnInit, ViewChild, Input } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import {
  FormBuilder,
  FormGroup,
  Validators,
  NgForm,
  FormControl
} from '@angular/forms';
import { SimpleGlobal } from 'ng2-simple-global';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { User } from '../../interfaces/user';
import { first } from 'rxjs/operators';
import { AlertService } from 'src/app/services/alert.service';

@Component({
  selector: 'app-recovery',
  templateUrl: './recovery.component.html',
  styleUrls: ['./recovery.component.css']
})
export class RecoveryPassComponent implements OnInit {
  curp: string;
  refeer: string;
  typeWindow: string;
  form: FormGroup;
  token: string;
  processSuccess = false;

  usuario: any = {
    confirmPass: '',
    pwdEnc: ''
  };

  confirmPwVar: string;
  submitted: boolean;
  loading: boolean;

  constructor(
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private sg: SimpleGlobal,
    private authenticationService: AuthenticationService,
    private alertService: AlertService
  ) {
    // leemos variables de get
    this.curp = this.route.snapshot.queryParams.curp;
    console.log(this.curp);
  }

  ngOnInit() {
    this.token = this.route.snapshot.queryParams.token;
    // removemos variables de get
    const uri = window.location.toString();
    if (uri.indexOf('?') > 0) {
      const cleanUri = uri.substring(0, uri.indexOf('?'));
      window.history.replaceState({}, document.title, cleanUri);
    }

    // asignamos referencia de vuelta en caso de typeWindow redirect
    if (this.typeWindow === '0' && this.route.snapshot.queryParams.t) {
      localStorage.setItem('refeer', document.referrer);
      this.refeer = document.referrer;
    }
  }

  onSubmit() {
    /*if (this.form.valid) {
console.log('valido');i
}*/
  }

  guarda(f: NgForm) {
    this.submitted = true;
    this.loading = true;
    if (f.invalid) {
      this.loading = false;
      console.log(f.controls.confirmEmailLaboral.errors);
      return;
    }

    this.loading = true;
    this.authenticationService
      .recoveryPass(this.curp, f.controls.pw.value, this.token)
      .pipe(first())
      .subscribe(
        data => {
          if (data.status === 200) {
            this.alertService.success(
              'Contraseña reestablecida, cierre está ventana y vaya al sistema donde desee iniciar sesión.'
            );
            this.processSuccess = true;
          }
        },
        error => {
          this.loading = false;
          if (error.status === 401) {
            this.alertService.error(
              'Token expirado, favor de iniciar sesión para reenviar correo de activación.');
            // this.alertService.error('Token expirado, vuelve a intentarlo');
          } else  {
            this.alertService.error(
              'Ha ocurrido un error, por favor comunicarse con el administrador'
            );
          }
        }
      );
  }
}
