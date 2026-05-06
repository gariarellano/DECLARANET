import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatosEmpleoModalComponent } from './datos-empleo-modal.component';

describe('DatosEmpleoModalComponent', () => {
  let component: DatosEmpleoModalComponent;
  let fixture: ComponentFixture<DatosEmpleoModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatosEmpleoModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatosEmpleoModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
