import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PrestamoBienVehiculoComponent } from './prestamo-bien-vehiculo.component';

describe('PrestamoBienVehiculoComponent', () => {
  let component: PrestamoBienVehiculoComponent;
  let fixture: ComponentFixture<PrestamoBienVehiculoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PrestamoBienVehiculoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PrestamoBienVehiculoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
