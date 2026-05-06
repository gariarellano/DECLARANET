import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BeneficiosModalComponent } from './beneficios-modal.component';

describe('BeneficiosModalComponent', () => {
  let component: BeneficiosModalComponent;
  let fixture: ComponentFixture<BeneficiosModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BeneficiosModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BeneficiosModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
