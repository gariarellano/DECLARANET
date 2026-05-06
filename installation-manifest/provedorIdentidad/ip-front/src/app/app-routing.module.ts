import { Routes, RouterModule } from '@angular/router';

import { LoginComponent } from './components/login/login.component';
import { ErrorComponent } from './components/error/error.component';
import { UserAddComponent } from './components/user/add/add.component';
import { MaintainComponent } from './components/error/maintain.component';
import { RecoveryPassComponent } from './components/recovery/recovery.component';
import { ActivateComponent } from './components/activate/activate.component';
import { UserEditComponent } from './components/user/edit/edit.component';

const appRoutes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'error', component: ErrorComponent },
    { path: 'maintain', component: MaintainComponent },
    // { path: 'registro', component: MaintainComponent },
    { path: 'user/add', component: UserAddComponent },
    { path: 'user/edit', component: UserEditComponent },
    { path: 'recovery', component: RecoveryPassComponent },
    { path: 'activate', component: ActivateComponent },
    // otherwise redirect to home
    { path: '**', redirectTo: 'login' }
];

export const routing = RouterModule.forRoot(appRoutes);
