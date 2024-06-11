import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CartaComponent } from './Componentes/carta/carta.component';
import { ReservasComponent } from './Componentes/reservas/reservas.component';
import { LoginComponent } from './Componentes/login/login.component';
import { RegistroComponent } from './Componentes/registro/registro.component';
import { MapaComponent } from './Componentes/mapa/mapa.component';
import { MesasDisponiblesComponent } from './Componentes/mesas-disponibles/mesas-disponibles.component';
import { ReservarMesaComponent } from './Componentes/reservar-mesa/reservar-mesa.component';
import { PlateFormComponent } from './Componentes/plate-form/plate-form.component';
import { UsuariosComponent } from './Componentes/usuarios/usuarios.component';
import { ReservaListadoComponent } from './Componentes/reserva-listado/reserva-listado.component';

const routes: Routes = [
  { path: 'registro', component: RegistroComponent },
  {path: 'carta', component: CartaComponent},
  {path: 'reservas', component: ReservasComponent},
  {path: 'login', component: LoginComponent},
  { path: 'plate/form/:id', component: PlateFormComponent },
  {path: 'reservas/mapa', component: MapaComponent},
  {path: 'reservas/mesas-disponibles', component: MesasDisponiblesComponent},
  {path: 'reservas/reservar-mesa', component: ReservarMesaComponent},
  {path: 'reservas/listado', component: ReservaListadoComponent},
  {path: 'usuarios', component: UsuariosComponent},
  {path: 'plate/form', component: PlateFormComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
