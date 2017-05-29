import { RouterModule, Routes } from '@angular/router';
import { RulesComponent } from './rules/rules.component';
import { NgModule } from '@angular/core';
import {PeriodsComponent} from './periods/periods.component';

const routes: Routes = [
  { path: '', redirectTo: 'rules', pathMatch: 'full' },
  { path: '', children: [
    { path: 'rules', component: RulesComponent },
    { path: 'periods', component: PeriodsComponent }
  ]}
];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class LettrageRoutingModule {}
