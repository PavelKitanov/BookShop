import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CartItemService {

  readonly cartItemServiceUrl = 'http://127.0.0.1:8765/book-service/cartItems'

  constructor(private http: HttpClient) { }

  updateCart(tokenHeader: string, cartItemId: number, quantity:number): Observable<any>{
    let header = new HttpHeaders();
    header = header.set('Authorization', 'Bearer ' + tokenHeader );

    return this.http.post<any>(this.cartItemServiceUrl + "/updateCartItem/" + cartItemId + "?quantity=" + quantity, null, {headers: header});
  }
}
