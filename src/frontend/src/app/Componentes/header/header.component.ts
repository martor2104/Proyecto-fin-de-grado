import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../Servicios/AuthService/auth-service.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  isLoggedIn: boolean = false;
  username: string | null = '';
  isAdmin: boolean = false; // Verifica si el usuario es administrador
  profileImageUrl: string | null = 'assets/utilities/sinPerfil.png'; // Imagen predeterminada
  userId: number | null = null; // ID del usuario logueado

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.authService.isLoggedIn().subscribe(loggedIn => {
      this.isLoggedIn = loggedIn;
      if (loggedIn) {
        this.loadUserData();
      }
    });
  }

  loadUserData(): void {
    // Obtener el nombre de usuario
    this.authService.getUsername().subscribe(username => {
      this.username = username;
    });

    // Verificación del rol de admin
    this.authService.getRole().subscribe(role => {
      this.isAdmin = role?.toLowerCase() === 'admin';
    });

    // Obtener la imagen de perfil
    this.authService.getProfileImage().subscribe(imageUrl => {
      console.log("Profile Image URL in HeaderComponent:", imageUrl); // Verificar la URL de la imagen de perfil
      
      // Asignar la URL de la imagen o la imagen predeterminada
      this.profileImageUrl = imageUrl
  ? `assets/Images/${imageUrl}`  // Accede a la imagen de manera relativa
  : 'assets/utilities/sinPerfil.png'; // Imagen predeterminada si no hay imagen
    });

    // Obtener el ID del usuario logueado
    this.authService.getUserId().subscribe(id => {
      this.userId = id;
    });
  }

  // Método para redirigir al formulario de edición del perfil del usuario
  goToProfile(): void {
    if (this.userId !== null) {
      this.router.navigate([`/user/form/${this.userId}`]);
    }
  }

  // Método para cerrar sesión
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
