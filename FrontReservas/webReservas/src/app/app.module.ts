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
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MapaComponent } from './Componentes/mapa/mapa.component';
import { ReservarMesaComponent } from './Componentes/reservar-mesa/reservar-mesa.component';
import { PlateFormComponent } from './Componentes/plate-form/plate-form.component';
import { AuthService } from './Servicios/AuthService/auth-service.service';
import { UsuariosComponent } from './Componentes/usuarios/usuarios.component';
import { ReservaListadoComponent } from './Componentes/reserva-listado/reserva-listado.component';
import { LoginAlertComponent } from './Componentes/login-alert/login-alert.component';
import { provideAnimations } from '@angular/platform-browser/animations';
import { UsuariosFormComponent } from './Componentes/usuarios-form/usuarios-form.component';
import { JwtInterceptor } from './Servicios/JwtInterceptor ';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    CartaComponent,
    ReservasComponent,
    HeaderComponent,
    FooterComponent,
    RegistroComponent,
    MapaComponent,
    ReservarMesaComponent,
    PlateFormComponent,
    UsuariosComponent,
    ReservaListadoComponent,
    LoginAlertComponent,
    UsuariosFormComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    },
    provideClientHydration(),
    provideAnimations(),
    AuthService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
