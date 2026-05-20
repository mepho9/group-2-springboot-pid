import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface User {
  id: number;
  login: string;
  firstname: string;
  lastname: string;
  email: string;
  langue: string;
  created_at: string;
  roles?: string[];
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = '/api/users';

  constructor(private http: HttpClient) { }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  registerUser(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, userData);
  }

  getUserByLogin(login: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${login}`);
  }

  updateUser(login: string, userData: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${login}`, userData);
  }
}
