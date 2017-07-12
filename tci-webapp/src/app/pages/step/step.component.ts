import { Component, OnInit } from '@angular/core';
import {LoggerService} from "../../services/logger.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Build} from "../../models/build";
import {Step} from "../../models/step";
import {ToasterConfig, ToasterService} from "angular2-toaster";

@Component({
  templateUrl: './step.component.html'
})
export class StepComponent implements OnInit {

  private build : any = {};

  private steps : any  =[];
  private stepsLoading: any;

  public toasterConfig: ToasterConfig = new ToasterConfig({
    tapToDismiss: true,
    animation: 'fade',
    positionClass: 'toast-custom-top-right',
    timeout: 5000
  });

  constructor(
    private loggerService : LoggerService,
    private toasterService: ToasterService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.retrieveStepsForBuild();
  }

  retrieveStepsForBuild() {
    this.route.params.subscribe(params => {

      if (params['id'] == undefined) {
        this.router.navigate(['projects', 'builds']);
        return;
      }

      this.loggerService.getBuildById(params['id'])
        .then((res: Build) => {
          this.build = res;

          this.stepsLoading = this.loggerService.getAllStepsForBuild(params['id'])
            .then((res: Step[]) => {
              this.steps = res;
            })
            .catch((err: any) => {
              this.toasterService.pop('error', 'Unable to retrieve Steps List', err);
            });
        })
        .catch((err: any) => {
          this.router.navigate(['projects', 'builds']);
        });
    });
  }

  classDependingOnStepStatus(step: Step) {
    switch(step.status) {
      case 'SUCCESS':
        return 'badge-success';

      case 'ERROR':
        return 'badge-danger';

      case 'IN_PROGRESS':
        return 'badge-primary';
    }
  }

  redirectToBuildPage(build: Build) {
    this.router.navigate(['projects', 'builds', build.projectId]);
  }
}
