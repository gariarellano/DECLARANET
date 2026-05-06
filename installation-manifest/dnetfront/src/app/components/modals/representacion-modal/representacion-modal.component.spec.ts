import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RepresentacionModalComponent } from './representacion-modal.component';

describe('RepresentacionModalComponent', () => {
  let component: RepresentacionModalComponent;
  let fixture: ComponentFixture<RepresentacionModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RepresentacionModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RepresentacionModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
