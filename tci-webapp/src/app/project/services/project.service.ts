import { Injectable } from '@angular/core';
import {Project} from '../models/project';
import {Http, Headers, RequestOptions, Response} from '@angular/http';
import 'rxjs/add/operator/toPromise';
import {environment} from "../../../environments/environment";

@Injectable()
export class ProjectService {

  constructor(private http: Http) {}

  create(project: Project): Promise<Project> {

    let headers = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: headers });

    return this.http.post('/api/projects', project, options)
    .toPromise()
    .then((res: Response) => res.json() as Project)
    .catch((err: Error) => this.handleError(err));
  }

  update(project: Project): Promise<Project> {

    let headers = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: headers });

    return this.http.put('/api/projects', project, options)
      .toPromise()
      .then((res: Response) => res.json() as Project)
    .catch((err: Error) => this.handleError(err));
  }

  getAllProjects(): Promise<Project[]> {

    return this.http.get('/api/projects')
      .toPromise()
      .then((res: Response) => res.json() as Project[])
      .catch((err: Error) => this.handleError(err));
  }

  getProjectById(id: string): Promise<Project> {
    return this.http.get('/api/projects/' + id).toPromise()
      .then((res: Response) => res.json() as Project)
      .catch((err: Error) => this.handleError(err));
  }

  handleError(err: Error): Promise<Project[]> {

    if(environment.enableDebug)
      console.error(err);

    return Promise.reject(err.message || err);
  }
}
