import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatosDependienteModalComponent } from './datos-dependiente-modal.component';

describe('DatosDependienteModalComponent', () => {
  let component: DatosDependienteModalComponent;
  let fixture: ComponentFixture<DatosDependienteModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatosDependienteModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatosDependienteModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
