import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom, fromEvent } from 'rxjs';
import { environment } from 'src/environments/environment';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl: string = environment.baseUrl;
  private http = inject(HttpClient);

  constructor(private router: Router) {
  }

  login(formValue: any) {
    return firstValueFrom(
      this.http.post<any>(`${this.baseUrl}/auth/authenticate`, formValue)
    );
  }

  register(formValue: any) {
    return firstValueFrom(
      this.http.post<any>(`${this.baseUrl}/auth/register`, formValue)
    );
  }

  sendResetPasswordToken(formValue: any) {
    return firstValueFrom(
      this.http.post<any>(`${this.baseUrl}/auth/send-reset-password-token`, formValue)
    );
  }

  resetPassword(formValue: any) {
    return firstValueFrom(
      this.http.patch<any>(`${this.baseUrl}/auth/reset-password`, formValue)
    );
  }

  // cierro sesión
  logout() {
    localStorage.removeItem('jwt');
    this.router.navigate(['/']);
  }
}
