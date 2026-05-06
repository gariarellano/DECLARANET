import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BusquedaComponent } from './busqueda/busqueda.component';
import { GabineteComponent } from './gabinete/gabinete.component';
import { DatosAbiertosComponent } from './datos-abiertos/datos-abiertos.component';
import { FormatoSipotComponent } from './formato-sipot/formato-sipot.component';
import { appRoutingProviders, routing } from './routing/routing';
import { DeclaracionesComponent } from './declaraciones/declaraciones.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { PrevisualizarComponent } from './previsualizar/previsualizar.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxUiLoaderModule } from 'ngx-ui-loader';
import { NgxExtendedPdfViewerModule } from 'ngx-extended-pdf-viewer';
import { ToastrModule } from 'ngx-toastr';
import { ReporteInaiComponent } from './reporte-inai/reporte-inai.component';
import { MaintenanceComponent } from './maintenance/maintenance.component';



@NgModule({
  declarations: [
    AppComponent,
    BusquedaComponent,
    GabineteComponent,
    DatosAbiertosComponent,
    FormatoSipotComponent,
    DeclaracionesComponent,
    PrevisualizarComponent,
    ReporteInaiComponent,
    MaintenanceComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    routing,
    HttpClientModule,
    FontAwesomeModule,
    NgbModule,
    NgxUiLoaderModule,
    NgxExtendedPdfViewerModule,
    ToastrModule.forRoot()
  ],
  providers: [
    appRoutingProviders
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
