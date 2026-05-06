import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MenuNavComponent } from './menu/menu-nav.component';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatProgressBarModule } from "@angular/material/progress-bar";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { RouterModule, Routes } from "@angular/router";





@NgModule({
  declarations: [
  MenuNavComponent

  ],
  imports: [
    CommonModule,
    MatSlideToggleModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    RouterModule
  ],
  exports: [
    MenuNavComponent
  ]
})
export class MenuNavModule { }
