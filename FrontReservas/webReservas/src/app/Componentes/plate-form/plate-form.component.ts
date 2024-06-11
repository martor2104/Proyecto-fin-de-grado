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
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id !== null) {
        this.isEditMode = true;
        this.editingPlatoId = +id;
        this.plateService.getPlato(this.editingPlatoId).subscribe(plato => {
          this.platoForm.patchValue(plato);
        });
      } else {
        this.isEditMode = false;
        this.editingPlatoId = null;
      }
    });
  }

  onSubmit(): void {
    if (this.platoForm.valid) {
      if (this.isEditMode && this.editingPlatoId !== null) {
        this.plateService.putPlato(this.editingPlatoId, this.platoForm.value).subscribe(response => {
          this.router.navigate(['/carta']);
        });
      } else {
        this.plateService.addPlato(this.platoForm.value).subscribe(response => {
          this.router.navigate(['/carta']);
        });
      }
    }
  }
}
