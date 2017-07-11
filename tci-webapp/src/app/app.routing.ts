import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LayoutComponent } from './pages/layout/layout.component';
import {ProjectsComponent} from './pages/projects/projects.component';
import {AddComponent} from './pages/add/add.component';
import {BuildsComponent} from './pages/builds/builds.component';
import {StepComponent} from "./pages/step/step.component";

export const routes: Routes = [
  { path: '', redirectTo: 'projects', pathMatch: 'full' },
  { path: 'projects', component: LayoutComponent, children: [
    { path: '', component: ProjectsComponent },
    { path: 'add', component: AddComponent },
    { path: 'edit/:id', component: AddComponent },
    { path: 'builds/:id', component: BuildsComponent },
    { path: 'builds/:id/steps', component: StepComponent }
  ]},
  { path: '**', redirectTo: 'projects'}
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
