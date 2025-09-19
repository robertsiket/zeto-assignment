import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EdfListComponent } from './edf-list.component';

describe('EdfListComponent', () => {
  let component: EdfListComponent;
  let fixture: ComponentFixture<EdfListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EdfListComponent]
    });
    fixture = TestBed.createComponent(EdfListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
