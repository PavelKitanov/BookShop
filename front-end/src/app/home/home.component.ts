import { Component, OnInit } from '@angular/core';
import { BookService } from '../services/book-service/book.service';
import { TokenService } from '../services/token-service/token.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  books: any = [];

  constructor(private bookService: BookService,
              private tokenService: TokenService){}

  ngOnInit(): void {
  }

}
