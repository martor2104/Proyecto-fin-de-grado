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
        this.mesas = data.sort((a: { numeroMesa: number; }, b: { numeroMesa: number; }) => a.numeroMesa - b.numeroMesa);
        console.log('Mesas cargadas:', this.mesas);
      },
      (error) => {
        console.error('Error al obtener mesas', error);
      }
    );
  }

  addMesa(): void {
    const numeroMesa = prompt('Introduce el número de la nueva mesa:');
  
    if (numeroMesa !== null && numeroMesa.trim() !== '') {
      const numeroMesaInt = parseInt(numeroMesa, 10);
  
      if (isNaN(numeroMesaInt) || numeroMesaInt <= 0) {
        alert('Por favor, introduce un número válido para la mesa.');
        return;
      }
  
      // Crear un objeto con la propiedad `numeroMesa`
      const mesaData = { numeroMesa: numeroMesaInt };
  
      this.mesaService.addMesa(mesaData).subscribe(
        (nuevaMesa) => {
          this.mesas.push(nuevaMesa);
          this.mesas.sort((a, b) => a.numeroMesa - b.numeroMesa);
        },
        (error) => {
          console.error('Error al añadir mesa', error);
          alert(error.error || 'Hubo un error al añadir la mesa.');
        }
      );
    } else {
      console.log('La operación de añadir mesa fue cancelada.');
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

  getMesaImage(mesa: any): string {
    if (mesa.reservation) {
      return mesa.reservation.user.id === this.usuarioActualId
        ? 'assets/utilities/mesa_verde_RESPLANDOR.png'
        : 'assets/utilities/mesa_roja_RESPLANDOR.png';
    }
    return 'assets/utilities/MESA_CHULI_ORIGINAL.png';
  }
}
