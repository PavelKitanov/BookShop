import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth-service/auth.service';
import { TokenService } from '../services/token-service/token.service';
import { FacebookService } from '../services/facebook-service/facebook.service';
import { FacebookLoginProvider, GoogleLoginProvider, SocialAuthService } from '@abacritt/angularx-social-login';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthGuardService } from '../services/auth-guard/auth-guard.service';
import { CartService } from '../services/cart-service/cart.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit{

  loginForm: FormGroup = new FormGroup({});
  showPassword = false;
  user: any;
  loggedIn: any;
  googleAccessToken: string;

  constructor(private formBuilder: FormBuilder,
              private authService: AuthService,
              private cartService: CartService,
              private facebookService: FacebookService,
              private socialAuthService: SocialAuthService,
              private tokenService: TokenService,
              private router: Router){}

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      'username': new FormControl('',[Validators.required]),
      'password': new FormControl('',[Validators.required]),
    });


    this.socialAuthService.authState.subscribe(async (user) => {
      this.user = user;
      console.log(this.user);
      if(this.user.provider === "GOOGLE"){
        await this.getGoogleAccessToken();

        this.authService.googleLogin(this.googleAccessToken).subscribe(data => {
          this.tokenService.setToken(data.token);
          this.tokenService.setRole(data.role);
          this.tokenService.setUserId(data.userId);

          this.cartService.getCartByCustomer(this.tokenService.getToken()).subscribe(data => {
            this.cartService.removeAllItemsFromCart(this.tokenService.getToken()).subscribe(data => {
            });
          }, (error) => {
            this.cartService.createCart(this.tokenService.getToken()).subscribe(data => {
            })
          });
          
          this.router.navigateByUrl('/home');
          console.log(data);
        });
        
      }
      else{
        this.authService.facebookLogin(user.authToken).subscribe(data => {
          this.tokenService.setToken(data.token);
          this.tokenService.setRole(data.role);
          this.tokenService.setUserId(data.userId);

          this.cartService.getCartByCustomer(this.tokenService.getToken()).subscribe(data => {
            this.cartService.removeAllItemsFromCart(this.tokenService.getToken()).subscribe(data => {
            });
          }, (error) => {
            this.cartService.createCart(this.tokenService.getToken()).subscribe(data => {
            })
          });
          
          this.router.navigateByUrl('/home');
          console.log(data);
        });
      }
      this.loggedIn = (user != null);
      
    });
  }

  async getGoogleAccessToken(): Promise<void> {
    try {
      const value = await this.socialAuthService.getAccessToken(GoogleLoginProvider.PROVIDER_ID);
      this.googleAccessToken = value;
      console.log('Google access token:', this.googleAccessToken);
  
    } catch (error) {
      console.error('Error getting Google access token:', error);
    }
  }

  login(){
    this.authService.login(this.loginForm.get('username').value, this.loginForm.get('password').value).subscribe(data => {
      this.tokenService.setToken(data.token);
      this.tokenService.setRole(data.role);
      this.tokenService.setUserId(data.userId);
      console.log(data);
      this.cartService.getCartByCustomer(this.tokenService.getToken()).subscribe(data => {
        this.cartService.removeAllItemsFromCart(this.tokenService.getToken()).subscribe(data => {
        });
      }, (error) => {
        this.cartService.createCart(this.tokenService.getToken()).subscribe(data => {
        })
      });
          
        this.router.navigateByUrl('/home');
    });
  };

  isLoggedIn(): boolean {
    return this.loggedIn;
  }

  signInWithFB(): void {
    this.socialAuthService.signIn(FacebookLoginProvider.PROVIDER_ID);
  }

  signOut(): void {
    this.socialAuthService.signOut();
  }

  loginWithFacebook(): void {
    this.facebookService.loginWithFacebook();
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  getUsernameErrorMessage(){
    return this.loginForm.get('username')?.hasError('required') ? 'You must enter a username' : '';
  }

  getPasswordErrorMessage(){
    return this.loginForm.get('password')?.hasError('required') ? 'You must enter a password' : '';
  }

}
