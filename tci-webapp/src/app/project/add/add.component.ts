import { Component, OnInit } from '@angular/core';
import {ProjectService} from '../services/project.service';
import {ToasterConfig, ToasterService} from "angular2-toaster";
import {Project} from "../models/project";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  templateUrl: './add.component.html'
})
export class AddComponent implements OnInit {

  private project: any = {};
  private isEdited: Boolean = false;

  public toasterConfig: ToasterConfig = new ToasterConfig({
    tapToDismiss: true,
    animation: 'fade',
    positionClass: 'toast-custom-top-right',
    timeout: 5000
  });

  constructor(
    private projectService: ProjectService,
    private toasterService: ToasterService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.fillFieldsForEdit();
  }

  createProject() {
    this.project.branches = this.project.branches.replace(" ", "").split(";");
    this.projectService.create(this.project)
      .then((res: Project) => {
        this.toasterService.pop('success', 'Project Created', 'Your Project has been created.');
        this.clearFields();
      })
      .catch((res: any) => {
        this.toasterService.pop('error', 'Creation Failed', res);
      });
  }

  fillFieldsForEdit() {
    this.route.params.subscribe(params => {
      if(params['id'] == undefined)
        return;

      this.isEdited = true;

      this.projectService.getProjectById(params['id'])
        .then((res: Project) => {
          let project: any = res;
          project.branches = res.branches.join("; ");
          this.project = project;
        })
        .catch((err: any) => {
          this.router.navigate(['/project'])
        });
    });
  }

  clearFields() {
    this.project = {};
  }
}
