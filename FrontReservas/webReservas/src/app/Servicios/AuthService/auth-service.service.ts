import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, map } from 'rxjs';
import jwt_decode from 'jwt-decode';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private loggedIn = new BehaviorSubject<boolean>(this.checkLoggedIn());
  private username = new BehaviorSubject<string | null>(this.getUsernameFromLocalStorage());
  private userRol = new BehaviorSubject<string | null>(this.getRoleFromLocalStorage());

  constructor(private http: HttpClient) {}

  isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  getUsername(): Observable<string | null> {
    return this.username.asObservable();
  }

  getUserId(): Observable<string | null> {
    return this.http.get<{ userId: string | null }>(`${this.apiUrl}/getUserId`).pipe(
      map(response => response.userId)
    );
  }

  registrarUsuario(user: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/register`, user);
  }

  login(request: { name: string, password: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, request).pipe(
      map(response => {
        if (this.isBrowser()) {
          // Guardar el token en localStorage
          localStorage.setItem('token', response.token);
          localStorage.setItem('username', request.name);

          // Decodificar el token para extraer el rol
          const decodedToken: any = jwt_decode(response.token);// Utilizar jwt-decode
          const userRol = decodedToken.role || decodedToken.rol || decodedToken.userRol;  // Asegúrate de obtener el campo correcto
          
          if (userRol) {
            localStorage.setItem('userRol', userRol);  // Almacenar userRol en localStorage
            this.userRol.next(userRol);  // Actualizar BehaviorSubject
          } else {
            console.error('No se pudo extraer el rol del token');
          }
        }
        this.loggedIn.next(true);
        this.username.next(request.name);
        return response;
      })
    );
  }

  logout(): void {
    if (this.isBrowser()) {
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      localStorage.removeItem('userRol');
    }
    this.loggedIn.next(false);
    this.username.next(null);
    this.userRol.next(null);
  }

  getRole(): Observable<string | null> {
    return this.userRol.asObservable();
  }

  private checkLoggedIn(): boolean {
    if (this.isBrowser()) {
      return !!localStorage.getItem('token');
    }
    return false;
  }

  private getUsernameFromLocalStorage(): string | null {
    if (this.isBrowser()) {
      return localStorage.getItem('username');
    }
    return null;
  }

  private getRoleFromLocalStorage(): string | null {
    if (this.isBrowser()) {
      return localStorage.getItem('userRol');
    }
    return null;
  }

  private isBrowser(): boolean {
    return typeof window !== 'undefined' && typeof localStorage !== 'undefined';
  }
}

