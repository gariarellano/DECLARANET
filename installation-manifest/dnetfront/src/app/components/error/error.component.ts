import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Globals } from 'src/app/globals';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.scss']
})
export class ErrorComponent implements OnInit {

  errorText: string;
  errorCode: string;
  constructor(private route: ActivatedRoute,
              private router: Router,
              public globals: Globals) {
    this.errorCode = this.route.snapshot.queryParams.error;
  }

  ngOnInit() {
    switch (this.errorCode) {
      case 'badEnte':
        this.errorText = 'Ruta incorrecta, por favor verifique con su institución la ruta de acceso';
        break;
      case 'badInicio':
        this.errorText = 'Se ha presentado un problema en el inicio de sesión por favor inténtelo más tarde';
        break;
      case 'badCustom':
        this.errorText = this.globals.errorComponentText;
        break;

      default:
        this.errorText = 'Error no identificado, favor de comunicarse con un administrador';
        break;
    }
  }

}
