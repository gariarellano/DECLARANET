import { Component, OnInit, AfterViewInit, ViewChild } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, MatPaginatorIntl } from '@angular/material/paginator';
import { CommonService } from '../../../services/common.service';
import * as $ from 'jquery';
import { Globals } from 'src/app/globals';
import { NgbModal, NgbModalConfig } from '@ng-bootstrap/ng-bootstrap';
import { CumplimientoManualComponent } from '../modals/cumplimiento-manual/cumplimiento-manual.component';
import Swal from 'sweetalert2';
import { ExportToCsv } from 'src/app/services/exportcsv.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-detalle',
  templateUrl: './detalle.component.html',
  styleUrls: ['./detalle.component.scss'],
  providers: [NgbModalConfig, NgbModal]
})

export class DetalleComponent extends MatPaginatorIntl implements OnInit, AfterViewInit {

  //sweet alert 
  swalWithBootstrapButtons: any;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  //MODAL
  modalRef: any;
  count: number;
  data: any;
  filtro: any;
  mensaje: string = '';
  tipoDeclaracion: string = 'Modificación';
  flagUA: boolean;
  servidorPublico: any = {};
  esAdmin: boolean;
  displayedColumns: string[] = [
    'idPuesto',
    'fechaTomaPosesionPuesto',
    'curp',
    'primerApellido',
    'segundoApellido',
    'nombres',
    'unidadAdministrativa',
    'empleo',
    'activo',
  ];
  dataFiltrada = [];
  //displayedColumns: string[] = ["ente", "servidorPublico", "puesto", "estatus"];
  dataSource: any;
  objCopiaNivel: any;
  datos: any;
  id = '';
  ente = '';
  anio: string;
  ua: any;
  uaName = '';
  servidores = [];
  inst = '';
  nivelJerar: string;
  nombreCorto: string;
  nivelJerarCopia: any = [];
  cumplimientoManual: boolean = false;
  listaObligados: any;
  sindicalizado: string;
  tipoPersonal: string;
  registrosTabla: number;
  filtroCopia: any;
  cargaCsv: boolean;
  instReceptora: any;

  constructor(
    private modo: ExportToCsv,
    private readonly modalService: NgbModal,
    private rutaActiva: ActivatedRoute,
    private commonService: CommonService,
    private location: Location,
    private globals: Globals, private readonly toastrService: ToastrService
  ) {
    super();
    this.nextPageLabel = 'Siguiente página';
    this.previousPageLabel = 'Página anterior';
    this.itemsPerPageLabel = 'Registros por página';
    this.firstPageLabel = 'Primer página';
    this.lastPageLabel = 'Última página';
    this.flagUA = true;
    this.swalWithBootstrapButtons = Swal.mixin({
      customClass: {
        confirmButton: 'btn btn-success',
        cancelButton: 'btn btn-danger'
      },
      buttonsStyling: false
    });
    // tslint:disable-next-line:max-line-length
    this.esAdmin = (this.globals.profile.perfil === 'ADMINISTRADOR' || this.globals.profile.perfil === 'CONSULTA ADMIN');
  }

