import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PlatosService } from '../../Servicios/platos/platos.service';

@Component({
  selector: 'app-plate-form',
  templateUrl: './plate-form.component.html',
  styleUrls: ['./plate-form.component.scss']
})
export class PlateFormComponent implements OnInit {
  platoForm: FormGroup;
  isEditMode = false;
  editingPlatoId: number | null = null;
  selectedFile: File | null = null;
  categoria: string = ''; // Variable para almacenar la categoría

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private plateService: PlatosService
  ) {
    this.platoForm = this.fb.group({
      namePlate: ['', Validators.required],
      description: ['', Validators.required],
      price: ['', [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    // Obtener el parámetro de categoría desde queryParams
    this.route.queryParams.subscribe(params => {
      this.categoria = params['category'] || ''; // Almacena la categoría
    });

    // Comprobar si estamos en modo edición
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id !== null) {
        this.isEditMode = true;
        this.editingPlatoId = +id;
        this.plateService.getPlato(this.editingPlatoId).subscribe(plato => {
          this.platoForm.patchValue(plato);
          this.categoria = plato.category; // Configurar la categoría del plato en edición
        });
      } else {
        this.isEditMode = false;
        this.editingPlatoId = null;
      }
    });
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0] || null;
  }

  onSubmit(): void {
    if (this.platoForm.valid) {
      const formData = new FormData();
      const entries = (formData as any).entries();
      for (const [key, value] of entries) {
        console.log("<asdg" + `${key}: ${value}`);
      }
  
      const plateData = {
        namePlate: this.platoForm.get('namePlate')?.value,
        description: this.platoForm.get('description')?.value,
        price: this.platoForm.get('price')?.value,
        category: this.categoria
      };
  
      formData.append('plate', JSON.stringify(plateData));
  
      if (this.selectedFile) {
        formData.append('image', this.selectedFile, this.selectedFile.name);
      }
  
      if (this.isEditMode && this.editingPlatoId !== null) {
        this.plateService.updatePlateWithImage(this.editingPlatoId, formData).subscribe(
          () => this.router.navigate(['/carta']),
          error => console.error('Error al actualizar el plato:', error)
        );
      } else {
        this.plateService.addPlateWithImage(formData).subscribe(
          () => this.router.navigate(['/carta']),
          error => console.error('Error al enviar el formulario:', error)
        );
      }
    }
  }
  
  
  
}
