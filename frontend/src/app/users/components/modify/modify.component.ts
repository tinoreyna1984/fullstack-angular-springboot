import { Component, Inject } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import Swal from 'sweetalert2';
import { UsersService } from '../../services/users.service';
import { TokenValuesService } from 'src/app/auth/services/token-values.service';
import { HelperService } from 'src/app/shared/services/helper.service';

@Component({
  selector: 'app-modify',
  templateUrl: './modify.component.html',
  styleUrls: ['./modify.component.css'],
})
export class ModifyComponent {
  form: FormGroup;

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: any,
    private usersService: UsersService,
    private tokenValuesService: TokenValuesService,
    private helperService: HelperService
  ) {
    this.form = new FormGroup({
      username: new FormControl(),
      password: new FormControl(),
      email: new FormControl(),
      role: new FormControl(),
      userCreator: new FormControl(),
      userStatus: new FormControl(),
    });

    this.form.get('username')!.setValue(this.data.username);
    this.form.get('password')!.setValue('');
    this.form.get('email')!.setValue(this.data.email);
    this.form.get('role')!.setValue(this.data.role);
    this.form.get('userCreator')!.setValue(this.data.userCreator);
    this.form.get('userStatus')!.setValue(this.data.userStatus);
  }

  validaFormulario(form: any): boolean {
    const username = form.get('username')?.value;
    const password = form.get('password')?.value;
    const email = form.get('email')?.value;
    const role = form.get('role')?.value;
    if (!username || !password || !email || !role) {
      Swal.fire('Error', 'No debe estar en blanco', 'error');
      return false;
    }
    return true;
  }

  modify() {
    if (!this.validaFormulario(this.form)) return;
    this.usersService.editarUsuario(this.data.id, this.form.value).subscribe({
      next: (res: any) => {
        if (res.httpCode < 400) {
          this.helperService.snackBarMsg(res.message, 3500);
        } else {
          Swal.fire('Error ' + res.httpCode, res.message, 'error');
        }
      },
      error: (e: any) => {
        console.log(e);
        Swal.fire(
          'Error inesperado',
          'Por favor, contacta con el administrador.',
          'error'
        );
      },
    });
  }
}
