import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  readonly cartServiceUrl = 'http://127.0.0.1:8765/book-service/carts'

  constructor(private http: HttpClient) { }

  createCart(tokenHeader: string): Observable<any>{
    let header = new HttpHeaders();
    header = header.set('Authorization', 'Bearer ' + tokenHeader )

    return this.http.post<any[]>(this.cartServiceUrl + "/createCart", null, {headers: header});
  }

  getCartByCustomer(tokenHeader: string): Observable<any>{
    let header = new HttpHeaders();
    header = header.set('Authorization', 'Bearer ' + tokenHeader )

    return this.http.get<any[]>(this.cartServiceUrl + "/cartByCustomer", {headers: header});
  }

  addItemToCart(tokenHeader: string, bookId: number): Observable<any>{
    let header = new HttpHeaders();
    header = header.set('Authorization', 'Bearer ' + tokenHeader )

    return this.http.post<any[]>(this.cartServiceUrl + "/addItemToCart?bookId=" + bookId + "&quantity=1", null, {headers: header});
  }

  removeItemFromCart(tokenHeader: string, cartItemId: number): Observable<any>{
    let header = new HttpHeaders();
    header = header.set('Authorization', 'Bearer ' + tokenHeader )

    return this.http.delete<any[]>(this.cartServiceUrl + "/removeItemFromCart/" + cartItemId, {headers: header});
  }

  couponCode(tokenHeader: string, code: string){
    let header = new HttpHeaders();
    header = header.set('Authorization', 'Bearer ' + tokenHeader )

    return this.http.get<number>(this.cartServiceUrl + "/couponCode?discount=" + code, {headers: header});
  }

  removeAllItemsFromCart(tokenHeader: string): Observable<any>{
    return this.http.delete<any>(this.cartServiceUrl + "/removeAllItems");
  }
}
