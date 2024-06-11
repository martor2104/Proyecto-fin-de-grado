import { Component, OnInit } from '@angular/core';
import { ReservasService } from '../../Servicios/reservas/reservas.service';
import { Mesa } from '../../Model/mesa.model';
import { Reserva } from '../../Model/reserva.model';

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
            this.reservas = reservas.map((reserva: Reserva) => {
              const mesa = mesas.find((m: Mesa) => m.reservaId === reserva.id);
              return {
                usuarioNombre: reserva.usuario ? reserva.usuario.name : 'N/A',
                fecha: reserva.fecha ? new Date(reserva.fecha) : new Date(),
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
