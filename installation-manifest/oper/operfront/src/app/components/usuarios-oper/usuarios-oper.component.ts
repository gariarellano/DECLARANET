import { Component, OnInit } from '@angular/core';
import * as $ from 'jquery';
import { ThemePalette } from '@angular/material/core';
import { MatTableDataSource } from '@angular/material/table';
import Swal from 'sweetalert2';
import { first } from 'rxjs/operators';
import { EnteReceptorService } from 'src/app/services/ente-receptor.service';
import { CommonService } from 'src/app/services/common.service';
import { environment } from 'src/environments/environment';
import { Globals } from 'src/app/globals';
import { UsuarioOperService } from '../../services/usuario-oper.service';
import { IdpService } from 'src/app/services/idp.service';

@Component({
  selector: 'app-usuarios-oper',
  templateUrl: './usuarios-oper.component.html',
  styleUrls: ['./usuarios-oper.component.scss']
})

export class UsuariosOperComponent implements OnInit {
  // value: string;
  institucionReceptora: any;
  color: ThemePalette;
  activo: boolean;
  valorConsulta: string;
  datosLocalizados: any;
  valorCheckTipoValidacion: string;
  usuarioExiste: boolean;
  loading: boolean;
  tipoBusqueda: Array<any>;
  tipoValidacion: any;
  saveModel: boolean;
  respaldoDatPers: any;
  user: any;
  dataSource = null;
  proceso: string;
  perfilesList: Array<any>;
  // perfilesSeleccionados: Array<any>;
  perfilesArray: any;
  // perfilesArrayLimpio: Array<any>;
  perfilSeleccionado: boolean;
  encontroPermisos: boolean;
  // perfilesInst: Array<any>;
  // selectIdDNetNoLocalizado: string = '';
  // columnas: string[] = ['nombre', 'select'];
  // users: any;
  // perfilOpen: string;
  // instOpen: any;
  // idInst: number;
  // instReceptora: any;

  constructor(private readonly enteReceptorService: EnteReceptorService,
    private commonService: CommonService,
    public globals: Globals,
    private usuariosOperService: UsuarioOperService,
    private idpService: IdpService) {
    this.color = 'warn';
    this.activo = true;
    this.saveModel = false;
    // // this.perfilesArray = [];
    // // this.perfilesArrayLimpio = [];
    // // this.perfilesSeleccionados = [];
    // this.perfilesInst = [];
    this.valorConsulta = null;
    this.usuarioExiste = false;
    this.loading = false;
    this.institucionReceptora = JSON.parse(sessionStorage.getItem('institucionReceptora'));
    this.proceso = 'Inicio';
    this.tipoBusqueda = [
      {
        id: 'nombre',
        texto: 'Nombre',
        max: 30,
        min: 4,
        placeholder: 'JUAN PEREZ',
        pattern: /^((\b[a-zA-ZÑñÁÉÍÓÚáéíóú]{2,})\s*){2,7}$/
      },
      {
        id: 'curp',
        texto: 'CURP',
        max: 18,
        min: 6,
        placeholder: 'XXXX010101XXXXXX01',
        // pattern: /^([A-Z][AEIOUX][A-Z]{2}\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\d])(\d)$/
        pattern: /^([A-Z][AEIOUX][A-Z]{2})(\d{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])([HM])(AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)([B-DF-HJ-NP-TV-Z]{3})([A-Z\d])(\d)$|^([A-Z][AEIOUX][A-Z]{2})(\d{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])([HM])(AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)([B-DF-HJ-NP-TV-Z]{3})$|^([A-Z][AEIOUX][A-Z]{2})(\d{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])([HM])(AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)$|^([A-Z][AEIOUX][A-Z]{2})(\d{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])([HM])$|^([A-Z][AEIOUX][A-Z]{2})(\d{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])$|^([A-Z][AEIOUX][A-Z]{2})(\d{2})(0[1-9]|1[0-2])$|^([A-Z][AEIOUX][A-Z]{2})(\d{2})$/
      },
      {
        id: 'rfc',
        texto: 'RFC',
        max: 13,
        min: 6,
        placeholder: 'XXXX010101XX0',
        // pattern: /^([A-ZÑ\x26]{4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[A-Z\d]{3})?$/
        pattern: /^([A-ZÑ\x26]{4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[A-Z\d]{3})$|^([A-ZÑ\x26]{4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1]))$|^([A-ZÑ\x26]{4}([0-9]{2})(0[1-9]|1[0-2]))$|^([A-ZÑ\x26]{4}([0-9]{2}))$/
      }
    ];
    this.tipoValidacion = {
      id: 'nombre',
      texto: 'Nombre',
      max: 30,
      min: 4,
      placeholder: 'JUAN PEREZ',
      pattern: /^((\b[a-zA-ZÑñÁÉÍÓÚáéíóú]{2,})\s*){2,7}$/
    };
    this.valorCheckTipoValidacion = 'nombre';
    this.cargaPerfiles();
  }

  tipoBusquedaClear() {
    console.log(this.valorCheckTipoValidacion);
    this.tipoValidacion = this.tipoBusqueda.find(b => b.id === this.valorCheckTipoValidacion);
    this.valorConsulta = null;
    document.getElementById('valorConsulta').focus();
  }

  busquedaUsuario() {
    this.loading = true;
    this.usuariosOperService
      .getUsuarioByField(this.tipoValidacion.id, this.valorConsulta)
      .pipe(first())
      .subscribe(
        data => {
          console.log(data);
          if (data.status === 200) {

            this.loading = false;
            this.proceso = 'Buscando';
            this.datosLocalizados = data.body.datosLocalizados;

          }
        },
        error => {
          console.log('Error', error);
          this.loading = false;
        }
      );
  }

