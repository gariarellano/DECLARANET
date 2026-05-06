import { TestBed } from '@angular/core/testing';

import { AuthProfileService } from './auth-profile.service';

describe('AuthProfileService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AuthProfileService = TestBed.get(AuthProfileService);
    expect(service).toBeTruthy();
  });
});
