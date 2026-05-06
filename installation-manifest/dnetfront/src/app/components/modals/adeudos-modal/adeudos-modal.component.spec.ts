import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdeudosModalComponent } from './adeudos-modal.component';

describe('AdeudosModalComponent', () => {
  let component: AdeudosModalComponent;
  let fixture: ComponentFixture<AdeudosModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdeudosModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdeudosModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
