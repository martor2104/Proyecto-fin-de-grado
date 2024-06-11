import { Component, OnInit } from '@angular/core';
import { MesasService } from '../../Servicios/mesas/mesas.service';

@Component({
  selector: 'app-mesas-disponibles',
  templateUrl: './mesas-disponibles.component.html',
  styleUrl: './mesas-disponibles.component.scss'
})
export class MesasDisponiblesComponent implements OnInit {
  mesasDisponibles: any[] | undefined;
  mesasReservadas: any[] | undefined;

  constructor(private mesaService: MesasService) {}

  ngOnInit(): void {
    this.getMesaData();
  }

  getMesaData(): void {
    this.mesaService.getMesas().subscribe(data => {
      // Filtrar mesas disponibles y reservadas
      this.mesasDisponibles = data.filter((table: any) => table.tableStatus === 'PENDING');
      this.mesasReservadas = data.filter((table: any) => table.tableStatus === 'RESERVED');
    });
  }
}
