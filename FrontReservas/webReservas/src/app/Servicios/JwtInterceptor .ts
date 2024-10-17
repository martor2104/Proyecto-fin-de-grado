import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // Verificar si localStorage está disponible
        let token: string | null = null;
        if (typeof localStorage !== 'undefined') {
            token = localStorage.getItem('token');
        }

        if (token) {
            // Clonar la solicitud para agregar el token a las cabeceras
            request = request.clone({
                setHeaders: {
                    Authorization: `Bearer ${token}`
                }
            });
        }

        return next.handle(request);
    }
}
