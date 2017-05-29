import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RulesComponent } from './rules/rules.component';
import { LettrageRoutingModule } from './lettrage.routing';
import { PeriodsComponent } from './periods/periods.component';
import { SidebarItem } from '../shared/models/sidebar/sidebar-item.model';
import { SidebarService } from '../shared/sidebar/sidebar.service';
import { DataTableModule } from 'primeng/primeng';

@NgModule({
  imports: [
    CommonModule,
    LettrageRoutingModule,
    DataTableModule
  ],
  declarations: [
    RulesComponent,
    PeriodsComponent
  ]
})
export class LettrageModule {

  private sidebar: SidebarItem[] = [{
    title: 'Lettrage',
    children: [
      { path: '/lettrage', label: 'Lettrage', active: true },
      { path: '/ran', label: 'Ran Automator', active: false },
      { path: '/tlm', label: 'Table Link Manager', active: true }
    ]
  }];

  constructor(private sidebarService: SidebarService) {
    this.sidebarService.changeSidebar(this.sidebar);
  }
}
