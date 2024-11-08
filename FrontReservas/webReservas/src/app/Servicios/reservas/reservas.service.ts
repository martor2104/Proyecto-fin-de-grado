import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Reservation } from '../../Model/reservation.model';
import { Mesa } from '../../Model/mesa.model';

@Injectable({
  providedIn: 'root'
})
export class ReservasService {

  private reservasUrl = 'http://localhost:8080/api/reservations';
  private mesasUrl = 'http://localhost:8080/api/tables';

  constructor(private http: HttpClient) { }

  getReservas(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(this.reservasUrl);
  }

  getMesas(): Observable<Mesa[]> {
    return this.http.get<Mesa[]>(this.mesasUrl);
  }

  getReservationById(reservaId: number): Observable<Reservation> {
    const url = `${this.reservasUrl}/${reservaId}`;
    return this.http.get<Reservation>(url);
  }

  updateReservation(reservation: Reservation): Observable<Reservation> {
    const url = `${this.reservasUrl}/${reservation.id}`;
    return this.http.put<Reservation>(url, reservation);
  }

  crearReserva(reserva: { reservationDate: string }, mesaId: number): Observable<Reservation> {
    return this.http.post<Reservation>(`${this.reservasUrl}/create?mesaId=${mesaId}`, reserva);
  }

  cancelarReserva(reservaId: number): Observable<void> {
    return this.http.delete<void>(`${this.reservasUrl}/${reservaId}`);
  }
  
}
