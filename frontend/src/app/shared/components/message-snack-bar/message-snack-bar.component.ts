import { Component, inject, Inject } from '@angular/core';
import {
  MAT_SNACK_BAR_DATA,
  MatSnackBarRef,
} from '@angular/material/snack-bar';

@Component({
  selector: 'app-message-snack-bar',
  templateUrl: './message-snack-bar.component.html',
  styleUrls: ['./message-snack-bar.component.css'],
})
export class MessageSnackBarComponent {
  snackBarRef = inject(MatSnackBarRef);
  message: string = '';

  constructor(@Inject(MAT_SNACK_BAR_DATA) data: any) {
    console.log(data);
    this.message = data;
  }
}
