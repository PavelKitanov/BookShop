import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  private readonly TOKEN_KEY = 'jwt_token';
  private readonly ROLES_KEY = 'user_roles';
  private readonly USERID_KEY = 'user_id'

  constructor() {}

  setToken(token: string): void {
    sessionStorage.setItem(this.TOKEN_KEY, token);
  }

  getToken(): string | null {
    return sessionStorage.getItem(this.TOKEN_KEY);
  }

  setRole(role: string): void {
    sessionStorage.setItem(this.ROLES_KEY, role);
  }

  getRole(): string {
    const rolesString = sessionStorage.getItem(this.ROLES_KEY);
    return rolesString;
  }

  setUserId(userId: string): void {
    sessionStorage.setItem(this.USERID_KEY, userId);
  }

  getUserId(): string {
    return sessionStorage.getItem(this.USERID_KEY);
  }

  clearStorage(): void {
    sessionStorage.removeItem(this.TOKEN_KEY);
    sessionStorage.removeItem(this.ROLES_KEY);
    sessionStorage.removeItem(this.USERID_KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

}
