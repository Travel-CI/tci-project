import { Injectable } from '@angular/core';
import {Http, Headers, RequestOptions, Response} from "@angular/http";
import 'rxjs/add/operator/toPromise';
import {Build} from "../models/build";
import {environment} from "environments/environment";
import {Step} from "app/models/step";
import {Observable} from 'rxjs/Observable';

@Injectable()
export class LoggerService {

  constructor(private http: Http) { }

  getAllBuildsForProject(id: number) : Promise<Build[]> {
    return this.http.get('/api/builds/' + id)
      .toPromise()
      .then((res: Response) => res.json() as Build[])
      .catch((err: Error) => this.handleError(err));
  }

  deleteBuildForProject(projectId: number, buildId: number): Promise<number> {

    let headers = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: headers });

    return this.http.delete('/api/builds/' + projectId + '/' + buildId, options)
      .toPromise()
      .then((res: Response) => res.json() as number)
      .catch((err: Error) => this.handleError(err));
  }

  getBuildById(id: number) : Promise<Build> {
    return this.http.get('/api/builds/id/' + id)
      .toPromise()
      .then((res: Response) => res.json() as Build)
      .catch((err: Error) => this.handleError(err));
  }

  getAllStepsForBuild(id : number) : Observable<Step[]> {
    return this.http.get('/api/steps/' + id)
      .map((res: Response) => res.json());
  }

  handleError(err: Error): Promise<Build[]> {

    if(environment.enableDebug)
      console.error(err);

    return Promise.reject(err.message || err);
  }
}
