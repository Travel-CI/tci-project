import { Component, OnInit } from '@angular/core';
import {Project} from './models/project';
import {ProjectService} from './services/project.service';
import {Router} from "@angular/router";

@Component({
  templateUrl: './project.component.html'
})

export class ProjectComponent implements OnInit {

  private loading: Boolean = false;

  private projects: Project[];

  constructor(
    private projectService: ProjectService,
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
}
