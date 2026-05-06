import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NotaAclaraComponent } from './nota-aclara.component';

describe('NotaAclaraComponent', () => {
  let component: NotaAclaraComponent;
  let fixture: ComponentFixture<NotaAclaraComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NotaAclaraComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotaAclaraComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
