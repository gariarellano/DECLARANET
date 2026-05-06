import {
  Component,
  OnInit,
  ViewChild,
  AfterViewInit
} from "@angular/core";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { MatPaginator, MatPaginatorIntl } from "@angular/material/paginator";
import { CommonService } from "../../../services/common.service";
import { Globals } from "src/app/globals";
import { ActivatedRoute, Router } from "@angular/router";
import * as $ from "jquery";
import { ExportToCsv } from '../../../services/exportcsv.service';

@Component({
  selector: 'app-unidad-admin',
  templateUrl: './unidad-admin.component.html',
  styleUrls: ['./unidad-admin.component.scss']
})
export class UnidadAdminComponent extends MatPaginatorIntl implements OnInit, AfterViewInit {
  count: number;
  data: any[];
  tipoDeclaracion: string = "Modificación";
  condiciones: any;
  carga: boolean;
  inst: boolean;
  displayedColumns: string[] = [
    "unidadAdministrativa",
    "obligado",
    "cumplio",
    "pendiente",
    "extemporaneo",
    "porcCumplimiento",
  ];
  dataSource: any;
  objCopiaNivel: any;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  datos = [];
  idEnte: string;
  anio: string;
  ente: string;
  ramo: string;
  ur: string;
  nivelJerar: string;
  nombreCorto: string;
  nivelJerarCopia: any = [];
  sindicalizado: string ;
  listaObligados: any;
  instReceptora: any;
  constructor(private modo: ExportToCsv, private commonService: CommonService, private globals: Globals, private rutaActiva: ActivatedRoute, private router: Router) {
    super();
    this.inst = false;
  }

  ngAfterViewInit(): void {
    this.paginator._intl.itemsPerPageLabel = this.globals.itemsPerPageLabel;
    this.paginator._intl.nextPageLabel = this.globals.nextPageLabel;
    this.paginator._intl.previousPageLabel = this.globals.previousPageLabel;
    this.paginator._intl.firstPageLabel = this.globals.firstPageLabel;
    this.paginator._intl.lastPageLabel = this.globals.lastPageLabel;
    this.paginator._intl.getRangeLabel = this.globals.spanishRangeLabel;

    this.nivelJerar = this.rutaActiva.snapshot.paramMap.get('nivelJerarquico');
    this.nombreCorto = this.rutaActiva.snapshot.paramMap.get('nombreCorto');
    this.instReceptora = JSON.parse(sessionStorage.getItem('institucionReceptora'));


    this.condiciones = {
      collName: this.instReceptora.collName,
      condiciones: {
        tipoDeclaracion: "MODIFICACION",
        anio: this.anio,
        idEnte: this.idEnte,
        ur: this.ur,
        idNivelJerarquico: this.nivelJerarCopia.listaNiveles,
        sindicalizado: this.nivelJerarCopia.sindicalizado
      }
    };
    this.commonService.obtenerInfoUA(this.condiciones).subscribe(
      (res) => {

        if (!res.obtenerInformacionUA.resultado) {
          //this.carga = false;
          $(".spinner").css("display", "flex");
        } else {
          this.datos = res.obtenerInformacionUA.resultado;

          this.datos.forEach((val) => {
            val.porcCumplimiento = val.porcCumplimiento.toFixed(2);
          });

          this.count = res.obtenerInformacionUA.total;
          if (this.count > 1) {
            this.inst = false;
          } else {
            this.inst = true;
          }

          this.dataSource = new MatTableDataSource(this.datos);
          this.dataSource.paginator = this.paginator;
          let pageSize = this.objCopiaNivel.paginator.size;
          let pageIndex = this.objCopiaNivel.paginator.pageIndex;
          this.dataSource.paginator._pageSize = pageSize;
          this.dataSource.paginator._pageIndex = pageIndex;

          this.dataSource.filterPredicate = (
            data: any,
            filter: string
          ): boolean => {
            const dataStr = Object.keys(data)
              .reduce((currentTerm: string, key: string) => {
                return (
                  currentTerm + (data as { [key: string]: any })[key] + "◬"
                );
              }, "")
              .normalize("NFD")
              .replace(/[\u0300-\u036f]/g, "")
              .toLowerCase();
            const transformedFilter = filter
              .trim()
              .normalize("NFD")
              .replace(/[\u0300-\u036f]/g, "")
              .toLowerCase();

            return dataStr.indexOf(transformedFilter) !== -1;
          };

          this.dataSource.sort = this.sort;
          let sort = this.objCopiaNivel.sort;
          this.dataSource.sort.active = sort.active;
          this.dataSource.sort._direction = sort._direction;
          this.dataSource.sort = this.dataSource.sort;
          this.applyFilter();
          //this.carga = true;

          $(".spinner").fadeOut("slow");
        }
      },
      (error) => {
        $(".spinner").fadeOut("slow");
        alert(`Ha ocurrido un error.  ${error}`);
      }
    );

  }

