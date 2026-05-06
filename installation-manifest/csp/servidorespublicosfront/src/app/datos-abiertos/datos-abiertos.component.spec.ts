import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatosAbiertosComponent } from './datos-abiertos.component';

describe('DatosAbiertosComponent', () => {
  let component: DatosAbiertosComponent;
  let fixture: ComponentFixture<DatosAbiertosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatosAbiertosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatosAbiertosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
