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
@Component({
  selector: 'app-admin-usuarios',
  templateUrl: './admin-usuarios.component.html',
  styleUrls: ['./admin-usuarios.component.scss']
})
export class AdminUsuariosComponent implements OnInit {
  value: string;
  institucionReceptora: any;
  color: ThemePalette;
  activo: boolean;
  dataSource = null;
  perfilesList: Array<any>;
  perfilesSeleccionados: Array<any>;
  perfilesArray: any;
  perfilesArrayLimpio: Array<any>;
  perfil: string;
  perfilesInst: Array<any>;
  selectIdDNetNoLocalizado: string = '';
  columnas: string[] = ['nombre', 'select'];
  curpConsulta: string;
  users: any;
  user: any;
  perfilOpen: string;
  instOpen: any;
  idInst: number;
  instReceptora: any;
  nvoUsuario: boolean;
  saveModel: boolean;
  respaldoDatPers: any;
  constructor(private readonly enteReceptorService: EnteReceptorService, 
              private commonService: CommonService,
              public globals: Globals) {
    this.perfilesList = [
      { value: 'ADMINISTRADOR', text: 'ADMINISTRADOR' },
      { value: 'OIC', text: 'OIC' },
      { value: 'CONSULTA', text: 'CONSULTA' },
      { value: 'CONSULTA ADMIN', text: 'CONSULTA ADMIN' },
    ];
    this.color = 'warn';
    this.activo = true;
    this.perfilesArray = [];
    this.perfilesArrayLimpio = [];
    this.perfilesSeleccionados = [];
    this.perfilesInst = [];
    this.curpConsulta = null;
    this.saveModel = false;
    this.institucionReceptora = JSON.parse(sessionStorage.getItem('institucionReceptora'));
    this.getInstituciones(this.institucionReceptora);
  }


