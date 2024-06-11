import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './Componentes/login/login.component';
import { CartaComponent } from './Componentes/carta/carta.component';
import { ReservasComponent } from './Componentes/reservas/reservas.component';
import { HeaderComponent } from './Componentes/header/header.component';
import { FooterComponent } from './Componentes/footer/footer.component';
import { RegistroComponent } from './Componentes/registro/registro.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MesasDisponiblesComponent } from './Componentes/mesas-disponibles/mesas-disponibles.component';
import { MapaComponent } from './Componentes/mapa/mapa.component';
import { ReservarMesaComponent } from './Componentes/reservar-mesa/reservar-mesa.component';
import { PlateFormComponent } from './Componentes/plate-form/plate-form.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    CartaComponent,
    ReservasComponent,
    HeaderComponent,
    FooterComponent,
    RegistroComponent,
    MesasDisponiblesComponent,
    MapaComponent,
    ReservarMesaComponent,
    PlateFormComponent,
    
  ],
  imports: [
    FormsModule,
    BrowserModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [
    provideClientHydration()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
