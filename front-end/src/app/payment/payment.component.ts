import { Component, OnInit, ViewChild } from '@angular/core';
import { switchMap } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { FormGroup, Validators, FormBuilder, FormControl } from '@angular/forms';
import { StripeCardComponent, StripeCardNumberComponent, StripeService } from 'ngx-stripe';
import { environment as env } from '../../environments/environment';
import { Observable } from 'rxjs';
import { PaymentIntent, StripeCardElementOptions, StripeElement, StripeElementsOptions } from '@stripe/stripe-js';
import { OrderService } from '../services/order-service/order.service';
import { TokenService } from '../services/token-service/token.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss']
})
export class PaymentComponent implements OnInit {

  amount: number;
  couponCode: string;

  @ViewChild(StripeCardNumberComponent) card: StripeCardNumberComponent;

  cardOptions: StripeCardElementOptions = {
    style: {
      base: {
        iconColor: '#666EE8',
        color: '#31325F',
        fontWeight: '300',
        fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
        fontSize: '18px',
        '::placeholder': {
          color: '#CFD7E0',
        },
      },
    },
  };

  elementsOptions: StripeElementsOptions = {
    locale: 'es',
  };

  stripe: FormGroup;

  constructor(
    private fb: FormBuilder,
    private stripeService: StripeService,
    private orderService: OrderService,
    private tokenService: TokenService,
    private route: ActivatedRoute,
    private _snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.stripe = this.fb.group({
      'name': new FormControl('',[Validators.required]),
    });
    this.amount = parseInt(this.route.snapshot.paramMap.get('amount'));
    this.couponCode = this.route.snapshot.paramMap.get('couponCode');
  }

  getNameErrorMessage(){
    return this.stripe.get('name')?.hasError('required') ? 'You must enter a name' : '';
  }

  createToken(): void {
    const name = this.stripe.get('name').value;
    this.stripeService
      .createToken(this.card.element, { name })
      .subscribe((result) => {
        if (result.token) {
          this.orderService.createOrder(this.tokenService.getToken(), result.token.id, name, this.amount, this.couponCode? this.couponCode : '').subscribe(data => {
            console.log(data);
            this.openSnackBar("Payment completed","");
            this.router.navigateByUrl("/orders");
          });
          console.log(result.token.id);
        } else if (result.error) {
          console.log(result.error.message);
        }
      });
  }

  openSnackBar(message: string,action: string): MatSnackBarRef<SimpleSnackBar> {
    return this._snackBar.open(message, action, {
      duration: 2000
    });
  }
}
