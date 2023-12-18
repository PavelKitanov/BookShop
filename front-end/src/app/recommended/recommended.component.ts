import { Component, OnInit } from '@angular/core';
import { BookService } from '../services/book-service/book.service';
import { TokenService } from '../services/token-service/token.service';

@Component({
  selector: 'app-recommended',
  templateUrl: './recommended.component.html',
  styleUrls: ['./recommended.component.scss']
})
export class RecommendedComponent implements OnInit {
  books: any = [];

  constructor(private bookService: BookService,
              private tokenService: TokenService){}

  ngOnInit(): void {
    this.getBookRecommendations();
  }

  getBookRecommendations(){
    const token = this.tokenService.getToken();

    this.bookService.bookRecommendations(token).subscribe(data => {
      this.books = data;
      console.log(data);
    });
  };
}
