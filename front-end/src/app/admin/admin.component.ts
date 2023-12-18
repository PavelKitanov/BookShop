import { Component, Input, OnInit } from '@angular/core';
import { BookService } from '../services/book-service/book.service';
import { TokenService } from '../services/token-service/token.service';
import { AuthorService } from '../services/author-service/author.service';
import { GenreService } from '../services/genre-service/genre.service';
import { OrderService } from '../services/order-service/order.service';
import { UserService } from '../services/user-service/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {

  activeTab: string = 'books';

  books: any = [];
  authors: any = [];
  genres: any = [];
  orders: any = [];
  users: any = [];

  constructor(private tokenService: TokenService,
              private bookService: BookService,
              private authorService: AuthorService,
              private genreService: GenreService,
              private orderService: OrderService,
              private userService: UserService,
              private route: ActivatedRoute, 
              private _snackBar: MatSnackBar,
              private router: Router){}

  ngOnInit(): void {
    this.getBooks();
    this.getAuthors();
    this.getGenres();
    this.getOrders();
    this.getUsers();

    this.activeTab = this.route.snapshot.paramMap.get('tab');
    if(this.activeTab == null)
      this.activeTab = 'books';
  }

  getBooks(){
    this.bookService.getBookList('','','').subscribe(data => {
      this.books = data;
      console.log(this.books);
    });
  }

  deleteBook(bookId: number){
    this.bookService.deleteBook(this.tokenService.getToken(), bookId).subscribe(data => {
      console.log(data);
      this.openSnackBar("Book Deleted","");
      this.getBooks();
    });
  }

  getAuthors(){
    this.authorService.getAuthorsList().subscribe(data => {
      this.authors = data;
      console.log(this.authors);
    });
  }

  deleteAuthor(authorId: number){
    this.authorService.deleteAuthor(authorId).subscribe(data => {
      console.log(data);
      this.openSnackBar("Author Deleted","");
      this.getAuthors();
    });
  }

  getGenres(){
    this.genreService.getGenreList().subscribe(data => {
      this.genres = data;
      console.log(this.genres);
    });
  }

  deleteGenre(genreId: number){
    this.genreService.deleteGenre(genreId).subscribe(data => {
      console.log(data);
      this.openSnackBar("Genre Deleted","");
      this.getGenres();
    });
  }

  getOrders(){
    this.orderService.getAllOrders().subscribe(data => {
      this.orders = data;
      console.log(this.orders);
    });
  }

  deleteOrder(orderId: number){
    this.orderService.deleteOrder(orderId).subscribe(data => {
      console.log(data);
      this.openSnackBar("Order Deleted","");
      this.getOrders();
    });
  }

  getUsers(){
    this.userService.getAllUsers().subscribe(data => {
      this.users= data;
      console.log(this.users);
    });
  }

  getCustomerUsernameForOrder(customerId: string){
    for(var user of this.users){
      if(user.userId === customerId)
        return user.username;
    }
  }

  getCustomerEmailForOrder(customerId: string){
    for(var user of this.users){
      if(user.userId === customerId)
        return user.email;
    }
  }

  openSnackBar(message: string,action: string): MatSnackBarRef<SimpleSnackBar> {
    return this._snackBar.open(message, action, {
      duration: 2000
    });
  }
}
