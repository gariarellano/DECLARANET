import { Component, OnInit, Input } from '@angular/core';
import { NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { Globals } from 'src/app/globals';
import { DeclaracionService } from 'src/app/services/declaracion.service';
import { SessionService } from 'src/app/services/session.service';
import { first } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { PreviewComponent } from '../preview/preview.component';


@Component({
  selector: 'app-historial-notas',
  templateUrl: './historial-notas.component.html',
  styleUrls: ['./historial-notas.component.scss']
})
export class HistorialNotasComponent implements OnInit {

  @Input() public objNotas;
  initLoad: any;
  usuario: any;
  notas: Array<any>;
  historial: boolean;
  modalRef: any;
  numeroDeclaracion: any;
  declaracion: string;
  fechaEnc: string;
  fechaPres: string;
  anio: any;
  ngOnInit(): void {
    this.historial = false;
    this.usuario = this.sessionService.currentUserValue.datosPersonales;
    console.log("se abrio el historial", this.objNotas);
    this.historialNota();

    console.log("obj ", this.objNotas.registro);
    this.declaracion = this.objNotas.registro.tipoDeclaracion;
    this.fechaEnc = this.objNotas.registro.fechaEncargo;
    this.fechaPres = this.objNotas.registro.fechaRecepcion;
    this.anio = this.objNotas.registro.anio;
  }

  constructor(public globals: Globals, private toastService: ToastrService, private router: Router, private modalService: NgbModal, private sessionService: SessionService, private declaracionService: DeclaracionService, ) { }

  close() {
    this.modalService.dismissAll('');
  }

  nuevaNota() {
    // this.globals.declaracionLoaded = declaracion;
    //Eliminar session en produccion
    // sessionStorage.setItem('declaracionLoaded', JSON.stringify(this.globals.declaracionLoaded));
    this.router.navigate(['/nota']);
    this.close();

  }

  showToast(texto) {

    this.toastService.warning(
      'No olvides enviar tu declaración',
      `${texto}`,
      {
        timeOut: 3000,
        extendedTimeOut: 800,
        closeButton: true,
        tapToDismiss: true,
        progressBar: true
      }
    );

  }

  presentarNota() {
    sessionStorage.removeItem('notaTemp');
    const pendiente = JSON.parse(sessionStorage.getItem('initLoad'));
    if (pendiente.declaracionPendiente) {
      this.showToast('No puedes crear un nota ya que tienes una declaración en proceso');
      return;
    }
    this.declaracionService
      .crearNota(this.objNotas.datosPersonales, this.objNotas.registro, this.objNotas.institucionReceptora)
      .pipe(first())
      .subscribe(
        data => {
          if (!data.body.declaracion.respuesta.declaracion) {
            this.showToast(data.body.declaracion.respuesta.mensaje);
            return;

          }

          if (data.status === 200) {

            const respuesta = data.body.declaracion.respuesta;
            // this.globals.systemLog = data.body;
            if (respuesta.estatus) {
              this.globals.declaracionLoaded = respuesta.declaracion;
              // Eliminar se}ssion en produccion
              console.log('session ', this.globals.declaracionLoaded);

              sessionStorage.setItem('declaracionLoaded', JSON.stringify(this.globals.declaracionLoaded));
              // console.log(data);
              this.globals.flagPresentar1Vez = true;
              if (data.body.declaracion.respuesta.declaracion.encabezado.tipoDeclaracion == 'NOTA') {
                sessionStorage.removeItem('notaTemp');
                this.router.navigate(['/nota']);
                this.close();
              } else {
                this.router.navigate(['/declaracion']);
                this.close();
              }
            } else {
              this.router.navigate(['/error']);
            }
          }
        },
        error => {

        }
      );


    // this.router.navigate(['/declaracion']);
  }

  historialNota() {
    this.declaracionService
      .historialUnaNota(this.objNotas.datosPersonales, this.objNotas.registro, this.objNotas.institucionReceptora)
      .pipe(first())
      .subscribe(
        data => {

          if (data.status === 200) {

            if (data.body.declaracion.respuesta.historial) {
              console.log("si tiene");

              this.historial = true;
              this.notas = data.body.declaracion.respuesta.historial;
            } else {
              this.historial = false;
            }




            // this.globals.systemLog = data.body;
            // if (respuesta.estatus) {
            //   this.globals.declaracionLoaded = respuesta.declaracion;
            //   console.log("session ", this.globals.declaracionLoaded);

            //   sessionStorage.setItem('declaracionLoaded', JSON.stringify(this.globals.declaracionLoaded));
            //   this.globals.flagPresentar1Vez = true;
            //   if (data.body.declaracion.respuesta.declaracion.encabezado.tipoDeclaracion == 'NOTA') {
            //     this.router.navigate(['/nota']);
            //     this.close();
            //   } else {
            //     this.router.navigate(['/declaracion']);
            //     this.close();
            //   }
            // } else {
            //   this.router.navigate(['/error']);
            // }
          }
        },
        error => {

        }
      );

  }

  previsualizar(tipoImpresion: string, titulo: string, nota) {
    console.log("nota content ", nota);

    // this.modalRef = this.modalService.open(PreviewComponent, {
    //   centered: true,
    //   windowClass: 'previewWindow'
    // });
    // this.modalRef.componentInstance.objPreview = {
    //   numeroDeclaracion: this.numeroDeclaracion,
    //   idUsuario: this.idUsu,
    //   collName: this.collName
    // };
    // this.modalRef.componentInstance.tipoImpresion = tipoImpresion;
    // this.modalRef.componentInstance.titulo = titulo;
    this.modalRef = this.modalService.open(PreviewComponent, {
      centered: true,
      windowClass: 'previewWindow'
    });
    this.modalRef.componentInstance.objPreview = {
      numeroDeclaracion: nota.numeroDeclaracion,
      idUsuario: nota.idUsuario,
      collName: nota.collName,
      tipoDeclaracion: 'NOTA'
    };
    this.modalRef.componentInstance.tipoImpresion = tipoImpresion;
    this.modalRef.componentInstance.titulo = titulo;
  }
}