  ngAfterViewInit() {
    this.paginator._intl.itemsPerPageLabel = this.globals.itemsPerPageLabel;
    this.paginator._intl.nextPageLabel = this.globals.nextPageLabel;
    this.paginator._intl.previousPageLabel = this.globals.previousPageLabel;
    this.paginator._intl.firstPageLabel = this.globals.firstPageLabel;
    this.paginator._intl.lastPageLabel = this.globals.lastPageLabel;
    this.paginator._intl.getRangeLabel = this.globals.spanishRangeLabel;
    $('.spinner').css('display', 'flex');
    this.commonService.obtenerServidores(this.filtro).subscribe(
      (res) => {
        if (!res.obtenerServidores) {
          $('.spinner').css('display', 'flex');
        } else {
          this.datos = res.obtenerServidores;
          this.count = res.obtenerServidores.paginacion.total;
          this.datos.servidoresPublicos.forEach((element) => {

            if (element.datosRusp.unidadAdministrativa === null) {
              this.flagUA = false;
            }
            element.cumplimiento = element.cumplimiento == 'CUMPLIO' ? 'CUMPLIÓ' : element.cumplimiento;
            const idDNetNoLocalizado = element.datosDecla == null ? '' : element.datosDecla.idDNetNoLocalizado;
            this.data = {
              nombres: element.datosRusp.nombres,
              primerApellido: element.datosRusp.primerApellido,
              segundoApellido: element.datosRusp.segundoApellido,
              curp: element.datosRusp.curp,
              unidadAdministrativa: element.datosRusp.unidadAdministrativa,
              empleoCargoComision: element.datosRusp.empleoCargoComision,
              cumplimiento: element.cumplimiento,
              idRusp: element.datosRusp.id,
              anio: element.datosRusp.anio,
              tipoObligacion: element.datosRusp.tipoObligacion,
              nombreEnte: element.datosRusp.nombreEnte,
              nombreCompleto: element.datosRusp.nombreCompleto,
              idPuesto: element.datosRusp.idPuesto,
              fechaTomaPosesionPuesto: element.datosRusp.fechaTomaPosesionPuesto,
              idDNetNoLocalizado,
            };
            this.servidores.push(this.data);
          });


          this.servidores.sort(function (a, b) {
            if (a.curp > b.curp) {
              return 1;
            }
            if (a.curp < b.curp) {
              return -1;
            }
            return 0;
          });

          this.dataSource = new MatTableDataSource(this.servidores);
          this.dataSource.paginator = this.paginator;


          this.dataSource.sort = this.sort;
          let sort = this.objCopiaNivel.sort;
          this.dataSource.sort.active = sort.active;
          this.dataSource.sort._direction = sort._direction;
          this.dataSource.sort = this.dataSource.sort;
          this.applyFilter();
          this.dataSource.filterPredicate = (
            data: any,
            filter: string
          ): boolean => {
            const dataStr = Object.keys(data)
              .reduce((currentTerm: string, key: string) => {
                return (
                  currentTerm + (data as { [key: string]: any })[key] + '◬'
                );
              }, '')
              .normalize('NFD')
              .replace(/[\u0300-\u036f]/g, '')
              .toLowerCase();
            const transformedFilter = filter
              .trim()
              .normalize('NFD')
              .replace(/[\u0300-\u036f]/g, '')
              .toLowerCase();

            return dataStr.indexOf(transformedFilter) !== -1;
          };

          this.dataSource.paginator._pageSize = this.objCopiaNivel.paginator.size;
          this.dataSource.paginator._pageIndex = 500;

          $('.spinner').fadeOut('slow');
        }
      },
      (error) => {
        $('.spinner').fadeOut('slow');
        alert(`Ha ocurrido un error.  ${error}`);
      }
    );

  }

