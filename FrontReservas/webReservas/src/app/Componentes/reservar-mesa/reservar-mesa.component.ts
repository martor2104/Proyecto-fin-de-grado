import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MesasService } from '../../Servicios/mesas/mesas.service';
import { ReservasService } from '../../Servicios/reservas/reservas.service';
import { AuthService } from '../../Servicios/AuthService/auth-service.service'; // Importar AuthService
import { Mesa } from '../../Model/mesa.model';
import { Reservation } from '../../Model/reservation.model';

@Component({
  selector: 'app-reservar-mesa',
  templateUrl: './reservar-mesa.component.html',
  styleUrls: ['./reservar-mesa.component.scss']
})
export class ReservarMesaComponent implements OnInit {

  mesa: Mesa | undefined;
  numeroMesa: number | null = null;  
  reservationDate: string = '';  
  mensaje: string = '';  
  isAdmin: boolean = false; 
  usuarioActualId: number | null = null; 

  constructor(
    private route: ActivatedRoute,
    private mesasService: MesasService,
    private reservasService: ReservasService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Obtener el ID del usuario logueado
    this.usuarioActualId = this.authService.getUserIdFromToken();
    console.log("ID del usuario logueado:", this.usuarioActualId);

    // Obtener el rol del usuario logueado para determinar si es administrador
    this.authService.getRole().subscribe(
      (role) => {
        this.isAdmin = role?.toLowerCase() === 'admin';
        console.log("Rol del usuario logueado:", role, "isAdmin:", this.isAdmin);
      },
      (error) => {
        console.error('Error al obtener el rol del usuario:', error);
      }
    );

    // Capturar el número de la mesa desde la URL
    this.numeroMesa = Number(this.route.snapshot.paramMap.get('numeroMesa'));

    if (this.numeroMesa) {
      // Cargar los detalles de la mesa
      this.mesasService.getMesaByNumero(this.numeroMesa).subscribe(
        (mesa: Mesa) => {
          this.mesa = mesa;
          console.log("Datos completos de la mesa:", this.mesa);

          // Verificar si `reservation` y `user` están presentes en la mesa cargada
          if (!this.mesa.reservation) {
            console.warn("La mesa no tiene una reserva asociada.");
          } else if (!this.mesa.reservation.user) {
            console.warn("La reserva no tiene un usuario asociado.");
          } else {
            console.log("ID del usuario en la reserva:", this.mesa.reservation.user.id);
          }
        },
        (error) => {
          console.error('Error al cargar la mesa:', error);
          this.mensaje = 'Error al cargar la mesa.';
        }
      );
    }
  }
  
  

  reservarMesa(): void {
    if (!this.reservationDate) {
      this.mensaje = 'Por favor, seleccione una fecha para la reserva.';
      return;
    }

    if (this.mesa) {
      const nuevaReserva: Reservation = {
        reservationDate: this.reservationDate
      };

      this.reservasService.crearReserva(nuevaReserva, this.mesa.id).subscribe(
        (response) => {
          this.mensaje = 'Mesa reservada con éxito.';
          this.router.navigate(['/reservas/mapa']);
        },
        (error) => {
          console.error('Error al reservar la mesa:', error);
          this.mensaje = 'Error al reservar la mesa.';
        }
      );
    }
  }

  cancelarReserva(): void {
    if (this.isAdmin || (this.mesa?.reservation?.user?.id === this.usuarioActualId)) {
      if (confirm('¿Estás seguro de que deseas cancelar esta reserva?')) {
        this.reservasService.cancelarReserva(this.mesa!.reservation!.id!).subscribe(
          () => {
            this.mensaje = 'Reserva cancelada con éxito.';
            this.mesa!.reservation = null; // Eliminar la reserva de la mesa en el frontend
          },
          (error) => {
            console.error('Error al cancelar la reserva', error);
            this.mensaje = 'Error al cancelar la reserva.';
          }
        );
      }
    } else {
      console.error("No tienes permisos para cancelar esta reserva.");
      this.mensaje = 'No tienes permisos para cancelar esta reserva.';
    }
  }
  
  
  
  

  borrarMesa(): void {
    if (this.mesa && confirm('¿Estás seguro de que deseas borrar esta mesa?')) {
      this.mesasService.deleteMesa(this.mesa.id).subscribe(
        () => {
          this.mensaje = 'Mesa borrada con éxito.';
          this.router.navigate(['/mesas']);
        },
        (error) => {
          console.error('Error al borrar la mesa:', error);
          this.mensaje = 'Error al borrar la mesa.';
        }
      );
    }
  }
}
