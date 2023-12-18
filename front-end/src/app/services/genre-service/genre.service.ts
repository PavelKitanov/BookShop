import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GenreService {

  readonly genreServiceUrl = 'http://127.0.0.1:8765/book-service/genres'

  constructor(private http: HttpClient) { }

  getGenreList(): Observable<any[]>{
    return this.http.get<any[]>(this.genreServiceUrl);
  }

  addGenre(genreName: string): Observable<any>{
    const genre = {genre: genreName}
    return this.http.post<any>(this.genreServiceUrl + "/createGenre", genre);
  }

  getGenreById(genreId: number): Observable<any>{
    return this.http.get<any>(this.genreServiceUrl + "/" + genreId);
  }

  deleteGenre(genreId: number): Observable<any>{
    return this.http.delete<any>(this.genreServiceUrl + "/deleteGenre/" + genreId);
  }

  updateGenre(genreId: number, genreName: string): Observable<any>{
    const genre = { genreId: genreId, genre: genreName };
    return this.http.post<any>(this.genreServiceUrl + "/createGenre", genre);
  }

}
