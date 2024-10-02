import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrestamoListComponent } from './prestamo-list.component';

describe('PrestamoListComponent', () => {
  let component: PrestamoListComponent;
  let fixture: ComponentFixture<PrestamoListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrestamoListComponent]
    });
    fixture = TestBed.createComponent(PrestamoListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
