import { Component, OnInit, ViewChild } from '@angular/core';
import { Idle } from '@ng-idle/core';
import { Keepalive } from '@ng-idle/keepalive';


import * as $ from 'jquery';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { SessionService } from './services/session.service';
import { Globals } from './globals';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonService } from './services/common.service';
import { IdpService } from './services/idp.service';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'omext';
  idleState = 'Not started.';
  timedOut = false;
  lastPing?: Date = null;


  @ViewChild('childModal') childModal: ModalDirective;

  constructor(private readonly keepalive: Keepalive,
    private readonly sessionService: SessionService,
    private readonly commonService: CommonService,
    private readonly globals: Globals,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private idpService: IdpService,
    private readonly idle: Idle) {
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
        this.childModal.show();
      }
    });

    idle.onTimeoutWarning.subscribe((countdown) => {
      if (globals.component === '280419') {
        this.reset();
      } else {
        this.idleState = 'Tu sesión expirará en ' + countdown + ' segundos, por favor seleccione una opción:';
      }
    });

    // sets the ping interval to 15 seconds
    keepalive.interval(15);

    keepalive.onPing.subscribe(() => this.lastPing = new Date()
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
  logout() {
    this.childModal.hide();
    const navOut = JSON.parse(sessionStorage.getItem('enteReceptor')).enteContext || '';
    this.sessionService.logout();
    this.router.navigate([navOut]);
    // tslint:disable-next-line:max-line-length
    // window.location.href = window.location.protocol + '//' + window.location.hostname + (window.location.port !== '' ? ':' + window.location.port : '') + '/' + navOut;
  }

  public onRouterOutletActivate(event: any) {
    if (this.globals.component !== '280419' && this.globals.component !== 'System-log-in'){
    this.renovarTokens();
    }
    //  this.nameComponent = event.elementRef.nativeElement.localName;
}

  ngOnInit() { }
}
