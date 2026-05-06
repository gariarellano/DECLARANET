import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FormatoSipotComponent } from './formato-sipot.component';

describe('FormatoSipotComponent', () => {
  let component: FormatoSipotComponent;
  let fixture: ComponentFixture<FormatoSipotComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FormatoSipotComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FormatoSipotComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
