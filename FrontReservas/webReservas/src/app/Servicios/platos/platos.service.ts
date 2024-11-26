import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';

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

  // Método para obtener un plato por ID
  getPlato(id: number): Observable<any> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.get<any>(url);
  }

  // Método para obtener todos los platos
  getPlatos(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }


  deletePlato(id: number): Observable<any> {
    const url = `${this.apiUrl}/${id}`;
    const token = localStorage.getItem('token');
  
    console.log('Token JWT:', token);
  
    
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  
    return this.http.delete<any>(url, { headers }); 
  }

  addPlateWithImage(formData: FormData): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}` // Solo añade el token
    });
  
    return this.http.post(`${this.apiUrl}`, formData, { headers });
  }
  
  
  updatePlateWithImage(id: number, formData: FormData): Observable<any> {
    const token = localStorage.getItem('token');
    if (!token) {
      console.error('Token no encontrado');
      return throwError(() => new Error('Token no encontrado'));
    }
  
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  
    return this.http.put(`${this.apiUrl}/${id}`, formData, { headers });
  }
  
  

}
