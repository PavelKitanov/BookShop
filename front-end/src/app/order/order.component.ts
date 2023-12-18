import { Component, OnInit } from '@angular/core';
import { TokenService } from '../services/token-service/token.service';
import { OrderService } from '../services/order-service/order.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})
export class OrderComponent implements OnInit {

  orders:any = [];

  constructor(private tokenSerivce: TokenService,
              private orderService: OrderService){}

  ngOnInit(): void {
    this.getOrdersByUser();
  }

  getOrdersByUser(){
    this.orderService.getOrderByUser().subscribe(data => {
      this.orders = data;
      console.log("orders:", JSON.stringify(data, null, 2));
    });
  }

}
