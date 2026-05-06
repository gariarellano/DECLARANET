import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CumplimientoManualComponent } from './cumplimiento-manual.component';

describe('CumplimientoManualComponent', () => {
  let component: CumplimientoManualComponent;
  let fixture: ComponentFixture<CumplimientoManualComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CumplimientoManualComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CumplimientoManualComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
