import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InversionesModalComponent } from './inversiones-modal.component';

describe('InversionesModalComponent', () => {
  let component: InversionesModalComponent;
  let fixture: ComponentFixture<InversionesModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InversionesModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InversionesModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
