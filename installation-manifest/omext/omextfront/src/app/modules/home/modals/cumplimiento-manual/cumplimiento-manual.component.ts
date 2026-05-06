import { Component, OnInit, Input } from '@angular/core';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { CommonService } from 'src/app/services/common.service';
import * as $ from 'jquery';
import Swal from 'sweetalert2';
import { Router } from "@angular/router";
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-cumplimiento-manual',
  templateUrl: './cumplimiento-manual.component.html',
  styleUrls: ['./cumplimiento-manual.component.scss']
})
export class CumplimientoManualComponent implements OnInit {
  servidorPublico: any = {};
  datosCard: any = {};
  flagBuscandoComprobante: boolean = false;
  noComprobante: number;
  instReceptora: any;
  numeroDeclaracion: any;
  dataComprobante: Array<any>;
  dataSourceModalComprobante: any;
  verDatosComprobante: boolean = false;
  swalWithBootstrapButtons: any;
  displayedColumns: string[] = ['nombreEnte', 'valorNivel', 'areaAds', 'empleoCargo'];
  selectIdDNetNoLocalizado: string = '';
  tipoDeclaracion: string;
  flagMatchCurp: boolean;
  flagMatchCurpComp: boolean;
  flagMatchAnio: boolean;
  enteReceptor: any;
  flagMatchTipoDeclaracion: boolean;


  @Input() public datos;
  tipoExlusion: any;
  motivo: string;
  constructor(private commonService: CommonService,
    private readonly modalService: NgbModal,
    private readonly activeModal: NgbActiveModal,
    private router: Router) {
    this.enteReceptor = JSON.parse(sessionStorage.getItem('enteReceptor'));
  }

