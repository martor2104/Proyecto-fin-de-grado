import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PlatosService {

  private apiUrl = 'http://localhost:8080/api/plates'; 

  constructor(private http: HttpClient) { }

  getPlatos() {
    return this.http.get<any[]>(this.apiUrl);
  }
}
