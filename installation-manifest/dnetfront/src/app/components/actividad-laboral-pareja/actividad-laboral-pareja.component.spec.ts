import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ActividadLaboralParejaComponent } from './actividad-laboral-pareja.component';

describe('ActividadLaboralParejaComponent', () => {
  let component: ActividadLaboralParejaComponent;
  let fixture: ComponentFixture<ActividadLaboralParejaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ActividadLaboralParejaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActividadLaboralParejaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
