import { TestBed } from '@angular/core/testing';

import { EdfApiService } from './edf-api.service';

describe('EdfApiService', () => {
  let service: EdfApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EdfApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
