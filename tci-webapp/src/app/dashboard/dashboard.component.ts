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
    'dashboard.apps.lettrage.title',
    'dashboard.apps.lettrage.description',
    'dashboard.apps.ran.title',
    'dashboard.apps.ran.description',
    'dashboard.apps.trec.title',
    'dashboard.apps.trec.description',
    'dashboard.apps.osqarr.title',
    'dashboard.apps.osqarr.description',
    'dashboard.apps.tlm.title',
    'dashboard.apps.tlm.description',
    'dashboard.sidebar'
  ];

  constructor(private _router: Router,
    private _sidebarService: SidebarService,
    private _translateService: TranslateService) {}

  ngOnInit() {
    this._translateService.get(this.translationKeys).subscribe((translation: string[]) => {

      this.applications = [
        { path: '/lettrage', label: translation[this.translationKeys[0]], active: true, description : translation[this.translationKeys[1]] },
        { path: '/ran', label: translation[this.translationKeys[2]], active: true, description: translation[this.translationKeys[3]] },
        { path: '/trec', label: translation[this.translationKeys[4]], active: true, description: translation[this.translationKeys[5]] },
        { path: '/osqarr', label: translation[this.translationKeys[6]], active: true, description: translation[this.translationKeys[7]] },
        { path: '/tlm', label: translation[this.translationKeys[8]], active: true, description: translation[this.translationKeys[9]] }
      ];

      let sidebar = [{
        title: translation[this.translationKeys[10]],
        children: this.applications
      }];

      this._sidebarService.changeSidebar(sidebar);
    });
  }

  onAppClicked(path: string) {
    this._router.navigate([path]);
  }
}
