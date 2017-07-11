import { Component, OnInit } from '@angular/core';
import {LoggerService} from "../../services/logger.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Build} from "../../models/build";
import {Step} from "../../models/step";

@Component({
  templateUrl: './step.component.html'
})
export class StepComponent implements OnInit {

  private build : any = {};
  private buildSteps : any  =[];

  constructor(
    private loggerService : LoggerService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.route.params.subscribe(params => {

      if (params['id'] == undefined) {
        this.router.navigate(['/projects/builds']);
        return;
      }

      this.loggerService.getBuildById(params['id'])
        .then((res: Build) => {
          this.build = res;
          console.log(res);
          this.loggerService.getAllStepsForBuild(params['id'])
            .then((res: Step[]) => {
              this.buildSteps = res;
            })
        })
        .catch((err: any) => {
          this.router.navigate(['/projects/builds']);
        });
    });
  }

}
