import { Component } from '@angular/core';
import { UsersService } from '../../services/users.service';
import { TokenValuesService } from 'src/app/auth/services/token-values.service';
import { HelperService } from 'src/app/shared/services/helper.service';
import { FormControl, FormGroup } from '@angular/forms';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.css']
})
export class AddComponent {

  form: FormGroup;

  constructor(
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

    this.form.get('userCreator')!.setValue(this.tokenValuesService.getUsername());
    this.form.get('userStatus')!.setValue('NOT_APPROVED');
  }

  validaFormulario(form: any): boolean{
    const username = form.get('username')?.value;
    const password = form.get('password')?.value;
    const email = form.get('email')?.value;
    const role = form.get('role')?.value;
    if(!username || !password || !email || !role){
      Swal.fire('Error', 'No debe estar en blanco', 'error');
      return false;
    }
    return true;
  }

  add(){
    if(!this.validaFormulario(this.form)) return;
    this.usersService.guardarUsuario(this.form.value).subscribe(
      {
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
      }
    );
  }
}
