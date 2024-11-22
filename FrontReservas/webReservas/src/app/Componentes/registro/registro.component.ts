import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../Servicios/AuthService/auth-service.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { catchError } from 'rxjs/operators';

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
  nombreUsuarioRepetido: boolean = false;
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
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
  
    if (!this.isAdmin) {
      this.registro.role = 'USER';
    }
  
    // Enviar la solicitud al backend para registrar el usuario
    this.authService.registrarUsuario(this.registro).subscribe(
      response => {
        console.log('Registro exitoso', response);
  
        // Iniciar sesión automáticamente después del registro
        this.authService.login({ name: this.registro.name, password: this.registro.password }).subscribe(
          loginResponse => {
            console.log('Inicio de sesión automático exitoso', loginResponse);
            this.router.navigate(['/']);
          },
          loginError => {
            console.error('Error al iniciar sesión automáticamente:', loginError);
            this.errorMessage = 'Error al iniciar sesión. Por favor, intenta ingresar manualmente.';
          }
        );
      },
      error => {
        // Manejar errores específicos del backend
        if (error.status === 400) {
          if (error.error?.error === 'The username is already taken') {
            this.errorMessage = 'El nombre de usuario ya está registrado.';
          } else if (error.error?.error === 'The email is already registered') {
            this.errorMessage = 'El correo electrónico ya está registrado.';
          } else {
            this.errorMessage = 'Error al registrar: ' + (error.error?.error || 'Error desconocido');
          }
        } else {
          console.error('Error al registrar:', error);
          this.errorMessage = 'Error interno del servidor. Intenta nuevamente más tarde.';
        }
      }
    );
  }
  
  

  validarNombreUsuario() {
    if (!this.registro.name) return;

    this.authService.verificarNombreUsuario(this.registro.name).pipe(
      catchError((error) => {
        console.error('Error al verificar el nombre de usuario:', error);
        return of(false); // Considera como disponible en caso de error
      })
    ).subscribe((existe: boolean) => {
      this.nombreUsuarioRepetido = existe;
    });
  }

  validarContrasena(password: string): boolean {
    const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{5,}$/;
    return regex.test(password);
  }
}