  checkCambiosPersona() {
    if (this.saveModel){
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
    this.instReceptora = JSON.parse(sessionStorage.getItem('institucionReceptora'));
    this.limpiaUsr(null);

    $('.spinner').fadeOut('slow');
    this.value = '';


  }

  agregarInstMultipleUser(element, update?) {

    if (!update) {
      const found = this.perfilesArray.find(inst => inst === element);
      if (!found) {
        this.perfilesArray.push(element);
      } else {
        Swal.fire(
          'Esta institución ya se ha agregado',
          'Selecciona una institución diferente',
          'error'
        );
      }
    } else {
      const objElement = {
        desc:  element.nombre,
        ur:  element.id,
        ramo: this.instReceptora.collName
      };
      const found = this.instOpen.find(user => user === objElement);
      if (!found) {
        this.instOpen.push(objElement);
      } else {
        Swal.fire(
          'Esta institución ya se ha agregado',
          'Selecciona una institución diferente',
          'error'
        );
      }

    }

  }
  agregarInst(element, update?) {

      const found = this.perfilesArray.find(inst => inst.ur === element.id);
      if (!found) {
        const objElement = {
          desc:  element.nombre,
          ur:  element.id,
          ramo: this.instReceptora.collName
        };
        this.perfilesArray.push(objElement);
      } else {
        Swal.fire(
          'Esta institución ya se ha agregado',
          'Selecciona una institución diferente',
          'error'
        );
      }
    }

  eliminarInst(element, inst?) {

    if (!inst) {
      this.perfilesArray.splice(element, 1);
    } else {
      // this.perfilesArray.splice(element, 1);

      // this.perfilesInst[this.idInst].splice(element, 1);
      this.perfilesInst[this.idInst].inst.splice(element, 1);

    }
  }

  getInstituciones(ente: any) {
    this.enteReceptorService.getInstituciones(ente)
      .pipe(first())
      .subscribe(
        data => {
          if (data.status === 200) {
            this.users = data.body.catalogo;
            this.dataSource = new MatTableDataSource(this.users);
          }
        },
        error => {

        }
      );
  }
  openInst(inst) {
    this.idInst = inst;
    // this.perfilOpen = this.perfilesInst[inst].inst;
    this.instOpen = this.perfilesInst[inst].inst;
    this.perfilOpen = this.perfilesInst[inst].profile;
    // this.instOpen = this.perfilOpen;
  }
  guardarPerfil() {

    this.perfilesArray.forEach((dato) => {
      this.perfilesArrayLimpio.push({ ramo: this.instReceptora.collName, ur: dato.id, desc: dato.nombre });
    });

    this.perfilesInst.push({
      profile: this.perfil,
      inst: this.perfilesArrayLimpio
    });
    this.perfilesSeleccionados.push(this.perfil);
    this.perfilesArray = [];
    this.perfilesArrayLimpio = [];
    this.perfil = '';

  }

  consultaUsuario() {
    this.commonService.consultaUsuario(this.curpConsulta).pipe(first()).subscribe(respuesta => {
      if (respuesta.data.getUser.curp) {
        // this.user.curp = respuesta.data.getUser.curp;
        this.user = respuesta.data.getUser;
        this.user.app = environment.API_NAME_PERFILAMIENTO;
        this.user.profiles = [];
        this.user.institutions.forEach(inst => {
          this.user.profiles.push(inst.profile);
        });
        this.perfil = this.user.profiles[0];
        this.perfilesArray = this.user.institutions[0].inst;

        // Se usa solo en los multiples perfiles
        // this.perfilesSeleccionados = this.user.profiles;
        // this.perfilesInst = this.user.institutions;
        this.nvoUsuario = false;
      } else {
        this.limpiaUsr(this.curpConsulta);
        this.nvoUsuario = true;
      }
    }, (error) => {
      console.log(error);
    });
  }

  asignaUsuarioPerfil(){
    this.perfilesArrayLimpio = [];
    this.user.profiles = [];
    this.user.institutions = [];
    this.perfilesArray.forEach((dato) => {
      this.perfilesArrayLimpio.push({ ramo: this.instReceptora.collName, ur: dato.ur, desc: dato.desc });
    });
    this.user.institutions.push({
      profile: this.perfil,
      inst: this.perfilesArrayLimpio
    });
    this.user.profiles.push(this.perfil);
    this.perfilesArray = [];
    this.perfilesArrayLimpio = [];
    this.perfil = '';

  }

guardarUsuario() {

    // Para el caso de solo un perfil solo usamos el asignaUsuarioPerfil
    this.asignaUsuarioPerfil();

    // Se usa solo en los multiples perfiles
    // this.user.profiles = this.perfilesSeleccionados;
    // this.user.institutions = this.perfilesInst;
    this.user.instancia = String(this.instReceptora.collName);

    if (this.nvoUsuario) {

      this.commonService.createUsuario(this.user).pipe(first()).subscribe(respuesta => {
        if (respuesta.data.registerUser.msj == 'Usuario registrado exitosamente') {
          Swal.fire(
            '',
            respuesta.data.registerUser.msj,
            'success'
          );
        } else {
          Swal.fire(
            '',
            respuesta.data.registerUser.msj,
            'warning'
          );
        }
        this.cancelarUsuario();
      }, (error) => {
        console.log(error);
      });
    } else {
      const updateUser = {
        user: this.user.user,
        email: this.user.email,
        profiles: this.user.profiles,
        institutions: this.user.institutions,
        instancia: this.user.instancia,
        active: this.user.active
      };
      this.commonService.updateUsuario(updateUser, this.user.curp).pipe(first()).subscribe(respuesta => {
        if (respuesta.data.updateUser.msj === 'ok') {
          Swal.fire(
            '',
            'Usuario actualizado exitosamente',
            'success'
          );
        } else {
          Swal.fire(
            '',
            respuesta.data.updateUser.msj,
            'warning'
          );
        }
        this.cancelarUsuario();
    
      }, (error) => {
        console.log(error);
      });

    }

  }

  cancelarUsuario(){
    this.limpiaUsr(null);
    this.curpConsulta = null;
    this.saveModel = false;
    this.respaldoDatPers = null;
  }

filtrar(event: Event) {
    const filtro = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filtro.trim().toLowerCase();
  }

limpiaUsr(curp: string) {
    this.user = {
      app: environment.API_NAME_PERFILAMIENTO,
      user: null,
      curp: curp || null,
      email: null,
      active: true,
      instancia: String(this.instReceptora.collName),

      profiles: [],
      institutions: []
    };
    this.perfilesArray = [];
    this.perfil = null;
  }
}
