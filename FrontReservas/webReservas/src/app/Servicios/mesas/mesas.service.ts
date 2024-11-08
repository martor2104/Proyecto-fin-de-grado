import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Mesa } from '../../Model/mesa.model';

@Injectable({
  providedIn: 'root'
})
export class MesasService {

  private apiURL = 'http://localhost:8080/api/tables';

  constructor(private http: HttpClient) {}

  getMesas(): Observable<any> {
    return this.http.get<any>(`${this.apiURL}`);
  }

  addMesa(): Observable<Mesa> {
    const nuevaMesa = {
      tableStatus: 'PENDING',
      reservation: null
    };

    return this.http.post<Mesa>(this.apiURL, nuevaMesa);
  }

  getMesaByNumero(numeroMesa: number): Observable<Mesa> {
    const url = `${this.apiURL}/numero/${numeroMesa}`;
    return this.http.get<Mesa>(url);
  }

  updateMesa(mesa: Mesa): Observable<Mesa> {
    return this.http.put<Mesa>(`${this.apiURL}/${mesa.id}`, mesa);
  }

  deleteMesa(id: number): Observable<any> {
    const url = `${this.apiURL}/delete/${id}`;
    return this.http.delete(url);
  }
}
