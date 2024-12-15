import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CartaComponent } from './Componentes/carta/carta.component';
import { ReservasComponent } from './Componentes/reservas/reservas.component';
import { LoginComponent } from './Componentes/login/login.component';
import { RegistroComponent } from './Componentes/registro/registro.component';
import { MapaComponent } from './Componentes/mapa/mapa.component';
import { ReservarMesaComponent } from './Componentes/reservar-mesa/reservar-mesa.component';
import { PlateFormComponent } from './Componentes/plate-form/plate-form.component';
import { UsuariosComponent } from './Componentes/usuarios/usuarios.component';
import { ReservaListadoComponent } from './Componentes/reserva-listado/reserva-listado.component';
import { UsuariosFormComponent } from './Componentes/usuarios-form/usuarios-form.component';

const routes: Routes = [
  { path: 'registro', component: RegistroComponent },
  { path: 'user/form', component: UsuariosFormComponent},
  { path: 'user/form/:id', component: UsuariosFormComponent},
  { path: '', component: CartaComponent},
  {path: 'carta', component: CartaComponent},
  {path: 'reservas', component: ReservasComponent},
  {path: 'login', component: LoginComponent},
  { path: 'plate/form/:id', component: PlateFormComponent },
  {path: 'reservas/mapa', component: MapaComponent},
  {path: 'reservas/reservar-mesa/:numeroMesa', component: ReservarMesaComponent},
  {path: 'reservas/listado', component: ReservaListadoComponent},
  {path: 'usuarios', component: UsuariosComponent},
  {path: 'plate/form', component: PlateFormComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
