import { Component, OnInit } from '@angular/core';
import { multi, dataDona } from '.././data';
import { Router } from '@angular/router';
import * as $ from 'jquery';

@Component({
  selector: 'app-vista-graph',
  templateUrl: './vista-graph.component.html',
  styleUrls: ['./vista-graph.component.scss']
})
export class VistaGraphComponent implements OnInit {

  dataLinear: any;
  //Gráfica linear
  multi: any[];
  view: any[] = [1000, 300];

  // options
  legend: boolean = true;
  showLabels: boolean = true;
  animations: boolean = true;
  xAxis: boolean = true;
  yAxis: boolean = true;
  showYAxisLabel: boolean = true;
  showXAxisLabel: boolean = true;
  xAxisLabel: string = 'Año';
  yAxisLabel: string = 'Declaraciones';
  timeline: boolean = true;
  dataDona: any[];

  colorScheme = {
    domain: ['#5AA454', '#E44D25', '#CFC0BB', '#7aa3e5', '#a8385d', '#aae3f5']
  };



  //grafica donas
  data2: any[];
  view2: any[] = [500, 400];

  // options
  gradient: boolean = true;
  showLegend: boolean = true;
  showLabels2: boolean = true;
  isDoughnut: boolean = false;
  colorScheme2 = {
    domain: ["#5AA454", "#A10A28", "#C7B42C", "#AAAAAA"],
  };

  //grafica barras 
  view3: any[] = [700, 400];

  // options
  showXAxis = true;
  showYAxis = true;
  gradient3 = false;
  showLegend3 = true;
  showXAxisLabel3 = true;
  xAxisLabel3 = 'Country';
  showYAxisLabel3 = true;
  yAxisLabel3 = 'Population';

  colorScheme3 = {
    domain: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA']
  };
  constructor(private router: Router) {
    Object.assign(this, { multi });
    Object.assign(this, { dataDona });
  }


  ngOnInit(): void {
    $(".spinner").css("display", "none");
  }

  openGraph() {
    this.router.navigate(['/inicio/graph']);
  }

  //........FUNCIONES GRAFICA LINEAR.........
  onSelect(dataLinear): void {
    this.dataLinear = JSON.parse(JSON.stringify(dataLinear));
    console.log(' clicked', dataLinear);
    $("#btnGraphLineal").click();
  }

  onActivate(data): void {
    //console.log('Activate', JSON.parse(JSON.stringify(data)));
  }

  onDeactivate(data): void {
    // console.log('Deactivate', JSON.parse(JSON.stringify(data)));
  }

  //.............FUNCIONES GRAFICA DONA.................
  onSelect2(data): void {
    console.log("Item clicked", JSON.parse(JSON.stringify(data)));
  }

  onActivate2(data): void {
    console.log("Activate", JSON.parse(JSON.stringify(data)));
  }

  onDeactivate2(data): void {
    console.log("Deactivate", JSON.parse(JSON.stringify(data)));
  }

  //.............FUNCIONES GRAFICA BARRAS .............
  onSelect3(event) {
    console.log(event);
  }
}
