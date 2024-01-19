import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UsersLayoutComponent } from './layout/users-layout/users-layout.component';
import { UsersPageComponent } from './pages/users-page/users-page.component';

const routes: Routes = [
  {
    path: '',
    component: UsersLayoutComponent,
    children: [
      {
        path: '',
        component: UsersPageComponent,
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class UsersRoutingModule {}
