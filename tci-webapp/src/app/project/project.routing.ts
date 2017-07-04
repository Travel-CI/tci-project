import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import {AddComponent} from "./add/add.component";
import {ProjectComponent} from './project.component';
const routes: Routes = [
  { path: '', redirectTo: 'project', pathMatch: 'full' },
  { path: '', children: [
    { path: 'project', component: ProjectComponent },
    { path: 'add', component: AddComponent }
  ]}
];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class ProjectRoutingModule {

}
