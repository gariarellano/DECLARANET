import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';

@Injectable()
export class AuthAnonymousService implements CanActivate {

  constructor(
    private router: Router
  ) { }

  canActivate(): boolean {
    if (sessionStorage.getItem('token') && sessionStorage.getItem('idp')) {
      this.router.navigate(['perfil']);
      return false;
    }
    return true;
  }
}
