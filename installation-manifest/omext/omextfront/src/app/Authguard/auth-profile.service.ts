import { Injectable } from "@angular/core";
import { CanActivate } from "@angular/router";
import { CommonService } from "./../services/common.service";
@Injectable({
  providedIn: "root",
})
export class AuthProfileService implements CanActivate {
  constructor(private commonService: CommonService) {}

  // Authguard de la página seleccionar perfil
  canActivate() {
    return this.commonService.validateTokenSession();
  }
}
