import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PrestamoInmuebleModalComponent } from './prestamo-inmueble-modal.component';

describe('PrestamoInmuebleModalComponent', () => {
  let component: PrestamoInmuebleModalComponent;
  let fixture: ComponentFixture<PrestamoInmuebleModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PrestamoInmuebleModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PrestamoInmuebleModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
