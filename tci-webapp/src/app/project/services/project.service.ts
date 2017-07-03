import { Injectable } from '@angular/core';
import {Project} from '../models/project';
import {Http, Response} from '@angular/http';
import 'rxjs/add/operator/toPromise';
import {environment} from '../../../environments/environment';

@Injectable()
export class ProjectService {

  constructor(private http: Http) {}

  getAllEntities(): Promise<Project[]> {

    return this.http.get(environment.apiBaseUrl + '/project')
      .toPromise()
      .then((res: Response) => res.json() as Project[])
      .catch((err: Error) => this.handleError(err));
  }

  handleError(err: Error): Promise<Project[]> {

    if (environment.enableDebug)
      console.error(err);

    //return Promise.reject(err.message || err);

    // Fake Data for test
    return Promise.resolve([]);
  }
}