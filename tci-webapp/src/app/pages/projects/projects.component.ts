import {Component, OnDestroy, OnInit} from '@angular/core';
import {Project} from '../../models/project';
import {ProjectService} from '../../services/project.service';
import {Router} from "@angular/router";
import {ToasterConfig, ToasterService} from 'angular2-toaster';
import {Build} from "../../models/build";
import {AnonymousSubscription, Subscription} from "rxjs/Subscription";
import {IntervalObservable} from "rxjs/observable/IntervalObservable";

@Component({
  templateUrl: './projects.component.html'
})

export class ProjectsComponent implements OnInit, OnDestroy {

  private projects: any;
  private loading: Boolean = false;

  private timerSubscription: AnonymousSubscription;
  private projectsSubscription: AnonymousSubscription;

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

  ngOnDestroy() {
    if (this.timerSubscription)
      this.timerSubscription.unsubscribe();

    if (this.projectsSubscription)
      this.projectsSubscription.unsubscribe();
  }

  private getAllProjectsForList() {
    this.loading = true;

    this.refreshProjects();
    this.subscribeToProjects();
  }


  badgeDependingBuildStatus(build: Build) : string {
    switch(build.status) {
      case 'SUCCESS':
        return 'status-success';

      case 'ERROR':
        return 'status-danger';

      case 'IN_PROGRESS':
        return 'status-primary';
    }
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
        label: project.branches[i],
        value: {
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
      .catch((err: any) => this.toasterService.pop('error', 'Delete failed', err));

    this.hideDeleteProjectDialog();
  }

  getLastBuildStatus(projects: any){
    for(let i = 0; i< projects.length; i++){
      this.projectService.getLastBuildForProject(projects[i].id)
        .then((res: Build) => {
          projects[i].build = res;
          if(i == projects.length - 1)
            this.projects = projects;
        });
    }
  }

  private refreshProjects(): Subscription {

    return this.projectService.getAllProjects()
      .subscribe((res: Project[]) => {
        this.loading = false;
        this.getLastBuildStatus(res);
      });
  }

  private subscribeToProjects(): void {
    this.timerSubscription = IntervalObservable.create(5000).subscribe(
      () => this.projectsSubscription = this.refreshProjects()
    );
  }
}
