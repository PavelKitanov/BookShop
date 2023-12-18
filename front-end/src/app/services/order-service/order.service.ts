import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { themeSymbol } from 'igniteui-webcomponents/theming/theming-controller';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  readonly orderServiceUrl = 'http://127.0.0.1:8765/book-service/orders'

  constructor(private http: HttpClient) { }

  createOrder(tokenHeader: string, stripeTokenId: string, name: string, price: number, discount: string): Observable<any>{
    let header = new HttpHeaders();
    header = header.set('Authorization', 'Bearer ' + tokenHeader )

    const stripeChargeRequest = { stripeToken: stripeTokenId, name: name, amount: price }

    return this.http.post<any>(this.orderServiceUrl + "/createOrder?discount=" + discount, stripeChargeRequest, {headers: header});
  }

  getOrderByUser(): Observable<any>{
    return this.http.get<any>(this.orderServiceUrl + "/ordersByCustomer");
  }

  getAllOrders(): Observable<any[]>{
    return this.http.get<any[]>(this.orderServiceUrl);
  }

  deleteOrder(orderId: number): Observable<any>{
    return this.http.delete<any>(this.orderServiceUrl + "/delete/" + orderId);
  }
}
