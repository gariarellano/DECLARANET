import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ParticipacionEmpresasModalComponent } from './participacion-empresas-modal.component';

describe('ParticipacionEmpresasModalComponent', () => {
  let component: ParticipacionEmpresasModalComponent;
  let fixture: ComponentFixture<ParticipacionEmpresasModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ParticipacionEmpresasModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ParticipacionEmpresasModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
