import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { BookService } from 'src/app/services/book-service/book.service';
import { MdbModalRef, MdbModalService } from 'mdb-angular-ui-kit/modal';
import { AddBookComponent } from '../add-book/add-book.component';
import { defineComponents, IgcRatingComponent } from 'igniteui-webcomponents';
import { CartService } from 'src/app/services/cart-service/cart.service';
import { AuthorService } from 'src/app/services/author-service/author.service';
import { GenreService } from 'src/app/services/genre-service/genre.service';
import { TokenService } from 'src/app/services/token-service/token.service';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';

defineComponents(IgcRatingComponent);

@Component({
  selector: 'app-list-books',
  templateUrl: './list-books.component.html',
  styleUrls: ['./list-books.component.scss']
})
export class ListBooksComponent implements OnInit {

  @Input() bookList: any = [];
  ActivateAddEditDepartComp: boolean = false;
  selectedIndex: number | null = null;

  constructor (private bookService: BookService,
               private cartService: CartService,
               private authorService: AuthorService,
               private genreService: GenreService,
               public tokenService: TokenService,
               private _snackBar: MatSnackBar){}

  ngOnInit(): void {

  }


  getBooks(){
    this.bookService.getBookList('', '','').subscribe(data => {
      this.bookList = data;
      console.log(this.bookList);
    });
  }
  

  deleteBook(bookId: number){
    console.log(bookId);
    this.bookService.deleteBook(this.tokenService.getToken(), bookId).subscribe(data => {
      console.log(data);
      this.getBooks();
    });
  }

  toggleCard(index: number) {
    if (this.selectedIndex === index) {
      return;
    } else {
      this.selectedIndex = index;
    }
  }

  getAverageRating(ratings: any[]): number {
    if (!ratings || ratings.length === 0) {
      return 0;
    }
  
    const totalRatings = ratings.reduce((acc, curr) => acc + curr.rating, 0);
    const averageRating = totalRatings / ratings.length;

    return parseFloat(averageRating.toFixed(1));
  }

  getRatingByUser(ratings: any[]): number {
    for(var rat of ratings){
      if(rat.customerId == this.tokenService.getUserId()){
        return rat.rating;
      }
    }
    return 0;
  }

  updateUserRating(book: any, event: any){
    const newRating = event;
    var ratingId;
    for(var rat of book.ratings){
      if(rat.customerId == this.tokenService.getUserId()){
        rat.rating = parseInt(newRating);
        ratingId = rat.ratingId;
      }
    }

    if(parseInt(newRating) === 0){
      this.bookService.unrateBook(this.tokenService.getToken(), ratingId).subscribe(data => {
        console.log("Rating deleted");
      });
    }
    else{
      this.bookService.rateBook(this.tokenService.getToken(), book.bookId, parseInt(newRating)).subscribe(data => {
        console.log(data);
      });
    }

    console.log(parseInt(newRating));
  }

  addItemToCart(book: any){
    this.cartService.addItemToCart(this.tokenService.getToken(), book.bookId).subscribe(data => {
      this.openSnackBar("Book " + book.title +" added to cart","");
    });
  }

  openSnackBar(message: string,action: string): MatSnackBarRef<SimpleSnackBar> {
    return this._snackBar.open(message, action, {
      duration: 2000
    });
  }
}
