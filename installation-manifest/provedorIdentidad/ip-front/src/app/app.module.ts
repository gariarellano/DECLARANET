import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { SimpleGlobal } from 'ng2-simple-global';
import { QrCodeModule } from 'ng-qrcode';

// used to create fake backend
// import { fakeBackendProvider } from './_helpers';

import { AppComponent } from './app.component';
import { routing } from './app-routing.module';

import { AlertComponent } from './components/alert/alert.component';
import { LoginComponent } from './components/login/login.component';
import { ErrorComponent } from './components/error/error.component';
import { HeaderInterceptor } from '../helpers/HeaderInterceptor.interceptor';
import { UserAddComponent } from './components/user/add/add.component';
import { MaintainComponent } from './components/error/maintain.component';
// import { JuriNameValidator } from './validators/juriName.validator';
import { RfcValidator } from './validators/rfc.validator';
import { CurpValidator } from './validators/curp.validator';
import { PasswordValidator } from './validators/pass.validator';
import { EqualValidator } from './validators/match.validator';
import { RecoveryPassComponent } from './components/recovery/recovery.component';
import { ActivateComponent } from './components/activate/activate.component';
import { UserEditComponent } from './components/user/edit/edit.component';
import {
    RECAPTCHA_SETTINGS,
    RecaptchaLoaderService,
    RecaptchaFormsModule,
    RecaptchaModule,
    RecaptchaSettings,
} from 'ng-recaptcha';
import { environment } from 'src/environments/environment';

const globalSettings: RecaptchaSettings = { siteKey: `${environment.SECRET_RECAPTCHA}` };



@NgModule({
    imports: [
        RecaptchaModule,
        RecaptchaFormsModule,
        BrowserModule,
        ReactiveFormsModule,
        HttpClientModule,
        routing,
        FormsModule,
        QrCodeModule
    ],
    declarations: [
        AppComponent,
        AlertComponent,
        LoginComponent,
        ErrorComponent,
        UserAddComponent,
        MaintainComponent,
        RfcValidator,
        CurpValidator,
        PasswordValidator,
        EqualValidator,
        RecoveryPassComponent,
        ActivateComponent,
        UserEditComponent
    ],
    providers: [{ provide: HTTP_INTERCEPTORS, useClass: HeaderInterceptor, multi: true }, SimpleGlobal,
    {
        provide: RECAPTCHA_SETTINGS,
        useValue: globalSettings,
    }
    ],
    bootstrap: [AppComponent]
})

export class AppModule { }
