import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PlatosService } from '../../Servicios/platos/platos.service';

@Component({
  selector: 'app-carta',
  templateUrl: './carta.component.html',
  styleUrls: ['./carta.component.scss']
})
export class CartaComponent implements OnInit {
  platos: any[] = [];

  constructor(private router: Router, private platoService: PlatosService) {}

  ngOnInit(): void {
    this.loadPlatos();
  }

  loadPlatos(): void {
    this.platoService.getPlatos().subscribe(platos => {
      this.platos = platos;
    });
  }

  openAddPlatoForm(): void {
    this.router.navigate(['/plate/form']);
  }

  openEditPlatoForm(plato: any): void {
    this.router.navigate(['/plate/form', plato.id]);
  }

  borrarPlato(plato: any): void {
    this.platoService.deletePlato(plato.id).subscribe(response => {
      this.loadPlatos();
    });
  }
}