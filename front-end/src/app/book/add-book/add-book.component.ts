import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { BookService } from 'src/app/services/book-service/book.service';
import { GenreService } from 'src/app/services/genre-service/genre.service';
import { TokenService } from 'src/app/services/token-service/token.service';

@Component({
  selector: 'app-add-book',
  templateUrl: './add-book.component.html',
  styleUrls: ['./add-book.component.scss']
})
export class AddBookComponent implements OnInit {

  addImageForm: FormGroup = new FormGroup({});

  genres: any = [];

  constructor(private formBuilder: FormBuilder,
              private bookService: BookService,
              private tokenService: TokenService,
              private genreService: GenreService,
              private _snackBar: MatSnackBar,
              private router: Router) { }

  ngOnInit(): void {
    this.getGenres();

    this.addImageForm = this.formBuilder.group({
      'title': new FormControl('',[Validators.required]),
      'author': new FormControl('',[Validators.required]),
      'genre': new FormControl('',[Validators.required]),
      'description': new FormControl('',[Validators.required]),
      'price': new FormControl('',[Validators.required]),
      'imageURL': new FormControl('',[Validators.required])
    });

  }

  getGenres(){
    this.genreService.getGenreList().subscribe( data => {
      this.genres = data;
    });
  }

  getTitleErrorMessage(){
    return this.addImageForm.get('title')?.hasError('required') ? 'You must enter a title' : '';
  }

  getImageErrorMessage(){
    return this.addImageForm.get('imageURL')?.hasError('required') ? 'You must enter a image URL' : '';
  }

  getAuthorErrorMessage(){
    return this.addImageForm.get('author')?.hasError('required') ? 'You must enter an author' : '';
  }

  getGenreErrorMessage(){
    return this.addImageForm.get('genre')?.hasError('required') ? 'You must choose a genre' : '';
  }

  getDescriptionErrorMessage(){
    return this.addImageForm.get('description')?.hasError('required') ? 'You must enter a summary' : '';
  }

  getPriceErrorMessage(){
    return this.addImageForm.get('price')?.hasError('required') ? 'You must enter a price' : '';
  }

  openSnackBar(message: string,action: string): MatSnackBarRef<SimpleSnackBar> {
    return this._snackBar.open(message, action, {
      duration: 2000
    });
  }

  addBook(){
    this.bookService.addBook(this.tokenService.getToken(), 
      this.addImageForm.value.title, 
      this.addImageForm.value.author.toString().split(' ')[0],
      this.addImageForm.value.author.toString().split(' ')[1],
      this.addImageForm.value.genre, 
      this.addImageForm.value.description, 
      this.addImageForm.value.price,
      this.addImageForm.value.imageURL)
    .subscribe(
      (response) => {
        console.log('Book added successfully', response);
        this.openSnackBar("Book Added","");
        this.router.navigateByUrl("/admin/books");
      },
      (error) => {
        console.error('Error adding book', error);
      }
    );
  }

}
