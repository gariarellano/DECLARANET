import { Component, HostListener } from '@angular/core';
import { Router } from '@angular/router';

import { AuthenticationService } from './services/authentication.service';
import { User, UserLogin } from './interfaces/user';
import { SimpleGlobal } from 'ng2-simple-global';

// tslint:disable-next-line:component-selector
@Component({ selector: 'app',
             templateUrl: 'app.component.html' })
export class AppComponent {
    currentUser: UserLogin;

    constructor(
        private router: Router,
        private authenticationService: AuthenticationService,
        private sg: SimpleGlobal
    ) {
        this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    }

    @HostListener('window:resize', ['$event'])
    onResize(event) {
        // tslint:disable-next-line:no-string-literal
        this.sg['innerWidth'] = window.innerWidth;
        // tslint:disable-next-line:no-string-literal
        this.sg['innerHeight'] = window.innerHeight;
    }

    checkResponsive() {
        this.sg['isResponsive'] = this.sg['innerWidth'] < 740;
    }

    // logout() {
    //     this.authenticationService.logout();
    //     this.router.navigate(['/login']);
    // }
}
