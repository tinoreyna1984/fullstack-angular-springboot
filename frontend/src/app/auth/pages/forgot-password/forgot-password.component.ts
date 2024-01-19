import { Component, inject } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import Swal from 'sweetalert2';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  
  title = environment.websiteTitle;
  form: FormGroup;
  loading: boolean = false;
  errorMsg: string = '';

  constructor() {
    this.form = new FormGroup({
      email: new FormControl(),
    });
  }

  async onRetrievePassword() {
    if (this.loading) {
      return; // Evita múltiples solicitudes si ya se está cargando
    }

    // Validar credenciales antes de consumir el servicio
    const credenciales = this.form.value;
    const { email } = credenciales;
    if (!email) {
      Swal.fire('Error', 'Debes ingresar email.', 'error');
      return;
    }

    this.loading = true;
    try {
      const res: any = await this.authService.sendResetPasswordToken(this.form.value);
      if (res.httpCode >= 400) {
        Swal.fire('Error HTTP ' + res.httpCode, res.message, 'error');
        return;
      }
      if (res.data) {
        Swal.fire('¡Muy bien!', `Enviamos un correo a ${email}. Sigue las indicaciones.`, 'success');
      }
    } catch (error: any) {
      console.log(error);
    } finally {
      this.loading = false;
    }
  }
}
