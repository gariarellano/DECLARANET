import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ModalModule } from 'ngx-bootstrap/modal';


import { CurpValidator } from './validators/curp.validator';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MaterialModule } from './material/material.module';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HeaderInterceptor } from 'src/helpers/headerInterceptor.interceptor';
import { LoginComponent } from './components/login/login.component';
import { GraphQLModule } from './graphql.module';
import { CurpNombreRfcValidator } from './validators/curpNombreRfc.validator';
import { CurpNombreRfcIncompletoValidator } from './validators/curpNombreRfcIncompleto.validator';
import { ConsultaComponent } from './components/consulta/consulta.component';
import { NgxExtendedPdfViewerModule } from 'ngx-extended-pdf-viewer';
import { PrevisualizarComponent } from './components/previsualizar/previsualizar.component';
import { HomeComponent } from './components/home/home.component';
import { SystemLoginComponent } from './components/system-login/system-login.component';

import { NgxUiLoaderModule } from 'ngx-ui-loader';


// this includes the core NgIdleModule but includes keepalive providers for easy wireup
import { NgIdleKeepaliveModule } from '@ng-idle/keepalive';


import { PerfilesComponent } from './components/perfiles/perfiles.component';
import { UsuariosIdentidadComponent } from './components/usuarios-identidad/usuarios-identidad.component';
import { UsuariosOperComponent } from './components/usuarios-oper/usuarios-oper.component';


@NgModule({
  declarations: [
    AppComponent,
    UsuariosIdentidadComponent,
    UsuariosOperComponent,
    CurpValidator,
    CurpNombreRfcValidator,
    CurpNombreRfcIncompletoValidator,
    LoginComponent,
    ConsultaComponent,
    PrevisualizarComponent,
    SystemLoginComponent,
    HomeComponent,
    PerfilesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MaterialModule,
    BrowserAnimationsModule,
    NgbModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
    NgxUiLoaderModule,
    ToastrModule.forRoot(),
    ModalModule.forRoot(),
    NgIdleKeepaliveModule.forRoot(),
    NgxExtendedPdfViewerModule,
    GraphQLModule
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: HeaderInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
