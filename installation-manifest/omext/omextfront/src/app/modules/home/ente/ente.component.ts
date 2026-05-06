import {
  Component,
  OnInit,
  ViewChild,
  AfterViewInit,
  TemplateRef
} from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, MatPaginatorIntl } from '@angular/material/paginator';
import { CommonService } from '../../../services/common.service';
import { Globals } from 'src/app/globals';
import { Router } from '@angular/router';
import * as $ from 'jquery';
import { multi, dataDona } from '.././data'
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import Swal from 'sweetalert2';
import { CumplimientoManualComponent } from '../modals/cumplimiento-manual/cumplimiento-manual.component';
import { RevertimientoManualComponent } from '../modals/revertimiento-manual/revertimiento-manual.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormControl } from '@angular/forms';
import { ThemePalette } from '@angular/material/core';
import { ExportToCsv } from '../../../services/exportcsv.service';

@Component({
  selector: 'app-ente',
  templateUrl: './ente.component.html',
  styleUrls: ['./ente.component.scss'],
})
export class EnteComponent extends MatPaginatorIntl implements OnInit, AfterViewInit {

  nivelesJul = new FormControl();
  nivelesDic = new FormControl();
  nivelesTodos = new FormControl();
  listaNivelesJul: any[];

  listaNivelesDic: any[];

  listaNivelesTodos: any[];
  listaNivelesTodosOriginal: any[];
  listaNivelesTodosCopia: Array<any>;
  listaNivelesTodosRespaldoModal: Array<any>;

  //Busqueda servidores
  filtroServidores: any;
  filtroInputEntes: string = '';
  filtroGrupoNivelJerarquico: string;
  filtroAnio: string;
  filtroNivelJerarquico: any;
  //sweet alert 
  swalWithBootstrapButtons: any;
  modalRef2: any;
  //MODALS
  modalRef: BsModalRef | null;

  //Gráfica linear
  multi: any[];
  view: any[] = [1000, 300];
  esAdmin: boolean;
  // options
  legend: boolean = true;
  showLabels: boolean = true;
  animations: boolean = true;
  xAxis: boolean = true;
  yAxis: boolean = true;
  showYAxisLabel: boolean = true;
  showXAxisLabel: boolean = true;
  xAxisLabel: string = 'Año';
  yAxisLabel: string = 'Declaraciones';
  timeline: boolean = true;
  servidorPublico: any = {};
  colorScheme = {
    domain: ['#5AA454', '#E44D25', '#CFC0BB', '#7aa3e5', '#a8385d', '#aae3f5']
  };

  //grafica donas
  data2: any[];
  view2: any[] = [500, 400];

  // options
  gradient: boolean = true;
  showLegend: boolean = true;
  showLabels2: boolean = true;
  isDoughnut: boolean = false;
  colorScheme2 = {
    domain: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA'],
  };

  //grafica barras 
  view3: any[] = [700, 400];

  // options
  showXAxis = true;
  showYAxis = true;
  gradient3 = false;
  showLegend3 = true;
  showXAxisLabel3 = true;
  xAxisLabel3 = 'Country';
  showYAxisLabel3 = true;
  yAxisLabel3 = 'Population';

  colorScheme3 = {
    domain: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA']
  };
  datosCard: any = {};
  dataComprobante: Array<any>;
  count: number;
  data: any[];
  tipoDeclaracion: string = 'Modificación';
  anio: number;
  condiciones: any;
  inst: boolean;
  displayedColumns: string[] = [
    'nombreEnte',
    'obligado',
    'cumplio',
    'pendiente',
    'extemporaneo',
    'porcCumplimiento',
  ];
  displayedColumnsModal: string[] = ['curp', 'nombres', 'primerApellido', 'segundoApellido', 'entidad', 'estatus'];
  displayedColumnServidores: string[] = [
    'idPuesto', 'fechaTomaPosesionPuesto',
    'curp',
    'primerApellido',
    'segundoApellido',
    'nombres',
    'empleo',
    'cumplimiento',
  ];

