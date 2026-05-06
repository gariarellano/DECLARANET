import { BrowserModule } from '@angular/platform-browser';
import { NgModule, Injectable } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
// tslint:disable-next-line:max-line-length
import { NgIdleKeepaliveModule } from '@ng-idle/keepalive'; // this includes the core NgIdleModule but includes keepalive providers for easy wireup
import { MomentModule } from 'angular2-moment'; // optional, provides moment-style pipes for date formatting
import { ModalModule } from 'ngx-bootstrap/modal';
import { AppComponent } from './app.component';
import { PrincipalComponent } from './components/principal/principal.component';
import { DatosGeneralesComponent } from './components/datos-generales/datos-generales.component';
import { DomicilioComponent } from './components/domicilio/domicilio.component';
import { DatosCurricularesComponent } from './components/datos-curriculares/datos-curriculares.component';
import { DatosEmpleoComponent } from './components/datos-empleo/datos-empleo.component';
import { ExperienciaLaboralComponent } from './components/experiencia-laboral/experiencia-laboral.component';
import { DatosParejaComponent } from './components/datos-pareja/datos-pareja.component';
import { DatosDependienteComponent } from './components/datos-dependiente/datos-dependiente.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IngresosNetosComponent } from './components/ingresos-netos/ingresos-netos.component';
import { BienesInmueblesComponent } from './components/bienes-inmuebles/bienes-inmuebles.component';
import { VehiculosComponent } from './components/vehiculos/vehiculos.component';
import { BienesMueblesComponent } from './components/bienes-muebles/bienes-muebles.component';
import { InversionesComponent } from './components/inversiones/inversiones.component';
import { AdeudosComponent } from './components/adeudos/adeudos.component';
import { PrestamoBienInmuebleComponent } from './components/prestamo-bien-inmueble/prestamo-bien-inmueble.component';
import { PrestamoBienVehiculoComponent } from './components/prestamo-bien-vehiculo/prestamo-bien-vehiculo.component';
import { ParticipacionEmpresasComponent } from './components/participacion-empresas/participacion-empresas.component';
import { ActividadLaboralParejaComponent } from './components/actividad-laboral-pareja/actividad-laboral-pareja.component';
import { ParticipaTomaDecisionesComponent } from './components/participa-toma-decisiones/participa-toma-decisiones.component';
import { ApoyosBeneficiosComponent } from './components/apoyos-beneficios/apoyos-beneficios.component';
import { RepresentacionComponent } from './components/representacion/representacion.component';
import { ClientesPrincipalesComponent } from './components/clientes-principales/clientes-principales.component';
import { BeneficiosPrivadosComponent } from './components/beneficios-privados/beneficios-privados.component';
import { FideicomisosComponent } from './components/fideicomisos/fideicomisos.component';
import { NgbModule, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { DatosCurricularesModalComponent } from './components/modals/datos-curriculares-modal/datos-curriculares-modal.component';
import { DatosEmpleoModalComponent } from './components/modals/datos-empleo-modal/datos-empleo-modal.component';
import { ExperienciaLaboralModalComponent } from './components/modals/experiencia-laboral-modal/experiencia-laboral-modal.component';
import { DatosDependienteModalComponent } from './components/modals/datos-dependiente-modal/datos-dependiente-modal.component';
import { BienesInmueblesModalComponent } from './components/modals/bienes-inmuebles-modal/bienes-inmuebles-modal.component';
import { VehiculosModalComponent } from './components/modals/vehiculos-modal/vehiculos-modal.component';
import { BienesMueblesModalComponent } from './components/modals/bienes-muebles-modal/bienes-muebles-modal.component';
import { InversionesModalComponent } from './components/modals/inversiones-modal/inversiones-modal.component';
import { AdeudosModalComponent } from './components/modals/adeudos-modal/adeudos-modal.component';
import { PrestamoInmuebleModalComponent } from './components/modals/prestamo-inmueble-modal/prestamo-inmueble-modal.component';
import { PrestamoVehiculoModalComponent } from './components/modals/prestamo-vehiculo-modal/prestamo-vehiculo-modal.component';
import { ActividadAnualAnteriorComponent } from './components/actividad-anual-anterior/actividad-anual-anterior.component';
import { NotaAclaraComponent } from './components/nota-aclara/nota-aclara.component';
// tslint:disable-next-line: max-line-length
import { ParticipacionEmpresasModalComponent } from './components/modals/participacion-empresas-modal/participacion-empresas-modal.component';
import { ParticipaDesicionesModalComponent } from './components/modals/participa-desiciones-modal/participa-desiciones-modal.component';
import { ApoyosModalComponent } from './components/modals/apoyos-modal/apoyos-modal.component';
import { RepresentacionModalComponent } from './components/modals/representacion-modal/representacion-modal.component';
import { ClientesModalComponent } from './components/modals/clientes-modal/clientes-modal.component';
import { BeneficiosModalComponent } from './components/modals/beneficios-modal/beneficios-modal.component';
import { FideicomisosModalComponent } from './components/modals/fideicomisos-modal/fideicomisos-modal.component';
import { StartComponent } from './components/start/start.component';
import { LoginComponent } from './components/login/login.component';
import { SystemLogInComponent } from './components/system-log-in/system-log-in.component';
import { ErrorComponent } from './components/error/error.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatInputModule } from '@angular/material/input';
import { GraphQLModule } from './graphql.module';
import { Globals } from './globals';
import { HeaderInterceptor } from 'src/helpers/headerInterceptor.interceptor';
import { validadorRFC } from '../app/validators/validadorRFC';
import { validadorRFCMoral } from '../app/validators/validadorRFCMoral';
import { validadorCadena } from '../app/validators/caracteresValidaNgModel';
import { validadorRFCAmbos } from '../app/validators/validadorRFCAmbos';
import { validadorCurp } from '../app/validators/validadorCurp';
import { SelectRequiredValidatorDirective } from '../app/validators/validadorSelects';
import { validadorFecha } from '../app/validators/fechasValidator';
import { FirmaModalComponent } from './components/modals/firma-modal/firma-modal.component';
import { FirmaFUPModalComponent } from '../app/components/modals/firma-fup-modal/firma-fup-modal.component';
import { PreviewComponent } from './components/modals/preview/preview.component';
import { SignComponent } from './components/sign/sign.component';
import { AlertComponent } from './components/alert/alert.component';
import { ToastrModule } from 'ngx-toastr';
import { AvisoComponent } from './components/avisos/avisos.component';
import { AvisoCambioDependenciaComponent } from './components/aviso-cambio-dependencia/aviso-cambio-dependencia.component';
import { GetPipeNivAcaPipe } from '../pipes/pipe-niv-aca.pipe';
import { GetPipeBieMue } from '../pipes/pipeBieMue';
import { GetPipeVeh } from '../pipes/pipeVeh';
import { GetPipeAde } from '../pipes/pipeAde';
import { GetPipeAde2 } from '../pipes/pipeAde2';
import { PipeSubTipoInversionPipe } from '../pipes/pipe-sub-tipo-inversion.pipe';
import { PipeSubTipoInversionPipe2 } from '../pipes/pipe-subtipo-inversion.pipe';

import { GetPipeNonmbreDependientePipe } from '../pipes/pipe-nombre-dependient.pipe';
import { PipeTipoInversionPipe } from '../pipes/pipe-tipo-inversion.pipe';


import * as Hammer from 'hammerjs';
import { HammerGestureConfig, HAMMER_GESTURE_CONFIG } from '@angular/platform-browser';
import { HistorialNotasComponent } from './components/modals/historial-notas/historial-notas.component';
import { GetPipeVehD } from 'src/pipes/pipeVehi2.pipe';
import { MaintenanceComponent } from './components/maintenance/maintenance.component';
import { NgxExtendedPdfViewerModule } from 'ngx-extended-pdf-viewer';
import { NgxUiLoaderModule } from 'ngx-ui-loader';



@Injectable()
export class MyHammerConfig extends HammerGestureConfig {
  overrides = {
    // override hammerjs default configuration
    'swipe': {
      velocity: 0.4,
      threshold: 5,
      direction: Hammer.DIRECTION_ALL,
      pointers: 1
    }
  } as any;
}

@NgModule({
  declarations: [
    AppComponent,
    DatosGeneralesComponent,
    DomicilioComponent,
    DatosCurricularesComponent,
    DatosEmpleoComponent,
    ExperienciaLaboralComponent,
    DatosParejaComponent,
    DatosDependienteComponent,
    IngresosNetosComponent,
    BienesInmueblesComponent,
    VehiculosComponent,
    BienesMueblesComponent,
    InversionesComponent,
    AdeudosComponent,
    PrestamoBienInmuebleComponent,
    PrestamoBienVehiculoComponent,
    ParticipacionEmpresasComponent,
    ActividadLaboralParejaComponent,
    ParticipaTomaDecisionesComponent,
    ApoyosBeneficiosComponent,
    RepresentacionComponent,
    ClientesPrincipalesComponent,
    BeneficiosPrivadosComponent,
    FideicomisosComponent,
    DatosCurricularesModalComponent,
    DatosEmpleoModalComponent,
    ExperienciaLaboralModalComponent,
    DatosDependienteModalComponent,
    BienesInmueblesModalComponent,
    VehiculosModalComponent,
    BienesMueblesModalComponent,
    InversionesModalComponent,
    AdeudosModalComponent,
    PrestamoInmuebleModalComponent,
    PrestamoVehiculoModalComponent,
    ParticipacionEmpresasModalComponent,
    ParticipaDesicionesModalComponent,
    ApoyosModalComponent,
    RepresentacionModalComponent,
    ClientesModalComponent,
    BeneficiosModalComponent,
    FideicomisosModalComponent,
    StartComponent,
    LoginComponent,
    SystemLogInComponent,
    ErrorComponent,
    PrincipalComponent,
    AvisoComponent,
    AvisoCambioDependenciaComponent,
    validadorRFC,
    validadorCurp,
    validadorRFCMoral,
    validadorCadena,
    validadorRFCAmbos,
    validadorFecha,
    SelectRequiredValidatorDirective,
    FirmaModalComponent,
    FirmaFUPModalComponent,
    PreviewComponent,
    SignComponent,
    AlertComponent,
    ActividadAnualAnteriorComponent,
    NotaAclaraComponent,
    GetPipeNivAcaPipe,
    GetPipeBieMue,
    GetPipeVeh,
    GetPipeVehD,
    GetPipeAde,
    GetPipeAde2,
    PipeSubTipoInversionPipe,
    PipeSubTipoInversionPipe2,
    PipeTipoInversionPipe,
    GetPipeNonmbreDependientePipe,
    HistorialNotasComponent,
    MaintenanceComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    NgbModule,
    BrowserAnimationsModule,
    MatInputModule,
    GraphQLModule,
    NgIdleKeepaliveModule.forRoot(),
    MomentModule,
    NgxExtendedPdfViewerModule,
    NgxUiLoaderModule,
    ModalModule.forRoot(),
    ToastrModule.forRoot()
  ],
  providers: [
    { provide: HAMMER_GESTURE_CONFIG, useClass: MyHammerConfig },
    { provide: HTTP_INTERCEPTORS, useClass: HeaderInterceptor, multi: true },
    NgbActiveModal,
    Globals
  ],
  bootstrap: [AppComponent],
  entryComponents: [
    DatosCurricularesModalComponent,
    DatosEmpleoModalComponent,
    ExperienciaLaboralModalComponent,
    DatosDependienteModalComponent,
    BienesInmueblesModalComponent,
    VehiculosModalComponent,
    BienesMueblesModalComponent,
    InversionesModalComponent,
    AdeudosModalComponent,
    PrestamoInmuebleModalComponent,
    PrestamoVehiculoModalComponent,
    ParticipacionEmpresasModalComponent,
    ParticipaDesicionesModalComponent,
    ApoyosModalComponent,
    RepresentacionModalComponent,
    ClientesModalComponent,
    BeneficiosModalComponent,
    FideicomisosModalComponent,
    FirmaModalComponent,
    FirmaFUPModalComponent,
    PreviewComponent,
    SignComponent,
    HistorialNotasComponent
  ]
})
export class AppModule { }
