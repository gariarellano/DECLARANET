import { TestBed } from '@angular/core/testing';

import { AuthAnonymousService } from './auth-anonymous.service';

describe('AuthAnonymousService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AuthAnonymousService = TestBed.get(AuthAnonymousService);
    expect(service).toBeTruthy();
  });
});