  ngOnInit() {

    this.instReceptora = JSON.parse(sessionStorage.getItem('institucionReceptora'));
    if (!this.instReceptora.collName) {
      this.logout();
    }
    this.motivo = '0';
    this.tipoExlusion = {
      estatus: {
        id: null,
        descripcion: null
      },
      comentarios: null
    }
    if (this.datos) {
      this.servidorPublico = this.datos;
      this.tipoDeclaracion = this.getTipoDeclaracion(this.servidorPublico.tipoObligacion);
    }

    this.swalWithBootstrapButtons = Swal.mixin({
      customClass: {
        confirmButton: 'btn btn-success',
        cancelButton: 'btn btn-danger'
      },
      buttonsStyling: false
    })
  }
  logout() {

    this.commonService.destroySession("Sesión cerrada");
    sessionStorage.clear();
    if (this.enteReceptor.enteContext != null) {
      this.router.navigate([this.enteReceptor.enteContext]);
    } else {
      this.router.navigate(["login"]);
    }
  }
  getTipoDeclaracion(tipoObligacion) {
    let tipoDeclaracion: string;
    switch (tipoObligacion) {
      case 'ACTIVO_MAYO':
        tipoDeclaracion = 'MODIFICACION';
        break;
      case 'BAJA':
        tipoDeclaracion = 'CONCLUSION';
        break;
      case 'ALTA':
        tipoDeclaracion = 'INICIO';
        break;

      default:
        tipoDeclaracion = null;
        break;
    }
    return tipoDeclaracion;
  }
  buscarComprobante() {
    this.flagBuscandoComprobante = true;
    this.commonService.consultaObligacion(this.noComprobante, this.instReceptora.collName).subscribe(
      ente => {
        if (ente.status === 200) {
          // if (!ente) {
          //   console.log("no sirve");
          //   this.dataComprobante = null;
          //   Swal.fire(
          //     'No se encontró el comprobante',
          //     'Escribe un comprobante válido',
          //     'error'
          //   );
          //   this.flagBuscandoComprobante = false;
          //   this.verDatosComprobante = false;
          // } else {
          this.datosCard = ente.body['data'][0];
          this.numeroDeclaracion = this.datosCard.numeroDeclaracion;
          this.dataComprobante = ente.body['data'];
          // console.log("dataa ", this.datosCard);
          this.flagMatchCurpComp = (this.datosCard.curp === this.servidorPublico.curp);
          this.dataSourceModalComprobante = new MatTableDataSource(this.dataComprobante);
          this.flagBuscandoComprobante = false;
          this.verDatosComprobante = true;
          // }
        } else if (ente.status === 202) {
          this.flagBuscandoComprobante = false;
          const dato = ente.body;
          Swal.fire(
            `${dato.data}`,
            '',
            'success'
          ).then(() => {
            this.closeModal();
            location.reload();
          });
        } else if (ente.status === 204) {
          this.flagBuscandoComprobante = false;
          Swal.fire(
            `No se ha encontrado el comprobante`,
            'Verifica los datos de comprobante',
            'warning'
          );
        }

      }, ((err) => {
        if (err.status == 302) {
          this.flagBuscandoComprobante = false;
          Swal.fire(
            `El comprobante proporcionado se encuentra actualmente en cumplimiento.`,
            'Escribe un comprobante válido',
            'warning'
          );
        } else if (err.status === 504) {
          this.flagBuscandoComprobante = false;
          Swal.fire(
            `Tiempo de respuesta agotado`,
            'Error, intenta de nuevo',
            'error'
          );
        } else {
          this.flagBuscandoComprobante = false;
          Swal.fire(
            `${err.error.data}`,
            'Error, intenta de nuevo',
            'error'
          );
        }
        this.verDatosComprobante = false;

      }));

  }
  selectRow(row, col) {
    this.dataComprobante.forEach((element, index) => {
      if (index != col) {
        $(`.comprobante tr:nth-child(${index + 1})`).removeClass('selected');
      }
    });

    $(`.comprobante tr:nth-child(${col + 1})`).addClass('selected');

    this.selectIdDNetNoLocalizado = row.idDNetNoLocalizado
  }
  aplicarCumplimiento(status) {
    // SE ARMA EL BODY Y SE OBTIENEN LOS PARAMETROS QUE SE ENVIARAN
    const usuario = JSON.parse(sessionStorage.getItem('usuario'));
    const params = {
      idRusp: this.servidorPublico.idRusp,
      idDNetNoLocalizado: this.motivo === '1' ? this.selectIdDNetNoLocalizado : null,
      instReceptora: this.instReceptora.collName
    }

    if (params.idDNetNoLocalizado === '' && this.motivo === '1') {
      Swal.fire(
        'Datos incompletos',
        'Debes seleccionar un puesto o nivel para el cumplimiento',
        'warning'
      );
      return;
    }
    let textHtml: string;
    switch (this.motivo) {

      case '1':
        this.flagMatchAnio = (this.datosCard.anio === this.servidorPublico.anio);
        this.flagMatchCurp = (this.datosCard.curp === this.servidorPublico.curp);
        this.flagMatchTipoDeclaracion = (this.datosCard.tipoDeclaracion === this.tipoDeclaracion);

        if (!this.flagMatchAnio || !this.flagMatchTipoDeclaracion) {
          textHtml = `<div class="alert alert-warning" role="alert">
                        Los siguientes campos no concuerdan:
                      </div>`;
          textHtml += '<table class="table table-striped"> <thead><th>Campo</th><th>Omext</th><th>Cumplimiento</th></thead><tbody>';

          if (!this.flagMatchAnio) {
            textHtml += `<tr><td>Año</td> <td>${this.servidorPublico.anio}</td> <td>${this.datosCard.anio}</td></tr>`;
          }
          if (!this.flagMatchTipoDeclaracion) {
            textHtml += `<tr><td>Tipo de Declaración</td> <td>${this.tipoDeclaracion}</td> <td>${this.datosCard.tipoDeclaracion}</td></tr>`;
          }
          textHtml += `</tbody></table>`


          this.swalWithBootstrapButtons.fire(
            {
              title: 'Error',
              html: `${textHtml}`,
              icon: 'error'
            }
          );
          return;
        }


        this.tipoExlusion.estatus.descripcion = 'Error en el CURP u otro dato';
        textHtml = `<h2>Se aplicara el cumplimiento manual</h2>`
        if (!this.flagMatchCurp) {
          textHtml += `<div class="alert alert-warning" role="alert">
                        Los siguientes campos no concuerdan
                      </div>`;
          textHtml += '<table class="table table-striped"> <thead><th>Campo</th><th>Omext</th><th>Cumplimiento</th></thead><tbody>';
          textHtml += `<tr><td>Curp</td> <td>${this.servidorPublico.curp}</td> <td>${this.datosCard.curp}</td></tr></tbody></table>`;
        }
        break;
      case '2':
        textHtml = '<h2>Se aplicará exclusión de cumplimiento</h2>';
        this.tipoExlusion.estatus.descripcion = 'No le es exigible la presentación de la Declaración Patrimonial y de Intereses';
        break;
    }
    this.tipoExlusion.estatus.id = Number(this.motivo);
    this.swalWithBootstrapButtons.fire({
      title: '¿Estas seguro?',
      html: `${textHtml}`,
      icon: 'warning',
      showCancelButton: true,
      width: 1200,
      customClass: {
        confirmButton: 'btn btn-success mr-3',
        cancelButton: 'btn btn-danger'
      },
      buttonsStyling: false,
      confirmButtonText: 'Aceptar',
      cancelButtonText: 'Cancelar',
      reverseButtons: false
    }).then((result) => {
      if (result.value) {
        sessionStorage.setItem('leyendaSpinner', 'Guardando cumplimiento, por favor espera...');
        $('.spinner').css('display', 'flex');
        this.commonService.registrarCumplimiento(usuario, params, this.tipoExlusion).subscribe(
          (res) => {
            if (res.status === 200) {
              $('.spinner').fadeOut('slow');
              this.swalWithBootstrapButtons.fire(
                'Exclusión realizada con éxito',
                'El estatus ha cambiado a CUMPLIDO',
                'success'
              ).then(() => {
                this.closeModal();
                location.reload();
              });
            }
          }, (err) => {
            $('.spinner').fadeOut('slow');
            this.swalWithBootstrapButtons.fire(
              'Error al realizar el cumplimiento',
              `${err.message}`,
              'error'
            )
          });


      } else if (
        result.dismiss === Swal.DismissReason.cancel
      ) {
        this.swalWithBootstrapButtons.fire(
          'Cancelado',
          'El usuario ha cancelado el proceso',
          'error'
        )
      }
    });
  }

  closeModal() {
    this.modalService.dismissAll(this);
  }

  formattedDate(d1) {
    const d = new Date(d1);
    return [d.getDate(), d.getMonth() + 1, d.getFullYear()]
      .map(n => n < 10 ? `0${n}` : `${n}`).join('-');
  }

}
