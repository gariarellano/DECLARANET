import { NgModule } from "@angular/core";

import { RouterModule, Routes } from "@angular/router";
import { DetalleComponent } from "./detalle/detalle.component";
import { EnteComponent } from "./ente/ente.component";
//Componentes Material
import { MatTableModule } from "@angular/material/table";
import { MatSortModule } from "@angular/material/sort";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { MatInputModule } from "@angular/material/input";
import { CommonModule } from "@angular/common";
import { MatProgressBarModule } from "@angular/material/progress-bar";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { validadorFiltro } from './../../validators/filtroValidator';
import { validadorCurp } from './../../validators/validadorCurp';
import { validadorEmail } from './../../validators/validadorEmail';
import { ReactiveFormsModule } from '@angular/forms';

import {
  // MatPaginatorIntl,
  MatPaginatorModule,
} from "@angular/material/paginator";
import { MatAutocompleteModule } from "@angular/material/autocomplete";
import { UnidadAdminComponent } from './unidad-admin/unidad-admin.component';
//grafica
import { FormsModule } from '@angular/forms';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { VistaGraphComponent } from './vista-graph/vista-graph.component';
import { CumplimientoManualComponent } from './modals/cumplimiento-manual/cumplimiento-manual.component';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatRadioModule } from '@angular/material/radio';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { RevertimientoManualComponent } from './modals/revertimiento-manual/revertimiento-manual.component';
import { ExportToCsv } from '../../services/exportcsv.service';
import { AdminUsuariosComponent } from './admin-usuarios/admin-usuarios.component';
import { MatIconModule } from '@angular/material/icon';
import { VistasComponent } from './vistas/vistas.component';
// import { MenuNavModule } from "../menu-nav/menu-nav.module";


const routes: Routes = [
  { path: "", pathMatch: "full", component: EnteComponent },
  { path: "usuarios", component: AdminUsuariosComponent },
  { path: "detalle/:anio/:id/:dato/:inst/:ua/:uaName/:nivelJerarquico/:nombreCorto", component: DetalleComponent },
  { path: "graph", component: VistaGraphComponent },
  { path: "unidad/:anio/:id/:ente/:ramo/:ur/:nivelJerarquico/:nombreCorto", component: UnidadAdminComponent },
  //{ path: "details", component: DetalleComponent },
];
@NgModule({
  // tslint:disable-next-line:max-line-length
  declarations: [DetalleComponent, EnteComponent, UnidadAdminComponent, VistaGraphComponent, CumplimientoManualComponent, validadorFiltro, validadorCurp, validadorEmail, RevertimientoManualComponent, AdminUsuariosComponent, VistasComponent],
  imports: [
    FormsModule,
    NgxChartsModule,
    MatTableModule,
    MatSortModule,
    NgbModule,
    MatInputModule,
    MatPaginatorModule,
    MatAutocompleteModule,
    CommonModule,
    MatSlideToggleModule,
    ReactiveFormsModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatCheckboxModule,
    MatRadioModule,
    RouterModule.forChild(routes),
    MatSelectModule,
    MatIconModule,
    // MenuNavModule
  ],
  providers: [ExportToCsv]
})
export class HomeModule { }
