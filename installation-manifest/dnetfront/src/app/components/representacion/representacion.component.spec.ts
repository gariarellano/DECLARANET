import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RepresentacionComponent } from './representacion.component';

describe('RepresentacionComponent', () => {
  let component: RepresentacionComponent;
  let fixture: ComponentFixture<RepresentacionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RepresentacionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RepresentacionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
