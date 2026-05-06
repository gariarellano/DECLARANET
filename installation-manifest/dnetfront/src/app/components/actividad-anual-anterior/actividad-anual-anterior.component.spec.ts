import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ActividadAnualAnteriorComponent } from './actividad-anual-anterior.component';

describe('ActividadAnualAnteriorComponent', () => {
  let component: ActividadAnualAnteriorComponent;
  let fixture: ComponentFixture<ActividadAnualAnteriorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ActividadAnualAnteriorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActividadAnualAnteriorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