  ngOnInit() {
    this.listaObligados = [];
    this.nivelJerar = this.rutaActiva.snapshot.paramMap.get('nivelJerarquico');
    this.idEnte = this.rutaActiva.snapshot.paramMap.get("id");
    this.anio = this.rutaActiva.snapshot.paramMap.get('anio');

    this.ente = this.rutaActiva.snapshot.paramMap.get("ente");
    this.ente = this.ente.toUpperCase();

    this.ramo = this.rutaActiva.snapshot.paramMap.get("ramo");
    this.ramo = this.ramo.toUpperCase();

    this.ur = this.rutaActiva.snapshot.paramMap.get("ur");
    this.ur = this.ur.toUpperCase();
    this.nivelJerarCopia = JSON.parse(sessionStorage.getItem('copiaNivel'));
    this.nivelJerarCopia.filtroAvanzado.forEach(element => {
      if (element.checked) {
        this.listaObligados.push(element.valor);
      }
    });
    switch (this.nivelJerarCopia.sindicalizado) {
      case null:
        this.sindicalizado = "TODOS";
        break;
      case 'SOLO_SINDICALIZADOS':
        this.sindicalizado = "SINDICALIZADOS";
        break;
      case 'SIN_SINDICALIZADOS':
        this.sindicalizado = "NO SINDICALIZADOS";
        break;
      case 'INCLUIR_SINDICALIZADOS':
        this.sindicalizado = "INCLUIR SINDICALIZADOS";
        break;
    }
    this.objCopiaNivel = {
      filter: "",
      paginator: {
        size: 100,
        pageIndex: 0
      },
      sort: {
        active: null,
        _direction: ""
      }
    }
    if (sessionStorage.getItem('copiaNivelUnidad')) {
      this.objCopiaNivel = JSON.parse(sessionStorage.getItem('copiaNivelUnidad'));
    }
    sessionStorage.setItem('leyendaSpinner', 'Cargando Unidades Administrativas, por favor espera...');

  }
  exportCSV() {
    let datosFiltrados = JSON.parse(JSON.stringify(this.datos));
    datosFiltrados.forEach((element, index) => {
      delete datosFiltrados[index].idEnte;
      delete datosFiltrados[index].claveUa;
      delete datosFiltrados[index].ur;
      delete datosFiltrados[index].__typename;
    });
    let fechaHoy = new Date();
    const headers = ['Unidad Admin', 'Obligado', 'Pendiente', 'Cumplio', 'Extemporáneo', '% Cumplimiento'];
    this.modo.exportDocument(fechaHoy.getFullYear() + 'ModUA_' + this.ramo + '_' + this.nombreCorto + '_' + fechaHoy.getFullYear() + (fechaHoy.getMonth() + 1) + fechaHoy.getDate() + '.csv', datosFiltrados, headers);
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

  redirect(column: string, idEnte: string, ente: string, claveUa: string, unidadAdministrativa: number, nivelJerar) {
    this.guardaCopiaDatos();
    this.router.navigate(['/inicio/detalle', this.anio, column, idEnte, ente, claveUa, unidadAdministrativa, nivelJerar, this.nombreCorto]);
  }

  applyFilter() {
    this.dataSource.filter = this.objCopiaNivel.filter.trim().toLowerCase();
  }

  guardaCopiaDatos() {
    this.objCopiaNivel.paginator.size = this.dataSource.paginator._pageSize;
    this.objCopiaNivel.sort.active = this.dataSource.sort.active;
    this.objCopiaNivel.sort._direction = this.dataSource.sort._direction;
    this.objCopiaNivel.paginator.pageIndex = this.dataSource.paginator._pageIndex;
    sessionStorage.setItem('copiaNivelUnidad', JSON.stringify(this.objCopiaNivel));
  }

}
