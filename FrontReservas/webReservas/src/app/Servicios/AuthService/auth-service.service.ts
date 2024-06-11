import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

  isLoggedIn(): Observable<boolean> {
    return this.http.get<{ loggedIn: boolean }>(`${this.apiUrl}/isLoggedIn`).pipe(
      map(response => response.loggedIn)
    );
  }

  getUserId(): Observable<string | null> {
    return this.http.get<{ userId: string | null }>(`${this.apiUrl}/getUserId`).pipe(
      map(response => response.userId)
    );
  }

  registrarUsuario(user: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/register`, user);
  }
}
