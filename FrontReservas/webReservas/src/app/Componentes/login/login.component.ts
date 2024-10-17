import { Component } from '@angular/core';
import { AuthService } from '../../Servicios/AuthService/auth-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  name: string = '';
  password: string = '';
  showAlert: boolean = false;
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin(): void {
    const loginRequest = {
      name: this.name,
      password: this.password
    };

    this.authService.login(loginRequest).subscribe(
      response => {
        console.log('Login exitoso', response);
        this.router.navigate(['/carta']);  // Redirige a la carta después del login
      },
      error => {
        console.error('Error de login', error);
        this.showAlert = true;
        this.errorMessage = 'El usuario o la contraseña son erróneos';
        setTimeout(() => {
          this.showAlert = false;
        }, 3000);
      }
    );
  }
}
