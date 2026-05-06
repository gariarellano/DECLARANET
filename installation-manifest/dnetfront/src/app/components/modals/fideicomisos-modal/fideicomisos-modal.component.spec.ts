import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FideicomisosModalComponent } from './fideicomisos-modal.component';

describe('FideicomisosModalComponent', () => {
  let component: FideicomisosModalComponent;
  let fixture: ComponentFixture<FideicomisosModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FideicomisosModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FideicomisosModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
