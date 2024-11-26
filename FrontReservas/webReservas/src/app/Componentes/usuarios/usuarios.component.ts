import { Component, OnInit } from '@angular/core';
import { User } from '../../Model/user.model';
import { UsuariosServiceService } from '../../Servicios/usuarios/usuarios-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-usuarios',
  templateUrl: './usuarios.component.html',
  styleUrl: './usuarios.component.scss'
})
export class UsuariosComponent implements OnInit {

  usuarios: User[] = [];

  constructor(private usuariosService: UsuariosServiceService, private router: Router) { }

  ngOnInit(): void {
    this.loadUsuarios();
  }

  loadUsuarios(): void {
    this.usuariosService.getUsuarios().subscribe(data => {
      this.usuarios = data;
    });
  }

  confirmDeleteUsuario(id: number): void {
    const confirmed = window.confirm('¿Estás seguro de que deseas eliminar este usuario?');
    if (confirmed) {
      this.deleteUsuario(id);
    }
  }

  deleteUsuario(id: number): void {
    this.usuariosService.deleteUsuario(id).subscribe(() => {
      this.loadUsuarios();
    });
  }

    addUsuario(): void {
      this.router.navigate(['/user/form']);
    }
  
    editUsuario(id: number): void {
      this.router.navigate(['/user/form', id]);
    }
}
