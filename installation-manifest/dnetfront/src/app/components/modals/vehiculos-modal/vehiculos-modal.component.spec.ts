import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VehiculosModalComponent } from './vehiculos-modal.component';

describe('VehiculosModalComponent', () => {
  let component: VehiculosModalComponent;
  let fixture: ComponentFixture<VehiculosModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VehiculosModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VehiculosModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
