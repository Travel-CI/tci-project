import {Component, OnDestroy, OnInit} from '@angular/core';
import {LoggerService} from "../../services/logger.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Build} from "../../models/build";
import {Step} from "../../models/step";
import {AnonymousSubscription, Subscription} from "rxjs/Subscription";
import {IntervalObservable} from 'rxjs/observable/IntervalObservable';

@Component({
  templateUrl: './step.component.html'
})
export class StepComponent implements OnInit, OnDestroy {

  private build : any = {};

  private steps : any =[];
  private stepsLoading: AnonymousSubscription;

  private timerSubscription: AnonymousSubscription;
  private stepsSubscription: AnonymousSubscription;

  constructor(
    private loggerService : LoggerService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.retrieveStepsForBuild();
  }

  ngOnDestroy() {
    if (this.timerSubscription)
      this.timerSubscription.unsubscribe();

    if (this.stepsSubscription)
      this.stepsSubscription.unsubscribe();

    if (this.stepsLoading)
      this.stepsLoading.unsubscribe();
  }

  private retrieveStepsForBuild() {
    this.route.params.subscribe(params => {

      if (params['id'] == undefined) {
        this.router.navigate(['projects', 'builds']);
        return;
      }

      this.loggerService.getBuildById(params['id'])
        .then((res: Build) => {
          this.build = res;

          this.stepsLoading = this.refreshSteps(res.id);

          // Update steps only if build is not end
          if (res.status == 'IN_PROGRESS')
            this.subscribeToData(res.id);
        })
        .catch((err: any) => {
          this.router.navigate(['projects', 'builds']);
        });
    });
  }

  classDependingOnStepStatus(step: Step) {
    switch (step.status) {
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

  private refreshSteps(id: number): Subscription {
    return this.loggerService.getAllStepsForBuild(id).subscribe((res: Step[]) => {
      this.steps = res;
    });
  }

  private subscribeToData(id: number): void {
    this.timerSubscription = IntervalObservable.create(5000).subscribe(() => {
      this.stepsSubscription = this.refreshSteps(id);
    });
  }
}
