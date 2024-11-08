import { Component, OnInit } from '@angular/core';
import { User } from '../../Model/user.model';
import { UsuariosServiceService } from '../../Servicios/usuarios/usuarios-service.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-usuarios-form',
  templateUrl: './usuarios-form.component.html',
  styleUrls: ['./usuarios-form.component.scss']
})
export class UsuariosFormComponent implements OnInit {

  usuario: User = {
    id: null, 
    name: '',
    email: '',
    password: '',
    userRol: null
  };

  isEditMode: boolean = false; // Indicador de si es modo edición

  constructor(
    private usuariosService: UsuariosServiceService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // Comprueba si hay un ID en la ruta para determinar si es una edición
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.loadUsuario(+id); // Carga los datos del usuario si hay un ID
    }
  }

  // Método para cargar los datos del usuario cuando estamos en modo edición
  loadUsuario(id: number): void {
    this.usuariosService.getUsuario(id).subscribe(
      (data) => {
        this.usuario = data; 
      },
      (error) => {
        console.error('Error al cargar usuario:', error);
      }
    );
  }

  // Método para manejar el envío del formulario
  onSubmit(): void {
    if (this.isEditMode) {
      this.updateUsuario(); 
    } else {
      this.addUsuario(); 
    }
  }

  // Método para añadir un nuevo usuario
  addUsuario(): void {
    this.usuariosService.addUsuario(this.usuario).subscribe(
      () => {
        this.router.navigate(['/usuarios']); 
      },
      (error) => {
        console.error('Error al añadir usuario:', error);
      }
    );
  }

  // Método para actualizar un usuario existente
  updateUsuario(): void {
    if (this.usuario.id !== null) {
      this.usuariosService.updateUsuario(this.usuario.id, this.usuario).subscribe(
        () => {
          this.router.navigate(['/usuarios']); 
        },
        (error) => {
          console.error('Error al actualizar usuario:', error);
        }
      );
    }
  }
}
