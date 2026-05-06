import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { EnteReceptorService } from '../routing/servicioentes.service';
import { first } from 'rxjs/operators';
import { DeclaracionService } from '../routing/declaracion.service';
import { NgForm } from '@angular/forms';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { ToastrService } from 'ngx-toastr';
import {AppComponent} from '../app.component';


declare var $: any;

@Component({
  selector: 'app-busqueda',
  templateUrl: './busqueda.component.html',
  styleUrls: ['./busqueda.component.css']
})
export class BusquedaComponent implements OnInit {
  enteImage: string;
  nombreEnte: string;
  flagEnte: boolean;
  funcionarios: any;
  nombre: string;
  spinnerText: string;
  spinner = {
    fgsType: 'square-jelly-box',
    fgsColor: '#902444',
  };
  interval: NodeJS.Timeout;
  msjCloseModal: string;
  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly enteReceptorService: EnteReceptorService,
    private readonly declaracionService: DeclaracionService,
    private readonly router: Router,
    private readonly ngxService: NgxUiLoaderService,
    private readonly toastr: ToastrService,
    public myapp: AppComponent


  ) {
    this.flagEnte = false;
    this.spinnerText = 'Preparando base de servidores, por favor espera...';
    this.ngxService.start();
    this.msjCloseModal = null;
  }

  ngOnInit(): void {

    let ente: any = null;
    if (this.activatedRoute.snapshot.url.length > 0) {
      ente = this.activatedRoute.snapshot.url[0].path === 'busqueda' ? null : this.activatedRoute.snapshot.url[0].path;
    }
    this.myapp.sfpEnteContext(ente);
    console.log('servicio ente');
    this.enteReceptorService
      .access(ente)
      .pipe(first())
      .subscribe(
        data => {

          if (data.status === 200) {
            console.log(data.body);
            this.enteImage = data.body.frontInfo.imagenB64;
            sessionStorage.setItem('enteImage', JSON.stringify(this.enteImage));
            this.nombreEnte = data.body.frontInfo.nombre;
            this.ngxService.stop();

            if (!this.nombreEnte) {
              this.router.navigate(['/error'], { queryParams: { error: 'badEnte' } });
            }
            sessionStorage.setItem('institucionReceptora', JSON.stringify(data.body.institucionReceptora));
            const enteReceptor = {
              enteImage: data.body.frontInfo.imagenB64,
              enteContext: ente,
              prefijo: data.body.frontInfo.prefijo
            };
            sessionStorage.setItem('enteReceptor', JSON.stringify(enteReceptor));
            this.flagEnte = true;

            if (!sessionStorage.getItem('modalInfo')) {
              $('#modalInfo').modal('show');
              $(document).keydown((event) => {
                if (event.keyCode === 27) {
                  $('#modalInfo').modal('hide');
                }
              });
              let counter = 60;
              this.interval = setInterval(() => {
                counter--;
                if (counter >= 0) {
                  this.msjCloseModal = 'Cerrando en ' + counter + ' segundos';
                }
                if (counter === 0) {
                  $('#modalInfo').modal('hide');
                }
              }, 1000);
              sessionStorage.setItem('modalInfo', '1');

            }
          }
        },
        error => {
          this.ngxService.stop();
          alert('Error de comunicación... Favor de intentarlo mas tarde.');

        }
      );

  }
  cancelCloseModal() {
    if (this.interval) {
      this.msjCloseModal = null;
      clearInterval(this.interval);
    }
  }
  busqueda(n: NgForm) {
    this.spinnerText = 'Buscando coincidencias de servidores públicos, por favor espera...';
    this.toastr.clear();
    this.ngxService.start();
    const nombre = n.controls['nombre'].value;
    const ins = JSON.parse(sessionStorage.getItem('institucionReceptora'));
    const collName = ins.collName;
    this.declaracionService
      .busquedaServidores(nombre, collName)
      .pipe(first())
      .subscribe(
        data => {
          if (data.estatus) {
            console.log(data);
            this.ngxService.stop();
            if (data.mensaje && data.mensaje === '200') {
              this.toastr.info('La búsqueda arroja demasiados resultados, le sugerimos redefinirla.', '', {
                disableTimeOut: true,
                positionClass: 'toast-top-center',
                closeButton: true
              });
            }
            this.funcionarios = data.datos;
          }
        },
        error => {
          console.log(error);
          this.ngxService.stop();
          alert('Error al consultar');
        }
      );
  }

}
