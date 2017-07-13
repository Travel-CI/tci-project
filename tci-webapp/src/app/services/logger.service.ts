import { Injectable } from '@angular/core';
import {Http, Headers, RequestOptions, Response} from '@angular/http';
import 'rxjs/add/operator/toPromise';
import {Build} from '../models/build';
import {environment} from 'environments/environment';
import {Step} from 'app/models/step';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class LoggerService {

  private readonly BUILD_PROXY_URL = '/api/builds';
  private readonly STEP_PROXY_URL = '/api/steps';

  constructor(private http: Http) { }

  getAllBuildsForProject(id: number): Observable<Build[]> {
    return this.http.get(this.BUILD_PROXY_URL + '/' + id)
      .map((res: Response) => res.json() as Build[]);
  }

  deleteBuildForProject(projectId: number, buildId: number): Promise<number> {

    let headers = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: headers });

    return this.http.delete(this.BUILD_PROXY_URL + '/' + projectId + '/' + buildId, options)
      .toPromise()
      .then((res: Response) => res.json() as number)
      .catch((err: Error) => this.handleError(err));
  }

  getBuildById(id: number): Promise<Build> {
    return this.http.get(this.BUILD_PROXY_URL + '/id/' + id)
      .toPromise()
      .then((res: Response) => res.json() as Build)
      .catch((err: Error) => this.handleError(err));
  }

  getAllStepsForBuild(id: number): Observable<Step[]> {
    return this.http.get(this.STEP_PROXY_URL + '/' + id)
      .map((res: Response) => res.json() as Step[]);
  }

  handleError(err: Error): Promise<Build[]> {

    if (environment.enableDebug)
      console.error(err);

    return Promise.reject(err.message || err);
  }
}
