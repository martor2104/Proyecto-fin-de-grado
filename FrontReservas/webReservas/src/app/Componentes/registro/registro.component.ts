import { Component } from '@angular/core';
import { AuthService } from '../../Servicios/AuthService/auth-service.service';

@Component({
  selector: 'app-registro',
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.scss']
})
export class RegistroComponent {
  registro: any = {
    name: '',
    email: '',
    password: '',
    role: 'USER'
  };

  constructor(private authService: AuthService) { }

  onSubmit() {
    console.log('Datos de registro enviados:', this.registro);

    this.authService.registrarUsuario(this.registro).subscribe(
      response => {
        console.log('Registro exitoso', response);
      },
      error => {
        console.error('Error al registrar:', error);
      }
    );
  }
}
