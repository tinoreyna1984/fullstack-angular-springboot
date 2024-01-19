import { NgModule } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatDialogModule } from '@angular/material/dialog';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';



@NgModule({
  declarations: [],
  exports: [
    MatTableModule,
    MatPaginatorModule,
    MatDialogModule,
    MatCardModule,
    MatListModule,
    MatSnackBarModule,
    MatButtonModule,
    MatFormFieldModule,
    MatProgressSpinnerModule,
  ]
})
export class MaterialModule { }
