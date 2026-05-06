import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PrestamoBienInmuebleComponent } from './prestamo-bien-inmueble.component';

describe('PrestamoBienInmuebleComponent', () => {
  let component: PrestamoBienInmuebleComponent;
  let fixture: ComponentFixture<PrestamoBienInmuebleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PrestamoBienInmuebleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PrestamoBienInmuebleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
