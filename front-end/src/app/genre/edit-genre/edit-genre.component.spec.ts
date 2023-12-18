import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditGenreComponent } from './edit-genre.component';

describe('EditGenreComponent', () => {
  let component: EditGenreComponent;
  let fixture: ComponentFixture<EditGenreComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditGenreComponent]
    });
    fixture = TestBed.createComponent(EditGenreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
