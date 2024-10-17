import { Component, OnInit } from '@angular/core';
import { Mesa } from '../../Model/mesa.model';
import { TableStatus } from '../../Model/tableStatus.model';
import { MesasService } from '../../Servicios/mesas/mesas.service';

@Component({
  selector: 'app-mapa',
  templateUrl: './mapa.component.html',
  styleUrl: './mapa.component.scss'
})
export class MapaComponent implements OnInit{
  
  mesas: any[] = []; // Array para almacenar las mesas obtenidas del backend

  constructor(private mesaService: MesasService) { }

  ngOnInit(): void {
    // Llamamos al servicio para obtener las mesas del backend
    this.mesaService.getMesas().subscribe(
      (data) => {
        this.mesas = data; // Guardamos las mesas en el array
      },
      (error) => {
        console.error('Error al obtener mesas', error);
      }
    );
  }
}
