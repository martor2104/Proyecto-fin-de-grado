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
  private profileImageUrl = new BehaviorSubject<string | null>(this.getProfileImageFromLocalStorage());


  constructor(private http: HttpClient) {}

  isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  getUsername(): Observable<string | null> {
    return this.username.asObservable();
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
          const decodedToken: any = jwt_decode(response.token);
          const userRol = decodedToken.role || decodedToken.rol || decodedToken.userRol;
  
          if (userRol) {
            localStorage.setItem('userRol', userRol);
            this.userRol.next(userRol);
          }
  
          // Guardar la URL de la imagen de perfil
          const profileImageUrl = response.profileImageUrl || 'assets/utilities/sinPerfil.png';
          console.log("Profile Image URL from response:", profileImageUrl); // Verificar la URL de la imagen de perfil
  
          localStorage.setItem('profileImageUrl', profileImageUrl);
          this.profileImageUrl.next(profileImageUrl); // Actualizar BehaviorSubject con la URL
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
      localStorage.removeItem('profileImageUrl');
    }
    this.loggedIn.next(false);
    this.username.next(null);
    this.userRol.next(null);
    this.profileImageUrl.next('assets/utilities/sinPerfil.png');
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

  getUserId(): Observable<number | null> {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const decodedToken: any = jwt_decode(token);  // Decodifica el token JWT
        const userId = decodedToken.userId;  // Asegúrate de que el 'userId' sea el campo correcto
        return new BehaviorSubject<number | null>(userId).asObservable();
      } catch (error) {
        console.error('Error al decodificar el token', error);
        return new BehaviorSubject<number | null>(null).asObservable();
      }
    }
    return new BehaviorSubject<number | null>(null).asObservable();
  }

  verificarNombreUsuario(username: string): Observable<boolean> {
    return this.http.get<boolean>(`/api/auth/exists/username/${username}`);
  }
  
  
  
  getProfileImage(): Observable<string | null> {
    return this.profileImageUrl.asObservable();
  }


  private getProfileImageFromLocalStorage(): string | null {
    return this.isBrowser() ? localStorage.getItem('profileImageUrl') : null;
  }

    getUserIdFromToken(): number | null {
    // Verifica si `localStorage` está disponible
    if (typeof localStorage === 'undefined') {
      console.warn('localStorage no está disponible.');
      return null;
    }
  
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const decodedToken: any = jwt_decode(token);
        const userId = decodedToken.userId;  // Asegúrate de que `userId` sea el campo correcto
        return userId ? Number(userId) : null;
      } catch (error) {
        console.error('Error al decodificar el token', error);
        return null;
      }
    }
    return null;
  }
}

