import { Component, OnInit } from '@angular/core';
import {Project} from '../models/project';
import {ProjectService} from '../services/project.service';

@Component({
  templateUrl: './add.component.html'
})
export class AddComponent implements OnInit {

  private project: any = {};

  constructor(
    private projectService: ProjectService
  ) { }

  ngOnInit() {
  }

  createProject() {
    this.projectService.create();
  }

}
