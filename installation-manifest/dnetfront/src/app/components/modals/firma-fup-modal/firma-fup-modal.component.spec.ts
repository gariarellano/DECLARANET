import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FirmaFUPModalComponent } from './firma-fup-modal.component';

describe('FirmaFUPModalComponent', () => {
  let component: FirmaFUPModalComponent;
  let fixture: ComponentFixture<FirmaFUPModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FirmaFUPModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FirmaFUPModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
