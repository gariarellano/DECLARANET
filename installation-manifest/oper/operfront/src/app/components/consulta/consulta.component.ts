import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EnteReceptorService } from 'src/app/services/ente-receptor.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { AppComponent } from 'src/app/app.component';
import { first } from 'rxjs/operators';
import { UsuarioIdentidadService } from '../../services/usuario-identidad.service';
import { NgForm } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PrevisualizarComponent } from '../previsualizar/previsualizar.component';
import * as JSZip from 'jszip';
import * as FileSaver from 'file-saver';
declare var $: any;
import { DeclaracionService } from 'src/app/services/declaracion.service';
import { ExportCsvService } from '../../services/export-csv.service';
import { Globals } from 'src/app/globals';

@Component({
  selector: 'app-consulta',
  templateUrl: './consulta.component.html',
  styleUrls: ['./consulta.component.scss']
})
export class ConsultaComponent implements OnInit{

  enteImage: string;
  nombreEnte: string;
  flagEnte: boolean;
  interval: NodeJS.Timeout;
  msjCloseModal: string;
  curp: string;
  funcionarios: any;
  histPersona: any;
  nombreyreservado: any;
  modalRef: any;
  datoDecla: any;
  tipoTabla: string;
  usuario: any;
  exportaMasivaCheck: boolean;
  domicilios: any;
  spinnerText: string;
  collName: string;
  permiso: any;
  
