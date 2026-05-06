import { Component, HostListener, ViewChild } from '@angular/core';
import { Idle } from '@ng-idle/core';
import { Keepalive } from '@ng-idle/keepalive';
import { Router, ActivatedRoute } from '@angular/router';
import { BsModalService, BsModalRef, ModalDirective } from 'ngx-bootstrap/modal';
import { SessionService } from 'src/app/services/session.service';
import { Globals } from './globals';

declare var $: any;
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Declaranet';
  myCount: 10;
  declaracionLoad: any;

  idleState = 'Not started.';
  timedOut = false;
  lastPing?: Date = null;
  public modalRef: BsModalRef;
  nameComponent: string;
  @ViewChild('childModal') childModal: ModalDirective;

  @HostListener('mousewheel', ['$event'])
  wheel(event: MouseEvent) {
    var btn
    if (event['wheelDelta'] < -100) {
      btn = document.getElementById('goForwardBtn')
      if (btn) { btn.click(); }
    } else if (event['wheelDelta'] > 100) {
      btn = document.getElementById('goBackBtn')
      if (btn) { btn.click(); }
    }
  }
  // @HostListener('window:beforeunload', ['$event'])
  // unloadNotification($event: any) {
  //   if (this.globals.component === '030612') {
  //       $event.returnValue = 'true';  // Descomentar esto si quieres evitar que refresquen o cierren
  //   }
  // }

  constructor(private readonly idle: Idle,
    private readonly keepalive: Keepalive,
    private readonly router: Router,
    private readonly modalService: BsModalService,
    private readonly sessionService: SessionService,
    private readonly route: ActivatedRoute,
    private readonly globals: Globals) {


    /*
    if (this.declaracionLoad.encabezado.tipoDeclaracion === "MODIFICACION") {
      this.encargo = this.declaracionLoad.encabezado.anio;
    } else {
       this.encargo = this.declaracionLoad.encabezado.fechaEncargo;
    }*/
    //console.log("encargo ", this.encargo);

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
      if (globals.component === '030612') {
        this.reset();
      } else {
        this.idleState = 'You\'ve gone idle!';
        // console.log(this.idleState);
        this.childModal.show();
      }
    });

    idle.onTimeoutWarning.subscribe((countdown) => {
      if (globals.component === '030612') {
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

  stay() {
    this.sessionService.renovarToken().subscribe(
      data => {
        console.log('Renovado');
      }
    );
    this.childModal.hide();
    this.reset();
  }

  logout() {
    this.childModal.hide();
    // this.appService.setUserLoggedIn(false);
    const navOut = JSON.parse(sessionStorage.getItem('enteReceptor')).enteContext || '';
    this.sessionService.logout();
    this.router.navigate(['/' + navOut]);
  }

  public onRouterOutletActivate(event: any) {
    this.nameComponent = event.constructor.name;
    console.log('nameComponent en app', this.nameComponent);
    //  this.nameComponent = event.elementRef.nativeElement.localName;
  }


}

