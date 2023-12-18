import { Component, OnInit } from '@angular/core';
import { BookService } from '../services/book-service/book.service';
import { PageEvent } from '@angular/material/paginator';
import { AuthorService } from '../services/author-service/author.service';
import { GenreService } from '../services/genre-service/genre.service';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.scss']
})
export class BookComponent implements OnInit {

  books: any = [];
  displayedBooks: any[] = [];
  pageSize = 8;
  currentPage = 1;
  selectedAuthorId = '';
  selectedGenreId = '';
  filterByTitle = '';
  authors: any = [];
  genres: any =[];

  constructor(private bookService: BookService,
              private authorService: AuthorService,
              private genreService: GenreService){}

  ngOnInit(): void {
    this.getBooks();
    this.getAuthors();
    this.getGenres();
  }

  getBooks(){
    this.bookService.getBookList('','','').subscribe(data => {
      this.books = data;
      this.onPageChange(this.currentPage);
    });
  }

  onAuthorChange(){
    console.log(this.selectedAuthorId);
  }

  onPageChange(page: number) {
    this.currentPage = page;
    const startIndex = (page - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.displayedBooks = this.books.slice(startIndex, endIndex);
  }
  
  getPages(): number[] {
    const totalBooks = this.books.length;
    const pageCount = Math.ceil(totalBooks / this.pageSize);
    return Array.from({ length: pageCount }, (_, i) => i + 1);
  }

  filterBooks(){
    console.log(this.filterByTitle);
    this.bookService.getBookList(this.filterByTitle, this.selectedAuthorId, this.selectedGenreId).subscribe(data => {
      this.books = data;
      console.log(data);
      this.onPageChange(1);
      this.getPages();
    });
  }

  
  getAuthors(){
    this.authorService.getAuthorsList().subscribe(data => {
      this.authors = data;
    });
  }

  getGenres(){
    this.genreService.getGenreList().subscribe(data => {
      this.genres = data;
    });
  }
}
