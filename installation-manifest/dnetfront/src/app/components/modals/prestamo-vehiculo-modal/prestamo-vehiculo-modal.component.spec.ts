import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PrestamoVehiculoModalComponent } from './prestamo-vehiculo-modal.component';

describe('PrestamoVehiculoModalComponent', () => {
  let component: PrestamoVehiculoModalComponent;
  let fixture: ComponentFixture<PrestamoVehiculoModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PrestamoVehiculoModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PrestamoVehiculoModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
