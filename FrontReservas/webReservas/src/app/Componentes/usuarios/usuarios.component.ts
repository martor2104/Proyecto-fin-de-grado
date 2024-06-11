import { Component, OnInit } from '@angular/core';
import { UserDTO } from '../../Model/usuario.model';
import { UsuariosServiceService } from '../../Servicios/usuarios/usuarios-service.service';

@Component({
  selector: 'app-usuarios',
  templateUrl: './usuarios.component.html',
  styleUrl: './usuarios.component.scss'
})
export class UsuariosComponent implements OnInit {
  usuarios: UserDTO[] = [];

  constructor(private usuariosService: UsuariosServiceService) { }

  ngOnInit(): void {
    this.loadUsuarios();
  }

  loadUsuarios(): void {
    this.usuariosService.getUsuarios().subscribe(data => {
      this.usuarios = data;
    });
  }

  deleteUsuario(id: number): void {
    this.usuariosService.deleteUsuario(id).subscribe(() => {
      this.loadUsuarios();
    });
  }
}
