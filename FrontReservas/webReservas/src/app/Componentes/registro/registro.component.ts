import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../Servicios/AuthService/auth-service.service';

@Component({
  selector: 'app-registro',
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.scss']
})
export class RegistroComponent implements OnInit {
  registro: any = {
    name: '',
    email: '',
    password: '',
    role: 'USER'
  };
  
  isAdmin: boolean = false;

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    // Obtener el rol del usuario logueado y verificar si es administrador
    this.authService.getRole().subscribe(
      (role) => {
        this.isAdmin = role === 'ADMIN';
      },
      (error) => {
        console.error('Error al obtener el rol del usuario:', error);
      }
    );
  }

  onSubmit() {
    console.log('Datos de registro enviados:', this.registro);

    // Asegurarse de que solo los administradores puedan enviar role 'ADMIN'
    if (!this.isAdmin) {
      this.registro.role = 'USER';  // Forzar el rol a USER si no es administrador
    }

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
