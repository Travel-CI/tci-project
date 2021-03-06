import { Component, OnInit } from '@angular/core';
import {ProjectService} from '../../services/project.service';
import {ToasterConfig, ToasterService} from 'angular2-toaster';
import {Project} from '../../models/project';
import {ActivatedRoute, Router} from '@angular/router';
import {Command} from 'app/models/command';
import {CommandService} from '../../services/command.service';

@Component({
  templateUrl: './add.component.html'
})
export class AddComponent implements OnInit {

  private project: any = {};

  private commands: any = [];
  private loading: Boolean = false;

  private isEdited: Boolean = false;

  private commandsCounter = 1;

  public toasterConfig: ToasterConfig = new ToasterConfig({
    tapToDismiss: true,
    animation: 'fade',
    positionClass: 'toast-custom-top-right',
    timeout: 5000
  });

  constructor(
    private projectService: ProjectService,
    private toasterService: ToasterService,
    private commandService: CommandService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.fillFieldsForEdit();
  }

  createProject() {

    if (this.validateCommands() === false) {
      this.toasterService.pop('error', 'Creation Failed', 'Invalid Commands Fields');
      return;
    }

    this.project.branches = this.project.branches.toString().replace(' ', '').split(',');

    this.projectService.create(this.project)
      .then((res: Project) => {
        this.project = res;

        if (this.commands.length > 0) {

          // Store projectId in commands object
          for (let i = 0; i < this.commands.length; i++)
            this.commands[i].projectId = res.id;

          this.commandService.addNewCommands(this.commands)
            .then((res: Command[]) => {
              if (res.length === this.commands.length) {
                this.toasterService.pop('success', 'Project Successfully Created', 'Your Project has been created.');
                this.clearFields();
                this.router.navigate(['projects']);
              }
            })
            .catch((err: any) => this.toasterService.pop('error', 'Commands Creation Failed', err));
        } else {
          this.toasterService.pop('success', 'Project Successfully Created', 'Your Project has been created.');
          this.clearFields();
          this.router.navigate(['projects']);
        }
      })
      .catch((res: any) => this.toasterService.pop('error', 'Creation Failed', res));
  }

  updateProject() {

    if (this.validateCommands() === false) {
      this.toasterService.pop('error', 'Creation Failed', 'Invalid Commands Fields');
      return;
    }

    this.project.branches = this.project.branches.toString().replace(" ", "").split(",");

    this.projectService.update(this.project)
      .then((res: Project) => {

        // Store project Id in command object
        for (let i = 0; i < this.commands.length; i++)
          this.commands[i].projectId = res.id;

        // Send updated commands
        this.commandService.updateCommandsByProjectId(res.id, this.commands)
          .then((res: Command[]) => this.toasterService
            .pop('success', 'Project Successfully Updated', 'Your Project has been updated.'))
          .catch((err: any) => this.toasterService.pop('error', 'Update Failed', err));
      })
      .catch((err: any) => {
        this.toasterService.pop('error', 'Update Failed', err);
      });
  }

  fillFieldsForEdit() {
    this.route.params.subscribe(params => {

      // In case of Add page
      if(params['id'] === undefined) {
        this.project.enable = true;
        return;
      }

      // In case of edit page
      this.isEdited = true;
      this.loading = true;

      this.projectService.getProjectById(params['id'])
        .then((res: Project) => {
          let project: any = res;
          project.branches = res.branches.join(', ');
          this.project = project;

          this.commandService.getCommandsByProjectId(res.id)
            .then((res: Command[]) => {
              let commands = [...this.commands];

              for (let i = 0; i < res.length; i++)
                commands.push(res[i]);

              this.commands = commands;
              this.commandsCounter = res.length + 1;
              this.loading = false;
            })
            .catch((err: any) => {
              this.toasterService.pop('error', 'Get Commands Failed', err);
            });
        })
        .catch((err: any) => {
          this.router.navigate(['projects']);
        });
    });
  }

  addNewCommand() {
    let commands = [...this.commands];
    commands.push({
      command: '',
      commandOrder: this.commandsCounter++,
      enabled: true,
      enableLogs: true
    });

    this.commands = commands;
  }

  deleteCommand(command: Command) {

    let commands = [...this.commands];
    let index = this.commands.indexOf(command);

    for (let i = index + 1; i < this.commands.length; i++) {
      commands[i].commandOrder--;
    }
    commands.splice(index, 1);

    this.commands = commands;
    this.commandsCounter--;
  }

  private validateCommands(): Boolean {

    for (let i = 0; i < this.commands.length; i++)
      if (this.commands[i].command === '')
        return false;

    return true;
  }

  clearFields() {

    if (this.isEdited)
      this.router.navigate(['projects']);

    this.project = { enable: true };
    this.commands = [];
  }
}
