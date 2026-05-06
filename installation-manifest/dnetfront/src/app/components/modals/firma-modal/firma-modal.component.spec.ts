import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FirmaModalComponent } from './firma-modal.component';

describe('FirmaModalComponent', () => {
  let component: FirmaModalComponent;
  let fixture: ComponentFixture<FirmaModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FirmaModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FirmaModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
