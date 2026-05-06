import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ParticipaDesicionesModalComponent } from './participa-desiciones-modal.component';

describe('ParticipaDesicionesModalComponent', () => {
  let component: ParticipaDesicionesModalComponent;
  let fixture: ComponentFixture<ParticipaDesicionesModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ParticipaDesicionesModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ParticipaDesicionesModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
