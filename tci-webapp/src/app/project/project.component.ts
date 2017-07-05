import { Component, OnInit } from '@angular/core';
import {Project} from './models/project';
import {ProjectService} from './services/project.service';

@Component({
  templateUrl: './project.component.html'
})

export class ProjectComponent implements OnInit {

  private projects: Project[];

  constructor(
    private projectService: ProjectService
  ) {}

  ngOnInit() {
    this.projectService.getAllProjects()
      .then((res: Project[]) => {
        this.projects = res;
      });
  }
}
