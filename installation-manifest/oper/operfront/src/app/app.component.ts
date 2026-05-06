import { Component, OnInit, AfterViewInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { EnteReceptorService } from './services/ente-receptor.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
// import { AppComponent } from 'src/app/app.component';
import { first } from 'rxjs/operators';
import { UsuarioIdentidadService } from 'src/app/services/usuario-identidad.service';
import { NgForm } from '@angular/forms';
import { CommonService } from './services/common.service';
import { Globals } from './globals';
import { Idle } from '@ng-idle/core';
import { Keepalive } from '@ng-idle/keepalive';
import { ModalDirective } from 'ngx-bootstrap/modal';

//declare var $: any;
import * as $ from 'jquery';
// import { setInterval } from 'timers';
import { interval } from 'rxjs';
import { SessionService } from './services/session.service';
import { IdpService } from './services/idp.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, AfterViewInit {
  title = 'operfront';

  idleState = 'Not started.';
  timedOut = false;
  lastPing?: Date = null;

  enteContext: string;
  enteImage: string;
  nombreEnte: string;
  flagEnte: boolean;
  interval: NodeJS.Timeout;
  msjCloseModal: string;
  spinnerText: string;
  curp: string;
  collName: number;
  user: string;
  enteReceptor: any;
  enteImagen: string;
  @ViewChild('childModal') childModal: ModalDirective;
  pages: any[];
  showMenu: boolean;


  constructor(
    private readonly keepalive: Keepalive,
    private readonly activatedRoute: ActivatedRoute,
    private commonService: CommonService,
    private readonly enteReceptorService: EnteReceptorService,
    private readonly ngxService: NgxUiLoaderService,
    private readonly router: Router,
    private readonly sessionService: SessionService,
    public globals: Globals,
    private readonly idle: Idle,
    private idpService: IdpService) {
    this.enteContext = '';
    this.collName = 0;
    this.enteReceptor = JSON.parse(sessionStorage.getItem('enteReceptor'));
    if (this.enteReceptor) {
      this.enteImagen = this.enteReceptor.enteImage;
    }
    // sets an idle timeout of 5 seconds, for testing purposes.
    idle.setIdle(780);
    // idle.setIdle(30);
    // sets a timeout period of 5 seconds. after 10 seconds of inactivity, the user will be considered timed out.
    idle.setTimeout(60);
    // sets the default interrupts, in this case, things like clicks, scrolls, touches to the document
    // idle.setInterrupts(DEFAULT_INTERRUPTSOURCES);

    idle.onTimeout.subscribe(() => {
      this.idleState = 'Timed out!';
      this.timedOut = true;
      // console.log(this.idleState);
      this.childModal.hide();
      const navOut = JSON.parse(sessionStorage.getItem('enteReceptor')).enteContext || '';
      this.sessionService.logout();
      // tslint:disable-next-line:max-line-length
      window.location.href = window.location.protocol + '//' + window.location.hostname + (window.location.port !== '' ? ':' + window.location.port : '') + '/' + navOut;
    });


    idle.onIdleStart.subscribe(() => {
      // if (this.nameComponent === 'LoginComponent') {
      if (globals.component === '280419') {
        this.reset();
      } else {
        this.idleState = 'You\'ve gone idle!';
        // console.log(this.idleState);
        this.childModal.show();
      }
    });

    idle.onTimeoutWarning.subscribe((countdown) => {
      if (globals.component === '280419') {
        this.reset();
      } else {
        this.idleState = 'Tu sesión expirará en ' + countdown + ' segundos, por favor seleccione una opción:';
      }
      // console.log(this.idleState);
    });

    // sets the ping interval to 15 seconds
    keepalive.interval(15);

    keepalive.onPing.subscribe(() => this.lastPing = new Date(), console.log('inicio')
    );

    this.reset();
  }

  reset() {
    this.idle.watch();
    // this.sessionService.renovar();
    this.timedOut = false;
  }

  hideChildModal(): void {
    this.childModal.hide();
  }

  renovarTokens() {
    this.idpService.recuperaToken().subscribe(
      (dataRecuperaToken) => {
        console.log({dataRecuperaToken});
        sessionStorage.setItem('Authorization', dataRecuperaToken.headers.get('Authorization'));
        console.log('Renovado');
        this.childModal.hide();
        this.reset();
      },
      (error) => {
        this.logout();
      });
  }

  public onRouterOutletActivate(event: any) {
    // if (this.globals.component !== '280419' && this.globals.component !== 'System-log-in') {
    //   console.log('onRouterOutletActivate');
    //   this.renovarTokens();
    // }

    this.router.events.subscribe(ev => {
      if (ev instanceof NavigationEnd) {
        this.showMenu = this.activatedRoute.firstChild.snapshot.data.showMenu !== false;
      }
    });

    //  this.nameComponent = event.elementRef.nativeElement.localName;
  }
  ngAfterViewInit(): void {

    const interv = setInterval(() => {
      if (this.globals.usuario && this.globals.profile) {
        this.prepareMenuCuenta();
        this.pages = [];
        this.globals.profile.permisos.forEach((permiso) => this.pages.push(permiso.pagina) );
        clearInterval(interv);

      }
    }, 100);



    const intervcollName = setInterval(() => {
      if (JSON.parse(sessionStorage.getItem('institucionReceptora')).collName) {
        this.collName = JSON.parse(sessionStorage.getItem('institucionReceptora')).collName;
        clearInterval(intervcollName);

      }
    }, 100);


  }

  ngOnInit() {


  }

  navInicio() {
    this.router.navigate(['home']);
  }
  sfpEnteContext(val: string) {
    setTimeout(() => {
      console.log('enteContext', val);
      this.enteContext = val;
    }, 100);
  }

  logout() {
    this.childModal.hide();
    const navOut = JSON.parse(sessionStorage.getItem('enteReceptor')).enteContext || '';
    this.sessionService.logout();
    this.router.navigate([navOut]);
    // tslint:disable-next-line:max-line-length
    // window.location.href = window.location.protocol + '//' + window.location.hostname + (window.location.port !== '' ? ':' + window.location.port : '') + '/' + navOut;
  }


  prepareMenuCuenta() {
    // Abrir cerrar menú cuenta
    var cuenta = false;
    $('.hola').click(() => {
      console.log('Si entra a hola');
    });

    $('.cuenta').click(() => {
      console.log('Si entra');
      cuenta = !cuenta;
      if (cuenta) {
        $('.menu-cuenta1').css('height', '35px');
        $('.menu-cuenta2').css('height', '70px');
        $('.menu-cuenta3').css('height', '105px');
        setTimeout(() => {
          $('.menu-cuenta li').css('display', 'flex');
        }, 300);
      } else {
        $('.menu-cuenta').css('height', '0px');
        $('.menu-cuenta li').css('display', 'none');
      }
    });
  }
}

