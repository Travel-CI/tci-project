import { Component, OnInit } from '@angular/core';
import {ProjectService} from '../services/project.service';
import {ToasterConfig, ToasterService} from "angular2-toaster";
import {Project} from "../models/project";
import {ActivatedRoute, Router} from "@angular/router";
import {Command} from "app/project/models/command";

@Component({
  templateUrl: './add.component.html'
})
export class AddComponent implements OnInit {

  private project: any = {};
  private commands: any = [];
  private isEdited: Boolean = false;
  private commandsCounter: number = 1;

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
        this.toasterService.pop('success', 'Project Successfully Created', 'Your Project has been created.');
        this.clearFields();
      })
      .catch((res: any) => {
        this.toasterService.pop('error', 'Creation Failed', res);
      });
  }

  updateProject() {
    this.project.branches = this.project.branches.replace(" ", "").split(";");
    this.projectService.update(this.project)
      .then((res: Project) => {
        this.toasterService.pop('success', 'Project Successfully Updated', 'Your Project has been updated.');
      })
      .catch((res: any) => {
        this.toasterService.pop('error', 'Update Failed', res);
      })
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

  addNewCommand() {
    let commands = [...this.commands];
    commands.push(
      {command: "", commandOrder: this.commandsCounter++, enable: true, enableLogs: true}
    );
    this.commands = commands;
  }

  deleteCommand(command: Command) {
    let commands = [...this.commands];
    let index = this.commands.indexOf(command);
    for(let i = index + 1; i < this.commands.length; i++) {
      commands[i].commandOrder--;
    }
    commands.splice(index, 1);
    this.commands = commands;
    this.commandsCounter--;
  }

  clearFields() {
    this.project = {};
    this.commands = [];
  }
}
