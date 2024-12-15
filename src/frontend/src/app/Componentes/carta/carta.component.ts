import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PlatosService } from '../../Servicios/platos/platos.service';
import { AuthService } from '../../Servicios/AuthService/auth-service.service';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

interface Categoria {
  nombre: string;
  platos: any[]; // Define 'any' o usa una interfaz específica si tienes un tipo de plato
}

@Component({
  selector: 'app-carta',
  templateUrl: './carta.component.html',
  styleUrls: ['./carta.component.scss']
})
export class CartaComponent implements OnInit {
  categorias: Categoria[] = []; // Lista de categorías dinámica
  isAdmin: boolean = false;
  isAddingCategory: boolean = false; // Estado para mostrar el formulario de categoría
  newCategoryName: string = ''; // Nombre de la nueva categoría

  constructor(
    private router: Router,
    private platoService: PlatosService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.checkUserRole();
    this.loadPlatos();
  }

  private checkUserRole(): void {
    this.authService.getRole().subscribe(
      role => {
        this.isAdmin = role?.toLowerCase() === 'admin';
      },
      () => {
        this.isAdmin = false; // Usuario no autenticado
      }
    );
  }
  

  private loadPlatos(): void {
    this.categorias = []; // Limpiar las categorías antes de recargar los platos
  
    this.platoService.getPlatos().subscribe((platos: any[]) => {
      platos.forEach(plato => {
        // Ajustar la URL de la imagen para cada plato
        if (plato.img) {
          plato.img = `/Images/${plato.img}`; // Construir la ruta completa
        }
  
        // Buscar si la categoría del plato ya existe en `categorias`
        let categoria = this.categorias.find(cat => cat.nombre === plato.category);
  
        // Si no existe, agregar la nueva categoría con el plato
        if (!categoria) {
          categoria = { nombre: plato.category, platos: [] };
          this.categorias.push(categoria);
        }
  
        // Añadir el plato a la categoría correspondiente
        categoria.platos.push(plato);
      });
    });
  }
  
  

  toggleAddCategoryForm(): void {
    this.isAddingCategory = !this.isAddingCategory;
  }

  addCategory(): void {
    if (this.newCategoryName.trim()) {
      // Agregar la nueva categoría a la lista de categorías
      this.categorias.push({ nombre: this.newCategoryName.trim(), platos: [] });
      this.newCategoryName = '';
      this.isAddingCategory = false;
    }
  }

  openAddPlatoForm(categoria: string): void {
    this.router.navigate(['/plate/form'], { queryParams: { category: categoria } });
  }

  openEditPlatoForm(plato: any): void {
    this.router.navigate(['/plate/form', plato.id]);
  }

  borrarPlato(plato: any): void {
    this.platoService.deletePlato(plato.id).pipe(
      catchError(error => {
        if (error.status === 403) {
          alert('No tienes permisos para realizar esta acción.');
        }
        return throwError(error);
      })
    ).subscribe(() => {
      this.loadPlatos();
    });
  }
}
