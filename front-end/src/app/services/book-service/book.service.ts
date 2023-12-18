import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  readonly bookServiceUrl = 'http://127.0.0.1:8765/book-service/books'

  constructor(private http: HttpClient) { }

  getBookList(filterByName: string, authorId: string, genreId: string): Observable<any[]> {
    return this.http.get<any[]>(this.bookServiceUrl + "?title=" + filterByName + "&authorId=" + authorId + "&genreId=" + genreId);
  }

  getBookById(bookId: number): Observable<any>{
    return this.http.get<any[]>(this.bookServiceUrl + "/" + bookId);
  }

  addBook(tokenHeader: string, title: string, firstName: string, lastName: string, genreId: number, description: string, price: number,imageURL: string): Observable<any>{
    let header = new HttpHeaders();
    header = header.set('Authorization', 'Bearer ' + tokenHeader )


    return this.http.post<any>(this.bookServiceUrl + '/add?title='+ title +'&firstName='+ firstName +'&lastName='+ lastName +'&genreId='+ 
    genreId+'&description='+ description +'&price=' + price + '&imageURL=' + imageURL,
    null,
    {headers: header});
  }

  deleteBook(tokenHeader: string, bookId: number): Observable<any>{
    let header = new HttpHeaders();
    header = header.set('Authorization', 'Bearer ' + tokenHeader);

    return this.http.delete<any>(this.bookServiceUrl + "/" + bookId + '/delete', {headers: header});
  }

  updateBook(tokenHeader: string, bookId: number, title: string, author: string, genreId: number, description: string, price: number,imageURL: string){
    let header = new HttpHeaders();
    header = header.set('Authorization', 'Bearer ' + tokenHeader )

    console.log(author.toString().split(' ')[0] + " " + author.toString().split(' ')[1]);

    return this.http.post<any>(this.bookServiceUrl + '/add?id=' + bookId +'&title='+ title +'&firstName='+ author.toString().split(' ')[0] +'&lastName='+ author.toString().split(' ')[1] +'&genreId='+ 
    genreId+'&description='+ description +'&price=' + price + '&imageURL=' + imageURL,
    null,
    {headers: header});
  }

  rateBook(tokenHeader: string, bookId: number, rating: number): Observable<any>{
    let header = new HttpHeaders();
    header = header.set('Authorization', 'Bearer ' + tokenHeader )
    
    return this.http.post<any>(this.bookServiceUrl + '/rate?bookId=' + bookId + '&rating=' + rating, null, {headers: header});
  }

  unrateBook(tokenHeader: string, ratingId: number): Observable<any>{
    let header = new HttpHeaders();
    header = header.set('Authorization', 'Bearer ' + tokenHeader )

    return this.http.delete<any>(this.bookServiceUrl + "/unrate?ratingId=" + ratingId, {headers: header});
  }

  bookRecommendations(tokenHeader: string): Observable<any>{
    let header = new HttpHeaders();
    header = header.set('Authorization', 'Bearer ' + tokenHeader )

    return this.http.get<any>(this.bookServiceUrl + "/recommendations", {headers: header});
  }

}
