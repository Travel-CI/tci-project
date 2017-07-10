import { Component, OnInit } from '@angular/core';
import {ProjectService} from "../services/project.service";
import {Project} from "../models/project";
import {ActivatedRoute, Router} from "@angular/router";
import {Build} from "../models/build";
import {LoggerService} from "../services/logger.service";

@Component({
  selector: 'app-logger',
  templateUrl: './logger.component.html'
})
export class LoggerComponent implements OnInit {

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
        this.router.navigate(['/project']);
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
          this.router.navigate(['/project']);
        });
    });
  }

  deleteBuild() {
    this.router.navigate(['/project']);
  }

  cardClassDependingOnStatus(build : Build): string {
    switch(build.status) {
      case 'SUCCESS':
        return 'card-accent-success';

      case 'ERROR':
        return 'card-accent-danger';

      case 'IN_PROGRESS':
        return 'card-accent-primary';

    }
  }

  badgeDependingOnStatus(build : Build): string {
    switch(build.status) {
      case 'SUCCESS':
        return 'badge-success';

      case 'ERROR':
        return 'badge-danger';

      case 'IN_PROGRESS':
        return 'badge-primary';

    }
  }

}
