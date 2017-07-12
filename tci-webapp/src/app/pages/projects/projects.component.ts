import { Component, OnInit } from '@angular/core';
import {Project} from '../../models/project';
import {ProjectService} from '../../services/project.service';
import {Router} from "@angular/router";
import {ToasterConfig, ToasterService} from 'angular2-toaster';

@Component({
  templateUrl: './projects.component.html'
})

export class ProjectsComponent implements OnInit {

  private projects: Project[];
  private loading: Boolean = false;

  private dialogBranchesVisible: Boolean = false;
  private dialogBranches: any = [];
  private selectedBranch: any = null;

  private dialogDeleteProjectVisible: Boolean = false;
  private confirmDeleteProject: any = {};


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
      })
      .catch((err: any) => {
        this.toasterService.pop('error', 'Projects List', 'Unable to load Projects List.');
    });
  }

  redirectToEditPage(project: Project) {
    this.router.navigate(['projects', 'edit', project.id]);
  }

  redirectToBuildsPage(project: Project) {
    this.router.navigate(['projects', 'builds', project.id]);
  }

  showStartDialog(project: Project) {
    this.dialogBranchesVisible = true;

    for (let i = 0; i < project.branches.length; i++)
      this.dialogBranches.push({
        label: project.branches[i], value: {
          branch: project.branches[i],
          project: project
        }
      });
  }

  hideStartDialog() {
    this.dialogBranchesVisible = false;
    this.dialogBranches = [];
    this.selectedBranch = null;
  }

  startManualBuild() {
    if (this.selectedBranch != null) {
      this.projectService.startBuildForProject(this.selectedBranch.project.id, this.selectedBranch.branch);
      this.toasterService.pop('success', 'Build Started', 'Your build is running');
      this.hideStartDialog();
    }
  }

  showDeleteProjectDialog(project: Project) {
    this.dialogDeleteProjectVisible = true;
    this.confirmDeleteProject = project;
  }

  hideDeleteProjectDialog() {
    this.dialogDeleteProjectVisible = false;
    this.confirmDeleteProject = {};
  }

  deleteProject() {

    this.projectService.deleteProjectById(this.confirmDeleteProject.id)
      .then((res: number) => {
        if(res == 1) {
          let projects = [...this.projects];
          projects.splice(this.projects.indexOf(this.confirmDeleteProject), 1);
          this.projects = projects;
        }
      })
      .catch((err: any) => {
        this.toasterService.pop('error', 'Delete failed', err);
      });

    this.hideDeleteProjectDialog();
  }
}
