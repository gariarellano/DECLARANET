import { Component, OnInit } from '@angular/core';
import { CommonService } from 'src/app/services/common.service';
import { Router } from '@angular/router';
import { IdpService } from 'src/app/services/idp.service';
import { Globals } from 'src/app/globals';
import { ToastrService } from 'ngx-toastr';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-page',
  templateUrl: './perfiles.component.html',
  styleUrls: ['./perfiles.component.scss']
})
export class PerfilesComponent implements OnInit {

  profiles;
  selectedProfile;

  constructor(
    private commonService: CommonService,
    private router: Router,
    private idpService: IdpService,
    private globals: Globals,
    private toastService: ToastrService,
    private sessionService: SessionService

  ) { }

  ngOnInit() {
    this.selectedProfile = null;
    this.profiles = this.commonService.getValueSession('profiles');
  }

  // Set datos del perfil
  async setProfile() {
    const perfil = JSON.parse(JSON.stringify(this.selectedProfile));
    perfil.usuario = this.globals.usuario;
    this.idpService.seleccionarPerfil(perfil).subscribe(
      (dataSeleccionarPerfil) => {
        console.log({ dataSeleccionarPerfil });
        sessionStorage.setItem('Authorization', dataSeleccionarPerfil.headers.get('Authorization'));
        this.globals.setValueSession('profile', dataSeleccionarPerfil.body.perfil);
        this.globals.setValueSession('permisos', dataSeleccionarPerfil.body.perfil.permisos);
        this.router.navigate(['home']);

      },
      (error) => {
        this.toastService.error(
          `Error`,
          'No se pudo asignar perfil',
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
      });
    
  }

}
