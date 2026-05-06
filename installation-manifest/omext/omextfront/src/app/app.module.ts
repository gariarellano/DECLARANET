import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { PieGraphComponent } from './components/pie-graph/pie-graph.component';
import { PieGraph2Component } from './components/pie-graph2/pie-graph2.component';
import { PruebaService } from './../app/services/prueba.service';
import { GraphQLModule } from './../app/graphql.module.ts/graphql.module';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ModalModule } from 'ngx-bootstrap/modal';
import { CumplimientoManualComponent } from '../app/modules/home/modals/cumplimiento-manual/cumplimiento-manual.component';
import { RevertimientoManualComponent } from '../app/modules/home/modals/revertimiento-manual/revertimiento-manual.component';
import { ToastrModule } from 'ngx-toastr';
//Componentes Material
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';


import { NgbModule, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { MatInputModule } from '@angular/material/input';
import {
  MatPaginatorModule,
  MatPaginatorIntl,
} from '@angular/material/paginator';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { PrincipalComponent } from './components/principal/principal.component';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { SystemLoginComponent } from './components/system-login/system-login.component';
import { HeaderInterceptor } from 'src/helpers/headerInterceptor.interceptor';
import { HomeModule } from './modules/home/home.module';
import { ErrorComponent } from './components/error/error.component';

// this includes the core NgIdleModule but includes keepalive providers for easy wireup
import { NgIdleKeepaliveModule } from '@ng-idle/keepalive';
import { UsuariosOmextComponent } from './components/usuarios-omext/usuarios-omext.component';
import { PerfilesComponent } from './components/perfiles/perfiles.component';
import { MenuNavModule } from './modules/menu-nav/menu-nav.module';


@NgModule({
  declarations: [
    AppComponent,
    PieGraphComponent,
    PieGraph2Component,
    PrincipalComponent,
    LoginComponent,
    SystemLoginComponent,
    ErrorComponent,
    PerfilesComponent,
    UsuariosOmextComponent
  ],
  imports: [
    MenuNavModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    NgxChartsModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatTableModule,
    MatInputModule,
    MatCheckboxModule,
    MatButtonModule,
    HomeModule,
    MatSortModule,
    NgbModule,
    MatAutocompleteModule,
    MatPaginatorModule,
    HttpClientModule,
    GraphQLModule,
    CommonModule,
    ToastrModule.forRoot(),
    ModalModule.forRoot(),
    NgIdleKeepaliveModule.forRoot()
  ],
  providers: [
    {
      provide: MatPaginatorIntl,
    },
    { provide: HTTP_INTERCEPTORS, useClass: HeaderInterceptor, multi: true },
    NgbActiveModal,
    PruebaService
  ],
  bootstrap: [AppComponent],
  entryComponents: [CumplimientoManualComponent, RevertimientoManualComponent]
})
export class AppModule { }
//Parche en actualización Angular
declare module '@angular/core' {
  interface ModuleWithProviders<T = any> {
    ngModule: Type<T>;
    providers?: Provider[];
  }
}