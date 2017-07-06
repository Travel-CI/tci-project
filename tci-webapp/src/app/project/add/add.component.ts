import { Component, OnInit } from '@angular/core';
import {ProjectService} from '../services/project.service';

@Component({
  templateUrl: './add.component.html'
})
export class AddComponent implements OnInit {

  private project: any = {};
  private branches: string = "";

  constructor(
    private projectService: ProjectService
  ) { }

  ngOnInit() {
  }

  createProject() {
    let branchesToArray = this.branches.replace(" ", "").split(";");
    this.project.branches = branchesToArray;
    this.projectService.create(this.project);
  }

}
