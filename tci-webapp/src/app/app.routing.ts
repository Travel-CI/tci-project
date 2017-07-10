import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';

export const routes: Routes = [
  { path: '', redirectTo: 'project', pathMatch: 'full'},
  { path: '', component: LayoutComponent, children: [
    { path: 'project', loadChildren: './project/project.module#ProjectModule' }
  ]},
  { path: '**', redirectTo: 'project'}
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
