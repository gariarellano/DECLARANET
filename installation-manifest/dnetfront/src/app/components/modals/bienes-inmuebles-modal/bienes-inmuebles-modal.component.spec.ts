import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BienesInmueblesModalComponent } from './bienes-inmuebles-modal.component';

describe('BienesInmueblesModalComponent', () => {
  let component: BienesInmueblesModalComponent;
  let fixture: ComponentFixture<BienesInmueblesModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BienesInmueblesModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BienesInmueblesModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
