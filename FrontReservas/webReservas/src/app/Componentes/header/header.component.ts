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
  isAdmin: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    // Verificar si el usuario está logueado
    this.authService.isLoggedIn().subscribe(loggedIn => {
      this.isLoggedIn = loggedIn;
    });

    // Obtener el nombre de usuario
    this.authService.getUsername().subscribe(username => {
      this.username = username;
    });

    // Verificación del rol de admin
    this.authService.getRole().subscribe(role => {
      console.log('User role:', role);
      this.isAdmin = role?.toLowerCase() === 'admin'; 
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
