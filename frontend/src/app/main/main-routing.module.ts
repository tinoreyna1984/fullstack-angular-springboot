import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { MainPageComponent } from './pages/main-page/main-page.component';

const routes: Routes = [
    {
      path: '',
      component: MainLayoutComponent,
      children: [
        {
          path: '',
          component: MainPageComponent,
        },
        // colocar más módulos involucrados
        {
          path: 'users',
          loadChildren: () => import('../users/users.module').then( m => m.UsersModule ),
        },
        {
          path: '**',
          redirectTo: '',
        },
      ]
    },
  ];
  
  @NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
  })
  export class MainRoutingModule { }