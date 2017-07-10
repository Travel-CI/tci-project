import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import {AddComponent} from "./add/add.component";
import {ProjectComponent} from './project.component';
import {LoggerComponent} from "./logger/logger.component";

const routes: Routes = [
  { path: '', redirectTo: 'projects', pathMatch: 'full' },
  { path: '', children: [
    { path: '', component: ProjectComponent },
    { path: 'add', component: AddComponent },
    { path: 'edit/:id', component: AddComponent },
    { path: 'builds/:id', component: LoggerComponent }
  ]}
];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class ProjectRoutingModule {

}
