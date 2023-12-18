import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { GenreService } from 'src/app/services/genre-service/genre.service';

@Component({
  selector: 'app-edit-genre',
  templateUrl: './edit-genre.component.html',
  styleUrls: ['./edit-genre.component.scss']
})
export class EditGenreComponent implements OnInit{

  updateGenreForm: FormGroup = new FormGroup({});

  genre: any;

  disabled!: boolean;

  constructor(private route: ActivatedRoute,
              private genreService: GenreService,
              private formBuilder: FormBuilder,
              private router: Router,
              private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get("id"));
    this.genreService.getGenreById(id).subscribe( data => {
      console.log(data);
      this.genre = data;
    });

    this.disabled = true;

    this.updateGenreForm = this.formBuilder.group({
      'genre': new FormControl({value: this.genre?.genre,disabled: this.disabled}), 
    });

  }

  edit(){
    this.disabled = !this.disabled;
    if(!this.disabled){
      this.updateGenreForm.controls['genre'].enable();
    }
    else{
      this.updateGenreForm.controls['genre'].disable();
    }
  }

  updateGenre(id: any){
    this.genreService.updateGenre(id,
      this.updateGenreForm.value.genre = this.updateGenreForm.value.genre || this.genre.genre)
    .subscribe(data => {
      this.openSnackBar("Genre Edited","");
      this.edit();
      console.log(data);
    }, (error: any) => {
      console.log(error);
    });
  }

  openSnackBar(message: string,action: string): MatSnackBarRef<SimpleSnackBar> {
    return this._snackBar.open(message, action, {
      duration: 2000
    });
  }
}
