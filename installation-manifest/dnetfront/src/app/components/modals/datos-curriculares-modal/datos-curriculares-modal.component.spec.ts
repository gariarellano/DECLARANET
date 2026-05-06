import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatosCurricularesModalComponent } from './datos-curriculares-modal.component';

describe('DatosCurricularesModalComponent', () => {
  let component: DatosCurricularesModalComponent;
  let fixture: ComponentFixture<DatosCurricularesModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatosCurricularesModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatosCurricularesModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
