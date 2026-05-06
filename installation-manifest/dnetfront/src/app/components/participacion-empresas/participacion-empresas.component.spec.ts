import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ParticipacionEmpresasComponent } from './participacion-empresas.component';

describe('ParticipacionEmpresasComponent', () => {
  let component: ParticipacionEmpresasComponent;
  let fixture: ComponentFixture<ParticipacionEmpresasComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ParticipacionEmpresasComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ParticipacionEmpresasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
