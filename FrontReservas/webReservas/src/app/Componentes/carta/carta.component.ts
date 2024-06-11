import { Component, OnInit } from '@angular/core';
import { PlatosService } from '../../Servicios/platos/platos.service';

@Component({
  selector: 'app-carta',
  templateUrl: './carta.component.html',
  styleUrls: ['./carta.component.scss']
})
export class CartaComponent implements OnInit {
  platos: any[] = [];

  constructor(private platosService: PlatosService) { }

  ngOnInit(): void {
    this.platosService.getPlatos().subscribe(
      data => {
        this.platos = data;
      },
      error => {
        console.error('Error al obtener platos:', error);
      }
    );
  }
}