  ngOnInit() {
    this.instReceptora = JSON.parse(sessionStorage.getItem('institucionReceptora'));
    this.listaObligados = [];
    this.tipoPersonal = null;
    this.registrosTabla = 0;
    const permiso = this.globals.permisos.find(x => x.pagina === 0);
    const cumplimiento = permiso.acciones.indexOf('CUMPLIMIENTO_MANUAL');

    if (cumplimiento != -1) {
      this.cumplimientoManual = true;
    }
    sessionStorage.setItem('leyendaSpinner', 'Cargando servidores públicos, por favor espera...');
    this.inst = this.rutaActiva.snapshot.paramMap.get('inst');
    this.inst = this.inst.toUpperCase();

    this.id = this.rutaActiva.snapshot.paramMap.get('id');
    this.id = this.id.toUpperCase();
    switch (this.id) {
      case 'OBLIGADO':
        this.mensaje = 'OBLIGADOS ';
        break;
      case 'CUMPLIO':
        this.mensaje = 'CUMPLIERON ';
        break;
      case 'PENDIENTE':
        this.mensaje = 'PENDIENTES';
        break;
      case 'EXTEMPORANEO':
        this.mensaje = 'EXTEMPORÁNEOS ';
        break;
    }
    this.ente = this.rutaActiva.snapshot.paramMap.get('dato');
    this.anio = this.rutaActiva.snapshot.paramMap.get('anio');
    this.ua = this.rutaActiva.snapshot.paramMap.get('ua');
    this.nivelJerar = this.rutaActiva.snapshot.paramMap.get('nivelJerarquico');
    this.nombreCorto = this.rutaActiva.snapshot.paramMap.get('nombreCorto');
    this.nivelJerarCopia = JSON.parse(sessionStorage.getItem('copiaNivel'));
    this.tipoPersonal = this.nivelJerarCopia.sindicalizado;

    switch (this.tipoPersonal) {
      case null:
        this.sindicalizado = 'TODOS';
        break;
      case 'SOLO_SINDICALIZADOS':
        this.sindicalizado = 'SINDICALIZADOS';
        break;
      case 'SIN_SINDICALIZADOS':
        this.sindicalizado = 'NO SINDICALIZADOS';
        break;
      case 'INCLUIR_SINDICALIZADOS':
        this.sindicalizado = 'INCLUIR SINDICALIZADOS';
        break;

    }
    this.nivelJerarCopia.filtroAvanzado.forEach(element => {
      if (element.checked) {
        this.listaObligados.push(element.valor);
      }
    });

    this.uaName = this.rutaActiva.snapshot.paramMap.get('uaName');
    if (this.ua == 0) {
      this.ua = null;
    }
    this.filtro = {
      offset: 0,
      tamanio: 100,
      cumplimiento: this.id,
      collName: this.instReceptora.collName,
      condiciones: {
        idEnte: this.ente,
        claveUa: this.ua,
        tipoDeclaracion: 'MODIFICACION',
        anio: this.anio,
        idNivelJerarquico: this.nivelJerarCopia.listaNiveles,
        sindicalizado: this.tipoPersonal
      },
      ordenamiento: [
        {
          orden: 'ASC',
          campo: 'CURP'
        }
      ],
    };
    this.objCopiaNivel = {
      filter: '',
      paginator: {
        size: 100,
        pageIndex: 0
      },
      sort: {
        active: null,
        _direction: ''
      }
    }
    if (sessionStorage.getItem('copiaNivelServidores')) {
      this.objCopiaNivel = JSON.parse(sessionStorage.getItem('copiaNivelServidores'));
    }

  }
  exportCSV() {

    let servidores = [];
    if (this.count >= 100000) {
      this.showToaster();
    }
    if (this.count <= 1000) {
      this.filtro.offset = 0;
      this.filtro.tamanio = 1000;
      this.commonService.obtenerServidores(this.filtro).subscribe((res) => {
        this.datos = res.obtenerServidores;
        this.datos.servidoresPublicos.forEach((element) => {

          // const idDNetNoLocalizado = element.datosDecla == null ? '' : element.datosDecla.idDNetNoLocalizado;
          this.data = {
            tipoObligacion: element.datosRusp.tipoDeclaracionDesc,
            anio: element.datosRusp.anio,
            idPuesto: element.datosRusp.idPuesto,
            curp: element.datosRusp.curp,
            nombres: element.datosRusp.nombres,
            primerApellido: element.datosRusp.primerApellido,
            segundoApellido: element.datosRusp.segundoApellido,
            unidadAdministrativa: element.datosRusp.unidadAdministrativa,
            empleoCargoComision: element.datosRusp.empleoCargoComision,
            fechaTomaPosesionPuesto: element.datosRusp.fechaTomaPosesionPuesto,
            cumplimiento: element.datosRusp.cumplimientoDesc,
          };
          servidores.push(this.data);
        });
        console.info('datos id ', this.id);
        let id = this.id;
        let ua = '';

        this.id = this.id == 'OBLIGADO' ? 'Obl' : this.id;
        this.id = this.id == 'PENDIENTE' ? 'Pen' : this.id;
        this.id = this.id == 'EXTEMPORANEO' ? 'Ext' : this.id;
        this.id = this.id == 'CUMPLIO' ? 'Cump' : this.id;

        ua = this.ua == null ? '' : '_' + this.ua;
        let fechaHoy = new Date();
        const headers = ['Tipo obligación', 'Año', 'Id puesto', 'CURP', 'Nombres', 'Primer Apellido', 'Segundo Apellido', 'Unidad Administrativa', 'Puesto', 'Fecha toma posesión', 'Estatus'];
        this.modo.exportDocument(fechaHoy.getFullYear() + 'ModDetalle_' + this.nombreCorto + ua + '_' + this.id + '_' + fechaHoy.getFullYear() + (fechaHoy.getMonth() + 1) + fechaHoy.getDate() + '.csv', servidores, headers);
      });
    } else {
      // console.error("API PENDING");
      let fechaHoy = new Date();
      let ua = '';
      let id = this.id;
      if (this.id == 'OBLIGADO') {
        id = 'Obl'
      }
      this.cargaCsv = true;
      // this.id = this.id == 'OBLIGADO' ? 'Obl' : this.id;
      this.filtroCopia = JSON.parse(JSON.stringify(this.filtro));
      delete this.filtroCopia.offset;
      delete this.filtroCopia.tamanio;
      delete this.filtroCopia.condiciones.sindicalizado;
      this.filtroCopia.nombreReporte = fechaHoy.getFullYear() + 'ModDetalle' + ua + '_' + this.nombreCorto + '_' + id + '_' + fechaHoy.getFullYear() + (fechaHoy.getMonth() + 1) + fechaHoy.getDate();
      this.commonService.descargarCSV(this.filtroCopia).subscribe((res) => {
        if (!res) {
          console.log('ocurrio un error');
          this.cargaCsv = false;
        } else {
          // window.open(res.generarReporteServidores);
          this.cargaCsv = false;
          location.href = res.generarReporteServidores;
        }
      });
    }

  }
  applyFilter() {
    this.dataSource.filter = this.objCopiaNivel.filter.trim().toLowerCase();
  }

