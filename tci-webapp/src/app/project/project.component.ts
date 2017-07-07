import { Component, OnInit } from '@angular/core';
import {Project} from './models/project';
import {ProjectService} from './services/project.service';

@Component({
  templateUrl: './project.component.html'
})

export class ProjectComponent implements OnInit {

  private loading: Boolean = false;

  private projects: Project[];

  constructor(
    private projectService: ProjectService
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
}
