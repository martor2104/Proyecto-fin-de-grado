import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

    constructor(private router: Router) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        let token: string | null = null;
        if (typeof localStorage !== 'undefined') {
            token = localStorage.getItem('token');
        }

        if (token) {
            request = request.clone({
                setHeaders: {
                    Authorization: `Bearer ${token}`
                }
            });
        }

        return next.handle(request).pipe(
            catchError((error: HttpErrorResponse) => {
                if (error.status === 401) {
                    // Token inválido o expirado, redirigir al login
                    console.warn('Sesión no autorizada. Redirigiendo al login.');
                    this.router.navigate(['/login']);
                } else if (error.status === 403) {
                    // Usuario sin permisos para el recurso
                    console.warn('Acceso denegado (403).');
                }

                return throwError(error); // Propagar el error si es necesario
            })
        );
    }
}
