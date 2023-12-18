import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  readonly roleServiceUrl = 'http://127.0.0.1:8765/authentication-service/roles'

  constructor(private http: HttpClient) { }

  getRoles(): Observable<any[]>{
    return this.http.get<any[]>(this.roleServiceUrl);
  }
}
