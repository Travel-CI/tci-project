import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import {AddComponent} from "./add/add.component";
import {ProjectComponent} from './project.component';
const routes: Routes = [
  { path: '', redirectTo: 'projects', pathMatch: 'full' },
  { path: '', children: [
    { path: '', component: ProjectComponent },
    { path: 'add', component: AddComponent },
    { path: 'edit/:id', component: AddComponent }
  ]}
];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class ProjectRoutingModule {

}
