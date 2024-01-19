import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddComponent } from './components/add/add.component';
import { ModifyComponent } from './components/modify/modify.component';
import { UsersLayoutComponent } from './layout/users-layout/users-layout.component';
import { UsersPageComponent } from './pages/users-page/users-page.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../material/material.module';
import { SharedModule } from '../shared/shared.module';
import { UsersRoutingModule } from './users-routing.module';



@NgModule({
  declarations: [
    UsersLayoutComponent,
    UsersPageComponent,
    AddComponent,
    ModifyComponent
  ],
  imports: [
    CommonModule,
    UsersRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    SharedModule
  ]
})
export class UsersModule { }
