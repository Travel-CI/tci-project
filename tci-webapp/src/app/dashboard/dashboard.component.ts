import { Component, OnInit } from '@angular/core';
import { SidebarService } from '../shared/sidebar/sidebar.service';
import { ChildItem } from '../shared/models/sidebar/child-item.model';
import { Router } from '@angular/router';

@Component({
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {

  private applications: ChildItem[];

  constructor(private _router: Router,
    private _sidebarService: SidebarService) {}

  ngOnInit() {

      this.applications = [
        { path: '/project', label: 'dashboard.apps.project.title', active: true, description : 'dashboard.apps.project.description' }
      ];

      let sidebar = [{
        title: 'dashboard.sidebar',
        children: this.applications
      }];

      this._sidebarService.changeSidebar(sidebar);
  }

  onAppClicked(path: string) {
    this._router.navigate([path]);
  }
}
