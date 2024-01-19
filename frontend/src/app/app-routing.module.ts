import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { isNotAuthenticatedGuard } from './auth/guards/is-not-authenticated.guard';
import { isAuthenticatedGuard } from './auth/guards/is-authenticated.guard';
import { Error404Component } from './shared/pages/error404/error404.component';

const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () => import('./auth/auth.module').then( m => m.AuthModule ),
    canActivate: [isNotAuthenticatedGuard] // ruta pública
  },
  {
    path: 'main',
    loadChildren: () => import('./main/main.module').then( m => m.MainModule ),
    canActivate: [isAuthenticatedGuard] // ruta privada, requiere autenticación
  },
  {
    path: '404',
    component: Error404Component
  },
  {
    path: '',
    redirectTo: 'auth',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: '404'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
