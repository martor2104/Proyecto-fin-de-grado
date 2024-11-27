import { Component, OnInit } from '@angular/core';
import { User } from '../../Model/user.model';
import { UsuariosServiceService } from '../../Servicios/usuarios/usuarios-service.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../Servicios/AuthService/auth-service.service';

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
    userRol: null,
    perfil: ''
  };

  selectedFile: File | null = null;
  isEditMode: boolean = false;
  

  constructor(
    private usuariosService: UsuariosServiceService,
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.loadUsuario(+id); 
    }
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0] || null;
  }

  loadUsuario(id: number): void {
    this.usuariosService.getUsuario(id).subscribe(
      (data) => {
        this.usuario = data;
  
        
        this.usuario.password = ''; 
      },
      (error) => {
        console.error('Error al cargar usuario:', error);
      }
    );
  }
  
  

  onSubmit(): void {
    const formData = new FormData();
    
    // Convertir el objeto usuario a JSON y añadirlo al FormData
    const userBlob = new Blob([JSON.stringify(this.usuario)], { type: 'application/json' });
    formData.append('user', userBlob);
    
    // Añadir la imagen de perfil si está presente
    if (this.selectedFile) {
      formData.append('image', this.selectedFile, this.selectedFile.name);
    }
  
    if (this.isEditMode) {
      this.updateUsuario(formData);
    } else {
      this.addUsuario(formData);
    }
  }  

  addUsuario(formData: FormData): void {
    this.usuariosService.addUsuario(formData).subscribe(
      () => this.router.navigate(['/usuarios']),
      (error) => console.error('Error al añadir usuario:', error)
    );
  }

  updateUsuario(formData: FormData): void {
    if (this.usuario.id !== null) {
      this.usuariosService.updateUsuario(this.usuario.id, formData).subscribe(
        () => {
          const usuarioActualId = this.authService.getUserIdFromToken(); 
          if (this.usuario.id === usuarioActualId) {
            window.alert('Usuario actualizado correctamente. Por favor, vuelve a iniciar sesión.');
            this.logoutAndRedirect(); 
          } else {
            window.alert('Usuario actualizado correctamente.');
          }
        },
        (error) => console.error('Error al actualizar usuario:', error)
      );
    }
  }
  
  
 
  logoutAndRedirect(): void { 
    localStorage.clear(); 
    this.router.navigate(['/login']); 
  }
  
}