  checkCambiosPersona() {
    if (this.saveModel) {
      this.respaldoDatPers = {
        user: this.user.user,
        email: this.user.email
      };
    } else {
      this.user.user = this.respaldoDatPers.user;
      this.user.email = this.respaldoDatPers.email;
    }
  }

  ngOnInit(): void {
    this.limpiaUsr(null);

    $('.spinner').fadeOut('slow');

    // this.value = '';


  }

  cargaPerfiles() {
    this.usuariosOperService
      .getPerfiles(this.institucionReceptora.collName)
      .pipe(first())
      .subscribe(
        data => {
          console.log(data);
          //  this.loading = false;
          if (data.status === 200) {
            this.perfilesList = data.body;
            this.perfilesList.forEach(perfil => {
              perfil.checked = false;
            });
          }
        },
        error => {
          console.log('Error', error);
          this.loading = false;
        }
      );
  }
  checkPerfiles() {
    const x = this.perfilesList.find(perfil => perfil.checked);
    this.perfilSeleccionado = x !== undefined;
  }

  consultaUsuario(usuario) {
    if (usuario) {
      this.getUserbyId(usuario);
    } else {
      this.proceso = 'nuevoCURP';
      this.limpiaUsr(null);
    }

  }
  consultarPermisos(curp) {
    this.usuariosOperService
      .getUsuarioByField('curp', curp.value)
      .pipe(first())
      .subscribe(
        data => {
          console.log(data);
          if (data.status === 200) {
            this.loading = false;
            this.datosLocalizados = data.body.datosLocalizados;
            if (this.datosLocalizados.length > 0) {
              const usrLocalizado = this.datosLocalizados[0];
              this.getUserbyId(usrLocalizado);
            } else {
              const usuario = {
                curp: curp.value,
                idUsuario: 0
              };
              this.getUserbyId(usuario);
            }
          }
        },
        error => {
          console.log('Error', error);
          this.loading = false;
        }
      );
  }

  getUserbyId(usuario: any) {
    this.idpService.getUserbyId(usuario, this.institucionReceptora.collName)
      .pipe(first()).subscribe(data => {
        console.log(data);
        if (data.body.length > 0) {
          const userLoaded = data.body[0];
          this.user = {
            rfc: usuario.rfc,
            curp: usuario.curp,
            nombre: `${usuario.nombre} ${usuario.primerApellido} ${usuario.segundoApellido}`,
            idUsuario: userLoaded.idUsuario,
            perfiles: userLoaded.perfiles,
            activo: userLoaded.activo,
            _id: userLoaded._id,
          };
          this.user.perfiles.forEach(p => {
            const perfil = this.perfilesList.find(per => per.perfil === p.perfil);
            if (perfil) {
              perfil.checked = true;
            }
          });
          if (this.proceso === 'nuevoCURP') {
            Swal.fire({
              icon: 'info',
              text: 'Permisos encontrados para este usuario.',
              showConfirmButton: true,
              timer: 1500
            });
          }
          this.encontroPermisos = true;
          this.checkPerfiles();
        } else {
          this.limpiaUsr(usuario.curp);
          this.encontroPermisos = false;
          this.checkPerfiles();

        }
        this.proceso = 'Editando';
      }, error => {

      });

  }


  guardarUsuario() {

    const perfiles: Array<any> = [];
    this.perfilesList.forEach(e => {
      if (e.checked) {
        perfiles.push({ perfil: e.perfil });
      }
    });
    if (this.user.idUsuario || this.encontroPermisos) { // Actualizar
      let body = {
        curp: this.user.idUsuario ? null : this.user.curp,
        _id: this.user._id,
        perfiles,
        activo: this.user.activo,
        idUsuario: this.user.idUsuario
      };
      body = this.globals.clearObj(body);
      this.idpService.updateUser(body, this.institucionReceptora.collName).pipe(first())
        .subscribe(data => {
          console.log(data);
          if (data.status === 202) {
            Swal.fire(
              '',
              'Usuario actualizado exitosamente',
              'success'
            );
            this.cancelarUsuario();
          } else {
            Swal.fire(
              '',
              'Error al actualizar usuario',
              'warning'
            );
          }
        }
          , error => {
            console.log(error);
            Swal.fire(
              '',
              'Error al actualizar usuario',
              'warning'
            );
          });
    } else {
      let body = {
        perfiles,
        activo: this.user.activo,
        idUsuario: this.user.idUsuario
      };
      body = this.globals.clearObj(body);
      this.idpService.createUser(body, this.institucionReceptora.collName, this.user.curp).pipe(first())
        .subscribe(data => {
          console.log(data);
          if (data.status === 201) {
            Swal.fire(
              '',
              'Usuario registrado exitosamente',
              'success'
            );
            this.cancelarUsuario();
          } else {
            Swal.fire(
              '',
              'Error al registrar usuario',
              'warning'
            );
          }
        }, error => {
          console.log(error);
          Swal.fire(
            '',
            'Error al actualizar usuario',
            'warning'
          );
        });

    }
  }

  cancelarUsuario() {
    this.limpiaUsr(null);
    this.valorConsulta = null;
    this.proceso = 'Inicio';
    this.saveModel = false;
    this.respaldoDatPers = null;
  }

  filtrar(event: Event) {
    const filtro = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filtro.trim().toLowerCase();
  }

  limpiaUsr(curp) {
    if (this.perfilesList) {
    this.perfilesList.forEach(element => {
      element.checked = false;
    });
  }
    this.user = {
      rfc: null,
      curp,
      nombre: null,
      idUsuario: null,
      perfiles: null,
      activo: true
    };
    this.perfilSeleccionado = false;

  }
}
