import { AfterViewInit, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DeclaracionService } from '../../services/declaracion.service';
import { first } from 'rxjs/operators';
import { InstitucionReceptora } from '../../interfaces/institucionReceptora';
import { Globals } from 'src/app/globals';
import { SessionService } from '../../services/session.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';

@Component({
  selector: 'app-system-log-in',
  templateUrl: './system-log-in.component.html',
  styleUrls: ['./system-log-in.component.scss']
})
export class SystemLogInComponent implements OnInit {
  token: any;
  transaction: any;
  institucionReceptora: any;
  spinnerText: string;
  spinner = {
    fgsType: 'fading-circle',
    fgsColor: '#ae0c0c',
  };
  constructor( private route: ActivatedRoute,
               private declaracionService: DeclaracionService,
               private router: Router,
               private globals: Globals,
               private sessionService: SessionService,
               private readonly ngxService: NgxUiLoaderService

    ) {

      this.token = this.route.snapshot.queryParams.access_token;
      this.transaction = this.route.snapshot.queryParams.transaction;
      this.institucionReceptora = JSON.parse(sessionStorage.getItem('institucionReceptora'));
      this.spinnerText = 'Cargando información de usuario';
      this.ngxService.start();
      // removemos variables de get
      const uri = window.location.toString();
      if (uri.indexOf('?') > 0) {
        const cleanUri = uri.substring(0, uri.indexOf('?'));
        window.history.replaceState({}, document.title, cleanUri);
      }
    }

  ngOnInit() {
    
    console.log(this.institucionReceptora);
    // tslint:disable-next-line: max-line-length
    this.declaracionService
    .verificarDeclaracionesInicio(this.token, this.transaction, this.institucionReceptora)
    .pipe(first())
    .subscribe(
      data => {
        if (data.status === 200) {
          const respuesta = data.body.declaracion.respuesta;
          // this.globals.systemLog = data.body;
          if (respuesta.estatus) {
          // tslint:disable-next-line: max-line-length
          this.sessionService.login(respuesta.authorization, respuesta.datosPersonales);
          sessionStorage.setItem('systemLog', JSON.stringify(respuesta));
          this.router.navigate(['/inicio']);
          } else {
            this.router.navigate(['/error']);
          }
        }
      },
      error => {
        this.ngxService.stop();
        this.router.navigate(['/error'], { queryParams: { error: 'badInicio' }});
      }
      );

  }

}
