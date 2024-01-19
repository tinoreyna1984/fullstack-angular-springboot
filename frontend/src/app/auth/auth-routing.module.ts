import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthLayoutComponent } from './layout/auth-layout/auth-layout.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { RegisterPageComponent } from './pages/register-page/register-page.component';
import { ForgotPasswordComponent } from './pages/forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './pages/reset-password/reset-password.component';


const routes: Routes = [
    {
      path: '',
      component: AuthLayoutComponent,
      children: [
        {
          path: 'login',
          component: LoginPageComponent,
        },
        {
          path: 'register',
          component: RegisterPageComponent,
        },
        {
          path: 'forgot-password',
          component: ForgotPasswordComponent,
        },
        {
          path: 'reset-password',
          component: ResetPasswordComponent,
        },
        {
          path: '**',
          redirectTo: 'login',
        },
      ]
    },
  ];
  
  @NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
  })
  export class AuthRoutingModule { }