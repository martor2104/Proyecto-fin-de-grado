import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserDTO } from '../../Model/usuario.model';

@Injectable({
  providedIn: 'root'
})
export class UsuariosServiceService {

  private apiUrl = 'http://localhost:8080/api/users'; 

  constructor(private http: HttpClient) { }

  // Método para obtener todos los usuarios
  getUsuarios(): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(this.apiUrl);
  }

  // Método para obtener un usuario por ID
  getUsuario(id: number): Observable<UserDTO> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.get<UserDTO>(url);
  }

  // Método para actualizar un usuario existente
  updateUsuario(id: number, usuario: UserDTO): Observable<UserDTO> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.put<UserDTO>(url, usuario);
  }

  // Método para eliminar un usuario
  deleteUsuario(id: number): Observable<void> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.delete<void>(url);
  }

  addUsuario(usuario: UserDTO): Observable<UserDTO>{
    return this.http.post<UserDTO>(`${this.apiUrl}`, usuario);
  }
}