  spinner = {
    fgsType: 'three-strings',
    fgsColor: '#ED3237',
  };
  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly enteReceptorService: EnteReceptorService,
    private readonly ngxService: NgxUiLoaderService,
    private readonly modalService: NgbModal,
    private readonly router: Router,
    private readonly usuarioService: UsuarioIdentidadService,
    private readonly declaracionService: DeclaracionService,
    private readonly exportCsvService: ExportCsvService,
    public myapp: AppComponent,
    public globals: Globals
  ) {
    this.msjCloseModal = null;
    this.flagEnte = false;
    this.exportaMasivaCheck = false;
    const ins = JSON.parse(sessionStorage.getItem('institucionReceptora'));
    this.collName = ins.collName;
    this.usuario = this.globals.usuario;


  }

  ngOnInit(): void {
    console.log(this.globals.permisos);
    this.permiso = this.globals.permisos.find(x => x.pagina === 2);
    
  }



  cancelCloseModal() {
    if (this.interval) {
      this.msjCloseModal = null;
      clearInterval(this.interval);
    }
  }
  busqueda(n: NgForm) {
    this.spinnerText = 'Buscando coincidencias de servidores públicos, por favor espera...';
    this.ngxService.start();
    const valor = n.controls.nombre.value.trim();

    $('#justifica').val('');
    $('#justificaMasivo').val('');
    $('#justificaDomicilio').val('');


    let tipoBusqueda: string;
    if (/^([A-Z][AEIOUX][A-Z]{2}\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\d])(\d)$/.test(valor)) {
      tipoBusqueda = 'curp';
    } else if (/^([A-ZÑ\x26]{4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[A-Z\d]{3})?$/.test(valor)) {
      tipoBusqueda = 'rfc';
    } else if (/^([A-ZÑ\x26]{4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1]))?$/.test(valor)) {
      tipoBusqueda = 'rfc';
    } else {
      tipoBusqueda = 'nombre';
    }

    
    this.usuarioService
      .getServidoresConsultaDeclaracion(valor, this.collName, tipoBusqueda, this.usuario)
      .subscribe(
        data => {
          if (data.body.data) {

            this.ngxService.stop();
            // if (data.mensaje && data.mensaje === '200') {
            //   this.toastr.info('La búsqueda arroja demasiados resultados, le sugerimos redefinirla.', '', {
            //     disableTimeOut: true,
            //     positionClass: 'toast-top-center',
            //     closeButton: true
            //   });
            // }
            this.funcionarios = data.body.data;
            this.tipoTabla = 'porNombre';
          } else if (data.body.historial) {
            this.exportaMasivaCheck = false;
            this.ngxService.stop();
            this.histPersona = data.body.historial;
            this.agregaChecks(this.histPersona);
            this.tipoTabla = 'porCurpRfc';
            this.nombreyreservado = data.body.cabecera;
            console.log('Fer Fer', this.nombreyreservado);
          }
        },
        error => {
          console.log(error);
          this.ngxService.stop();
          alert('Error al consultar');
        }
      );
  }

  agregaChecks(historial: any) {
    historial.forEach(e => {
      e.checked = false;
      e.checkedDecla = false;
      e.checkedAcuse = false;
    });

  }

  deNombreAHistorial(valor) {
    const tipoBusqueda = 'idUsrDecnet';
    this.exportaMasivaCheck = false;
    this.usuarioService
      .getServidoresConsultaDeclaracion(valor, this.collName, tipoBusqueda, this.usuario)
      .subscribe(
        data => {
          if (data.body.historial) {
            this.ngxService.stop();
            this.histPersona = data.body.historial;
            this.agregaChecks(this.histPersona);
            this.nombreyreservado = data.body.cabecera;
            this.tipoTabla = 'porCurpRfc';
          }
        },
        error => {
          console.log(error);
          this.ngxService.stop();
          alert('Error al consultar');
        }
      );
  }

  previsualizar(n: NgForm, tipoImpresion: string, titulo: string, numeroDeclaracion: string,
                idUsuario: number, collName: number, tipoDeclaracion: string, noComprobante: string, anio: number) {

    const justificacion = n.form.controls.justificacion.value;

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
    this.modalRef.componentInstance.justificacion = justificacion;
    this.modalRef.componentInstance.comprobante = noComprobante;


  }

  asignaDeclaracion(datos: any) {
    this.datoDecla = null;
    this.datoDecla = datos;
  }
  cambiaChecksExportar(index: number) {
    const val = this.histPersona[index].checked;
    this.histPersona[index].checkedDecla = val;
    this.histPersona[index].checkedAcuse = val;

  }

  cambiaAllChecksExportar(a: any) {
    const val = a.currentTarget.checked;
    this.histPersona.forEach((p, i) => {
      p.checked = val;
      this.cambiaChecksExportar(i);
    });

  }

  descargaArchivosMasivo(n: NgForm) {
    const justificacion = n.form.controls.justificacionMasivo.value;
    this.spinnerText = 'Preparando documento descargable...';
    this.ngxService.start();
    this.crearZipMasivo(justificacion);
  }
  descargaDecla(n: NgForm, dato: any) {
    const justificacion = n.form.controls.justificacion.value;
    this.spinnerText = 'Preparando documento descargable...';
    this.ngxService.start();
    this.crearZipDeclaracion(justificacion, dato);

  }
  descargaAcuseZip(n: NgForm, dato: any) {
    const justificacion = '';
    this.spinnerText = 'Preparando documento descargable...';
    this.ngxService.start();
    this.crearZipAcuse(justificacion, dato);

  }


  async crearZipDeclaracion(justifica: string, dato: any) {
    const zip = new JSZip();
    const name = dato.noComprobante + '.zip';

    // tslint:disable-next-line:prefer-for-of
    // for (let counter = 0; counter < files.length; counter++) {

    const promises = [];

    promises.push(
      this.getDeclaFile(dato, justifica).then((data) => {
        const b: any = new Blob([data], { type: 'application/pdf' });
        zip.file(`${dato.noComprobante}/${dato.noComprobante}_declaracion.pdf`, b);
        return data;
      })
    );

    if (dato.notas) {
      for (const nota of dato.notas) {
        promises.push(
          this.getDeclaFile(nota, justifica).then((data) => {
            const b: any = new Blob([data], { type: 'application/pdf' });
            zip.file(`${dato.noComprobante}/${nota.noComprobante}_nota.pdf`, b);
            return data;
          })
        );
      }
    }



    Promise.all(promises).then(() => {
      console.log('paso');
      zip.generateAsync({ type: 'blob' }).then((content) => {
        if (content) {
          FileSaver.saveAs(content, name);
          this.ngxService.stop();
        }
      });
    })
  }
  async crearZipAcuse(justifica: string, dato: any) {
    const zip = new JSZip();
    const name = dato.noComprobante + '_Acuse.zip';

    // tslint:disable-next-line:prefer-for-of
    // for (let counter = 0; counter < files.length; counter++) {

    const promises = [];

    promises.push(
      this.getAcuseFile(dato, justifica).then((data) => {
        const b: any = new Blob([data], { type: 'application/pdf' });
        zip.file(`${dato.noComprobante}/${dato.noComprobante}_acuse.pdf`, b);
        return data;
      })
    );

    if (dato.notas) {
      for (const nota of dato.notas) {
        promises.push(
          this.getAcuseFile(nota, justifica).then((data) => {
            const b: any = new Blob([data], { type: 'application/pdf' });
            zip.file(`${dato.noComprobante}/${nota.noComprobante}_notaAcuse.pdf`, b);
            return data;
          })
        );
      }
    }



    Promise.all(promises).then(() => {
      console.log('paso');
      zip.generateAsync({ type: 'blob' }).then((content) => {
        if (content) {
          FileSaver.saveAs(content, name);
          this.ngxService.stop();
        }
      });
    })
  }


  async crearZipMasivo(justifica: string) {
    const zip = new JSZip();
    const abrev = this.nombreyreservado.nombre.split(/\s/).reduce((response, word)=> response += word.slice(0, 1), '');
    const today = new Date();
    const re = /\-/gi;
    const fechaFormat = today.toISOString().substring(0, 10).replace(re, '');

    const name = `${abrev}_${fechaFormat}.zip`;

    // tslint:disable-next-line:prefer-for-of
    // for (let counter = 0; counter < files.length; counter++) {

    let promises = [];
    let count = 0;
    for (const dato of this.histPersona) {
      // this.histPersona.forEach(dato => {
      let fileAcuse: any;
      let fileDecla: any;

      if (dato.checkedDecla) {
        promises.push(
          this.getDeclaFile(dato, justifica).then((data) => {
            const b: any = new Blob([data], { type: 'application/pdf' });
            zip.file(`${dato.noComprobante}/${dato.noComprobante}_declaracion.pdf`, b);
            return data;
          })
        );

        if (dato.notas) {
          for (const nota of dato.notas) {
            promises.push(
              this.getDeclaFile(nota, justifica).then((data) => {
                const b: any = new Blob([data], { type: 'application/pdf' });
                zip.file(`${dato.noComprobante}/${nota.noComprobante}_nota.pdf`, b);
                return data;
              })
            );
            promises.push(
              this.getAcuseFile(nota, justifica).then((data) => {
                const b: any = new Blob([data], { type: 'application/pdf' });
                zip.file(`${dato.noComprobante}/${nota.noComprobante}_notaAcuse.pdf`, b);
                return data;
              })
            );
          }
        }
        // const b: any = new Blob( [fileData], { type: 'application/pdf' });
        // zip.file(`${dato.noComprobante}/${dato.noComprobante}_declaracion.pdf`, b);
      }

      if (dato.checkedAcuse) {
        promises.push(
          this.getAcuseFile(dato, justifica).then((data) => {
            const b: any = new Blob([data], { type: 'application/pdf' });
            zip.file(`${dato.noComprobante}/${dato.noComprobante}_acuse.pdf`, b);
            return data;
          })
        );
        // const b: any = new Blob( [fileData], { type: 'application/pdf' });
        // zip.file(`${dato.noComprobante}/${dato.noComprobante}_acuse.pdf`, b);
      }
      count++;

      // if (count > 5) {
      //   await this.wait(500).then(() => {
      //     console.log('espere');
      //     count = 0;
      //   }
      //   );
      // }

      if (promises.length > 10) {
        await Promise.all(promises).then(() => {
          console.log('descanso');
          promises = [];
        });
      }

    }
    Promise.all(promises).then(() => {
      console.log('paso');
      zip.generateAsync({ type: 'blob' }).then((content) => {
        if (content) {
          FileSaver.saveAs(content, name);
          this.ngxService.stop();
        }
      });
    })
  }

  async wait(ms) {
    return new Promise(resolve => {
      setTimeout(resolve, ms);
    });
  }

  async getDeclaFile(dato: any, justificacion: string) {
    const usuario = this.globals.usuario;
    const objPreview = {
      numeroDeclaracion: dato.numeroDeclaracion,
      idUsuario: dato.idUsuario,
      collName: dato.collName,
      tipoDeclaracion: dato.tipoDeclaracion
    };
    const data = await this.declaracionService.previewDeclaracionAsync(objPreview, usuario, justificacion).then((declaracion) => {
      const base64 = declaracion.body.data;
      const binary = atob(base64.replace(/\s/g, ''));
      const len = binary.length;
      const buffer = new ArrayBuffer(len);
      const view = new Uint8Array(buffer);
      for (let i = 0; i < len; i++) {
        view[i] = binary.charCodeAt(i);
      }
      return view;
    });

    return data;

  }


  async getAcuseFile(dato: any, justificacion: string) {
    const usuario = this.globals.usuario;
    const objPreview = {
      numeroDeclaracion: dato.numeroDeclaracion,
      idUsuario: dato.idUsuario,
      collName: dato.collName,
      tipoDeclaracion: dato.tipoDeclaracion
    };
    const data = await this.declaracionService.previewAcuseAsync(objPreview, usuario, justificacion).then((declaracion) => {
      const base64 = declaracion.body.data;
      const binary = atob(base64.replace(/\s/g, ''));
      const len = binary.length;
      const buffer = new ArrayBuffer(len);
      const view = new Uint8Array(buffer);
      for (let i = 0; i < len; i++) {
        view[i] = binary.charCodeAt(i);
      }
      return view;
    });

    return data;

  }

  descargaDomilioDeclarante(n: NgForm) {
    const justificacion = n.form.controls.justificaDomicilio.value;
    const usuario = this.globals.usuario;


    const declaracionDomicilio = {
      numeroDeclaracion: this.histPersona[0].numeroDeclaracion,
      idUsuario: this.histPersona[0].idUsuario,
      collName: this.histPersona[0].collName
    };

    this.declaracionService
      .getDomicilio(declaracionDomicilio, usuario, justificacion)
      .subscribe(
        data => {
          console.log(data);
          this.domicilios = data.body.domicilios;

          $('#justificarModalDomicilio').modal('hide');

          // Pasos necesario para remover completamente el backdrop
          $('body').removeClass('modal-open');
          $('.modal-backdrop').remove();

          
          $('#modalDomicilio').modal('show').on('shown.bs.modal', () => {
              $('body').css('overflow', 'hidden');
            }).on('hide.bs.modal', () => {
                $('body').css('overflow', 'auto');
              });

        },
        error => {
          console.log(error);
        }
      );


    
  }
  descargaDomiciliosCSV(){
    const abrev = this.nombreyreservado.nombre.split(/\s/).reduce((response, word)=> response += word.slice(0, 1), '');
    const today = new Date();
    const re = /\-/gi;
    const fechaFormat = today.toISOString().substring(0, 10).replace(re, '');

    const formatedDom = [];
    this.domicilios.forEach(dato => {
      const obj = {
        seccion: dato.seccion,
        propietario: dato.propietario,
        ubicacion: dato.ubicacion,
        pais: dato.pais,
        entidadProvincia: dato.entidadProvincia,
        municipioAlcaldia: dato.municipioAlcaldia,
        ciudadLocalidad: dato.ciudadLocalidad,
        codigoPostal: dato.codigoPostal,
        calle: dato.calle,
        numeroExterior: dato.numeroExterior,
        numeroInterior: dato.numeroInterior
      };
      formatedDom.push(obj);
    });

    const name = `${abrev}_${fechaFormat}`;
    const headers = ['Sección', 'Propietario', 'Ubicación', 'País', 'Entidad o Provincia', 'Municipio o Alcaldía', 'Ciudad o Localidad', 'Código Postal', 'Calle', 'Número Exterior', 'Número Interior'];
    this.exportCsvService.exportDocument(`${name}.csv`, formatedDom, headers);
  }
  
}
