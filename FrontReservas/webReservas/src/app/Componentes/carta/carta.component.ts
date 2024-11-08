import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PlatosService } from '../../Servicios/platos/platos.service';
import { AuthService } from '../../Servicios/AuthService/auth-service.service';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-carta',
  templateUrl: './carta.component.html',
  styleUrls: ['./carta.component.scss']
})
export class CartaComponent implements OnInit {
  platos: any[] = [];
  isAdmin: boolean = false;

  constructor(private router: Router, private platoService: PlatosService, private authService: AuthService) {}

  ngOnInit(): void {
    // Verificación del rol del usuario
    this.authService.getRole().subscribe(role => {
      console.log('User role:', role);
      this.isAdmin = role?.toLowerCase() === 'admin';
    });

    this.loadPlatos();
  }

  // Cargar los platos desde el servicio
  loadPlatos(): void {
    this.platoService.getPlatos().subscribe(platos => {
      this.platos = platos;
    });
  }

  // Navegar al formulario de agregar plato
  openAddPlatoForm(): void {
    this.router.navigate(['/plate/form']);
  }

  // Navegar al formulario de editar plato
  openEditPlatoForm(plato: any): void {
    this.router.navigate(['/plate/form', plato.id]);
  }

  // Borrar plato desde el servicio
  borrarPlato(plato: any): void {
    this.platoService.deletePlato(plato.id).pipe(
      catchError(error => {
        if (error.status === 403) {
          console.error('Acceso denegado. No tienes permisos para borrar este plato.');
          alert('No tienes permisos para realizar esta acción.');
        }
        return throwError(error);
      })
    ).subscribe(() => {
      this.loadPlatos();
    });
  }
}