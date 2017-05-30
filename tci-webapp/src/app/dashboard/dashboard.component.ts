import { Component, OnInit } from '@angular/core';
import { SidebarService } from '../shared/sidebar/sidebar.service';
import { TranslateService } from '@ngx-translate/core';
import { ChildItem } from '../shared/models/sidebar/child-item.model';
import { Router } from '@angular/router';

@Component({
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {

  private applications: ChildItem[];

  private translationKeys: Array<string> = [
    'dashboard.apps.projects.title',
    'dashboard.apps.projects.description',
    'dashboard.sidebar'
  ];

  constructor(private _router: Router,
    private _sidebarService: SidebarService,
    private _translateService: TranslateService) {}

  ngOnInit() {
    this._translateService.get(this.translationKeys).subscribe((translation: string[]) => {

      this.applications = [
        { path: '/projects', label: translation[this.translationKeys[0]], active: true, description : translation[this.translationKeys[1]] }
      ];

      let sidebar = [{
        title: translation[this.translationKeys[2]],
        children: this.applications
      }];

      this._sidebarService.changeSidebar(sidebar);
    });
  }

  onAppClicked(path: string) {
    this._router.navigate([path]);
  }
}
