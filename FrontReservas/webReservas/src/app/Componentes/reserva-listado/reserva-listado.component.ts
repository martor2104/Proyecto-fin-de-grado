import { Component, OnInit } from '@angular/core';
import { ReservasService } from '../../Servicios/reservas/reservas.service';
import { Mesa } from '../../Model/mesa.model';
import { Reservation } from '../../Model/reservation.model';

interface ReservaDetalle {
  usuarioNombre: string;
  fecha: Date;
  numeroMesa: number | null;
  tableStatus: string | null;
}

@Component({
  selector: 'app-reserva-listado',
  templateUrl: './reserva-listado.component.html',
  styleUrls: ['./reserva-listado.component.scss']
})
export class ReservaListadoComponent implements OnInit {
  reservas: ReservaDetalle[] = [];
  errorMessage: string = '';

  constructor(private reservasService: ReservasService) { }

  ngOnInit(): void {
    this.loadReservas();
  }

  loadReservas(): void {
    this.reservasService.getReservas().subscribe(
      reservas => {
        this.reservasService.getMesas().subscribe(
          mesas => {
            this.reservas = reservas.map((reserva: Reservation) => {
              const mesa = mesas.find((m: Mesa) => m.reservation && m.reservation.id === reserva.id);
              return {
                usuarioNombre: reserva.user ? reserva.user.name : 'N/A',
                fecha: reserva.reservationDate ? new Date(reserva.reservationDate) : new Date(),
                numeroMesa: mesa ? mesa.id : null,
                tableStatus: mesa ? mesa.tableStatus : 'UNKNOWN'
              };
            });
          },
          error => this.errorMessage = `Error al cargar las mesas: ${error.message}`
        );
      },
      error => this.errorMessage = `Error al cargar las reservas: ${error.message}`
    );
  }
}  
