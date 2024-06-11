import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Reserva } from '../../Model/reserva.model';
import { Mesa } from '../../Model/mesa.model';

@Injectable({
  providedIn: 'root'
})
export class ReservasService {

  private reservasUrl = 'http://localhost:8080/api/reservations';
  private mesasUrl = 'http://localhost:8080/api/tables';

  constructor(private http: HttpClient) { }

  getReservas(): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(this.reservasUrl);
  }

  getMesas(): Observable<Mesa[]> {
    return this.http.get<Mesa[]>(this.mesasUrl);
  }
}
