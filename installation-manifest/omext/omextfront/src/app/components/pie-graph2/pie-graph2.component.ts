import { Component, OnInit } from "@angular/core";
import { data } from "./../../data";

@Component({
  selector: "app-pie-graph2",
  templateUrl: "./pie-graph2.component.html",
  styleUrls: ["./pie-graph2.component.scss"],
})
export class PieGraph2Component implements OnInit {
  data: any[];
  view: any[] = [500, 400];

  // options
  gradient: boolean = true;
  showLegend: boolean = true;
  showLabels: boolean = true;
  isDoughnut: boolean = false;
  colorScheme = {
    domain: ["#5AA454", "#A10A28", "#C7B42C", "#AAAAAA"],
  };

  constructor() {
    Object.assign(this, { data });
  }

  ngOnInit(): void {}
  onSelect(data): void {
    console.log("Item clicked", JSON.parse(JSON.stringify(data)));
  }

  onActivate(data): void {
    console.log("Activate", JSON.parse(JSON.stringify(data)));
  }

  onDeactivate(data): void {
    console.log("Deactivate", JSON.parse(JSON.stringify(data)));
  }
}
