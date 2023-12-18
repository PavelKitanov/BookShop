import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  readonly userServiceUrl = 'http://127.0.0.1:8765/authentication-service/users'

  constructor(private http: HttpClient) { }

  getAllUsers(): Observable<any[]>{
    return this.http.get<any[]>(this.userServiceUrl);
  }
  
  getUserById(userId: number): Observable<any>{
    return this.http.get<any>(this.userServiceUrl + "/" + userId);
  }

  updateUser(userId: number, role: string): Observable<any>{
    return this.http.post<any>(this.userServiceUrl + "/updateRole?userId=" + userId + "&role=" + role,null);
  }

  async getUserRole(userId: number): Promise<Observable<any>>{
    return this.http.get<any>(this.userServiceUrl + "/" + userId + "/role");
  }


}
