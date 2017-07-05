import { Component, OnInit } from '@angular/core';
import {Project} from './models/project';

@Component({
  templateUrl: './project.component.html'
})

export class ProjectComponent implements OnInit {

  private projects: Project[];

  constructor() {}

  ngOnInit() {
    this.projects = [{
      id: 25,
      name: 'Project 1',
      description: 'Le meilleur projet',
      enable: true,
      repositoryUrl: 'https://github.com/mboisnard/test-repo',
      branches: [ "master", "dev"]
    }];
  }
}
