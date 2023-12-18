import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { GenreService } from 'src/app/services/genre-service/genre.service';

@Component({
  selector: 'app-add-genre',
  templateUrl: './add-genre.component.html',
  styleUrls: ['./add-genre.component.scss']
})
export class AddGenreComponent implements OnInit {

  addGenreForm: FormGroup = new FormGroup({});

  constructor(private formBuilder: FormBuilder,
              private genreService: GenreService,
              private _snackBar: MatSnackBar,
              private router: Router) { }

  ngOnInit(): void {
    this.addGenreForm = this.formBuilder.group({
      'genre': new FormControl('',[Validators.required]),
    });
  }

  getGenreErrorMessage(){
    return this.addGenreForm.get('genre')?.hasError('required') ? 'You must enter a genre' : '';
  }

  openSnackBar(message: string,action: string): MatSnackBarRef<SimpleSnackBar> {
    return this._snackBar.open(message, action, {
      duration: 2000
    });
  }

  addGenre(){
    this.genreService.addGenre(this.addGenreForm.value.genre)
    .subscribe(
      (response) => {
        console.log('Genre added successfully', response);
        this.openSnackBar("Genre Added","");
        this.router.navigateByUrl("/admin/genres");
      },
      (error) => {
        console.error('Error adding genre', error);
      }
    );
  }
}
