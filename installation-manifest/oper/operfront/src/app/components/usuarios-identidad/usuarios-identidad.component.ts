import { Component, OnInit, ViewChild } from '@angular/core';
import { UsuarioEdit } from '../../interfaces/usuario';
import { UsuarioIdentidadService } from '../../services/usuario-identidad.service';
// import {MatSnackBar} from '@angular/material/snack-bar';
import { first } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { Globals } from 'src/app/globals';

declare var pick: any;

@Component({
  selector: 'app-usuarios-identidad',
  templateUrl: './usuarios-identidad.component.html',
  styleUrls: ['./usuarios-identidad.component.scss']
})
export class UsuariosIdentidadComponent implements OnInit {
  usuarioEdit: UsuarioEdit;
  curpConsulta: string;
  usuarioExiste: boolean;
  tokenLogin: string;
  curpAnt: string;
  loading: boolean;
  rfc: string;
  nombre: string;
  primerApellido: string;
  segundoApellido: string;
  resetear: boolean;
  collName: number;

  @ViewChild('formConsulta', { static: false }) formConsulta: NgForm;
  @ViewChild('formEditCurp', { static: false }) formEditCurp: NgForm;
  @ViewChild('formEditEmail', { static: false }) formEditEmail: NgForm;
  constructor(
    private usuarioService: UsuarioIdentidadService,
    // private _snackBar: MatSnackBar,
    private toastService: ToastrService,
    private readonly router: Router,
    private globals: Globals,

  ) {
    sessionStorage.removeItem('authUE');
    this.curpConsulta = null;
    this.resetUsuario();
    this.usuarioExiste = false;
    this.curpAnt = null;
    this.loading = false;
    const ins = JSON.parse(sessionStorage.getItem('institucionReceptora'));
    this.collName = ins.collName;
    if (this.collName !== 100) {
      this.router.navigate(['home']);
    }
    const permiso = this.globals.permisos.find(x => x.pagina === 1);
    const resetear = permiso.acciones.indexOf('RESETEARPWD');
    this.resetear = (resetear !== -1);
  }

  ngOnInit(): void {
  }


  resetUsuario() {
    this.usuarioEdit = {
      curp: null,
      email: null,
      emailAlt: null,
      idUsuario: null,
      nombre: null,
      primerApellido: null,
      segundoApellido: null,
    };


  }
  consultaCurp() {
    this.loading = true;
    if (!this.formConsulta.valid) {
      this.loading = false;
      return;
    }
    this.usuarioService
      .getUsuarioByCurp(this.curpConsulta)
      .pipe(first())
      .subscribe(
        data => {
          if (data.status === 200) {
            this.usuarioExiste = true;
            this.curpAnt = data.body.curp;
            this.usuarioEdit = pick(data.body, ['curp', 'email', 'idUsuario', 'nombre', 'primerApellido', 'segundoApellido', 'emailAlt']);
            const Authorization = data.headers.get('Authorization');
            console.log("FEr", data);

            sessionStorage.setItem('authUE', 'Bearer ' + data.headers.get('authorization'));
            this.loading = false;
          } else if (data.status === 204) {
            this.toastService.error(
              `Favor de verificar`,
              'CURP ó RFC no encontrados',
              {
                timeOut: 2000,
                extendedTimeOut: 800,
                closeButton: true,
                tapToDismiss: true,
                progressBar: true
              }
            );
            this.loading = false;

            // console.log('Curp válido');
            // this.updateUser();
          }
        },
        error => {
          console.log('Error', error);
          this.loading = false;
        }
      );
  }

  generarTempPass() {
    this.loading = true;
    const obj = {
      idUsuario: this.usuarioEdit.idUsuario,
      curp: this.usuarioEdit.curp
    };
    this.usuarioService
      .recoveryTempPass(obj.curp, obj.idUsuario)
      .pipe(first())
      .subscribe(
        dataRecovery => {
          // if (dataRecovery.status === 200) {
          this.toastService.success(
            'Actualización completa',
            'Contraseña restablecida correctamente',
            {
              timeOut: 2000,
              extendedTimeOut: 800,
              closeButton: true,
              tapToDismiss: true,
              progressBar: true
            }
          );
          this.formConsulta.resetForm();
          this.cancelar();
          // }
          this.loading = false;
        },
        error => {
          console.log('Error', error);
          this.loading = false;
          this.toastService.error(
            `Error al generar`,
            'El password no fue generado, reintente nuevamente',
            {
              timeOut: 2000,
              extendedTimeOut: 800,
              closeButton: true,
              tapToDismiss: true,
              progressBar: true
            }
          );
        }
      );

  }

  editaUsuario(tipoEdicion: string) {
    let obj: any;
    switch (tipoEdicion) {
      case 'curp':
        if (!this.formEditCurp.valid) {
          return;
        }
        obj = {
          idUsuario: this.usuarioEdit.idUsuario,
          curp: this.usuarioEdit.curp
        };
        break;
      case 'email':
        if (!this.formEditEmail.valid) {
          return;
        }
        if (this.usuarioEdit.email === this.usuarioEdit.emailAlt) {
          this.toastService.error(
            `Favor de verificar`,
            'Los correos principal y alterno no pueden ser los mismos.',
            {
              timeOut: 2000,
              extendedTimeOut: 800,
              closeButton: true,
              tapToDismiss: true,
              progressBar: true
            }
          );
          return;
        }
        obj = {
          idUsuario: this.usuarioEdit.idUsuario,
          email: this.usuarioEdit.email,
          emailAlt: this.usuarioEdit.emailAlt
        };
        break;
    }
    this.loading = true;
    this.usuarioService
      .edit(obj, this.curpAnt)
      .pipe(first())
      .subscribe(
        dataEdit => {
          if (dataEdit.status === 200) {
            // this._snackBar.open('Usuario actualizado correctamente', 'cerrar', {
            //   duration: 2000,
            //   verticalPosition: 'top'
            // });
            this.toastService.success(
              'Actualización completa',
              'Usuario actualizado correctamente',
              {
                timeOut: 2000,
                extendedTimeOut: 800,
                closeButton: true,
                tapToDismiss: true,
                progressBar: true
              }
            );
            this.formConsulta.resetForm();
            this.cancelar();
          }
          this.loading = false;
        },
        error => {
          console.log('Error', error);
          this.loading = false;
          if (error.status === 409) {
            this.toastService.error(
              `Favor de verificar`,
              'La CURP nueva que deseas modificar ya existe en la base de usuarios',
              {
                timeOut: 2000,
                extendedTimeOut: 800,
                closeButton: true,
                tapToDismiss: true,
                progressBar: true
              }
            );
          }
        }
      );
  }
  cancelar() {
    this.resetUsuario();
    this.usuarioExiste = false;
  }

}

