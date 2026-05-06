import { Component, OnInit } from '@angular/core';
import { ServicioService } from '../routing/servicio';
import { Router, ActivatedRoute } from '@angular/router';
import { PrevisualizarComponent } from '../previsualizar/previsualizar.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { first } from 'rxjs/operators';
import { DeclaracionService } from '../routing/declaracion.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
declare var $: any;

@Component({
  selector: 'app-declaraciones',
  templateUrl: './declaraciones.component.html',
  styleUrls: ['./declaraciones.component.css'],
  providers: [ServicioService],
})
export class DeclaracionesComponent implements OnInit {
  modalRef: any;
  historial: any;
  enteImage: string;
  spinnerText = 'Cargando historial de declaraciones, por favor espera...';
  spinner = {
    fgsType: 'square-jelly-box',
    fgsColor: '#902444',
  };
  
  public funcionario: any;
  constructor(private readonly servicioService: ServicioService,
              private readonly modalService: NgbModal,
              private readonly router: Router,
              private readonly declaracionService: DeclaracionService,
              private readonly activatedRoute: ActivatedRoute,
              private readonly ngxService: NgxUiLoaderService
    ) {
   
  }

  ngOnInit(): void {
    this.ngxService.start(); // start foreground spinner of the master loader with 'default' taskId
    this.activatedRoute.params.subscribe(params => {
      this.funcionario = params;
      console.log(this.funcionario);
      if (!this.funcionario.idUsrDecnet) {
        this.regresar();
        this.ngxService.stop();
        return;

      }
      this.cargaHistorial();

      const uri = window.location.toString();
      if (uri.indexOf(';') > 0) {
        const cleanUri = uri.substring(0, uri.indexOf(';'));
        window.history.replaceState({}, document.title, cleanUri);
      }
  });
  }

  cargaHistorial(){
    const collName = JSON.parse(sessionStorage.getItem('institucionReceptora')).collName;
    this.enteImage = JSON.parse(sessionStorage.getItem('enteReceptor')).enteImage;
    this.declaracionService
    .historialDeclaracion(this.funcionario.idUsrDecnet, collName)
    .pipe(first())
    .subscribe(
      data => {
        if (data.estatus) {
          console.log(data);
          this.historial = data.datos;
          this.ngxService.stop();

        }
      },
      error => {
        console.log(error);
        this.ngxService.stop();

        alert('Error al consultar');
      }
      );
  }

  regresar(){
    const navOut = sessionStorage.getItem('enteReceptor') ? JSON.parse(sessionStorage.getItem('enteReceptor')).enteContext || '' : '';
    this.router.navigate([`/${navOut}`]);
  }

  previsualizar(tipoImpresion: string, titulo: string, numeroDeclaracion: string,
                idUsuario: number, collName: number, tipoDeclaracion: string, noComprobante: string, anio: number) {
    this.modalRef = this.modalService.open(PrevisualizarComponent, {
      centered: true,
      windowClass: 'previewWindow'
    });
    this.modalRef.componentInstance.objPreview = {
      numeroDeclaracion,
      idUsuario,
      collName,
      tipoDeclaracion
    };
    this.modalRef.componentInstance.tipoImpresion = tipoImpresion;
    this.modalRef.componentInstance.titulo = `No. comprobante: ${noComprobante}`;
    this.modalRef.componentInstance.anio = anio;
    this.modalRef.componentInstance.comprobante = noComprobante;

  }

}
