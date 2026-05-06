import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UsuarioService } from '../../services/usuario.service';
import { first } from 'rxjs/operators';

@Component({
selector: 'app-activate',
templateUrl: './activate.component.html',
styleUrls: ['./activate.component.css']
})

export class ActivateComponent implements OnInit {

bandera: boolean;
token: string;
msjError: string;
constructor(
  private usuarioService: UsuarioService,
  private route: ActivatedRoute,
  ) {
}
ngOnInit() {
  this.token = this.route.snapshot.queryParams.token;
  this.usuarioService
  .activateLink(this.token)
  .pipe(first())
  .subscribe(
    data => {
      if (data.status === 200) {
        this.bandera = true;
      }
    },
    error => {
      if (error.status === 401) {
        this.msjError = 'Token expirado, favor de iniciar sesión para reenviar correo de activación.';
        // this.alertService.error('Token expirado, vuelve a intentarlo');
      } else  {
        this.msjError = 'Ocurrió un error en la activación favor de comunicarse con el administrador.';
      }
    }
  );

}

}
