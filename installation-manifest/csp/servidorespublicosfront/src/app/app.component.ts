import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'servidorespublicos';
  enteContext: string;
  constructor( private readonly router: Router,
               private readonly activatedRoute: ActivatedRoute
    ){
      this.enteContext = '';
  }

  navBusqueda() {
    const navOut = sessionStorage.getItem('enteReceptor') ? JSON.parse(sessionStorage.getItem('enteReceptor')).enteContext || '' : '';
    this.router.navigate([`/${navOut}`]);
  }
  goToDeclaranet() {
    if (this.enteContext === null) {
      window.location.href = 'https://declaranet.gob.mx';
    }
  }

  sfpEnteContext(val: string) {
    setTimeout(() => {
      console.log('enteContext', val);
      this.enteContext = val;
    }, 100);
  }
}
