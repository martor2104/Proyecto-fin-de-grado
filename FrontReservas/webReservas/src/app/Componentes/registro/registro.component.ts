import { Component } from '@angular/core';
import { RegistroService } from '../../Servicios/registro/registro.service';

@Component({
  selector: 'app-registro',
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.scss']
})
export class RegistroComponent {
  registro: any = {
    name: '',
    email: '',
    password: '',
    role: 'USER'
  };

  constructor(private registroService: RegistroService) { }

  onSubmit() {
    console.log('Datos de registro enviados:', this.registro);

    this.registroService.registrarUsuario(this.registro).subscribe(
      response => {
        console.log('Registro exitoso', response);
      },
      error => {
        console.error('Error al registrar:', error);
      }
    );
  }
}
