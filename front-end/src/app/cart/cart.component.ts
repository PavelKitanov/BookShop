import { Component, OnInit } from '@angular/core';
import { CartService } from '../services/cart-service/cart.service';
import { CartItemService } from '../services/cart-item-service/cart-item.service';
import { TokenService } from '../services/token-service/token.service';
import { MatDialog } from '@angular/material/dialog';
import { PurchaseDialogComponent } from '../purchase-dialog/purchase-dialog.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {

  cart: any;
  couponCode: string = '';
  discount: number = 0.00;
  counter: any;

  constructor(private cartService: CartService,
              private tokenService: TokenService,
              private cartItemService: CartItemService,
              private dialog: MatDialog,
              private router: Router){}

  ngOnInit(): void {
    this.getCartByCustomer();
    console.log(this.couponCode);
  }

  getCartByCustomer(){
    this.cartService.getCartByCustomer(this.tokenService.getToken()).subscribe(data => {
      this.cart = data;
    })
  }

  removeItemFromCart(cartItem: any){
    this.cartService.removeItemFromCart(this.tokenService.getToken(), cartItem.cartItemId).subscribe(data => {
      this.cart = data;
    });
  }

  onQuantityChange(cartItem: any, quantity: any){
    console.log(quantity);
    this.cartItemService.updateCart(this.tokenService.getToken(), cartItem.cartItemId, parseInt(quantity)).subscribe(data => {
      cartItem.quantity = data.quantity;
      console.log(data);
      this.getCartByCustomer();
    })
  }

  getDiscountValue(code: string){
    this.cartService.couponCode(this.tokenService.getToken(), code).subscribe(data => {
      this.discount = data;
      console.log(this.discount);
    });
  }

  checkCode(){
    this.getDiscountValue(this.couponCode);
  }

  purchase() {
    if (this.cart?.cartItems?.length == 0) {
      this.dialog.open(PurchaseDialogComponent, {
        data: {
          messageLine1: 'Your shopping cart is empty.',
          messageLine2: 'Add books before making a purchase.',
        },
      });
    } else {
      const amount = this.cart?.cartTotalPrice - this.discount;
      console.log(amount);
      this.router.navigateByUrl(`/payment/${amount}/${this.couponCode}`);
    }
  }
}
