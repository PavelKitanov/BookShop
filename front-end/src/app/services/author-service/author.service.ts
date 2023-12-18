import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, last } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthorService {

  readonly authorServiceUrl = 'http://127.0.0.1:8765/book-service/authors'

  constructor(private http: HttpClient) { }

  getAuthorsList(): Observable<any[]>{
    return this.http.get<any[]>(this.authorServiceUrl);
  }

  addAuthor(firstName: string, lastName: string): Observable<any>{
    const author = { firstName: firstName, lastName: lastName};

    return this.http.post<any>(this.authorServiceUrl + "/add", author);
  }

  deleteAuthor(authorId: number): Observable<any>{
    return this.http.delete<any>(this.authorServiceUrl + "/"+ authorId + "/delete");
  }

  getAuthorById(authorId: number): Observable<any>{
    return this.http.get<any>(this.authorServiceUrl + "/" + authorId);
  }

  updateAuthor(authorId: number, firstName: string, lastName: string): Observable<any>{
    const author = {authorId: authorId, firstName: firstName, lastName: lastName}
    return this.http.post<any>(this.authorServiceUrl + "/add", author);
  }
}