  displayedColumnsModal2: string[] = ['valorNivel', 'areaAds', 'empleoCargo'];
  dataSource: any;
  dataSourceModal: any;
  dataSourceModalComprobante: any;
  @ViewChild('firstPaginator', { static: true }) paginator: MatPaginator;
  @ViewChild('firstSort', { static: true }) sort: MatSort;
  @ViewChild('secondPaginator', { static: true }) secondPaginator: MatPaginator;
  @ViewChild('secondSort', { static: true }) sort2: MatSort;
  datos = [];
  datosServidores: any;
  verDatosComprobante: boolean = false;
  noComprobante: number;
  instReceptora: any;
  flagBuscandoComprobante: boolean = false;
  flagBuscandoEnte: boolean = false;
  numeroDeclaracion: any;
  cumplimientoServidor: string;
  filtroServidorCumplimiento: string;
  servidores = [];
  listIdEntes: any[];
  objCopia: any;
  nivelSelect: any = [];
  color: ThemePalette;
  tipoPersonal: string;
  primeraCarga: boolean;
  listaTipoPersonal: any[] = [{ label: 'Todos', value: null }, { label: 'Solo sindicalizados', value: 'SOLO_SINDICALIZADOS' }, { label: 'Excluir sindicalizados', value: 'SIN_SINDICALIZADOS' }, { label: 'Incluir sindicalizados', value: 'INCLUIR_SINDICALIZADOS' }];
  cumplimientoManual: boolean = false;

  constructor(private modo: ExportToCsv, private commonService: CommonService, private readonly modalService2: NgbModal, private modalService: BsModalService, private globals: Globals, private router: Router) {

    super();
    this.inst = false;
    const uri = window.location.toString();
    if (uri.indexOf('?') > 0) {
      const cleanUri = uri.substring(0, uri.indexOf('?'));
      window.history.replaceState({}, document.title, cleanUri);
    }

    Object.assign(this, { multi });
    Object.assign(this, { dataDona });
    this.esAdmin = false;

    this.swalWithBootstrapButtons = Swal.mixin({
      customClass: {
        confirmButton: 'btn btn-success',
        cancelButton: 'btn btn-danger'
      },
      buttonsStyling: false
    });
    this.color = 'warn';
  }

