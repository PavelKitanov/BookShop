import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { BookService } from 'src/app/services/book-service/book.service';
import { GenreService } from 'src/app/services/genre-service/genre.service';
import { TokenService } from 'src/app/services/token-service/token.service';

@Component({
  selector: 'app-edit-book',
  templateUrl: './edit-book.component.html',
  styleUrls: ['./edit-book.component.scss']
})
export class EditBookComponent implements OnInit{

  updateImageForm: FormGroup = new FormGroup({});

  book: any;
  author: any;
  genres: any = [];

  disabled!: boolean;

  constructor(private route: ActivatedRoute,
              private bookService: BookService,
              private tokenService: TokenService,
              private formBuilder: FormBuilder,
              private genreService: GenreService,
              private router: Router,
              private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.getGenres();

    const id = Number(this.route.snapshot.paramMap.get("id"));
    this.bookService.getBookById(id).subscribe( data => {
      console.log(data);
      this.book = data;
      this.author = data.author.firstName + " " + data.author.lastName;
    });

    this.disabled = true;

    this.updateImageForm = this.formBuilder.group({
      'title': new FormControl({value: this.book?.title,disabled: this.disabled}), 
      'author': new FormControl({value: this.author,disabled: this.disabled}),
      'genre': new FormControl({value: this.book?.genre?.genreId,disabled: this.disabled}),
      'description': new FormControl({value: this.book?.description,disabled: this.disabled}),
      'price': new FormControl({value: this.book?.price,disabled: this.disabled}),
      'imageURL': new FormControl({value: this.book?.imageURL, disabled: this.disabled})
    });

  }

  getGenres(){
    this.genreService.getGenreList().subscribe( data => {
      this.genres = data;
    });
  }

  edit(){
    this.disabled = !this.disabled;
    if(!this.disabled){
      this.updateImageForm.controls['title'].enable();
      this.updateImageForm.controls['author'].enable();
      this.updateImageForm.controls['genre'].enable();
      this.updateImageForm.controls['description'].enable();
      this.updateImageForm.controls['price'].enable();
      this.updateImageForm.controls['imageURL'].enable();
    }
    else{
      this.updateImageForm.controls['title'].disable();
      this.updateImageForm.controls['author'].disable();
      this.updateImageForm.controls['genre'].disable();
      this.updateImageForm.controls['description'].disable();
      this.updateImageForm.controls['price'].disable();
      this.updateImageForm.controls['imageURL'].disable();
    }
    
  }

  updateImage(id: any){
    console.log(this.book);

    this.bookService.updateBook(this.tokenService.getToken(), 
      id, 
      this.updateImageForm.value.title = this.updateImageForm.value.title || this.book.title,
      this.updateImageForm.value.author = this.updateImageForm.value.author || this.author,
      this.updateImageForm.value.genre = this.updateImageForm.value.genre || this.book.genre.genreId,
      this.updateImageForm.value.description = this.updateImageForm.value.description || this.book.description,
      this.updateImageForm.value.price = this.updateImageForm.value.price || this.book.price,
      this.updateImageForm.value.imageURL = this.updateImageForm.value.imageURL || this.book.imageURL)
      .subscribe( data => {
      this.openSnackBar("Book Edited","");
      this.edit();
      console.log(data);
      this.book = data;
    },(err: any) => {
      console.log(err);
    });
  }

  openSnackBar(message: string,action: string): MatSnackBarRef<SimpleSnackBar> {
    return this._snackBar.open(message, action, {
      duration: 2000
    });
  }
}
