import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../Servicios/AuthService/auth-service.service';  // Asegúrate de que la ruta es correcta

@Component({
  selector: 'app-reservar-mesa',
  templateUrl: './reservar-mesa.component.html',
  styleUrls: ['./reservar-mesa.component.scss']
})
export class ReservarMesaComponent implements OnInit {
  tableNumber!: number;
  reservationDate!: string;
  tableNumbers: number[] = [];

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.tableNumbers = Array.from({ length: 17 }, (_, i) => i + 1); // Genera un array del 1 al 17
  }

  onSubmit() {
    this.authService.isLoggedIn().subscribe(isLoggedIn => {
      if (isLoggedIn) {
        this.authService.getUserId().subscribe(userId => {
          if (userId) {
            const reservation = {
              userId: userId,
              tableNumber: this.tableNumber,
              reservationDate: this.reservationDate
            };
            console.log('Reserva enviada:', reservation);
            // Aquí enviarías la reserva al servidor
          } else {
            alert('Error: No se pudo obtener el ID de usuario');
          }
        });
      } else {
        alert('Error: No estás loggeado');
      }
    });
  }
}