  getNextPaginator(event) {

    this.registrosTabla = 0;
    $('.spinner').css('display', 'flex');
    this.servidores = [];
    const offset = event.pageSize * event.pageIndex;
    this.filtro.offset = offset;
    this.filtro.tamanio = event.pageSize;
    this.commonService.obtenerServidores(this.filtro).subscribe(
      (res) => {

        this.datos = res.obtenerServidores;
        this.count = res.obtenerServidores.paginacion.total;

        this.datos.servidoresPublicos.forEach((element) => {
          const idDNetNoLocalizado = element.datosDecla == null ? '' : element.datosDecla.idDNetNoLocalizado;
          this.data = {
            nombres: element.datosRusp.nombres,
            primerApellido: element.datosRusp.primerApellido,
            segundoApellido: element.datosRusp.segundoApellido,
            curp: element.datosRusp.curp,
            unidadAdministrativa: element.datosRusp.unidadAdministrativa,
            empleoCargoComision: element.datosRusp.empleoCargoComision,
            cumplimiento: element.cumplimiento,
            idRusp: element.datosRusp.id,
            anio: element.datosRusp.anio,
            tipoObligacion: element.datosRusp.tipoObligacion,
            nombreEnte: element.datosRusp.nombreEnte,
            nombreCompleto: element.datosRusp.nombreCompleto,
            idPuesto: element.datosRusp.idPuesto,
            fechaTomaPosesionPuesto: element.datosRusp.fechaTomaPosesionPuesto,
            idDNetNoLocalizado,
          };
          this.servidores.push(this.data);
        });

        this.servidores.sort(function (a, b) {
          if (a.curp > b.curp) {
            return 1;
          }
          if (a.curp < b.curp) {
            return -1;
          }
          return 0;
        });

        this.dataSource = new MatTableDataSource(this.servidores);
        this.dataSource.sort = this.sort;

        let tiempo = setInterval(() => {
          this.registrosTabla = $('#tablaDetalle tr').length - 1;
          if (this.datos.servidoresPublicos.length == this.registrosTabla) {
            clearInterval(tiempo);
            $('.spinner').fadeOut('slow');
          }
        }, 1000);

      },
      (error) => {
        alert(`Ha ocurrido un error.  ${error}`);
      }
    );
  }

  volverAtras() {
    this.location.back();
  }

  // FUNCIONES MODALS
  openModal(obj: any) {
    this.modalRef = this.modalService.open(CumplimientoManualComponent, {
      centered: true,
      size: 'xl',
      scrollable: true
    });
    this.modalRef.componentInstance.datos = JSON.parse(JSON.stringify(obj));
    this.guardaCopiaDatosNivel();
  }

  aplicarCumplimiento() {
    this.swalWithBootstrapButtons.fire({
      title: '¿Estas seguro?',
      text: 'Se aplicara el cumplimiento manual',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Aceptar',
      cancelButtonText: 'Cancelar',
      reverseButtons: true
    }).then((result) => {
      if (result.value) {
        this.swalWithBootstrapButtons.fire(
          'Se capturo el cumplimiento manual!',
          'El estatus ha cambiado a CUMPLIDO',
          'success'
        );
        this.modalRef.hide();
      } else if (
        result.dismiss === Swal.DismissReason.cancel
      ) {
        this.swalWithBootstrapButtons.fire(
          'Cancelado',
          'No se realizo la captura manual de cumplimiento',
          'error'
        )
      }
    })
  }

  guardaCopiaDatosNivel() {
    this.objCopiaNivel.paginator.size = this.filtro.tamanio;
    this.objCopiaNivel.sort.active = this.dataSource.sort.active;
    this.objCopiaNivel.sort._direction = this.dataSource.sort._direction;
    this.objCopiaNivel.paginator.pageIndex = this.filtro.offset;
    sessionStorage.setItem('copiaNivelServidores', JSON.stringify(this.objCopiaNivel));
  }

  showToaster() {


    this.toastrService.info(
      `El proceso puede tardar algunos minutos, por la cantidad de información a exportar.`,
      `Por favor espere..`,
      {
        timeOut: 5000,
        extendedTimeOut: 3000,
        closeButton: true,
        tapToDismiss: true,
        positionClass: 'toast-top-right',
        progressBar: true
      }
    );
    // this.toastr.success(
    //   'No olvides guardar tus cambios.',
    //   ``,
    //   {
    //     timeOut: 5000,
    //     extendedTimeOut: 5000,
    //     closeButton: true,
    //     tapToDismiss: true,
    //     positionClass: 'toast-top-center',
    //     progressBar: true
    //   }
    // );
  }
}
