import { Component, OnInit, ViewChild } from '@angular/core';
import { TokenValuesService } from 'src/app/auth/services/token-values.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import Swal from 'sweetalert2';
import { UsersService } from '../../services/users.service';
import { HelperService } from 'src/app/shared/services/helper.service';
import { MatDialog } from '@angular/material/dialog';
import { AddComponent } from '../../components/add/add.component';
import { ModifyComponent } from '../../components/modify/modify.component';

@Component({
  selector: 'app-users-page',
  templateUrl: './users-page.component.html',
  styleUrls: ['./users-page.component.css'],
})
export class UsersPageComponent implements OnInit {
  isAdmin: boolean = false;
  constructor(
    private usersService: UsersService,
    private tokenValuesService: TokenValuesService,
    private helperService: HelperService,
    private dialog: MatDialog
  ) {
    this.isAdmin = this.tokenValuesService.isAdmin();
  }

  public dataSource: MatTableDataSource<any> = new MatTableDataSource<any>([]);
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  archivo: any = null;
  loading: boolean = false;
  displayedColumns: string[] = [
    'id',
    'username',
    'email',
    'role',
    'creationDate',
    'userCreator',
    'approvalDate',
    'userStatus',
    'aprobar',
    'modificar',
    'borrar',
  ];

  ngOnInit(): void {
    this.loading = true;
    this.load();
  }

  onFileSelected(event: any): void {
    this.archivo = event.target.files[0];
  }

  setDatasource(dataSource: any) {
    this.dataSource = dataSource;
    this.dataSource.paginator = this.paginator;
  }

  load() {
    this.usersService.listarUsuarios().subscribe({
      next: (res: any) => {
        if (res.httpCode < 400) {
          this.setDatasource(new MatTableDataSource<any>(res.data));
        } else {
          Swal.fire('Error HTTP ' + res.httpCode, res.message, 'error');
        }
        if (this.loading) this.loading = false;
      },
      error: (e: any) => {
        console.log(e);
        Swal.fire(
          'Error inesperado',
          'Por favor, contacta con el administrador.',
          'error'
        );
        if (this.loading) this.loading = false;
      },
    });
  }

  deleteItem(id: number) {
    this.usersService.borrarUsuario(id).subscribe({
      next: (res: any) => {
        if (res.httpCode < 400) {
          this.helperService.snackBarMsg(res.message, 3500);
          this.load();
        } else {
          Swal.fire('Error ' + res.httpCode, res.message, 'error');
          if (this.loading) this.loading = false;
        }
      },
      error: (e: any) => {
        console.log(e);
        Swal.fire(
          'Error inesperado',
          'Por favor, contacta con el administrador.',
          'error'
        );
        if (this.loading) this.loading = false;
      },
    });
  }

  onLoadFile() {
    const formData = new FormData();
    if (this.archivo === null) return;
    formData.append('archivo', this.archivo);
    this.loading = true;
    this.usersService.cargarDesdeCSV(formData).subscribe({
      next: (res: any) => {
        if (res.httpCode < 400) {
          this.helperService.snackBarMsg(res.message, 3500);
          this.load();
        } else {
          Swal.fire('Error ' + res.httpCode, res.message, 'error');
          if (this.loading) this.loading = false;
        }
      },
      error: (e: any) => {
        console.log(e);
        Swal.fire(
          'Error inesperado',
          'Por favor, contacta con el administrador.',
          'error'
        );
        if (this.loading) this.loading = false;
      },
    });
  }

  onAdd() {
    const dialogRef = this.helperService.openDialog(
      AddComponent,
      this.dialog,
      250,
      250
    );

    dialogRef.afterClosed().subscribe(() => {
      this.loading = true;
      setTimeout(() => {
        this.load();
      }, 1800);
    });
  }

  onModify(data: any) {
    const dialogRef = this.helperService.openDialog(
      ModifyComponent,
      this.dialog,
      250,
      250,
      data
    );

    dialogRef.afterClosed().subscribe(() => {
      this.loading = true;
      setTimeout(() => {
        this.load();
      }, 1800);
    });
  }

  onDelete(id: number) {
    Swal.fire({
      title: '¿Deseas borrar al usuario?',
      showDenyButton: true,
      confirmButtonText: 'Sí',
      denyButtonText: 'No',
    }).then((res) => {
      if (res.isConfirmed) {
        this.loading = true;
        this.deleteItem(id);
      } else if (res.isDenied) {
        return;
      }
    });
  }
}
