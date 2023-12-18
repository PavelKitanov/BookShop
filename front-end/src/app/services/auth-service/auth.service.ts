import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  readonly authServiceUrl = 'http://127.0.0.1:8765/authentication-service/auth'

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<any>{
    const loginRequest = { username: username, password: password }
    return this.http.post<any>(this.authServiceUrl + "/signin", loginRequest);;
  };
  
  register(username: string, email: string, password: string){
    const registerRequest = { username: username, email: email, password: password, role: "ROLE_USER" };
    return this.http.post(this.authServiceUrl + "/register", registerRequest, { responseType: 'text' });
  }

  facebookLogin(facebookAccessToken: string){
    return this.http.post<any>(this.authServiceUrl + "/facebook/signin?facebookAccessToken=" + facebookAccessToken, null);
  }

  googleLogin(googleAccessToken: string) {
    return this.http.post<any>(this.authServiceUrl + "/google/signin?googleAccessToken=" + googleAccessToken, null);
  }
}
