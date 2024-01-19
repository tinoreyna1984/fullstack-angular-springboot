import { Component, inject } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import Swal from 'sweetalert2';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css'],
})
export class LoginPageComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  title = environment.websiteTitle;
  form: FormGroup;
  loading: boolean = false;
  errorMsg: string = '';

  constructor() {
    this.form = new FormGroup({
      username: new FormControl(),
      password: new FormControl(),
    });
  }

  async onLogin() {
    if (this.loading) {
      return; // Evita múltiples solicitudes si ya se está cargando
    }

    // Validar credenciales antes de consumir el servicio
    const credenciales = this.form.value;
    const { username, password } = credenciales;
    if (!username || !password) {
      Swal.fire('Error', 'Debes ingresar usuario y contraseña.', 'error');
      return;
    }

    this.loading = true;
    try {
      const res: any = await this.authService.login(this.form.value);
      if (res.httpCode >= 400) {
        Swal.fire('Error HTTP ' + res.httpCode, res.message, 'error');
        return;
      }
      if (res.data) {
        localStorage.setItem('jwt', res.data); // Almacena el token JWT en el almacenamiento local
        this.router.navigate(['/main']);
      }
    } catch (error: any) {
      console.log(error);
    } finally {
      this.loading = false;
    }
  }
}
