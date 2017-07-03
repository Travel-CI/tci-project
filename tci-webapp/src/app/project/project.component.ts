import { Component, OnInit } from '@angular/core';
import {Project} from './model/project';

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
      lastStart: '24/12/94'
    },{
      id: 26,
      name: 'Project 2',
      description: 'Le meilleur projet (l\'unique)',
      lastStart: '23/11/1962'
    },{
      id: 29,
      name: 'Project 45',
      description: 'Emma la plus belle :p',
      lastStart: '23/11/1962'
    },{
      id: 31,
      name: 'Project 25',
      description: 'julien le boss !!',
      lastStart: '06/11/1994'
    }];
  }

}
