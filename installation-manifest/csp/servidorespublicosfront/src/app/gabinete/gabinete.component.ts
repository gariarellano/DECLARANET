import { Component, OnInit } from '@angular/core';
import { ServicioService } from '../routing/servicio';
import { DeclaracionService } from '../routing/declaracion.service';

import * as $ from 'jquery';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PrevisualizarComponent } from '../previsualizar/previsualizar.component';
import { first } from 'rxjs/operators';
import { NgxUiLoaderService } from 'ngx-ui-loader';


@Component({
  selector: 'app-gabinete',
  templateUrl: './gabinete.component.html',
  styleUrls: ['./gabinete.component.css'],
  providers: [ServicioService]
})
export class GabineteComponent implements OnInit {
  modalRef: any;
  spinnerText = 'Cargando datos, por favor espera...';
  spinner = {
    fgsType: 'square-jelly-box',
    fgsColor: '#902444',
  };

  public funcionario: any;
  constructor(private readonly declaracionService: DeclaracionService,
              private readonly modalService: NgbModal,
              private readonly ngxService: NgxUiLoaderService
              ) { }

  ngOnInit(): void {
    $('.dropdown-inverse li > a').click(function(e){
      $('.status').text(this.innerHTML);
  });
    this.cargaGabinete(1);

  }

  cargaGabinete(tipo: number) {
    this.ngxService.start(); // start foreground spinner of the master loader with 'default' taskId
    this.declaracionService.gabinete(tipo)
    .pipe(first())
    .subscribe(
      resultado => {
        console.log(resultado);
        this.funcionario = resultado.datos;
        this.ngxService.stop();

      },
      error => {
        alert('Ha ocurrido un error en la carga de la información, intente de nuevo');
        this.ngxService.stop();

      }
    );
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
