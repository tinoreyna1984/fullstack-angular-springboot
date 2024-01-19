import { Component, inject } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import Swal from 'sweetalert2';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-register-page',
  templateUrl: './register-page.component.html',
  styleUrls: ['./register-page.component.css']
})
export class RegisterPageComponent {
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
      name: new FormControl(),
      lastName: new FormControl(),
      email: new FormControl(),
    });
  }

  // llamo al servicio AuthService
  async onRegister() {
    if (this.loading) {
      return; // Evita múltiples solicitudes si ya se está cargando
    }

    // Validar credenciales antes de consumir el servicio
    const credenciales = this.form.value;
    const { username, password, name, lastName, email } = credenciales;
    if (!username || !password || !name || !lastName || !email) {
      Swal.fire('Error', 'Ningún valor debe ser nulo.', 'error');
      return;
    }

    this.loading = true;
    try {
      const res: any = await this.authService.register(this.form.value);
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
