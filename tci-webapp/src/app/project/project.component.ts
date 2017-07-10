import { Component, OnInit } from '@angular/core';
import {Project} from './models/project';
import {ProjectService} from './services/project.service';
import {Router} from "@angular/router";
import {ToasterConfig, ToasterService} from 'angular2-toaster';

@Component({
  templateUrl: './project.component.html'
})

export class ProjectComponent implements OnInit {

  private loading: Boolean = false;
  private dialogEnabled: Boolean = false;
  private dialogBranches: any = [];
  private selectedBranch: any = null;

  private projects: Project[];

  public toasterConfig: ToasterConfig = new ToasterConfig({
    tapToDismiss: true,
    animation: 'fade',
    positionClass: 'toast-custom-top-right',
    timeout: 5000
  });

  constructor(
    private projectService: ProjectService,
    private toasterService: ToasterService,
    private router: Router
  ) {}

  ngOnInit() {
    this.getAllProjectsForList();
  }

  private getAllProjectsForList() {
    this.loading = true;

    this.projectService.getAllProjects()
      .then((res: Project[]) => {
        this.projects = res;
        this.loading = false;
      });
  }

  redirectToEditPage(project: Project){
    this.router.navigate(['/project/edit', project.id]);
  }

  redirectToBuildsPage(project: Project){
    this.router.navigate(['/project/builds', project.id]);
  }

  deleteProject(project: Project){
    this.projectService.deleteProjectById(project.id)
      .then((res: number) => {
        if(res == 1) {
          let projects = [...this.projects];
          projects.splice(this.projects.indexOf(project), 1);
          this.projects = projects;
        }
      });
  }

  showDialog(project: Project) {
    this.dialogEnabled = true;

    for (let i = 0; i < project.branches.length; i++)
      this.dialogBranches.push({
        label: project.branches[i], value: {
          branch: project.branches[i],
          project: project
        }
      });
  }

  startManualBuild() {

    if (this.selectedBranch != null) {
      this.projectService.startBuildForProject(this.selectedBranch.project.id, this.selectedBranch.branch);
      this.toasterService.pop('success', 'Build Started', 'Your build is running');
      this.dialogEnabled = false;
      this.dialogBranches = [];
      this.selectedBranch = null;
    }
    else
      this.toasterService.pop('error', 'Branch', 'Select a branch to build');
  }

  hideDialog() {
    this.dialogEnabled = false;
    this.dialogBranches = [];
  }
}
