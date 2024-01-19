import { Component, Input, OnInit, inject } from '@angular/core';
import {HttpParams} from "@angular/common/http";
import { FormControl, FormGroup } from '@angular/forms';
import Swal from 'sweetalert2';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  private authService = inject(AuthService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  
  title = environment.websiteTitle;
  form: FormGroup;
  loading: boolean = false;
  errorMsg: string = '';
  token: string = '';

  constructor() {
    this.form = new FormGroup({
      nuevaClave: new FormControl(),
    });
  }
  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.token = params['token'];
    });
  }

  async onResetPassword() {
    if (this.loading) {
      return; // Evita múltiples solicitudes si ya se está cargando
    }

    // Validar credenciales antes de consumir el servicio
    const token = this.token;
    const credenciales = this.form.value;
    const { nuevaClave } = credenciales;
    if (!nuevaClave) {
      Swal.fire('Error', 'Debes ingresar tu nueva clave.', 'error');
      return;
    }
    this.loading = true;
    try {
      const res: any = await this.authService.resetPassword({resetPasswordToken: token, password: nuevaClave});
      if (res.httpCode >= 400) {
        Swal.fire('Error HTTP ' + res.httpCode, res.message, 'error');
        return;
      }
      else {
        Swal.fire('¡Muy bien!', `Se restableció la clave.`, 'success');
        this.router.navigate(['/auth/login']);
      }
    } catch (error: any) {
      console.log(error);
    } finally {
      this.loading = false;
    }
  }
}
