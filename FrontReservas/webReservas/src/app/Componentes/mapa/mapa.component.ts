import { Component, OnInit } from '@angular/core';
import { MesasService } from '../../Servicios/mesas/mesas.service';
import { AuthService } from '../../Servicios/AuthService/auth-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-mapa',
  templateUrl: './mapa.component.html',
  styleUrls: ['./mapa.component.scss']
})
export class MapaComponent implements OnInit {
  mesas: any[] = [];
  isAdmin: boolean = false;
  usuarioActualId: number | null = null;

  constructor(
    private mesaService: MesasService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadMesas();

    this.authService.isLoggedIn().subscribe((isLoggedIn) => {
      if (isLoggedIn) {
        this.authService.getRole().subscribe(role => {
          this.isAdmin = role?.toLowerCase() === 'admin';
        });

        this.usuarioActualId = this.authService.getUserIdFromToken();
        console.log("ID del usuario logueado:", this.usuarioActualId);
      } else {
        this.isAdmin = false;
        this.usuarioActualId = null;
      }
    });
  }

  loadMesas(): void {
    this.mesaService.getMesas().subscribe(
      (data) => {
        this.mesas = data;
        console.log('Mesas cargadas:', this.mesas);
      },
      (error) => {
        console.error('Error al obtener mesas', error);
      }
    );
  }

  addMesa(): void {
    if (this.isAdmin) {
      this.mesaService.addMesa().subscribe(
        (nuevaMesa) => {
          this.mesas.push(nuevaMesa);
        },
        (error) => {
          console.error('Error al añadir mesa', error);
        }
      );
    } else {
      console.error('No tienes permisos para añadir una mesa.');
    }
  }

  actualizarMesa(mesa: any): void {
    if (this.usuarioActualId === null) {
      alert('Necesitas iniciar sesión o registrarte para acceder a esta mesa.');
      return;
    }
    
    if (this.isAdmin || !mesa.reservation || mesa.reservation.user.id === this.usuarioActualId) {
      this.router.navigate(['/reservas/reservar-mesa', mesa.numeroMesa]);
    } else {
      console.warn('No tienes permiso para acceder a esta mesa.');
      alert('Esta mesa ya está reservada.');
    }
  }
}
