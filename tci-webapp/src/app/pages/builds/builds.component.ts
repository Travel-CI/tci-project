import { Component, OnInit } from '@angular/core';
import {ProjectService} from "../../services/project.service";
import {Project} from "../../models/project";
import {ActivatedRoute, Router} from "@angular/router";
import {Build} from "../../models/build";
import {LoggerService} from "../../services/logger.service";

@Component({
  templateUrl: './builds.component.html'
})
export class BuildsComponent implements OnInit {

  private project : any = {};
  private projectBuilds : Build[];

  constructor(
    private projectService: ProjectService,
    private loggerService: LoggerService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.route.params.subscribe(params => {

      if (params['id'] == undefined) {
        this.router.navigate(['/projects']);
        return;
      }

      this.projectService.getProjectById(params['id'])
        .then((res: Project) => {
          this.project = res;
          this.loggerService.getAllBuildsForProject(params['id'])
            .then((res: Build[]) => {
              this.projectBuilds = res;
            })
        })
        .catch((err: any) => {
          this.router.navigate(['/projects']);
        });
    });
  }

  deleteBuild() {
    this.router.navigate(['/projects']);
  }

  classDependingOnStatus(build : Build, isBadge: Boolean): string {

    switch(build.status) {
      case 'SUCCESS':
        return (isBadge) ? 'badge-success': 'card-accent-success';

      case 'ERROR':
        return (isBadge) ? 'badge-danger' : 'card-accent-danger';

      case 'IN_PROGRESS':
        return (isBadge) ? 'badge-primary' : 'card-accent-primary';

    }
  }
}
