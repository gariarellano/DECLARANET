import { TestBed } from '@angular/core/testing';

import { PruebaPaginationService } from './prueba-pagination.service';

describe('PruebaPaginationService', () => {
  let service: PruebaPaginationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PruebaPaginationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
