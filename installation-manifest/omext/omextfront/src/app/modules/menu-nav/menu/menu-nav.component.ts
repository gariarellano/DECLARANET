import { AfterViewInit, Component, OnInit } from "@angular/core";
import { CommonService } from "../../../services/common.service";
import { SessionService } from "../../../services/session.service";
import * as $ from "jquery";
import { Router } from "@angular/router";
import { ProgressSpinnerMode } from "@angular/material/progress-spinner";
import { ThemePalette } from "@angular/material/core";
import { Globals } from "src/app/globals";

@Component({
  selector: "app-menu-nav",
  templateUrl: "./menu-nav.component.html",
  styleUrls: ["./menu-nav.component.scss"],
})
export class MenuNavComponent implements OnInit, AfterViewInit {
  mode: ProgressSpinnerMode = "indeterminate";
  value = 10;
  user: string;
  enteReceptor: any;
  enteImagen: string;
  pages: any[];
  profile: string;

  constructor(
    private sessionService: SessionService,
    private router: Router,
    private commonService: CommonService,
    public globals: Globals
  ) {
    this.enteReceptor = JSON.parse(sessionStorage.getItem('enteReceptor'));
  }
  ngAfterViewInit(): void {

    const interv = setInterval(() => {
      if (this.globals.usuario && this.globals.profile) {
        this.prepareMenuCuenta();
        this.pages = [];
        this.globals.profile.permisos.forEach((permiso) => this.pages.push(permiso.pagina) );
        // En teoria para habilitar solo usaremos el page, pero al no modificar pages usaremos el perfil para
        this.profile = this.globals.profile;
        clearInterval(interv);

      }
    }, 100);

    // const interv = setInterval(() => {
    //   if (this.globals.usuario && this.globals.profiles) {
    //     this.prepareMenuCuenta();
    //     const perfil = this.globals.profiles.find(profile => profile.type === this.globals.profile);
    //     this.pages = [];
    //     perfil.pages.forEach((profile) =>
    //       this.pages.push(profile.page)
    //     );

    //     // En teoria para habilitar solo usaremos el page, pero al no modificar pages usaremos el perfil para
    //     this.profile = this.globals.profile;
    //     clearInterval(interv);

    //   }
    // }, 100);  
  
  }

  ngOnInit(): void {
    $('.spinner').fadeOut('slow');
    this.user = this.globals.usuario;
    this.cargaMenu();
    this.prepareMenuCuenta();

    // if (this.enteReceptor.enteContext != null) {
    this.enteImagen = this.enteReceptor.enteImage;
    // }
  }
  logout() {
    // this.childModal.hide();
    this.pages = [];
    const navOut = JSON.parse(sessionStorage.getItem('enteReceptor')).enteContext || '';
    this.sessionService.logout();
    this.router.navigate([navOut]);
    // tslint:disable-next-line:max-line-length
    // window.location.href = window.location.protocol + '//' + window.location.hostname + (window.location.port !== '' ? ':' + window.location.port : '') + '/' + navOut;
  }
  cargaMenu() {
    if ($(window).width() <= 600) {
      $("#btn-abre-menu").click(() => {
        $(".menu-lateral").animate({ width: "toggle" }, 250);
        $(".background-opacity").css({ display: "flex" });
      });
      $(".background-opacity").click(() => {
        $(".menu-lateral").animate({ width: "toggle" }, 250);
        $(".background-opacity").css("display", "none");
      });
    } else {
      $("#open-filtrar").click(() => {
        $(".filter-space").css("display", "flex");
        var leftPos = $(".menu-lateral").scrollLeft();
        $(".filter-space").animate(
          {
            scrollLeft: leftPos + 200,
          },
          800
        );
      });
      $("#cerrar").css("display", "none");

      $("#btn-abre-menu").mouseover(() => {
        $(".menu-lateral").animate({ width: "toggle" }, 250);
        $(".background-opacity").css({ display: "flex" });
      });
      $(".background-opacity").click(() => {
        $(".menu-lateral").animate({ width: "toggle" }, 250);
        $(".background-opacity").css("display", "none");
      });
      $(".menu-lateral").mouseleave(() => {
        $(".menu-lateral").animate({ width: "toggle" }, 250);
        $(".background-opacity").css("display", "none");
      });
    }
  }

  prepareMenuCuenta() {
    // Abrir cerrar menú cuenta
    var cuenta = false;

    $(".cuenta").click(() => {
      cuenta = !cuenta;
      if (cuenta) {
        $(".menu-cuenta1").css("height", "35px");
        $(".menu-cuenta2").css("height", "35px");
        $(".menu-cuenta3").css("height", "105px");
        setTimeout(() => {
          $(".menu-cuenta li").css("display", "flex");
        }, 300);
      } else {
        $(".menu-cuenta").css("height", "0px");
        $(".menu-cuenta li").css("display", "none");
      }
    });
  }
  getValueLeyenda() {
    return sessionStorage.getItem('leyendaSpinner');
  }
}
