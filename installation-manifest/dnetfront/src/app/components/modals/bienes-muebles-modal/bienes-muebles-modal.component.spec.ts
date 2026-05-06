import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BienesMueblesModalComponent } from './bienes-muebles-modal.component';

describe('BienesMueblesModalComponent', () => {
  let component: BienesMueblesModalComponent;
  let fixture: ComponentFixture<BienesMueblesModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BienesMueblesModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BienesMueblesModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
