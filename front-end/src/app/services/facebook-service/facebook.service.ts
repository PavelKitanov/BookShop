import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

declare const FB: any;

@Injectable({
  providedIn: 'root'
})
export class FacebookService {

  private _authState: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  get authState(): Observable<boolean> {
    return this._authState.asObservable();
  }

  constructor() {
    this.initFacebookSDK();
  }

  private initFacebookSDK(): void {
    (window as any).fbAsyncInit = () => {
      FB.init({
        appId: '972437717384615',
        autoLogAppEvents: true,
        xfbml: true,
        version: 'v18.0',
      });

      FB.getLoginStatus((response: any) => {
        this.handleAuthResponse(response);
      });
    };
  }

  loginWithFacebook(): void {
    FB.login((response: any) => {
      this.handleAuthResponse(response);
    }, { scope: 'email' }); 
  }

  private handleAuthResponse(response: any): void {
    if (response.authResponse) {
      this._authState.next(true);
      const accessToken = response.authResponse.accessToken;
      this.sendTokenToBackend(accessToken);
    } else {
      this._authState.next(false);
    }
  }

  private sendTokenToBackend(token: string): void {
    // Send the token to your backend for further processing
    console.log('Sending Facebook token to backend:', token);
  }
}