  ngAfterViewInit() {

    this.listIdEntes = [];
    this.instReceptora = JSON.parse(sessionStorage.getItem('institucionReceptora'));
    const entes = this.globals.entes;

    this.paginator._intl.itemsPerPageLabel = this.globals.itemsPerPageLabel;
    this.paginator._intl.nextPageLabel = this.globals.nextPageLabel;
    this.paginator._intl.previousPageLabel = this.globals.previousPageLabel;
    this.paginator._intl.firstPageLabel = this.globals.firstPageLabel;
    this.paginator._intl.lastPageLabel = this.globals.lastPageLabel;
    this.paginator._intl.getRangeLabel = this.globals.spanishRangeLabel;

    this.listIdEntes = entes.map((inst) => {
      return inst.id;
    });


    // tslint:disable-next-line:max-line-length
    if ((this.globals.profile.perfil === 'ADMINISTRADOR' || this.globals.profile.perfil === 'CONSULTA ADMIN')) {
      this.listIdEntes = [];
      setTimeout(() => {
        this.esAdmin = true;
      }, 150);
      if (this.objCopia.obligados === '0') {
        this.condiciones = {
          idEnte: this.listIdEntes,
          //mes: "MAYO",
          anio: this.filtroAnio,
          tipoDeclaracion: 'MODIFICACION',
          idNivelJerarquico: null,
          sindicalizado: this.tipoPersonal
        };

        this.filtroGrupoNivelJerarquico = '0';
      } else {
        this.condiciones = {
          idEnte: this.listIdEntes,
          //mes: "MAYO",
          anio: this.filtroAnio,
          tipoDeclaracion: 'MODIFICACION',
          idNivelJerarquico: this.objCopia.listaNiveles,
          sindicalizado: this.tipoPersonal
        };
        this.filtroGrupoNivelJerarquico = this.objCopia.obligados;
      }
    } else {
      this.esAdmin = false;
      this.condiciones = {
        idEnte: this.listIdEntes,
        //mes: "MAYO",
        anio: this.filtroAnio,
        tipoDeclaracion: 'MODIFICACION',
        idNivelJerarquico: this.objCopia.obligados === 'NJ_31_12_20' ? [1, 2, 3] : this.objCopia.listaNiveles,
        sindicalizado: this.tipoPersonal
      };
      this.filtroGrupoNivelJerarquico = this.objCopia.obligados == 'NJ_31_12_20' ? 'NJ_31_12_20' : this.objCopia.obligados;

    }

    switch (this.objCopia.obligados) {
      case '0':
        this.nivelesTodos.setValue(this.objCopia.listaNiveles);
        break;
      case 'NJ_31_07_20':
        this.nivelesJul.setValue(this.objCopia.listaNiveles);
        break;
      case 'NJ_31_12_20':
        this.nivelesDic.setValue(this.objCopia.listaNiveles);
        break;
    }
    if (!this.primeraCarga) {
      this.obtenerInst();
    }
  }
  exportCSV() {

    const datosFiltrados = JSON.parse(JSON.stringify(this.datos));
    datosFiltrados.forEach((element, index) => {
      delete datosFiltrados[index].ur;
      delete datosFiltrados[index].ramo;
      delete datosFiltrados[index].__typename;
      delete datosFiltrados[index].idEnte;
      delete datosFiltrados[index].situacion;
      delete datosFiltrados[index].nivelJerarquico;
      delete datosFiltrados[index].nombreCorto;
    });


    const fechaHoy = new Date();
    const headers = ['Ente', 'Obligados', 'Pendientes', 'Cumplieron', 'Extemporáneo', '% Cumplimiento'];
    this.modo.exportDocument(fechaHoy.getFullYear() + 'ModResumen_' + fechaHoy.getFullYear() + (fechaHoy.getMonth() + 1) + fechaHoy.getDate() + '.csv', datosFiltrados, headers);
  }
  creaObjPlantillaObligados() {
    this.listaNivelesJul = [
      {
        fk: 10,
        id: 1,
        valor: 'OPERATIVO (A) U HOMOLOGO (A)',
        valorUno: 'S',
        checked: false
      }, {
        fk: 20,
        id: 2,
        valor: 'ENLACE U HOMOLOGO (A)',
        valorUno: 'S',
        checked: false
      },
      {
        fk: 30,
        id: 3,
        valor: 'JEFE (A) DE DEPARTAMENTO U HOMOLOGO (A)',
        valorUno: 'C',
        checked: false
      },
      {
        fk: 40,
        id: 4,
        valor: 'SUBDIRECTOR (A) DE ÁREA U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 50,
        id: 5,
        valor: 'DIRECTOR (A) DE ÁREA U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 60,
        id: 6,
        valor: 'DIRECTOR (A) GENERAL ADJUNTO U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 70,
        id: 7,
        valor: 'DIRECTOR (A) GENERAL U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 80,
        id: 8,
        valor: 'TITULAR DE UNIDAD U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 90,
        id: 9,
        valor: 'SUBSECRETARIO (A) DE ESTADO U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 100,
        id: 10,
        valor: 'SECRETARIO (A) DE ESTADO U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 110,
        id: 11,
        valor: 'PRESIDENTE (A) DE LA REPÚBLICA U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      }
    ];
    this.listaNivelesDic = [
      {
        fk: 10,
        id: 1,
        valor: 'OPERATIVO (A) U HOMOLOGO (A)',
        valorUno: 'S',
        checked: true
      }, {
        fk: 20,
        id: 2,
        valor: 'ENLACE U HOMOLOGO (A)',
        valorUno: 'S',
        checked: true
      },
      {
        fk: 30,
        id: 3,
        valor: 'JEFE (A) DE DEPARTAMENTO U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 40,
        id: 4,
        valor: 'SUBDIRECTOR (A) DE ÁREA U HOMOLOGO (A)',
        valorUno: 'C',
        checked: false
      },
      {
        fk: 50,
        id: 5,
        valor: 'DIRECTOR (A) DE ÁREA U HOMOLOGO (A)',
        valorUno: 'C',
        checked: false
      },
      {
        fk: 60,
        id: 6,
        valor: 'DIRECTOR (A) GENERAL ADJUNTO U HOMOLOGO (A)',
        valorUno: 'C',
        checked: false
      },
      {
        fk: 70,
        id: 7,
        valor: 'DIRECTOR (A) GENERAL U HOMOLOGO (A)',
        valorUno: 'C',
        checked: false
      },
      {
        fk: 80,
        id: 8,
        valor: 'TITULAR DE UNIDAD U HOMOLOGO (A)',
        valorUno: 'C',
        checked: false
      },
      {
        fk: 90,
        id: 9,
        valor: 'SUBSECRETARIO (A) DE ESTADO U HOMOLOGO (A)',
        valorUno: 'C',
        checked: false
      },
      {
        fk: 100,
        id: 10,
        valor: 'SECRETARIO (A) DE ESTADO U HOMOLOGO (A)',
        valorUno: 'C',
        checked: false
      },
      {
        fk: 110,
        id: 11,
        valor: 'PRESIDENTE (A) DE LA REPÚBLICA U HOMOLOGO (A)',
        valorUno: 'C',
        checked: false
      }
    ];
    this.listaNivelesTodos = [
      {
        fk: 10,
        id: 1,
        valor: 'OPERATIVO (A) U HOMOLOGO (A)',
        valorUno: 'S',
        checked: true
      }, {
        fk: 20,
        id: 2,
        valor: 'ENLACE U HOMOLOGO (A)',
        valorUno: 'S',
        checked: true
      },
      {
        fk: 30,
        id: 3,
        valor: 'JEFE (A) DE DEPARTAMENTO U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 40,
        id: 4,
        valor: 'SUBDIRECTOR (A) DE ÁREA U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 50,
        id: 5,
        valor: 'DIRECTOR (A) DE ÁREA U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 60,
        id: 6,
        valor: 'DIRECTOR (A) GENERAL ADJUNTO U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 70,
        id: 7,
        valor: 'DIRECTOR (A) GENERAL U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 80,
        id: 8,
        valor: 'TITULAR DE UNIDAD U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 90,
        id: 9,
        valor: 'SUBSECRETARIO (A) DE ESTADO U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 100,
        id: 10,
        valor: 'SECRETARIO (A) DE ESTADO U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 110,
        id: 11,
        valor: 'PRESIDENTE (A) DE LA REPÚBLICA U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      }
    ];
    this.listaNivelesTodosOriginal = [
      {
        fk: 10,
        id: 1,
        valor: 'OPERATIVO (A) U HOMOLOGO (A)',
        valorUno: 'S',
        checked: true
      }, {
        fk: 20,
        id: 2,
        valor: 'ENLACE U HOMOLOGO (A)',
        valorUno: 'S',
        checked: true
      },
      {
        fk: 30,
        id: 3,
        valor: 'JEFE (A) DE DEPARTAMENTO U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 40,
        id: 4,
        valor: 'SUBDIRECTOR (A) DE ÁREA U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 50,
        id: 5,
        valor: 'DIRECTOR (A) DE ÁREA U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 60,
        id: 6,
        valor: 'DIRECTOR (A) GENERAL ADJUNTO U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 70,
        id: 7,
        valor: 'DIRECTOR (A) GENERAL U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 80,
        id: 8,
        valor: 'TITULAR DE UNIDAD U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 90,
        id: 9,
        valor: 'SUBSECRETARIO (A) DE ESTADO U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 100,
        id: 10,
        valor: 'SECRETARIO (A) DE ESTADO U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      },
      {
        fk: 110,
        id: 11,
        valor: 'PRESIDENTE (A) DE LA REPÚBLICA U HOMOLOGO (A)',
        valorUno: 'C',
        checked: true
      }

    ];

  }

  ngOnInit() {
    this.tipoPersonal = null;
    // this.filtroGrupoNivelJerarquico = 'NJ_31_12_20';
    this.filtroAnio = '2021';
    this.listaNivelesTodosCopia = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11];

    const permiso = this.globals.permisos.find(x => x.pagina === 0);
    const cumplimiento = permiso.acciones.indexOf('CUMPLIMIENTO_MANUAL');
    if (cumplimiento !== -1) {
      this.cumplimientoManual = true;
    }

    this.creaObjPlantillaObligados();
    // $('.spinner').css('display', 'flex');
    sessionStorage.setItem('leyendaSpinner', 'Cargando instituciones, por favor espera...');
    this.objCopia = {
      filter: '',
      obligados: '0',
      listaNiveles: [1, 2, 3],
      paginator: {
        size: 100,
        pageIndex: 0
      },
      sort: {
        active: null,
        _direction: ''
      },
      filtroAvanzado: null,
      sindicalizado: null

    }
    if (sessionStorage.getItem('copiaNivel')) {
      this.objCopia = JSON.parse(sessionStorage.getItem('copiaNivel'));
      this.filtroAnio = this.objCopia.anio;
      this.filtroGrupoNivelJerarquico = this.objCopia.obligados;
      this.tipoPersonal = this.objCopia.sindicalizado;
      this.nivelSelect = this.objCopia.listaNiveles;
      this.listaNivelesTodosCopia = this.objCopia.filtroAvanzado;
      this.applyFilter();
    } else {
      // this.listaNivelesTodosCopia = JSON.parse(JSON.stringify(this.listaNivelesTodos));
      // TEMPORAL PARA QUITAR TODOS DE LA CARGA INICIAL
      this.primeraCarga = true;
      this.listaNivelesTodosCopia = JSON.parse(JSON.stringify(this.listaNivelesTodos));
      this.nivelSelect = [];
      this.listaNivelesTodosCopia.forEach(nivel => {
        if (nivel.checked) {
          this.nivelSelect.push(nivel.id);
        }
      });
    }

  }

  obtenerInst() {
    $('.spinner').css('display', 'flex');
    if (this.condiciones.anio.toString() === '2021') {
      this.condiciones.idNivelJerarquico = null;
    }

    this.commonService.obtenerInstituciones(this.instReceptora.collName, this.condiciones).subscribe(
      (res) => {
        if (!res.obtenerInformacionInst.resultado) {
          $('.spinner').css('display', 'flex');
        } else {
          this.datos = res.obtenerInformacionInst.resultado;
          this.datos.forEach((val) => {
            val.porcCumplimiento = val.porcCumplimiento.toFixed(2);
            val.nivelJerarquico = this.filtroGrupoNivelJerarquico;
          });

          this.count = res.obtenerInformacionInst.total;
          if (this.count > 1) {
            this.inst = false;
          } else {
            this.inst = true;
          }

          this.dataSource = new MatTableDataSource(this.datos);
          this.dataSource.paginator = this.paginator;
          const pageSize = this.objCopia.paginator.size;
          const pageIndex = this.objCopia.paginator.pageIndex;
          this.dataSource.paginator._pageSize = pageSize;
          this.dataSource.paginator._pageIndex = pageIndex;


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

          this.dataSource.sort = this.sort;
          const sort = this.objCopia.sort;
          this.dataSource.sort.active = sort.active;
          this.dataSource.sort._direction = sort._direction;
          this.dataSource.sort = this.dataSource.sort;
          this.applyFilter();

          $('.spinner').fadeOut('slow');


        }
      },
      (error) => {
        $('.spinner').fadeOut('slow');
        Swal.fire(
          'Ha ocurrido un error',
          `Por favor, intente de nuevo mas tarde...`,
          'error'
        )
      }
    );
  }

  changeAnio() {
    this.filtroGrupoNivelJerarquico = '0';
    this.changeNivelJerarquico();
  }

  changeNivelJerarquico() {

    // $('.spinner').css('display', 'flex');
    this.listaNivelesTodosCopia.forEach(nivel => {
      switch (this.filtroGrupoNivelJerarquico) {
        case '0':
          nivel.checked = true;
          this.tipoPersonal = null;
          break;
        case 'NJ_31_07_20':
          nivel.checked = nivel.id > 3;
          this.tipoPersonal = 'SIN_SINDICALIZADOS';

          break;
        case 'NJ_31_12_20':
          nivel.checked = nivel.id <= 3;
          this.tipoPersonal = 'INCLUIR_SINDICALIZADOS';

          break;
      }
    });
    this.aplicarFiltro();
  }

  buscarServidor() {
    this.flagBuscandoEnte = true;
    this.servidores = [];
    this.filtroServidores = {
      collName: this.instReceptora.collName,
      cumplimiento: this.filtroServidorCumplimiento,
      condiciones: {
        idEnte: this.esAdmin ? null : this.listIdEntes,
        tipoDeclaracion: 'MODIFICACION',
        anio: this.filtroAnio,
        nombresApellidos: this.filtroInputEntes,
        sindicalizado: null
      }
    };
    this.commonService.buscarServidores(this.filtroServidores).subscribe((res) => {
      this.datosServidores = res.buscarServidores;
      this.datosServidores.servidoresPublicos.forEach((element) => {

        element.cumplimiento = element.cumplimiento === 'CUMPLIO' ? 'CUMPLIÓ' : element.cumplimiento;
        const idDNetNoLocalizado = element.datosDecla == null ? '' : element.datosDecla.idDNetNoLocalizado;
        const data = {
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
          idDNetNoLocalizado
        };

        this.servidores.push(data);

      }
      );
      if (this.servidores.length > 0) {
        this.flagBuscandoEnte = false;
      } else {
        this.flagBuscandoEnte = false;
        Swal.fire(
          'No existen coincidencias',
          'Intenta con una búsqueda más específica',
          'info'
        );
      }
      this.dataSourceModal = new MatTableDataSource(this.servidores);

    },
      (error) => {
        $('.spinner').fadeOut('slow');
        Swal.fire(
          'Ha ocurrido un error',
          `Por favor, intente de nuevo mas tarde...`,
          'error'
        );
        this.flagBuscandoEnte = false;
      });

  }

  toUpperCase() {
    this.filtroInputEntes = this.filtroInputEntes.toUpperCase();
  }

  aplicarFiltro() {
    this.objCopia.filtroAvanzado = this.listaNivelesTodosCopia;
    this.objCopia.sindicalizado = this.tipoPersonal;
    this.objCopia.anio = this.filtroAnio;
    this.listaNivelesTodosRespaldoModal = JSON.parse(JSON.stringify(this.listaNivelesTodosCopia));
    sessionStorage.setItem('copiaNivel', JSON.stringify(this.objCopia));
    this.nivelSelect = [];
    this.listaNivelesTodos = JSON.parse(JSON.stringify(this.listaNivelesTodosCopia));
    this.listaNivelesTodosCopia.forEach(nivel => {
      if (nivel.checked) {
        this.nivelSelect.push(nivel.id);
      }
    });

    switch (this.filtroGrupoNivelJerarquico) {
      case '0':
        this.listaNivelesTodosOriginal.forEach((value, index) => {
          if (value.checked != this.listaNivelesTodosCopia[index].checked) {
            this.filtroGrupoNivelJerarquico = 'avanzado';
          }
          if (this.tipoPersonal != null) {
            this.filtroGrupoNivelJerarquico = 'avanzado';
          }
        });

        break;
      case 'NJ_31_07_20': this.listaNivelesJul.forEach((value, index) => {
        if (value.checked != this.listaNivelesTodosCopia[index].checked) {
          this.filtroGrupoNivelJerarquico = 'avanzado';
        }
        if (this.tipoPersonal != 'SIN_SINDICALIZADOS') {
          this.filtroGrupoNivelJerarquico = 'avanzado';
        }
      });
        break;
      case 'NJ_31_12_20':
        this.listaNivelesDic.forEach((value, index) => {
          if (value.checked != this.listaNivelesTodosCopia[index].checked) {
            this.filtroGrupoNivelJerarquico = 'avanzado';
          }
          if (this.tipoPersonal != 'INCLUIR_SINDICALIZADOS') {
            this.filtroGrupoNivelJerarquico = 'avanzado';
          }
        });
        break;

      default:
        break;
    }

    this.condiciones = {
      //mes: "MAYO",
      anio: this.filtroAnio,
      tipoDeclaracion: 'MODIFICACION',
      idNivelJerarquico: this.nivelSelect,
      sindicalizado: this.tipoPersonal
    };

    if (this.globals.profile.perfil === 'ADMINISTRADOR' || this.globals.profile.perfil === 'CONSULTA ADMIN') {
      this.listIdEntes = [];
      setTimeout(() => {
        this.esAdmin = true;
      }, 150);
    }
    else {
      this.esAdmin = false;
      this.condiciones = {
        idEnte: this.listIdEntes,
        //mes: "MAYO",
        anio: this.filtroAnio,
        tipoDeclaracion: 'MODIFICACION',
        idNivelJerarquico: [],
        sindicalizado: this.tipoPersonal
      };
      if (this.filtroGrupoNivelJerarquico === '0') {
        this.condiciones.idNivelJerarquico = [];
      } else {
        this.condiciones.idNivelJerarquico = this.nivelSelect;
      }
    }

    // this.obtenerInst();

  }

  consultar() {
    this.obtenerInst();
  }

  abrirFiltroAvanzado() {
    this.listaNivelesTodosRespaldoModal = JSON.parse(JSON.stringify(this.listaNivelesTodosCopia));
    if (sessionStorage.getItem('copiaNivel')) {
      this.objCopia = JSON.parse(sessionStorage.getItem('copiaNivel'));
      this.listaNivelesTodosCopia = this.objCopia.filtroAvanzado;
      this.tipoPersonal = this.objCopia.sindicalizado;
      this.applyFilter();
    }
  }

  buscarComprobante() {

    this.flagBuscandoComprobante = true;
    this.commonService.consultaObligacion(this.noComprobante, this.instReceptora.collName).subscribe((ente) => {
      if (!ente) {
        this.dataComprobante = null;
        Swal.fire(
          'No se encontro el comprobante',
          'Escribe un comprobante válido',
          'info'
        );
        this.flagBuscandoComprobante = true;
      } else {
        this.datosCard = ente.body['data'][0];
        this.numeroDeclaracion = this.datosCard.numeroDeclaracion;
        this.dataComprobante = ente.body['data'];

        this.dataSourceModalComprobante = new MatTableDataSource(this.dataComprobante);
        this.flagBuscandoComprobante = false;
      }

    });

    this.verDatosComprobante = true;

  }

  getTotal(tipo: string) {
    switch (tipo) {
      case 'Obligado':
        return this.datos.map(t => t.obligado).reduce((acc, value) => acc + value, 0);
        break;
      case 'Cumplio':
        return this.datos.map(t => t.cumplio).reduce((acc, value) => acc + value, 0);
        break;
      case 'porcCumplimiento':
        const o = this.datos.map(t => t.obligado).reduce((acc, value) => acc + value, 0);
        const c = this.datos.map(t => t.cumplio).reduce((acc, value) => acc + value, 0)

        return ((c / o) * 100).toFixed(2);
        break;
      case 'Pendiente':
        return this.datos.map(t => t.pendiente).reduce((acc, value) => acc + value, 0);
        break;
      case 'Extemporaneo':
        return this.datos.map(t => t.extemporaneo).reduce((acc, value) => acc + value, 0);
        break;

    }
  }

  redirect(isIns: string, status: string, id: string, ente: string, ramo: number, ur, nivel, nombreCorto: string, anio: string) {
    anio = anio || this.filtroAnio;
    this.guardaDatosNivelStorage();
    if (isIns == 'ins') {
      if (ur === '0') {
        this.router.navigate(['/inicio/unidad', anio, id, ente, ramo, ur, nivel, nombreCorto]);
      } else {
        this.router.navigate(['/inicio/detalle', anio, status, id, ente, 0, 0, nivel, nombreCorto]);
      }
    } else {
      this.router.navigate(['/inicio/detalle', anio, status, id, ente, 0, 0, nivel, nombreCorto]);
    }
  }

  applyFilter() {
    if (this.dataSource) {
      this.dataSource.filter = this.objCopia ? this.objCopia.filter.trim().toLowerCase() : null;
    }
  }

  goGraph() {
    this.router.navigate(['/inicio/graph']);
  }
  //........FUNCIONES GRAFICA LINEAR.........
  onSelect(data): void {
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }

  onActivate(data): void {
    console.log('Activate', JSON.parse(JSON.stringify(data)));
  }

  onDeactivate(data): void {
    console.log('Deactivate', JSON.parse(JSON.stringify(data)));
  }

  //.............FUNCIONES GRAFICA DONA.................
  onSelect2(data): void {
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }

  onActivate2(data): void {
    console.log('Activate', JSON.parse(JSON.stringify(data)));
  }

  onDeactivate2(data): void {
    console.log('Deactivate', JSON.parse(JSON.stringify(data)));
  }

  //.............FUNCIONES GRAFICA BARRAS .............
  onSelect3(event) {
  }

  //FUNCIONES MODALS
  abrirModalCumplimiento(template: TemplateRef<any>, modal: string) {
    this.filtroServidorCumplimiento = modal;
    this.modalRef = this.modalService.show(template, { class: 'modal-xl modal-dialog-centered overflow-auto' });
  }
  //FUNCIONES MODALS
  abrirModalRevertirCumplimiento(template: TemplateRef<any>, modal: string) {
    this.filtroServidorCumplimiento = modal;
    this.modalRef = this.modalService.show(template, { class: 'modal-xl modal-dialog-centered overflow-auto' });
  }

  abrirModalCumplimientoServidorPublico(obj: any) {
    this.modalRef2 = this.modalService2.open(CumplimientoManualComponent, {
      centered: true,
      size: 'xl',
      scrollable: true
    });
    this.modalRef2.componentInstance.datos = JSON.parse(JSON.stringify(obj));
  }
  abrirModalRevertimientoServidorPublico(obj: any) {
    this.modalRef2 = this.modalService2.open(RevertimientoManualComponent, {
      centered: true,
      size: 'xl',
      scrollable: true
    });
    this.modalRef2.componentInstance.datos = JSON.parse(JSON.stringify(obj));
  }

  cerrarModalBuscarServidor() {
    this.filtroInputEntes = null;
    this.servidores = [];
    this.dataSourceModal = new MatTableDataSource(this.servidores);
    this.modalRef.hide();
  }

  guardaDatosNivelStorage() {
    this.objCopia.paginator.size = this.dataSource.paginator._pageSize;
    this.objCopia.sort.active = this.dataSource.sort.active;
    this.objCopia.sort._direction = this.dataSource.sort._direction;
    this.objCopia.paginator.pageIndex = this.dataSource.paginator._pageIndex;
    this.objCopia.obligados = this.filtroGrupoNivelJerarquico;
    this.objCopia.anio = this.filtroAnio;
    this.objCopia.listaNiveles = this.nivelSelect;
    this.objCopia.filtroAvanzado = this.listaNivelesTodosCopia;
    this.objCopia.sindicalizado = this.tipoPersonal;
    sessionStorage.setItem('copiaNivel', JSON.stringify(this.objCopia));
  }
}
