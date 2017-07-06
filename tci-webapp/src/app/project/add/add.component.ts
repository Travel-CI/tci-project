import { Component, OnInit } from '@angular/core';
import {ProjectService} from '../services/project.service';
import {ToasterConfig, ToasterService} from "angular2-toaster";
import {Project} from "../models/project";

@Component({
  templateUrl: './add.component.html'
})
export class AddComponent implements OnInit {

  private project: any = {};
  private branches: string = "";

  public toasterConfig: ToasterConfig = new ToasterConfig({
    tapToDismiss: true,
    animation: 'fade',
    positionClass: 'toast-custom-top-right',
    timeout: 5000
  });

  constructor(
    private projectService: ProjectService,
    private toasterService: ToasterService
  ) { }

  ngOnInit() {
  }

  createProject() {
    let branchesToArray = this.branches.replace(" ", "").split(";");
    this.project.branches = branchesToArray;
    this.projectService.create(this.project)
      .then((res: Project) => {
        this.toasterService.pop('success', 'Project Created', 'Your Project has been created.');
        this.clearFields();
      })
      .catch((res: any) => {
        this.toasterService.pop('error', 'Creation Failed', res);
      });
  }

  clearFields() {
    this.project = {};
    this.branches = "";
  }

}
