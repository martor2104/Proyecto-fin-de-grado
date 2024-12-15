import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../../Model/user.model';

@Injectable({
  providedIn: 'root'
})
export class UsuariosServiceService {

  private apiUrl = 'http://localhost:8080/api/users'; 

  constructor(private http: HttpClient) { }

  // Método para obtener todos los usuarios
  getUsuarios(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  // Método para obtener un usuario por ID
  getUsuario(id: number): Observable<User> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.get<User>(url);
  }

  // Método para actualizar un usuario existente
  updateUsuario(userId: number, formData: FormData): Observable<any> {
    return this.http.put(`${this.apiUrl}/${userId}`, formData);
  }

  // Método para eliminar un usuario
  deleteUsuario(id: number): Observable<void> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.delete<void>(url);
  }

  addUsuario(formData: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}`, formData);
  }
}
