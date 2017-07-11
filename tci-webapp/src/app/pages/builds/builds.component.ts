import { Component, OnInit } from '@angular/core';
import {ProjectService} from "../../services/project.service";
import {Project} from "../../models/project";
import {ActivatedRoute, Router} from "@angular/router";
import {Build} from "../../models/build";
import {LoggerService} from "../../services/logger.service";
import {ToasterConfig, ToasterService} from 'angular2-toaster';

@Component({
  templateUrl: './builds.component.html'
})
export class BuildsComponent implements OnInit {

  private project : any = {};
  private projectBuilds : any;

  private dialogDeleteBuildVisible: Boolean = false;
  private confirmDeleteBuild: any = {};

  public toasterConfig: ToasterConfig = new ToasterConfig({
    tapToDismiss: true,
    animation: 'fade',
    positionClass: 'toast-custom-top-right',
    timeout: 5000
  });

  constructor(
    private projectService: ProjectService,
    private loggerService: LoggerService,
    private toasterService: ToasterService,
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

  classDependingOnStatus(build : Build, isBadge: Boolean): string {

    switch (build.status) {
      case 'SUCCESS':
        return (isBadge) ? 'badge-success': 'card-accent-success';

      case 'ERROR':
        return (isBadge) ? 'badge-danger' : 'card-accent-danger';

      case 'IN_PROGRESS':
        return (isBadge) ? 'badge-primary' : 'card-accent-primary';
    }
  }

  showDeleteBuildDialog(build: Build) {
    this.confirmDeleteBuild = build;
    this.dialogDeleteBuildVisible = true;
  }

  hideDeleteBuildDialog() {
    this.confirmDeleteBuild = {};
    this.dialogDeleteBuildVisible = false;
  }

  deleteBuild() {

    this.loggerService.deleteBuildForProject(this.project.id, this.confirmDeleteBuild.id)
      .then((res: number) => {
        if (res == 1) {
          let index = this.projectBuilds.indexOf(this.confirmDeleteBuild);
          this.projectBuilds[index].hidden = true;
          this.hideDeleteBuildDialog();
        }
      })
      .catch((err: any) => {
        this.toasterService.pop('error', 'Delete Failed', err);
        this.hideDeleteBuildDialog();
      });
  }
}
