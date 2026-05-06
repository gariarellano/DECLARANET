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
  selector: 'app-user-edit-admin',
  templateUrl: './user-edit-admin.component.html',
  styleUrls: ['./user-edit-admin.component.scss']
})
export class UserEditAdminComponent implements OnInit {
  usuarioEdit: UsuarioEdit;
  curpConsulta: string;
  usuarioExiste: boolean;
  tokenLogin: string;
  curpAnt: string;
  loading: boolean;
  rfc: string;
  nombre: string; 
  primerApellido:string; 
  segundoApellido: string
  collName: number;
  tipoValidacion: any;
  tipoBusqueda: Array<any>;
  valorCheckTipoValidacion:string;


  @ViewChild('formConsulta', {static: false}) formConsulta: NgForm;
  @ViewChild('formEditCurp', {static: false}) formEditCurp: NgForm;
  @ViewChild('formEditEmail', {static: false}) formEditEmail: NgForm;
  constructor(
    private usuarioService: UsuarioIdentidadService,
    // private _snackBar: MatSnackBar,
    private toastService: ToastrService, 
    private readonly router: Router,
    public globals: Globals
  ) {
    sessionStorage.removeItem('authUE');
    this.curpConsulta = null;
    this.resetUsuario();
    this.usuarioExiste = false;
    this.curpAnt = null;
    this.loading = false;
    const ins = JSON.parse(sessionStorage.getItem('institucionReceptora'));
    this.collName = ins.collName;
    this.tipoBusqueda = [
      {
        id: 'nombre',
        texto: 'Nombre',
        max: 30,
        min: 4,
        placeholder:'JUAN PEREZ',
        pattern: /^((\b[a-zA-ZÑñÁÉÍÓÚáéíóú]{2,})\s*){2,7}$/
      },
      {
        id: 'curp',
        texto: 'CURP',
        max: 18,
        min: 6,
        placeholder:'XXXX010101XXXXXX01',
        // pattern: /^([A-Z][AEIOUX][A-Z]{2}\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\d])(\d)$/
        pattern: /^([A-Z][AEIOUX][A-Z]{2})(\d{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])([HM])(AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)([B-DF-HJ-NP-TV-Z]{3})([A-Z\d])(\d)$|^([A-Z][AEIOUX][A-Z]{2})(\d{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])([HM])(AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)([B-DF-HJ-NP-TV-Z]{3})$|^([A-Z][AEIOUX][A-Z]{2})(\d{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])([HM])(AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)$|^([A-Z][AEIOUX][A-Z]{2})(\d{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])([HM])$|^([A-Z][AEIOUX][A-Z]{2})(\d{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])$|^([A-Z][AEIOUX][A-Z]{2})(\d{2})(0[1-9]|1[0-2])$|^([A-Z][AEIOUX][A-Z]{2})(\d{2})$/
      },
      {
        id: 'rfc',
        texto: 'RFC',
        max: 13,
        min: 6,
        placeholder:'XXXX010101XX0',
        // pattern: /^([A-ZÑ\x26]{4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[A-Z\d]{3})?$/
        pattern: /^([A-ZÑ\x26]{4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[A-Z\d]{3})$|^([A-ZÑ\x26]{4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1]))$|^([A-ZÑ\x26]{4}([0-9]{2})(0[1-9]|1[0-2]))$|^([A-ZÑ\x26]{4}([0-9]{2}))$/
      }
      ];
    this.tipoValidacion = {
      id: 'nombre',
      texto: 'Nombre',
      max: 30,
      min: 4,
      placeholder:'JUAN PEREZ',
      pattern: /^((\b[a-zA-ZÑñÁÉÍÓÚáéíóú]{2,})\s*){2,7}$/
    };
    this.valorCheckTipoValidacion = 'nombre';
    if (this.collName !== 100) {
      this.router.navigate(['home']);
    }

    
  }

  tipoBusquedaClear() {
    console.log(this.valorCheckTipoValidacion);
    this.tipoValidacion = this.tipoBusqueda.find(b => b.id === this.valorCheckTipoValidacion);
    this.curpConsulta = null;
  }

  ngOnInit(): void {    
  }

  busquedaUsuario(){

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
        if (this.usuarioEdit.email === this.usuarioEdit.emailAlt){
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

