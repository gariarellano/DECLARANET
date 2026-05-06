import { Component, OnInit } from '@angular/core';
import * as $ from 'jquery';
import { Globals } from 'src/app/globals';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  pages: any;
  collName: number;
  constructor(
    private readonly globals: Globals
  ) { }

  ngOnInit(): void {
    console.log(this.globals);
    this.pages = [];
    this.globals.profile.permisos.forEach((permiso) => this.pages.push(permiso.pagina) );
    const ins = JSON.parse(sessionStorage.getItem('institucionReceptora'));
    this.collName = ins.collName;
    // let cumplimiento = this.globals
  }

}
