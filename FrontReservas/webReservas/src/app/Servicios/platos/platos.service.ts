import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PlatosService {

  private apiUrl = 'http://localhost:8080/api/plates'; 

  constructor(private http: HttpClient) { }

  putPlato(id: number, plato: any): Observable<any> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.put<any>(url, plato);
  }

  addPlato(plato: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, plato);
  }

  // Método para obtener un plato por ID (opcional)
  getPlato(id: number): Observable<any> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.get<any>(url);
  }

  // Método para obtener todos los platos
  getPlatos(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  // Método para borrar un plato
  deletePlato(id: number): Observable<any> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.delete<any>(url);
  }
}
