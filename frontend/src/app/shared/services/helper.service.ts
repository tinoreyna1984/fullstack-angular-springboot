import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MessageSnackBarComponent } from '../components/message-snack-bar/message-snack-bar.component';
import { ComponentType } from '@angular/cdk/portal';
import { MatDialog } from '@angular/material/dialog';

@Injectable({
  providedIn: 'root',
})
export class HelperService {
  constructor(private snackBar: MatSnackBar) {}

  // aparece un mensajito de resultado
  snackBarMsg(message: any, time: number) {
    this.snackBar.openFromComponent(MessageSnackBarComponent, {
      duration: time,
      data: message,
    });
  }

  // dialog para agregar y editar
  openDialog(
    component: ComponentType<any>,
    dialog: MatDialog,
    enterDuration: number,
    exitDuration: number,
    data?: any
  ) {
    return dialog.open(component, {
      data: data,
      enterAnimationDuration: enterDuration,
      exitAnimationDuration: exitDuration,
    });
  }
}
