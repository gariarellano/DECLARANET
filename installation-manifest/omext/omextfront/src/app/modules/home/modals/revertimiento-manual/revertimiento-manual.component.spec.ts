import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RevertimientoManualComponent } from './revertimiento-manual.component';

describe('RevertimientoManualComponent', () => {
  let component: RevertimientoManualComponent;
  let fixture: ComponentFixture<RevertimientoManualComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RevertimientoManualComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RevertimientoManualComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
