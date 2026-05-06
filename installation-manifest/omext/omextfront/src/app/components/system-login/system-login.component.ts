import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
//import { DeclaracionService } from '../../services/declaracion.service';
import { first } from 'rxjs/operators';
import { CommonService } from '../../services/common.service';
//import { InstitucionReceptora } from '../../interfaces/institucionReceptora';
//import { Globals } from 'src/app/globals';
import { environment } from '../../../environments/environment';
import { SessionService } from '../../services/session.service';
import { IdpService } from './../../services/idp.service';
import { Globals } from 'src/app/globals';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-system-login',
  templateUrl: './system-login.component.html',
  styleUrls: ['./system-login.component.scss'],
})
export class SystemLoginComponent implements OnInit {
  headersVar: any;
  token: any;
  transaction: any;
  institucionReceptora: any;
  transPerfil: boolean;
  transIdp: boolean;
  usuarioToken: any;

  constructor(
    private route: ActivatedRoute,
    private idpService: IdpService,
    private router: Router,
    private sessionService: SessionService,
    private commonService: CommonService,
    private globals: Globals,
    private toastService: ToastrService
  ) {
    this.globals.component = 'System-log-in';
    this.headersVar = {
      token: this.route.snapshot.queryParams.access_token,
      transaction: this.route.snapshot.queryParams.transaction,
    };
    this.institucionReceptora = JSON.parse(
      sessionStorage.getItem('institucionReceptora')
    );

    // removemos variables de get
    const uri = window.location.toString();
    if (uri.indexOf('?') > 0) {
      const cleanUri = uri.substring(0, uri.indexOf('?'));
      window.history.replaceState({}, document.title, cleanUri);
    }
  }


  ngOnInit() {
    this.usuarioToken = {
      accesToken: this.headersVar.token,
      transaction: this.headersVar.transaction,
      collName: this.institucionReceptora.collName
    };

    this.idpService.iniciarToken(this.usuarioToken).subscribe(
      (dataIniciarToken) => {
        console.log( { respuestaAsignacion: dataIniciarToken });
        if (dataIniciarToken.status === 200) {
          if(!dataIniciarToken.body.asignaciones.perfiles) {
            this.logout({type: 'Error', text: 'No existe el usuario en esta aplicación.'});
            return;
          }
          if (!dataIniciarToken.body.asignaciones.activo) {
            this.logout({type: 'Error', text: 'Usuario inactivo.'});
            return;
          }
          sessionStorage.setItem('Authorization', dataIniciarToken.headers.get('Authorization'));
          this.globals.setValueSession('tokenInicial', dataIniciarToken.body);
          const usuario = this.globals.pick(dataIniciarToken.body.usuario, ['curp', 'idUsuario', 'nombre', 'primerApellido', 'segundoApellido', 'rfc', 'homoclave', 'email']);
          this.globals.setValueSession('usuario', usuario);
          this.globals.setValueSession('entes', dataIniciarToken.body.asignaciones.entes);






          this.router.navigate(['inicio']);


        }
          },
      (error) => {
        
        console.log(error);
        this.logout({type: 'Error', text: 'Error al consultar datos del usuario'})
      }
    );

    }

  pick(obj, keys) {
    return keys.map(k => k in obj ? {
      [k]: obj[k]
    } : {})
      .reduce((res, o) => Object.assign(res, o), {});
  }

  cambioTransaction() {
    if (this.transIdp && this.transPerfil) {
      this.router.navigate(['inicio']);
    }
  }
  logout(errorMsj: any) {
    this.toastService.error(
      errorMsj.text,
      errorMsj.type,
      {
        timeOut: 2000,
        extendedTimeOut: 800,
        closeButton: true,
        tapToDismiss: true,
        progressBar: true
      }
    );
    const navOut = JSON.parse(sessionStorage.getItem('enteReceptor')).enteContext || '';
    this.sessionService.logout();
    this.router.navigate([navOut]);
    // tslint:disable-next-line:max-line-length
    // window.location.href = window.location.protocol + '//' + window.location.hostname + (window.location.port !== '' ? ':' + window.location.port : '') + '/' + navOut;
  }

}
